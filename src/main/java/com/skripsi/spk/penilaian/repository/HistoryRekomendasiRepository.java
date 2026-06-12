package com.skripsi.spk.penilaian.repository;

import com.skripsi.spk.penilaian.model.entity.HistoryRekomendasi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRekomendasiRepository extends JpaRepository<HistoryRekomendasi, Long> {
    List<HistoryRekomendasi> findByNimOrderByTanggalPenilaianDesc(String nim);
}
