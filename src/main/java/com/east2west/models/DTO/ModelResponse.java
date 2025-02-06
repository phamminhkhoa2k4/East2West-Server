package com.east2west.models.DTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ModelResponse<T> {
    private int status;
    private String message;
    private T data;
}
