package com.skripsi.spk.soaltest.service.impl;

import com.skripsi.spk.soaltest.model.dto.SoalTestRequestDto;
import com.skripsi.spk.soaltest.model.dto.SoalTestResponseDto;
import com.skripsi.spk.soaltest.model.entity.KonfigurasiUjian;
import com.skripsi.spk.soaltest.model.entity.SoalTest;
import com.skripsi.spk.soaltest.repository.KonfigurasiUjianRepository;
import com.skripsi.spk.soaltest.repository.SoalTestRepository;
import com.skripsi.spk.soaltest.service.interfaces.SoalTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SoalTestServiceImpl implements SoalTestService {

    private final SoalTestRepository repository;
    private final KonfigurasiUjianRepository konfigurasiRepository;

    @Override
    public void saveOrUpdate(SoalTestRequestDto dto) {
        SoalTest soal;

        if (dto.getId() != null) {
            soal = repository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Soal tidak ditemukan"));
        } else {
            soal = new SoalTest();
        }

        try {
            soal.setPertanyaan(dto.getPertanyaan());
            soal.setOpsi(dto.getOpsi());
            soal.setJawabanBenar(dto.getJawabanBenar());
            soal.setBidangId(dto.getBidangId());

            repository.save(soal);

        } catch (Exception e) {
            throw new RuntimeException("Error: ", e);
        }
    }

    @Override
    public void delete(Integer id) {
        SoalTest soal = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Soal tidak ditemukan"));

        soal.setIsDeleted(true);
        repository.save(soal);
    }

    @Override
    public List<SoalTestResponseDto> getAll() {
        return repository.findByIsDeletedFalseOrderByCreatedAtAsc()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<SoalTestResponseDto> getAllAdmin(String keyword, Integer bidangId, int page, int size) {
        // Membuat objek penomoran halaman & pengurutan otomatis berdasarkan CreatedAt Asc
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());

        // Mengambil data page dari repository
        Page<SoalTest> soalPage = repository.findByFilterAndPagination(keyword, bidangId, pageable);

        // Konversi Page entitas menjadi Page DTO menggunakan method reference bawaan Spring
        return soalPage.map(this::toResponseDto);
    }

    @Override
    public SoalTestResponseDto getById(Integer id) {
        SoalTest soal = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Soal tidak ditemukan"));

        return toResponseDto(soal);
    }

    @Override
    public Integer getJumlahSoalConfig() {
        return konfigurasiRepository.findById("TOTAL_SOAL")
                .map(KonfigurasiUjian::getNilaiKonfigurasi)
                .orElse(15); // Default 15 jika tabel kosong
    }

    @Override
    public void saveJumlahSoalConfig(Integer jumlahSoal) {
        // Anggap Anda memiliki 3 ID Bidang tetap. Sempurnakan bagian ini jika bidang Anda dinamis dari database.
        List<Integer> listBidangId = List.of(1, 2, 3);
        int jumlahBidang = listBidangId.size();

        int soalPerBidang = jumlahSoal / jumlahBidang;
        int sisaSoal = jumlahSoal % jumlahBidang;

        // Validasi aturan 1.5x dari kebutuhan per bidang
        for (int i = 0; i < jumlahBidang; i++) {
            int kebutuhanBidangIni = soalPerBidang;
            if (sisaSoal > 0) {
                kebutuhanBidangIni++;
                sisaSoal--;
            }

            long stokTersedia = repository.countByBidangIdAndIsDeletedFalse(listBidangId.get(i));
            double minimalStok = kebutuhanBidangIni * 1.5;

            if (stokTersedia < minimalStok) {
                throw new RuntimeException("Gagal menerapkan konfigurasi! Stok soal di database untuk Bidang ID (" + listBidangId.get(i) +
                        ") hanya tersedia " + stokTersedia + " soal. " +
                        "Dibutuhkan minimal " + (int) Math.ceil(minimalStok) + " soal (Batas 1.5x dari kebutuhan " + kebutuhanBidangIni + " soal).");
            }
        }

        KonfigurasiUjian config = KonfigurasiUjian.builder().id("TOTAL_SOAL").nilaiKonfigurasi(jumlahSoal).build();
        konfigurasiRepository.save(config);
    }

    @Override
    public List<SoalTestResponseDto> generateUjianSesuaiKebutuhan() {
        int totalSoal = this.getJumlahSoalConfig();
        List<Integer> listBidangId = List.of(1, 2, 3);
        int jumlahBidang = listBidangId.size();

        int soalPerBidang = totalSoal / jumlahBidang;
        int sisaSoal = totalSoal % jumlahBidang;

        List<SoalTest> semuaSoalUjian = new ArrayList<>();

        for (int i = 0; i < jumlahBidang; i++) {
            int limitAmbil = soalPerBidang;
            if (sisaSoal > 0) {
                limitAmbil++;
                sisaSoal--;
            }
            List<SoalTest> soalAcak = repository.findRandomByBidangId(listBidangId.get(i), limitAmbil);
            semuaSoalUjian.addAll(soalAcak);
        }

        Collections.shuffle(semuaSoalUjian); // Diacak kembali agar urutan materi berbaur rapi
        return semuaSoalUjian.stream().map(this::toResponseDto).collect(Collectors.toList());
    }

    private SoalTestResponseDto toResponseDto(SoalTest soal) {
        return SoalTestResponseDto.builder()
                .id(soal.getId())
                .pertanyaan(soal.getPertanyaan())
                .opsi(soal.getOpsi())
                .jawabanBenar(soal.getJawabanBenar())
                .bidangId(soal.getBidangId())
                .build();
    }
}
