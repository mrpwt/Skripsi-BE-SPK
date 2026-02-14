package com.skripsi.spk.penilaian.repository;

import com.skripsi.spk.penilaian.model.entity.Penilaian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
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
}
