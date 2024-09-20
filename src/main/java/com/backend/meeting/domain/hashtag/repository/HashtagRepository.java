package com.backend.meeting.domain.hashtag.repository;

import com.backend.meeting.domain.hashtag.entity.Hashtag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long>, HashtagRepositoryCustom {
    Optional<Hashtag> findByName(String name);
}
