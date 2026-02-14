package com.skripsi.spk.soaltest.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SoalTestRequestDto {

    private Integer id; // null = create | ada = update
    private String pertanyaan;
    private Map<String, String> opsi;
    private String jawabanBenar;
    private Integer bidangId;
}
