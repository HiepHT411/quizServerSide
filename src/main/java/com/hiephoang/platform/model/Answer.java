package com.hiephoang.platform.model;

import com.hiephoang.platform.dto.AnswerDto;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private boolean correct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    public static Answer fromDto(AnswerDto answerDto) {
        Answer answer = new Answer();
        answer.setId(answerDto.getId());
        answer.setText(answerDto.getText());
        answer.setCorrect(answerDto.isCorrect());
        return answer;
    }
}
