use kitching_db;

-- 외래 키 체크 비활성화
SET FOREIGN_KEY_CHECKS = 0;

-- 테이블 삭제
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS settings;
DROP TABLE IF EXISTS meetings;
DROP TABLE IF EXISTS chat_rooms;
DROP TABLE IF EXISTS chat_messages;
DROP TABLE IF EXISTS chat_room_users;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS hashtags;
DROP TABLE IF EXISTS meeting_hashtags;
DROP TABLE IF EXISTS meeting_images;
DROP TABLE IF EXISTS meeting_registrations;
DROP TABLE IF EXISTS notifications;

-- 외래 키 체크 다시 활성화
SET FOREIGN_KEY_CHECKS = 1;