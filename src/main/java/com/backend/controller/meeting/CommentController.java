package com.backend.controller.meeting;

import com.backend.annotation.CheckUserNotNull;
import com.backend.annotation.CurrentMember;
import com.backend.dto.comment.request.CommentCreateRequest;
import com.backend.dto.comment.response.CommentCreateResponse;
import com.backend.dto.comment.response.CommentResponseList;
import com.backend.entity.user.User;
import com.backend.service.meeting.CommentService;
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
    public ResponseEntity<CommentCreateResponse> createComment(@PathVariable Long meetingId,
                                                               @RequestBody CommentCreateRequest request,
                                                               @CurrentMember User user) {
        Long id = commentService.createComment(meetingId, request, user);
        CommentCreateResponse response = CommentCreateResponse.success(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{meetingId}/comments")
    public ResponseEntity<CommentResponseList> getComments(@PathVariable Long meetingId) {
        return ResponseEntity.ok(commentService.getComments(meetingId));
    }
}
