package com.skripsi.spk.penilaian.repository;

import com.skripsi.spk.penilaian.model.entity.HasilRekomendasi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HasilRekomendasiRepository extends JpaRepository<HasilRekomendasi, Long> {

    // Hapus data lama berdasarkan NIM
    void deleteByNim(String nim);

    // Ambil hasil urut berdasarkan Ranking 1, 2, 3
    List<HasilRekomendasi> findByNimOrderByRankingAsc(String nim);
}
