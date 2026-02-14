package com.skripsi.spk.login.controller;

import com.skripsi.spk.login.model.dto.LoginRequestDto;
import com.skripsi.spk.login.model.dto.LoginResponseDto;
import com.skripsi.spk.login.model.dto.RegisterRequestDto;
import com.skripsi.spk.security.JwtUtil;
import com.skripsi.spk.login.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {

        if (!userService.validateUser(request.getUsername(), request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Username atau password salah");
        }

        String token = JwtUtil.generateToken(request.getUsername());
        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto request) {

        try {
            userService.registerMahasiswa(request);
            return ResponseEntity.ok("Registrasi berhasil");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-request")
    public ResponseEntity<?> resetRequest(@RequestParam String email) {
        userService.requestResetPassword(email);
        return ResponseEntity.ok("Link reset password telah dikirim ke email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {

        try {
            userService.resetPassword(token, newPassword);
            return ResponseEntity.ok("Password berhasil direset");
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
    }

}
