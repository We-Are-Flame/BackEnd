-- Categories Table
CREATE TABLE categories
(
    category_id BIGINT NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255),
    PRIMARY KEY (category_id)
) ENGINE = InnoDB;

-- Settings Table
CREATE TABLE settings
(
    setting_id           BIGINT NOT NULL AUTO_INCREMENT,
    is_user_notification BIT,
    PRIMARY KEY (setting_id)
) ENGINE = InnoDB;

-- Users Table
CREATE TABLE users
(
    user_id       BIGINT NOT NULL AUTO_INCREMENT,
    temperature   INTEGER,
    email         VARCHAR(255),
    nickname      VARCHAR(255),
    profile_image VARCHAR(255),
    school_email  VARCHAR(255),
    setting_id    BIGINT,
    PRIMARY KEY (user_id),
    CONSTRAINT FK_users_setting FOREIGN KEY (setting_id)
        REFERENCES settings (setting_id) ON DELETE SET NULL
) ENGINE = InnoDB;

-- Meetings Table
CREATE TABLE meetings
(
    meeting_id           BIGINT NOT NULL AUTO_INCREMENT,
    current_participants INTEGER,
    is_evaluated         BIT    NOT NULL,
    max_participants     INTEGER,
    category_id          BIGINT NOT NULL,
    user_id              BIGINT NOT NULL,
    created_at           DATETIME(6),
    duration             BIGINT,
    end_time             DATETIME(6),
    start_time           DATETIME(6),
    description          VARCHAR(255),
    detail_location      VARCHAR(255),
    latitude             DECIMAL(10, 8),
    location             VARCHAR(255),
    longitude            DECIMAL(11, 8),
    thumbnail_url        VARCHAR(255),
    title                VARCHAR(255),
    PRIMARY KEY (meeting_id),
    CONSTRAINT FK_meetings_category FOREIGN KEY (category_id)
        REFERENCES categories (category_id) ON DELETE RESTRICT,
    CONSTRAINT FK_meetings_user FOREIGN KEY (user_id)
        REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT CHK_participants CHECK (current_participants <= max_participants)
) ENGINE = InnoDB;

-- Chat Rooms Table
CREATE TABLE chat_rooms
(
    chat_room_id   BIGINT NOT NULL AUTO_INCREMENT,
    user_count     INTEGER,
    created_at     DATETIME(6),
    meeting_id     BIGINT,
    chat_room_name VARCHAR(255),
    uuid           VARCHAR(255),
    PRIMARY KEY (chat_room_id),
    UNIQUE KEY UK_chat_rooms_meeting_id (meeting_id),
    CONSTRAINT FK_chat_rooms_meeting FOREIGN KEY (meeting_id)
        REFERENCES meetings (meeting_id) ON DELETE CASCADE
) ENGINE = InnoDB;

-- Chat Messages Table
CREATE TABLE chat_messages
(
    chat_message_id BIGINT       NOT NULL AUTO_INCREMENT,
    chat_room_id    BIGINT       NOT NULL,
    user_id         BIGINT       NOT NULL,
    created_at      DATETIME(6),
    message         VARCHAR(255) NOT NULL,
    message_type    ENUM ('ENTER','IMAGE','LEAVE','NOTICE','TALK'),
    PRIMARY KEY (chat_message_id),
    CONSTRAINT FK_chat_messages_chat_room FOREIGN KEY (chat_room_id)
        REFERENCES chat_rooms (chat_room_id) ON DELETE CASCADE,
    CONSTRAINT FK_chat_messages_user FOREIGN KEY (user_id)
        REFERENCES users (user_id) ON DELETE CASCADE
) ENGINE = InnoDB;

-- Chat Room Users Table
CREATE TABLE chat_room_users
(
    chat_room_user_id    BIGINT NOT NULL AUTO_INCREMENT,
    chat_room_id         BIGINT NOT NULL,
    user_id              BIGINT NOT NULL,
    is_owner             BIT,
    is_room_notification BIT,
    PRIMARY KEY (chat_room_user_id),
    CONSTRAINT FK_chat_room_users_chat_room FOREIGN KEY (chat_room_id)
        REFERENCES chat_rooms (chat_room_id) ON DELETE CASCADE,
    CONSTRAINT FK_chat_room_users_user FOREIGN KEY (user_id)
        REFERENCES users (user_id) ON DELETE CASCADE
) ENGINE = InnoDB;

-- Comments Table
CREATE TABLE comments
(
    comment_id  BIGINT NOT NULL AUTO_INCREMENT,
    meeting_id  BIGINT NOT NULL,
    user_id     BIGINT NOT NULL,
    created_at  DATETIME(6),
    description TEXT,
    PRIMARY KEY (comment_id),
    CONSTRAINT FK_comments_meeting FOREIGN KEY (meeting_id)
        REFERENCES meetings (meeting_id) ON DELETE CASCADE,
    CONSTRAINT FK_comments_user FOREIGN KEY (user_id)
        REFERENCES users (user_id) ON DELETE CASCADE
) ENGINE = InnoDB;

-- Hashtags Table
CREATE TABLE hashtags
(
    hashtag_id BIGINT NOT NULL AUTO_INCREMENT,
    name       VARCHAR(255),
    PRIMARY KEY (hashtag_id)
) ENGINE = InnoDB;

-- Meeting Hashtags Table
CREATE TABLE meeting_hashtags
(
    meeting_hashtag_id BIGINT NOT NULL AUTO_INCREMENT,
    meeting_id         BIGINT NOT NULL,
    hashtag_id         BIGINT NOT NULL,
    PRIMARY KEY (meeting_hashtag_id),
    CONSTRAINT FK_meeting_hashtags_meeting FOREIGN KEY (meeting_id)
        REFERENCES meetings (meeting_id) ON DELETE CASCADE,
    CONSTRAINT FK_meeting_hashtags_hashtag FOREIGN KEY (hashtag_id)
        REFERENCES hashtags (hashtag_id) ON DELETE CASCADE
) ENGINE = InnoDB;

-- Meeting Images Table
CREATE TABLE meeting_images
(
    meeting_image_id BIGINT NOT NULL AUTO_INCREMENT,
    meeting_id       BIGINT NOT NULL,
    image_url        VARCHAR(255),
    PRIMARY KEY (meeting_image_id),
    CONSTRAINT FK_meeting_images_meeting FOREIGN KEY (meeting_id)
        REFERENCES meetings (meeting_id) ON DELETE CASCADE
) ENGINE = InnoDB;

-- Meeting Registrations Table
CREATE TABLE meeting_registrations
(
    meeting_registration_id BIGINT NOT NULL AUTO_INCREMENT,
    meeting_id              BIGINT NOT NULL,
    user_id                 BIGINT NOT NULL,
    role                    ENUM ('MEMBER','OWNER'),
    status                  ENUM ('ACCEPTED','NONE','PENDING','REJECTED'),
    PRIMARY KEY (meeting_registration_id),
    CONSTRAINT FK_meeting_registrations_meeting FOREIGN KEY (meeting_id)
        REFERENCES meetings (meeting_id) ON DELETE CASCADE,
    CONSTRAINT FK_meeting_registrations_user FOREIGN KEY (user_id)
        REFERENCES users (user_id) ON DELETE CASCADE
) ENGINE = InnoDB;

-- Notifications Table
CREATE TABLE notifications
(
    notification_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id         BIGINT NOT NULL,
    is_read         BIT    NOT NULL,
    created_at      DATETIME(6),
    content         VARCHAR(255),
    type            ENUM ('EVALUATE_REQUEST','MEETING_ACCEPTED','MEETING_REJECTED','MEETING_REQUEST'),
    PRIMARY KEY (notification_id),
    CONSTRAINT FK_notifications_user FOREIGN KEY (user_id)
        REFERENCES users (user_id) ON DELETE CASCADE
) ENGINE = InnoDB;

-- 카테고리 이름으로 빠른 검색을 위한 인덱스
CREATE INDEX idx_categories_name ON categories (name);

-- 카테고리별 모임 조회 및 시간 기반 정렬을 위한 복합 인덱스
CREATE INDEX idx_meetings_category_start_end ON meetings (category_id, start_time, end_time);

-- 특정 모임의 해시태그를 빠르게 조회하기 위한 인덱스
CREATE INDEX idx_meeting_hashtags_meeting ON meeting_hashtags (meeting_id);

-- 해시태그 이름으로 빠른 검색을 위한 인덱스
CREATE INDEX idx_hashtags_name ON hashtags (name);

-- 이메일을 통한 사용자 조회 성능 향상을 위한 인덱스
CREATE INDEX idx_users_email ON users (email);

-- 모임 생성 시간 기반 조회 및 정렬을 위한 인덱스
CREATE INDEX idx_meetings_created_at ON meetings (created_at);

-- 사용자별 모임 참여 현황을 빠르게 조회하기 위한 인덱스
CREATE INDEX idx_meeting_registrations_user ON meeting_registrations (user_id, status);

-- 채팅방별 메시지 조회 성능 향상을 위한 인덱스
CREATE INDEX idx_chat_messages_room_created ON chat_messages (chat_room_id, created_at);

-- 사용자별 알림 조회 성능 향상을 위한 인덱스
CREATE INDEX idx_notifications_user_created ON notifications (user_id, created_at);