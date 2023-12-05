package com.backend.repository.meeting.meeting;

import com.backend.entity.meeting.QMeeting;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.DateTimePath;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
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

    public static OrderSpecifier[] createPageableSort(Pageable pageable) {
        return pageable.getSort().stream()
                .flatMap(order -> CustomSort.fromString(order.getProperty()).toOrderSpecifiers()
                        .stream())
                .toArray(OrderSpecifier[]::new);
    }

    private static CustomSort fromString(String key) {
        return switch (key.toLowerCase()) {
            case "new" -> NEW;
            case "title" -> TITLE;
            case "soon" -> SOON;
            default -> DEFAULT;
        };
    }

    private List<OrderSpecifier<?>> toOrderSpecifiers() {
        return switch (this) {
            case NEW -> createOrderSpecifiers(QMeeting.meeting.createdAt, Order.DESC);
            case TITLE -> createOrderSpecifiers(QMeeting.meeting.title, Order.ASC);
            case SOON -> createSoonOrderSpecifiers();
            default -> List.of(new OrderSpecifier<>(Order.ASC, QMeeting.meeting.id));
        };
    }

    private List<OrderSpecifier<?>> createOrderSpecifiers(ComparableExpressionBase<?> path, Order order) {
        DateTimePath<LocalDateTime> endTimePath = QMeeting.meeting.meetingTime.endTime;
        LocalDateTime now = LocalDateTime.now();

        // 현재 시간을 지난 endTime에 대한 정렬 우선순위 설정
        OrderSpecifier<?> prioritySpecifier = new CaseBuilder()
                .when(endTimePath.before(now))
                .then(2)
                .otherwise(1)
                .asc();

        // 원래의 정렬 순서를 유지하면서 endTime 정렬 순위 적용
        OrderSpecifier<?> originalOrderSpecifier = new OrderSpecifier<>(order, path);

        return List.of(prioritySpecifier, originalOrderSpecifier);
    }

    private List<OrderSpecifier<?>> createSoonOrderSpecifiers() {
        DateTimePath<LocalDateTime> startTimePath = QMeeting.meeting.meetingTime.startTime;
        DateTimePath<LocalDateTime> endTimePath = QMeeting.meeting.meetingTime.endTime;
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
