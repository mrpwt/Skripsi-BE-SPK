package com.skripsi.spk.penilaian.service.impl;

import com.skripsi.spk.penilaian.model.dto.DetailPenilaianRequest;
import com.skripsi.spk.penilaian.model.dto.PenilaianBulkRequest;
import com.skripsi.spk.penilaian.model.entity.Penilaian;
import com.skripsi.spk.penilaian.repository.PenilaianRepository;
import com.skripsi.spk.penilaian.service.interfaces.PenilaianService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PenilaianServiceImpl implements PenilaianService {

    private final PenilaianRepository repository;

    @Override
    public void saveOrUpdateBulk(PenilaianBulkRequest request) {

        // Validasi dasar
        if (request.getNim() == null || request.getBidangId() == null) {
            throw new IllegalArgumentException("NIM dan Bidang ID tidak boleh kosong");
        }

        List<Penilaian> dataSimpan = new ArrayList<>();

        // Loop hanya item nilainya saja
        for (DetailPenilaianRequest dto : request.getPenilaian()) {

            // Query menggunakan NIM & Bidang dari Parent, Kriteria dari Child
            Penilaian penilaian = repository
                    .findByNimAndKriteriaIdAndBidangId(
                            request.getNim(),      // Ambil dari Parent
                            dto.getKriteriaId(),   // Ambil dari List Item
                            request.getBidangId()  // Ambil dari Parent
                    )
                    .orElse(new Penilaian());

            // Set Data
            penilaian.setNim(request.getNim());
            penilaian.setBidangId(request.getBidangId()); // Set Jurusan
            penilaian.setKriteriaId(dto.getKriteriaId());
            penilaian.setNilai(dto.getNilai());

            dataSimpan.add(penilaian);
        }

        repository.saveAll(dataSimpan);
    }
}

