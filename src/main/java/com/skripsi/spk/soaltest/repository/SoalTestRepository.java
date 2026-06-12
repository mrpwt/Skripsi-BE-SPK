package com.skripsi.spk.soaltest.repository;

import com.skripsi.spk.soaltest.model.entity.SoalTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SoalTestRepository extends JpaRepository<SoalTest, Integer> {
    List<SoalTest> findByIsDeletedFalseOrderByCreatedAtAsc();

    Optional<SoalTest> findByIdAndIsDeletedFalse(Integer id);

    @Query("SELECT s FROM SoalTest s WHERE s.isDeleted = false " +
            "AND (:keyword IS NULL OR CAST(:keyword AS string) = '' " +
            "     OR LOWER(CAST(s.pertanyaan AS string)) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%'))) " +
            "AND (:bidangId IS NULL OR s.bidangId = :bidangId)")
    Page<SoalTest> findByFilterAndPagination(
            @Param("keyword") String keyword,
            @Param("bidangId") Integer bidangId,
            Pageable pageable
    );

    @Query(value = "SELECT * FROM soal_tes WHERE bidang_id = :bidangId AND is_deleted = false ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<SoalTest> findRandomByBidangId(@Param("bidangId") Integer bidangId, @Param("limit") int limit);

    // Hitung stok soal aktif per bidang untuk validasi 1.5x
    long countByBidangIdAndIsDeletedFalse(Integer bidangId);
}
