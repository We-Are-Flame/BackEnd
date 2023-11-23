package com.backend.entity.meeting;

import com.backend.entity.base.BaseEntity;
import com.backend.entity.meeting.embeddable.MeetingAddress;
import com.backend.entity.meeting.embeddable.MeetingTime;
import com.backend.entity.user.User;
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

    @OneToMany(mappedBy = "meeting")
    private Set<MeetingImage> meetingImages;

    @OneToMany(mappedBy = "meeting")
    private Set<MeetingHashtag> meetingHashtags;

    @OneToMany(mappedBy = "meeting")
    private List<MeetingRegistration> registrations;

    @Transient
    private Set<Hashtag> hashtags;

    public void assignHashtags(Set<Hashtag> hashtags) {
        this.hashtags = hashtags;
    }
}
