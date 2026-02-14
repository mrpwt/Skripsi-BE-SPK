package com.skripsi.spk.login.service.interfaces;

import com.skripsi.spk.login.model.dto.MahasiswaResponseDto;
import com.skripsi.spk.login.model.dto.RegisterRequestDto;
import com.skripsi.spk.login.model.entity.User;

import java.util.Optional;

public interface UserService {

    // login
    boolean validateUser(String username, String rawPassword);

    Optional<User> findByUsername(String username);

    void registerMahasiswa(RegisterRequestDto request);

    MahasiswaResponseDto getMahasiswaByNim(String nim);

    // reset password via email
    void requestResetPassword(String email);

    void resetPassword(String token, String newPassword);
}
