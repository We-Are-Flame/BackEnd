package com.backend.service.meeting;

import com.backend.dto.meeting.request.rating.UserRatingRequest;
import com.backend.dto.meeting.request.rating.UserRatingRequestList;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.MeetingRegistration;
import com.backend.entity.user.User;
import com.backend.repository.meeting.meeting.MeetingRepository;
import com.backend.repository.user.UserRepository;
import com.backend.service.notification.NotificationService;
import com.backend.strategy.TemperatureRating;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RatingService {
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final NotificationService notificationService;

    public void ratingMeetingParticipants(UserRatingRequestList requestList) {
        List<User> users = fetchUsers(requestList);

        Map<Long, Integer> userRatings = calculateRating(requestList);

        updateTemperture(users, userRatings);

        userRepository.saveAll(users);
    }

    private List<User> fetchUsers(UserRatingRequestList requestList) {
        List<Long> userIds = requestList.getUserRatingRequest().stream()
                .map(UserRatingRequest::userId)
                .toList();

        return userRepository.findAllById(userIds);
    }

    private Map<Long, Integer> calculateRating(UserRatingRequestList requestList) {
        return requestList.getUserRatingRequest().stream()
                .collect(Collectors.toMap(
                        UserRatingRequest::userId,
                        req -> TemperatureRating.fromStars(req.stars()).getTemperatureChange()
                ));
    }

    private void updateTemperture(List<User> users, Map<Long, Integer> userRatings) {
        users.forEach(user -> {
            Integer ratingTemperature = userRatings.get(user.getId());
            if (ratingTemperature != null) {
                int newTemperature = user.getTemperature() + ratingTemperature;
                user.updateTemperature(newTemperature);
            }
        });
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void sendEvaluationNotifications() {
        LocalDateTime now = LocalDateTime.now();
        List<Meeting> meetings = meetingRepository.findForEvaluation(now);

        meetings.forEach(meeting -> {
            List<User> participants = meeting.getRegistrations().stream()
                    .map(MeetingRegistration::getUser)
                    .toList();

            participants.forEach(participant -> notificationService.sendEvaluationRequest(meeting, participant));
        });
    }
}
