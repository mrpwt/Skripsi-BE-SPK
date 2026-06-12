package com.skripsi.spk.matakuliah.controller;

import com.skripsi.spk.matakuliah.model.entity.MataKuliah;
import com.skripsi.spk.matakuliah.service.interfaces.MataKuliahService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mata-kuliah")
@RequiredArgsConstructor
public class MataKuliahController {
    @Autowired
    private MataKuliahService mataKuliahService;

    @GetMapping
    public ResponseEntity<List<MataKuliah>> getAll() {
        return ResponseEntity.ok(mataKuliahService.getAllMataKuliah());
    }

    @GetMapping("/bidang/{bidangId}")
    public ResponseEntity<List<MataKuliah>> getByBidangId(@PathVariable Long bidangId) {
        List<MataKuliah> list = mataKuliahService.getMataKuliahByBidangId(bidangId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MataKuliah> getById(@PathVariable Long id) {
        return mataKuliahService.getMataKuliahById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody MataKuliah mataKuliah) {
        try {
            MataKuliah savedData = mataKuliahService.saveOrUpdate(mataKuliah);
            return ResponseEntity.ok(savedData);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            mataKuliahService.deleteMataKuliah(id);
            return ResponseEntity.ok("Mata Kuliah dengan ID " + id + " berhasil dihapus.");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
