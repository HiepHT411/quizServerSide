package com.hiephoang.platform.dto;

import com.hiephoang.platform.model.Answer;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerDto {
    private Long id;
    @NotBlank
    private String text;
    @NotBlank
    private boolean correct;

    public static AnswerDto fromModel(Answer answer) {
        AnswerDto answerDto = new AnswerDto();
        answerDto.setId(answer.getId());
        answerDto.setText(answer.getText());
        answerDto.setCorrect(answer.isCorrect());
        return answerDto;
    }
}
