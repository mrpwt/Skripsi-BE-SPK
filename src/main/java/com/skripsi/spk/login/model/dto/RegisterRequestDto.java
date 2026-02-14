package com.skripsi.spk.login.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {

    private String username;
    private String password;
    private String email;

    private String nim;
    private String namaMahasiswa;

}
