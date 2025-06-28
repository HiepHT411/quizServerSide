package com.hiephoang.platform.controller;

import com.hiephoang.platform.dto.*;
import com.hiephoang.platform.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ResponseMetaData> createComment(@RequestBody CommentRequest commentRequest) {
        try {
            CommentDTO comment = commentService.createComment(commentRequest);

            if (comment == null) {
                return ResponseEntity.badRequest().body(new ResponseMetaData(new MetaDTO(MetaData.BAD_REQUEST), null));
            }
            return ResponseEntity.ok(new ResponseMetaData(new MetaDTO(MetaData.SUCCESS), comment));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMetaData(new MetaDTO(MetaData.INTERNAL_SERVER_ERROR), null));
        }
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<ResponseMetaData> removeComment(@PathVariable("id") String commentId) {
        try {
            boolean result = commentService.removeComment(commentId);

            if (!result) {
                return ResponseEntity.badRequest().body(new ResponseMetaData(new MetaDTO(MetaData.BAD_REQUEST), null));
            }
            return ResponseEntity.ok(new ResponseMetaData(new MetaDTO(MetaData.SUCCESS), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMetaData(new MetaDTO(MetaData.INTERNAL_SERVER_ERROR), null));
        }
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<ResponseMetaData> getAllCommentOfPost(@PathVariable("postId") String postId) {
        try {
            List<CommentDTO> comments = commentService.findAllByPostId(postId);

            if (comments == null) {
                return ResponseEntity.badRequest().body(new ResponseMetaData(new MetaDTO(MetaData.BAD_REQUEST), null));
            }
            return ResponseEntity.ok(new ResponseMetaData(new MetaDTO(MetaData.SUCCESS), comments));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseMetaData(new MetaDTO(MetaData.INTERNAL_SERVER_ERROR), null));
        }
    }
}
