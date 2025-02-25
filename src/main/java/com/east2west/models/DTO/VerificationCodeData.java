package com.east2west.models.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VerificationCodeData {
    private String code;
    private LocalDateTime createdAt;

    public VerificationCodeData(String code, LocalDateTime createdAt) {
        this.code = code;
        this.createdAt = createdAt;
    }
}