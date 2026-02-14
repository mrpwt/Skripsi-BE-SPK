package com.skripsi.spk.login.service.impl;

import com.skripsi.spk.login.model.dto.MahasiswaResponseDto;
import com.skripsi.spk.login.model.dto.RegisterRequestDto;
import com.skripsi.spk.login.model.entity.Mahasiswa;
import com.skripsi.spk.login.model.entity.User;
import com.skripsi.spk.login.repository.MahasiswaRepository;
import com.skripsi.spk.login.repository.UserRepository;
import com.skripsi.spk.login.service.interfaces.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MahasiswaRepository mahasiswaRepository;

    @Autowired
    private EmailService emailService;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            MahasiswaRepository mahasiswaRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mahasiswaRepository = mahasiswaRepository;
    }

    // ==========================
    // LOGIN
    // ==========================
    @Override
    public boolean validateUser(String username, String rawPassword) {
        return userRepository.findByUsername(username)
                .map(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
                .orElse(false);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public void registerMahasiswa(RegisterRequestDto request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username sudah digunakan");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email sudah digunakan");
        }

        if (mahasiswaRepository.findById(request.getNim()).isPresent()) {
            throw new IllegalArgumentException("NIM sudah terdaftar");
        }

        // ========================
        // 1️⃣ INSERT USERS
        // ========================

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // trigger DB akan hash
        user.setEmail(request.getEmail());
        user.setCreatedAt(LocalDateTime.now());
        user.setRoleId(2);

        User savedUser = userRepository.save(user);

        // ========================
        // 2️⃣ INSERT MAHASISWA
        // ========================

        Mahasiswa mahasiswa = new Mahasiswa();
        mahasiswa.setNim(request.getNim());
        mahasiswa.setNamaMahasiswa(request.getNamaMahasiswa());
        mahasiswa.setEmail(request.getEmail());
        mahasiswa.setUser(savedUser); // relasi FK

        mahasiswaRepository.save(mahasiswa);
    }

    // ==========================
    // REQUEST RESET PASSWORD
    // ==========================
    @Override
    @Transactional
    public void requestResetPassword(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Email tidak terdaftar"));

        String token = UUID.randomUUID().toString();
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(15);

        userRepository.updateResetTokenByEmail(
                email,
                token,
                expiredAt
        );

        emailService.sendResetPasswordEmail(email, token);
    }

    // ==========================
    // RESET PASSWORD
    // ==========================
    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {

        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("Password minimal 6 karakter");
        }

        User user = userRepository.findByResetToken(token)
                .orElseThrow(() ->
                        new IllegalArgumentException("Token reset tidak valid"));

        if (user.getResetTokenExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token reset sudah kedaluwarsa");
        }

        String hashedPassword = passwordEncoder.encode(newPassword);

        int updated = userRepository.updatePasswordByResetToken(
                token,
                hashedPassword
        );

        if (updated == 0) {
            throw new IllegalStateException("Gagal reset password");
        }
    }

    @Override
    public MahasiswaResponseDto getMahasiswaByNim(String nim) {

        Mahasiswa mahasiswa = mahasiswaRepository.findByNim(nim)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Mahasiswa dengan NIM " + nim + " tidak ditemukan"
                        ));

        return new MahasiswaResponseDto(
                mahasiswa.getNim(),
                mahasiswa.getNamaMahasiswa(),
                mahasiswa.getEmail()
        );
    }

}
