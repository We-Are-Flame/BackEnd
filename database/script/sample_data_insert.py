import os
import yaml
import random
import mysql.connector
from mysql.connector import Error
from datetime import datetime, timedelta


# YAML 파일에서 데이터베이스 설정을 읽어오기, 현재 스크립트의 위치를 기준으로 상대 경로 설정
def get_db_config():
    current_dir = os.path.dirname(os.path.abspath(__file__))
    config_path = os.path.join(current_dir, '..', '..', 'config', 'application-dev.yml')

    with open(config_path, 'r', encoding='utf-8') as file:
        config = yaml.safe_load(file)

    db_config = config['spring']['datasource']
    url_parts = db_config['url'].split('/')
    database = url_parts[3].split('?')[0]

    return {
        'host': url_parts[2].split(':')[0],
        'database': database,
        'user': db_config['username'],
        'password': db_config['password']
    }


# 데이터베이스 연결을 생성
def create_connection():
    db_config = get_db_config()
    connection = None
    try:
        connection = mysql.connector.connect(
            host=db_config['host'],
            user=db_config['user'],
            passwd=db_config['password'],
            database=db_config['database']
        )
        print("MySQL Database connection successful")
    except Error as err:
        print(f"Error: '{err}'")

    return connection


# 지정된 수만큼의 샘플 사용자를 데이터베이스에 삽입
def insert_sample_users(connection, num_users):
    cursor = connection.cursor()
    for i in range(num_users):
        query = """INSERT INTO users (temperature, email, nickname, profile_image, school_email)
                   VALUES (%s, %s, %s, %s, %s)"""
        values = (
            random.randint(0, 100),
            f'user{i}@example.com',
            f'User{i}',
            f'https://example.com/profile{i}.jpg',
            f'student{i}@school.edu'
        )
        cursor.execute(query, values)
        if i % 1000 == 0:
            connection.commit()
            print(f"Inserted {i} users")
    connection.commit()
    print(f"Inserted {num_users} users")


# 미리 정의된 카테고리 목록을 데이터베이스에 삽입
def insert_sample_categories(connection):
    categories = ['스포츠', '음악', '미술', '요리',
                  '여행', '독서', '영화', '게임', '프로그래밍', '언어']
    cursor = connection.cursor()
    query = "INSERT INTO categories (name) VALUES (%s)"
    cursor.executemany(query, [(category,) for category in categories])
    connection.commit()
    print("Inserted categories")


# 지정된 수만큼의 샘플 해시태그를 데이터베이스에 삽입
def insert_sample_hashtags(connection, num_hashtags):
    cursor = connection.cursor()
    for i in range(num_hashtags):
        query = "INSERT INTO hashtags (name) VALUES (%s)"
        cursor.execute(query, (f'#tag{i}',))
        if i % 1000 == 0:
            connection.commit()
            print(f"Inserted {i} hashtags")
    connection.commit()
    print(f"Inserted {num_hashtags} hashtags")


# 지정된 수만큼의 샘플 모임을 데이터베이스에 삽입
def insert_sample_meetings(connection, num_meetings):
    cursor = connection.cursor()

    cursor.execute("SELECT COUNT(*) FROM categories")
    category_count = cursor.fetchone()[0]
    cursor.execute("SELECT COUNT(*) FROM users")
    user_count = cursor.fetchone()[0]

    if category_count == 0 or user_count == 0:
        print("Categories and Users tables must have data.")
        return

    min_date = datetime(2023, 1, 1)
    max_date = datetime(2024, 12, 31)

    for i in range(num_meetings):
        current_participants = random.randint(1, 20)
        max_participants = current_participants + random.randint(0, 20)
        category_id = random.randint(1, category_count)
        user_id = random.randint(1, user_count)

        start_time = min_date + \
                     timedelta(seconds=random.randint(
                         0, int((max_date - min_date).total_seconds())))
        duration = timedelta(minutes=random.randint(30, 210))
        end_time = start_time + duration
        created_at = start_time - timedelta(days=random.randint(0, 30))

        latitude = random.uniform(33, 38)
        longitude = random.uniform(126, 131)

        query = """INSERT INTO meetings (current_participants, is_evaluated, max_participants,
                   category_id, user_id, created_at, duration, end_time, start_time,
                   description, detail_location, latitude, location, longitude,
                   thumbnail_url, title)
                   VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"""
        values = (
            current_participants, random.choice([0, 1]), max_participants,
            category_id, user_id, created_at, duration.seconds, end_time, start_time,
            f'Sample description {i}', f'Detail location {i}', latitude,
            f'Location {i}', longitude, f'https://example.com/thumbnail{i}.jpg',
            f'Sample Meeting {i}'
        )
        cursor.execute(query, values)
        if i % 1000 == 0:
            connection.commit()
            print(f"Inserted {i} meetings")
    connection.commit()
    print(f"Inserted {num_meetings} meetings")


# 각 모임에 대해 랜덤한 수의 해시태그를 연결하여 데이터베이스에 삽입
def insert_sample_meeting_hashtags(connection):
    cursor = connection.cursor()

    cursor.execute("SELECT COUNT(*) FROM meetings")
    meeting_count = cursor.fetchone()[0]
    cursor.execute("SELECT COUNT(*) FROM hashtags")
    hashtag_count = cursor.fetchone()[0]

    for i in range(1, meeting_count + 1):
        num_hashtags = random.randint(1, 5)
        for _ in range(num_hashtags):
            query = "INSERT INTO meeting_hashtags (meeting_id, hashtag_id) VALUES (%s, %s)"
            values = (i, random.randint(1, hashtag_count))
            cursor.execute(query, values)
        if i % 1000 == 0:
            connection.commit()
            print(f"Processed {i} meetings for hashtags")
    connection.commit()
    print("Inserted meeting hashtags")


# 데이터베이스 연결을 생성하고 샘플 데이터를 삽입
def main():
    connection = create_connection()

    if connection is not None:
        insert_sample_users(connection, 10000)
        insert_sample_categories(connection)
        insert_sample_hashtags(connection, 1000)
        insert_sample_meetings(connection, 50000)
        insert_sample_meeting_hashtags(connection)

        connection.close()
        print("Data insertion completed.")
    else:
        print("Failed to connect to the database.")


if __name__ == "__main__":
    main()
