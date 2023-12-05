package com.backend.entity.meeting;

import com.backend.entity.base.BaseEntity;
import com.backend.entity.meeting.embeddable.MeetingAddress;
import com.backend.entity.meeting.embeddable.MeetingTime;
import com.backend.entity.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "meetings")
public class Meeting extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id")
    private Long id;
    private String title;
    private String description;
    private String thumbnailUrl;

    @Builder.Default
    private Integer currentParticipants = 0;
    private Integer maxParticipants;

    @Embedded
    private MeetingAddress meetingAddress;

    @Embedded
    private MeetingTime meetingTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User host;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MeetingImage> meetingImages;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MeetingHashtag> meetingHashtags;

    @Builder.Default
    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingRegistration> registrations = new ArrayList<>();

    @Transient
    private Set<Hashtag> hashtags;

    public void assignHashtags(Set<Hashtag> hashtags) {
        this.hashtags = hashtags;
    }

    public boolean isUserOwner(User user) {
        return user != null && this.host.isSameId(user);
    }

    public RegistrationStatus determineParticipationStatus(User user) {
        if (user == null) {
            return RegistrationStatus.NONE;
        }
        return this.getParticipationStatusForUser(user);
    }

    private RegistrationStatus getParticipationStatusForUser(User user) {
        return registrations.stream()
                .filter(registration -> registration.isUserContained(user))
                .findFirst()
                .map(MeetingRegistration::getStatus)
                .orElse(RegistrationStatus.NONE);
    }

    public boolean isExpired() {
        return this.meetingTime.isBeforeNow();
    }

    public void addCurrentParticipants() {
        currentParticipants++;
    }

    public void enrichWithHashtags() {
        if (meetingHashtags != null) {
            Set<Hashtag> hashtags = meetingHashtags.stream()
                    .map(MeetingHashtag::getHashtag)
                    .collect(Collectors.toSet());
            this.assignHashtags(hashtags);
        }
    }

    public String getCategory() {
        return category.getName();
    }
}
