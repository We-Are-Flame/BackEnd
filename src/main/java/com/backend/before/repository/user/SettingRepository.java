package com.backend.before.repository.user;

import com.backend.before.entity.user.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingRepository extends JpaRepository<Setting, Long> {
}
