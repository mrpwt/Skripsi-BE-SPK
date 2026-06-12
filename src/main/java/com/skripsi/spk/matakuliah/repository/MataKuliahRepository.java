package com.skripsi.spk.matakuliah.repository;

import com.skripsi.spk.matakuliah.model.entity.MataKuliah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MataKuliahRepository extends JpaRepository<MataKuliah, Long> {
    List<MataKuliah> findByBidangId(Long bidangId);
}
