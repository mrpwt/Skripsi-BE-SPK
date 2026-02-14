package com.skripsi.spk.login.controller;

import com.skripsi.spk.login.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/mahasiswa")
public class MahasiswaController {

    @Autowired
    private UserService userService;

    @GetMapping("/{nim}")
    public ResponseEntity<?> getMahasiswaByNim(@PathVariable String nim) {

        try {
            return ResponseEntity.ok(userService.getMahasiswaByNim(nim));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}
