package com.backend.service.meeting;

import com.backend.dto.meeting.request.MeetingCreateRequest;
import com.backend.dto.meeting.response.MeetingReadResponse;
import com.backend.entity.meeting.Category;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.user.User;
import com.backend.repository.meeting.MeetingRepository;
import com.backend.util.mapper.MeetingMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final ImagesService imagesService;
    private final RegistrationService registrationService;
    private final CategoryService categoryService;
    private final HashtagService hashtagService;

    @Transactional
    public Long createMeeting(MeetingCreateRequest request, User user) {
        // 카테고리 탐색 후 Meeting생성과 함께 맵핑, 저장
        Category category = findCategory(request.getCategory());
        Meeting meeting = buildMeeting(request, category);
        meetingRepository.save(meeting);

        //이미지 저장 및 해시태그 생성, 맵핑
        processHashtagsAndImages(request, meeting);

        //Meeting의 생성자를 Owner로 등록
        createOwnerRegistration(meeting, user);

        return meeting.getId();
    }

    private Category findCategory(String categoryName) {
        return categoryService.findCategory(categoryName);
    }

    private Meeting buildMeeting(MeetingCreateRequest request, Category category) {
        return MeetingMapper.toMeeting(request, category);
    }

    private void processHashtagsAndImages(MeetingCreateRequest request, Meeting meeting) {
        hashtagService.processMeetingHashtags(request.getHashtagDTO(), meeting);
        imagesService.saveMeetingImages(meeting, request.getImageDTO());
    }

    private void createOwnerRegistration(Meeting meeting, User user) {
        registrationService.createOwnerStatus(meeting, user);
    }

    public Page<MeetingReadResponse> readMeetings(int page, int size, String sort) {
        Pageable pageable = createPageable(page, size, sort);
        Page<Meeting> meetings = meetingRepository.findAllWithDetails(pageable);

        // 엔티티를 DTO로 변환
        List<MeetingReadResponse> dtos = meetings.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, meetings.getTotalElements());
    }

    private MeetingReadResponse convertToDto(Meeting meeting) {
        // 엔티티에서 필요한 데이터만 추출하여 DTO 생성
        return MeetingReadResponse.builder()
                .id(meeting.getId())
                .title(meeting.getMeetingInfo().getTitle())
                .build();
    }

    private Pageable createPageable(int page, int size, String sort) {
        if (sort == null || sort.trim().isEmpty()) {
            return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        }

        Sort.Order sortOrder = switch (sort) {
            case "createdAt" -> new Sort.Order(Sort.Direction.DESC, "createdAt");
            case "title" -> new Sort.Order(Direction.ASC, "title");
            default -> new Sort.Order(Sort.Direction.DESC, "id");
        };

        return PageRequest.of(page, size, Sort.by(sortOrder));
    }
}
