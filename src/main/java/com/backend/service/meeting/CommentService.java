package com.backend.service.meeting;

import com.backend.dto.comment.request.CommentCreateRequest;
import com.backend.dto.comment.response.CommentResponse;
import com.backend.dto.comment.response.CommentResponseList;
import com.backend.entity.meeting.Comment;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.user.User;
import com.backend.exception.ErrorMessages;
import com.backend.exception.NotFoundException;
import com.backend.repository.meeting.CommentRepository;
import com.backend.repository.meeting.MeetingRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MeetingRepository meetingRepository;

    @Transactional
    public Long createComment(Long meetingId, CommentCreateRequest request, User user) {
        Meeting meeting = getMeetingById(meetingId);
        Comment comment = buildComment(request, user, meeting);
        commentRepository.save(comment);
        return comment.getId();
    }

    private Meeting getMeetingById(Long meetingId) {
        return meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.MEETING_NOT_FOUND));
    }

    private Comment buildComment(CommentCreateRequest request, User user, Meeting meeting) {
        return Comment.builder()
                .description(request.getDescription())
                .user(user)
                .meeting(meeting)
                .build();
    }

    public CommentResponseList getComments(Long meetingId) {
        List<Comment> comments = commentRepository.findByMeetingId(meetingId);
        List<CommentResponse> commentResponseList = convertToCommentData(comments);
        int count = commentResponseList.size();
        return new CommentResponseList(count, commentResponseList);
    }

    private List<CommentResponse> convertToCommentData(List<Comment> comments) {
        return comments.stream()
                .map(comment -> CommentResponse.builder()
                        .profileImage(comment.getUser().getProfileImage())
                        .nickname(comment.getUser().getNickname())
                        .description(comment.getDescription())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
