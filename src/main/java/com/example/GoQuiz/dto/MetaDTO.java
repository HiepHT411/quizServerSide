package com.example.GoQuiz.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MetaDTO {
    private Integer code;
    private String message;

    public MetaDTO(MetaData metaData) {
        this.code = metaData.getMetaCode();
        this.message = metaData.getMessage();
    }

    public MetaDTO(int code, String msg) {
        this.code = code;
        this.message = msg;
    }
}
