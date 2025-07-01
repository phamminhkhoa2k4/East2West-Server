package com.east2west.models.DTO;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModelResponse<T> {
    private int status;
    private String message;
    private T data;
}
