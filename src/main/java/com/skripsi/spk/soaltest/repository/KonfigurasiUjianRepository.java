package com.skripsi.spk.soaltest.repository;

import com.skripsi.spk.soaltest.model.entity.KonfigurasiUjian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KonfigurasiUjianRepository extends JpaRepository<KonfigurasiUjian, String> {
}