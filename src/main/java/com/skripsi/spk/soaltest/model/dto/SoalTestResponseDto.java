package com.skripsi.spk.soaltest.model.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SoalTestResponseDto {

    private Integer id;
    private String pertanyaan;
    private Map<String, String> opsi;
    private String jawabanBenar;
    private Integer bidangId;
}
