package com.skripsi.spk.matakuliah.service.impl;

import com.skripsi.spk.matakuliah.model.entity.MataKuliah;
import com.skripsi.spk.matakuliah.repository.MataKuliahRepository;
import com.skripsi.spk.matakuliah.service.interfaces.MataKuliahService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MataKuliahServiceImpl implements MataKuliahService {

    @Autowired
    private MataKuliahRepository mataKuliahRepository;

    @Override
    public List<MataKuliah> getAllMataKuliah() {
        return mataKuliahRepository.findAll();
    }

    @Override
    public List<MataKuliah> getMataKuliahByBidangId(Long bidangId) {
        return mataKuliahRepository.findByBidangId(bidangId); // Memanggil query method repository
    }

    @Override
    public Optional<MataKuliah> getMataKuliahById(Long id) {
        return mataKuliahRepository.findById(id);
    }

    @Override
    public MataKuliah saveOrUpdate(MataKuliah mataKuliah) {
        if (mataKuliah.getId() != null) {
            boolean exists = mataKuliahRepository.existsById(mataKuliah.getId());
            if (!exists) {
                throw new RuntimeException("Mata Kuliah dengan ID " + mataKuliah.getId() + " tidak ditemukan!");
            }
        }
        return mataKuliahRepository.save(mataKuliah);
    }

    @Override
    public void deleteMataKuliah(Long id) {
        MataKuliah mataKuliah = mataKuliahRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mata Kuliah tidak ditemukan dengan id: " + id));
        mataKuliahRepository.delete(mataKuliah);
    }
}
