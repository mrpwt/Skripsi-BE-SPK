package com.skripsi.spk.login.repository;

import com.skripsi.spk.login.model.entity.Mahasiswa;
import com.skripsi.spk.login.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MahasiswaRepository extends JpaRepository<Mahasiswa, String> {
    Optional<Mahasiswa> findById(String nim);

    Optional<Mahasiswa> findByNim(String nim);
}
