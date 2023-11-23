package com.backend.service.meeting;

import com.backend.entity.meeting.QMeeting;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.DateTimePath;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public enum CustomSort {
    NEW("new", Direction.DESC),
    TITLE("title", Direction.ASC),
    SOON("soon", Direction.ASC),
    DEFAULT("id", Direction.ASC);

    private final String field;
    private final Direction direction;

    CustomSort(String field, Direction direction) {
        this.field = field;
        this.direction = direction;
    }

    public static Sort getSort(String sort) {
        CustomSort customSort = fromString(sort);
        return Sort.by(new Sort.Order(customSort.direction, customSort.field));
    }

    public static CustomSort fromString(String key) {
        return switch (key.toLowerCase()) {
            case "new" -> NEW;
            case "title" -> TITLE;
            case "soon" -> SOON;
            default -> DEFAULT;
        };
    }

    public List<OrderSpecifier<?>> toOrderSpecifiers(QMeeting meeting) {
        return switch (this) {
            case NEW -> List.of(new OrderSpecifier<>(Order.DESC, meeting.createdAt));
            case TITLE -> List.of(new OrderSpecifier<>(Order.ASC, meeting.title));
            case SOON -> createSoonOrderSpecifiers(meeting);
            default -> List.of(new OrderSpecifier<>(Order.ASC, meeting.id));
        };
    }

    private List<OrderSpecifier<?>> createSoonOrderSpecifiers(QMeeting meeting) {
        DateTimePath<LocalDateTime> startTimePath = meeting.meetingTime.startTime;
        DateTimePath<LocalDateTime> endTimePath = meeting.meetingTime.endTime;
        LocalDateTime now = LocalDateTime.now();

        // 현재 시간과 비교하여 우선순위 정렬
        OrderSpecifier<?> prioritySpecifier = new CaseBuilder()
                .when(startTimePath.after(now))
                .then(1)
                .when(endTimePath.before(now))
                .then(2)
                .otherwise(3)
                .asc();

        // 현재 시간 이후인 startTime에 대한 오름차순 정렬
        OrderSpecifier<?> startTimeSpecifier = new OrderSpecifier<>(Order.ASC, startTimePath);

        // 현재 시간 이전인 endTime에 대한 내림차순 정렬
        OrderSpecifier<?> endTimeSpecifier = new OrderSpecifier<>(Order.DESC, endTimePath);

        return List.of(prioritySpecifier, startTimeSpecifier, endTimeSpecifier);
    }
}
