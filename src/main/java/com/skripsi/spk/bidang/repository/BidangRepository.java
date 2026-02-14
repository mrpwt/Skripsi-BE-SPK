package com.skripsi.spk.bidang.repository;

import com.skripsi.spk.bidang.model.entity.Bidang;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidangRepository extends JpaRepository<Bidang, Long> {
    boolean existsByKode(String kode);
}
