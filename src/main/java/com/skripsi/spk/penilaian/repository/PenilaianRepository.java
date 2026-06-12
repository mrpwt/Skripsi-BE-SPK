package com.skripsi.spk.penilaian.repository;

import com.skripsi.spk.penilaian.model.entity.Penilaian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface PenilaianRepository
        extends JpaRepository<Penilaian, Long> {

    Optional<Penilaian> findByNimAndKriteriaIdAndBidangId(
            String nim,
            Long kriteriaId,
            Integer bidangId
    );

    List<Penilaian> findByNim(String nim);

    // 1. Hitung jumlah mahasiswa unik yang sudah dinilai
    @Query("SELECT COUNT(DISTINCT p.nim) FROM Penilaian p")
    long countUniqueMahasiswa();

    // 2. Hitung rata-rata nilai dari seluruh data penilaian
    @Query("SELECT AVG(p.nilai) FROM Penilaian p")
    Double getRataRataNilaiTotal();

    // 3. Ambil rata-rata nilai dikelompokkan berdasarkan kriteriaId
    @Query("SELECT p.kriteriaId AS kriteriaId, AVG(p.nilai) AS rataRata FROM Penilaian p GROUP BY p.kriteriaId")
    List<Map<String, Object>> getRataRataPerKriteriaRaw();
}
