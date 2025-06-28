package com.hiephoang.platform.repository;

import com.hiephoang.platform.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Integer deleteByIdAndUserId(Long id, Long userId);
}
