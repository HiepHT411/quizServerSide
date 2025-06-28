package com.hiephoang.platform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hiephoang.platform.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private long id;

    private String body;

    @JsonProperty("user_id")
    private long userId;

    @JsonProperty("post_id")
    private long postId;

    public CommentDTO(Comment comment, long userId, long postId) {
        this.id = comment.getId();
        this.body = comment.getBody();
        this.userId = userId;
        this.postId = postId;
    }

    public static CommentDTO toCommentDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setBody(comment.getBody());
        dto.setUserId(comment.getUser().getId());
        dto.setPostId(comment.getPost().getId());
        return dto;
    }
}
