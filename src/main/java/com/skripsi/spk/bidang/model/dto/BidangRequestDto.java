package com.skripsi.spk.bidang.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BidangRequestDto {

    private Long id; // null = create | ada = update
    private String kode;
    private String namaBidang;
    private String deskripsi;
}
