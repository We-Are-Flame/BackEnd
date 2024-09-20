package com.backend.meeting.domain.meeting.entity;

import com.backend.before.entity.base.BaseEntity;
import com.backend.meeting.domain.category.entity.Category;
import com.backend.meeting.domain.hashtag.entity.Hashtag;
import com.backend.meeting.domain.hashtag.entity.MeetingHashtag;
import com.backend.meeting.domain.image.entity.MeetingImage;
import com.backend.before.entity.user.User;
import com.backend.registration.entity.MeetingRegistration;
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
    private Integer currentParticipants = 1;
    private Integer maxParticipants;

    @Builder.Default
    @Column(name = "is_evaluated", nullable = false)
    private Boolean isEvaluated = false;

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

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
    private Set<MeetingImage> meetingImages;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
    private Set<MeetingHashtag> meetingHashtags;

    @Builder.Default
    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
    private List<MeetingRegistration> registrations = new ArrayList<>();

    @Transient
    private Set<Hashtag> hashtags;

    public boolean isExpired() {
        return this.meetingTime.isBeforeNow();
    }

    public void addCurrentParticipants() {
        currentParticipants++;
    }

    public String getCategory() {
        return category.getName();
    }
}
