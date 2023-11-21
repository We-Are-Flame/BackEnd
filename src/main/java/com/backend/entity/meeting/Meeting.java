package com.backend.entity.meeting;

import com.backend.entity.base.BaseEntity;
import com.backend.entity.meeting.embeddable.MeetingAddress;
import com.backend.entity.meeting.embeddable.MeetingInfo;
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
import java.util.Optional;
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

    @Embedded
    private MeetingInfo meetingInfo;

    @Embedded
    private MeetingAddress meetingAddress;

    @Embedded
    private MeetingTime meetingTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "meeting")
    private Set<MeetingImage> meetingImages;

    @OneToMany(mappedBy = "meeting")
    private Set<MeetingHashtag> meetingHashtags;

    @OneToMany(mappedBy = "meeting")
    private List<MeetingRegistration> registrations;

    @Transient
    private User host;

    public void assignHost(User host) {
        this.host = host;
    }

    public Optional<MeetingImage> findThumbnailImage() {
        return meetingImages.stream()
                .filter(MeetingImage::getIsThumbnail)
                .findFirst();
    }

    public List<String> findTopHashtags(int limit) {
        return meetingHashtags.stream()
                .limit(limit)
                .map(MeetingHashtag::getHashtag)
                .map(Hashtag::getName)
                .collect(Collectors.toList());
    }

    public String getCategoryName() {
        return category.getName();
    }
}
