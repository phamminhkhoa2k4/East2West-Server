package com.east2west.models.payload.response;

import lombok.*;

@Data
@Getter
@Setter
public class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
