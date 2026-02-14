package com.skripsi.spk.penilaian.service.impl;

import com.skripsi.spk.kriteria.model.entity.Kriteria;
import com.skripsi.spk.kriteria.repository.KriteriaRepository;
import com.skripsi.spk.penilaian.model.dto.HasilRekomendasiResponseDto;
import com.skripsi.spk.penilaian.model.entity.HasilRekomendasi;
import com.skripsi.spk.penilaian.model.entity.Penilaian;
import com.skripsi.spk.penilaian.repository.HasilRekomendasiRepository;
import com.skripsi.spk.penilaian.repository.PenilaianRepository;
import com.skripsi.spk.penilaian.service.interfaces.SpkCalculationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpkCalculationServiceImpl implements SpkCalculationService {

    private final PenilaianRepository penilaianRepo;
    private final KriteriaRepository kriteriaRepo;
    private final HasilRekomendasiRepository hasilRepo;

    @Override
    @Transactional
    public List<HasilRekomendasiResponseDto> hitungRekomendasi(String nim) {

        List<Penilaian> nilaiList = penilaianRepo.findByNim(nim);
        if (nilaiList.isEmpty()) {
            throw new RuntimeException("Data penilaian tidak ditemukan untuk NIM: " + nim);
        }

        List<Kriteria> kriteriaList = kriteriaRepo.findAll();
        Map<Long, Kriteria> kriteriaMap = kriteriaList.stream()
                .collect(Collectors.toMap(Kriteria::getId, k -> k));

        // --- NORMALISASI (Cari Max/Min) ---
        Map<Long, Double> divisorMap = new HashMap<>();

        for (Kriteria k : kriteriaList) {
            List<Double> values = nilaiList.stream()
                    .filter(n -> n.getKriteriaId().equals(k.getId()))
                    .map(n -> n.getNilai().doubleValue())
                    .collect(Collectors.toList());

            if (values.isEmpty()) continue;

            if ("COST".equalsIgnoreCase(k.getTipe())) {
                divisorMap.put(k.getId(), Collections.min(values));
            } else {
                divisorMap.put(k.getId(), Collections.max(values));
            }
        }

        // --- HITUNG SKOR + NORMALIZED ---
        Map<Integer, List<Penilaian>> nilaiPerBidang =
                nilaiList.stream().collect(Collectors.groupingBy(Penilaian::getBidangId));

        List<HasilRekomendasiResponseDto> resultList = new ArrayList<>();

        for (Map.Entry<Integer, List<Penilaian>> entry : nilaiPerBidang.entrySet()) {

            Integer bidangId = entry.getKey();
            List<Penilaian> skorBidang = entry.getValue();

            double totalSkor = 0.0;
            Map<String, Double> normalizedMap = new HashMap<>();

            for (Penilaian p : skorBidang) {

                Kriteria k = kriteriaMap.get(p.getKriteriaId());
                double nilai = p.getNilai().doubleValue();
                double divisor = divisorMap.getOrDefault(k.getId(), 1.0);
                double bobot = k.getBobot().doubleValue();

                double normalized = "COST".equalsIgnoreCase(k.getTipe())
                        ? divisor / nilai
                        : nilai / divisor;

                normalizedMap.put(
                        k.getKode(),
                        BigDecimal.valueOf(normalized)
                                .setScale(3, BigDecimal.ROUND_HALF_UP)
                                .doubleValue()
                );

                totalSkor += (normalized * bobot);
            }

            resultList.add(
                    HasilRekomendasiResponseDto.builder()
                            .nim(nim)
                            .bidangId(bidangId)
                            .skorAkhir(
                                    BigDecimal.valueOf(totalSkor)
                                            .setScale(4, BigDecimal.ROUND_HALF_UP)
                            )
                            .normalizedValues(normalizedMap)
                            .build()
            );
        }

        // --- RANKING ---
        resultList.sort(
                Comparator.comparing(HasilRekomendasiResponseDto::getSkorAkhir)
                        .reversed()
        );

        for (int i = 0; i < resultList.size(); i++) {
            resultList.get(i).setRanking(i + 1);
        }

        hasilRepo.deleteByNim(nim);

        for (HasilRekomendasiResponseDto dto : resultList) {

            HasilRekomendasi entity = new HasilRekomendasi();

            entity.setNim(dto.getNim());
            entity.setBidangId(dto.getBidangId());
            entity.setSkorAkhir(dto.getSkorAkhir());
            entity.setRanking(dto.getRanking());

            hasilRepo.save(entity);
        }

        return resultList;
    }


    // Helper method untuk mapping manual
    private HasilRekomendasiResponseDto mapToDto(HasilRekomendasi entity) {
        return HasilRekomendasiResponseDto.builder()
                .nim(entity.getNim())
                .bidangId(entity.getBidangId())
                .skorAkhir(entity.getSkorAkhir())
                .ranking(entity.getRanking())
                .build();
    }
}
