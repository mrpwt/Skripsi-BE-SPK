package com.skripsi.spk.penilaian.model.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PenilaianBulkRequest {
    private String nim;
    private Integer bidangId;
    private List<DetailPenilaianRequest> penilaian;
}
