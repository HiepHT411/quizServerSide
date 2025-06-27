package com.hiephoang.platform.dto;

import com.hiephoang.platform.model.Answer;
import com.hiephoang.platform.model.Question;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuestionDto {
    private Long id;
    @NotBlank
    private String prompt;

    private List<AnswerDto> answers = new ArrayList<>();

    public static QuestionDto fromModel(Question question) {
        QuestionDto questionDto = new QuestionDto();
        questionDto.setId(question.getId());
        questionDto.setPrompt(question.getPrompt());
        for(Answer answer : question.getAnswers()) {
            questionDto.getAnswers().add(AnswerDto.fromModel(answer));
        }
        return questionDto;
    }
}
