package com.skripsi.spk.penilaian.service.impl;

import com.skripsi.spk.bidang.repository.BidangRepository;
import com.skripsi.spk.kriteria.model.entity.Kriteria;
import com.skripsi.spk.kriteria.repository.KriteriaRepository;
import com.skripsi.spk.penilaian.model.dto.AnalitikResponse;
import com.skripsi.spk.penilaian.model.dto.DetailPenilaianRequest;
import com.skripsi.spk.penilaian.model.dto.KriteriaStatResponse;
import com.skripsi.spk.penilaian.model.dto.PenilaianBulkRequest;
import com.skripsi.spk.penilaian.model.entity.Penilaian;
import com.skripsi.spk.penilaian.repository.PenilaianRepository;
import com.skripsi.spk.penilaian.service.interfaces.PenilaianService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PenilaianServiceImpl implements PenilaianService {

    private final PenilaianRepository repository;
    private final KriteriaRepository kriteriaRepository;
    private final BidangRepository bidangRepository;

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

    @Override
    public AnalitikResponse getDashboardStats() {
        // 1. Ambil data hitungan total angka box dashboard
        long totalMahasiswa = repository.countUniqueMahasiswa();
        long totalKriteria = kriteriaRepository.count();
        long totalBidang = bidangRepository.count();

        // 2. Ambil nilai rata-rata keseluruhan (handling jika null/masih kosong)
        Double avgTotal = repository.getRataRataNilaiTotal();
        double rataRataNilai = (avgTotal != null) ? Math.round(avgTotal * 10.0) / 10.0 : 0.0;

        // 3. Ambil data group by rata-rata nilai per kriteria ID dari DB
        List<Map<String, Object>> rawStats = repository.getRataRataPerKriteriaRaw();

        // Mapping List<Map> menjadi Map<Long, Double> agar lebih cepat di-query saat looping kriteria
        Map<Long, Double> rawStatsMap = rawStats.stream()
                .filter(m -> m.get("kriteriaId") != null && m.get("rataRata") != null)
                .collect(Collectors.toMap(
                        m -> (Long) m.get("kriteriaId"),
                        m -> Math.round(((Double) m.get("rataRata")) * 10.0) / 10.0 // Pembulatan 1 desimal
                ));

        // 4. Gabungkan dengan data Kriteria Asli untuk mendapatkan teks Kode & Nama Kriteria
        List<Kriteria> listKriteria = kriteriaRepository.findAll(Sort.by(Sort.Direction.ASC, "createdAt"));
        List<KriteriaStatResponse> rataRataPerKriteria = new ArrayList<>();

        for (Kriteria k : listKriteria) {
            // Jika kriteria belum ada nilainya di DB, otomatis beri nilai 0.0
            double avg = rawStatsMap.getOrDefault(k.getId(), 0.0);

            rataRataPerKriteria.add(KriteriaStatResponse.builder()
                    .kode(k.getKode()) // Pastikan entitas Kriteria punya field/getter getKode()
                    .nama(k.getNamaKriteria()) // Pastikan entitas Kriteria punya field/getter getNama()
                    .rataRata(avg)
                    .build());
        }

        // 5. Kembalikan data DTO Final
        return AnalitikResponse.builder()
                .totalMahasiswa(totalMahasiswa)
                .totalKriteria(totalKriteria)
                .totalBidang(totalBidang)
                .rataRataNilai(rataRataNilai)
                .rataRataPerKriteria(rataRataPerKriteria)
                .build();
    }
}

