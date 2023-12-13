package com.backend.repository.meeting.hashtag;

import com.backend.entity.meeting.Hashtag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long>, HashtagRepositoryCustom {
    Optional<Hashtag> findByName(String name);
}
