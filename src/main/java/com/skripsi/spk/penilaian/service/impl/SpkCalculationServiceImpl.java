package com.skripsi.spk.penilaian.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skripsi.spk.kriteria.model.entity.Kriteria;
import com.skripsi.spk.kriteria.repository.KriteriaRepository;
import com.skripsi.spk.login.repository.MahasiswaRepository;
import com.skripsi.spk.penilaian.model.dto.HasilRekomendasiResponseDto;
import com.skripsi.spk.penilaian.model.entity.HasilRekomendasi;
import com.skripsi.spk.penilaian.model.entity.HistoryRekomendasi;
import com.skripsi.spk.penilaian.model.entity.Penilaian;
import com.skripsi.spk.penilaian.repository.HasilRekomendasiRepository;
import com.skripsi.spk.penilaian.repository.HistoryRekomendasiRepository;
import com.skripsi.spk.penilaian.repository.PenilaianRepository;
import com.skripsi.spk.penilaian.service.interfaces.SpkCalculationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpkCalculationServiceImpl implements SpkCalculationService {

    private final PenilaianRepository penilaianRepo;
    private final KriteriaRepository kriteriaRepo;
    private final HasilRekomendasiRepository hasilRepo;
    private final HistoryRekomendasiRepository historyRepository;
    private final MahasiswaRepository mahasiswaRepository;
    private final SpringTemplateEngine templateEngine;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public List<HasilRekomendasiResponseDto> hitungRekomendasi(String nim) {

        List<Penilaian> nilaiList = penilaianRepo.findByNim(nim);
        if (nilaiList.isEmpty()) {
            return Collections.emptyList(); // atau new ArrayList<>();
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

                double normalizedRounded = BigDecimal.valueOf(normalized)
                        .setScale(3, BigDecimal.ROUND_HALF_UP)
                        .doubleValue();

                normalizedMap.put(k.getKode(), normalizedRounded);

                totalSkor += normalizedRounded * bobot;
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

        if (!resultList.isEmpty()) {
            try {
                var dataMahasiswa = mahasiswaRepository.findByNim(nim);
                String namaMhs = "";
                if (dataMahasiswa.isPresent()) {
                    namaMhs = dataMahasiswa.get().getNamaMahasiswa(); // Sesuaikan dengan method getter di entitas Mahasiswa Anda
                }
                // Rekomendasi teratas berada pada indeks ke-0 setelah proses perankingan
                HasilRekomendasiResponseDto topRecommendation = resultList.get(0);

                ObjectMapper objectMapper = new ObjectMapper();
                String jsonDetailSkor = objectMapper.writeValueAsString(resultList);

                HistoryRekomendasi history = new HistoryRekomendasi();
                history.setNim(nim);
                history.setNamaMahasiswa(namaMhs);
                history.setBidangRekomendasi(String.valueOf(topRecommendation.getBidangId()));
                history.setTanggalPenilaian(LocalDateTime.now());
                history.setDetailSkor(jsonDetailSkor);

                historyRepository.save(history);
                System.out.println("✅ Berhasil mengarsipkan riwayat kalkulasi SPK ke Database untuk NIM: " + nim);
            } catch (Exception e) {
                System.err.println("❌ Gagal menyimpan arsip riwayat SPK: " + e.getMessage());
            }
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

    @Override
    public ResponseEntity<byte[]> generateRaportPdf(Long historyId) {
        try {
            // 1. Ambil data history dari database berdasarkan ID
            var history = historyRepository.findById(historyId)
                    .orElseThrow(() -> new RuntimeException("Data riwayat SPK tidak ditemukan untuk ID: " + historyId));

            // 2. Parsing text JSON detail_skor menjadi List dari Map Java
            List<Map<String, Object>> detailSkorList = objectMapper.readValue(
                    history.getDetailSkor(),
                    new TypeReference<List<Map<String, Object>>>() {}
            );

            // Mapping ID bidang ke Nama Aslinya secara dinamis
            // Sesuai dengan database Anda: 1 = RPL, 2 = AI, 3 = Jaringan Komputer
            Map<Integer, String> namaBidangMap = Map.of(
                    1, "Rekayasa Perangkat Lunak",
                    2, "Artificial Intelligence",
                    3, "Jaringan Komputer"
            );

            // Tambahkan namaBidang ke tiap objek skor secara dinamis
            for (Map<String, Object> skor : detailSkorList) {
                Integer bidangId = (Integer) skor.get("bidangId");
                skor.put("namaBidang", namaBidangMap.getOrDefault(bidangId, "Bidang Tidak Diketahui"));
            }

            // 3. Ambil daftar key kriteria (K1, K2, dst) secara dinamis dari data alternatif pertama
            List<String> kriteriaKeys = new ArrayList<>();
            if (!detailSkorList.isEmpty() && detailSkorList.get(0).get("normalizedValues") != null) {
                Map<String, Object> normValues = (Map<String, Object>) detailSkorList.get(0).get("normalizedValues");
                kriteriaKeys.addAll(normValues.keySet());
                Collections.sort(kriteriaKeys); // Mengurutkan kriteria dari K1, K2, K3...
            }

            // Tentukan Rekomendasi Utama (Nama Bidang Rank #1)
            String rekomendasiUtama = "Tidak ada rekomendasi";
            for (Map<String, Object> skor : detailSkorList) {
                if (skor.get("ranking") != null && (Integer) skor.get("ranking") == 1) {
                    rekomendasiUtama = (String) skor.get("namaBidang");
                    break;
                }
            }

            // Format tanggal pengujian menjadi teks formal Indonesia
            String tanggalFormatted = history.getTanggalPenilaian()
                    .format(DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("id", "ID")));

            // 4. Masukkan seluruh variabel ke dalam Context Thymeleaf
            org.thymeleaf.context.Context context = new Context();
            context.setVariable("nim", history.getNim());
            context.setVariable("namaMahasiswa", history.getNamaMahasiswa()); // Aman & terisi dinamis
            context.setVariable("tanggal", tanggalFormatted);
            context.setVariable("rekomendasiUtama", rekomendasiUtama);
            context.setVariable("detailSkor", detailSkorList);
            context.setVariable("kriteriaKeys", kriteriaKeys);

            // 5. Render template rapi.html menjadi String HTML mentah
            String htmlContent = templateEngine.process("raport_template", context);

            // 6. Konversi String HTML menjadi dokumen fisik PDF (Flying Saucer)
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            renderer.finishPDF();

            byte[] pdfBytes = outputStream.toByteArray();

            // 7. Siapkan Response HTTP Header untuk mengunduh berkas
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Raport_SPK_" + history.getNim() + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
