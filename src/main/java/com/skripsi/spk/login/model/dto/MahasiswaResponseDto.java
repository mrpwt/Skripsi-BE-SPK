package com.skripsi.spk.login.model.dto;

import com.skripsi.spk.login.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MahasiswaResponseDto {
    public String nim;
    public String nama;
    public String email;
}
