package com.skripsi.spk.matakuliah.service.interfaces;

import com.skripsi.spk.matakuliah.model.entity.MataKuliah;

import java.util.List;
import java.util.Optional;

public interface MataKuliahService {
    List<MataKuliah> getAllMataKuliah();
    List<MataKuliah> getMataKuliahByBidangId(Long bidangId);
    Optional<MataKuliah> getMataKuliahById(Long id);
    MataKuliah saveOrUpdate(MataKuliah mataKuliah);
    void deleteMataKuliah(Long id);
}
