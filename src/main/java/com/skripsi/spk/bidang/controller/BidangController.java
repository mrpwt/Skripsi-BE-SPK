package com.skripsi.spk.bidang.controller;

import com.skripsi.spk.bidang.model.dto.BidangRequestDto;
import com.skripsi.spk.bidang.model.dto.BidangResponseDto;
import com.skripsi.spk.bidang.service.interfaces.BidangService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bidang")
public class BidangController {

    private final BidangService bidangService;

    public BidangController(BidangService bidangService) {
        this.bidangService = bidangService;
    }

    @GetMapping
    public ResponseEntity<List<BidangResponseDto>> getAll() {
        return ResponseEntity.ok(bidangService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BidangResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bidangService.getById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<BidangResponseDto> save(@RequestBody BidangRequestDto dto) {
        return ResponseEntity.ok(bidangService.save(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bidangService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
