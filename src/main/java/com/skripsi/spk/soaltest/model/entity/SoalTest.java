package com.skripsi.spk.soaltest.model.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "soal_tes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SoalTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, columnDefinition = "text")
    private String pertanyaan;

    @Column(nullable = false, columnDefinition = "jsonb")
    @Type(JsonType.class)
    private Map<String, String> opsi;

    @Column(nullable = false, length = 5)
    private String jawabanBenar;

    @Column(name = "bidang_id")
    private Integer bidangId;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updateAt = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate() {
        updateAt = LocalDateTime.now();
    }
}
