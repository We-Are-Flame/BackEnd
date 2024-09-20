package com.backend.meeting.domain.hashtag.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.backend.meeting.domain.hashtag.entity.Hashtag;
import org.springframework.transaction.annotation.Transactional;
import com.backend.meeting.domain.hashtag.repository.HashtagRepository;

@Component
@RequiredArgsConstructor
public class HashtagCreator {
    private final HashtagRepository hashtagRepository;

    @Transactional
    public Hashtag findOrCreate(String name) {
        return hashtagRepository.findByName(name)
                .orElseGet(() -> createAndSaveHashtag(name));
    }

    private Hashtag createAndSaveHashtag(String name) {
        Hashtag newHashtag = Hashtag.builder()
                .name(name)
                .build();
        return hashtagRepository.save(newHashtag);
    }
}