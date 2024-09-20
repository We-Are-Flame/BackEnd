package com.backend.comment.controller;

import com.backend.before.annotation.CheckUserNotNull;
import com.backend.before.annotation.CurrentMember;
import com.backend.before.dto.comment.request.CommentCreateRequest;
import com.backend.before.dto.comment.response.CommentResponseList;
import com.backend.before.dto.common.ResponseMessage;
import com.backend.before.dto.common.SuccessResponse;
import com.backend.before.entity.user.User;
import com.backend.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/meetings")
public class CommentController {

    private final CommentService commentService;

    @CheckUserNotNull
    @PostMapping("/{meetingId}/comments")
    public ResponseEntity<SuccessResponse> createComment(@PathVariable Long meetingId,
                                                         @RequestBody CommentCreateRequest request,
                                                         @CurrentMember User user) {
        Long id = commentService.createComment(meetingId, request, user);
        SuccessResponse response = SuccessResponse.create(id, ResponseMessage.COMMENT_CREATION);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{meetingId}/comments")
    public ResponseEntity<CommentResponseList> getComments(@PathVariable Long meetingId) {
        return ResponseEntity.ok(commentService.getComments(meetingId));
    }
}
