package com.example.GoQuiz.dto;

import com.example.GoQuiz.model.Answer;
import com.example.GoQuiz.model.Question;
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
