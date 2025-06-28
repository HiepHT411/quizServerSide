package com.hiephoang.platform.repository;

import com.hiephoang.platform.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Integer deleteByIdAndUserId(long commentId, long userid);
    List<Comment> findByPostId(long postId);
}
