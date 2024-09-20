package com.backend.comment.service;

import com.backend.before.dto.comment.request.CommentCreateRequest;
import com.backend.before.dto.comment.response.CommentResponse;
import com.backend.before.dto.comment.response.CommentResponseList;
import com.backend.comment.entity.Comment;
import com.backend.meeting.domain.meeting.entity.Meeting;
import com.backend.before.entity.user.User;
import com.backend.before.exception.ErrorMessages;
import com.backend.before.exception.NotFoundException;
import com.backend.comment.repository.CommentRepository;
import com.backend.meeting.domain.meeting.repository.MeetingRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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

    @Transactional(readOnly = true)
    public CommentResponseList getComments(Long meetingId) {
        Sort sort = Sort.by(Direction.ASC, "createdAt"); // 최신순 정렬
        List<Comment> comments = commentRepository.findByMeetingId(meetingId, sort);
        List<CommentResponse> commentResponseList = convertToCommentResponse(comments);
        int count = commentResponseList.size();
        return new CommentResponseList(count, commentResponseList);
    }

    private List<CommentResponse> convertToCommentResponse(List<Comment> comments) {
        return comments.stream()
                .map(comment -> CommentResponse.builder()
                        .profileImage(comment.getUser().getProfileImage())
                        .nickname(comment.getUser().getNickname())
                        .description(comment.getDescription())
                        .createdAt(comment.getCreatedAt())
                        .isSchoolEmail(comment.getUser().getIsSchoolVerified())
                        .build())
                .collect(Collectors.toList());
    }
}
