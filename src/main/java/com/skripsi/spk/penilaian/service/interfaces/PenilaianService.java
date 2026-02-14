package com.skripsi.spk.penilaian.service.interfaces;

import com.skripsi.spk.penilaian.model.dto.PenilaianBulkRequest;

public interface PenilaianService {
    void saveOrUpdateBulk(PenilaianBulkRequest request);
}

