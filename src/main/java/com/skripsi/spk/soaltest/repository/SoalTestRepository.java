package com.skripsi.spk.soaltest.repository;

import com.skripsi.spk.soaltest.model.entity.SoalTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SoalTestRepository extends JpaRepository<SoalTest, Integer> {
    List<SoalTest> findByIsDeletedFalseOrderByCreatedAtAsc();

    Optional<SoalTest> findByIdAndIsDeletedFalse(Integer id);
}
