package com.backend.meeting.common.type;

import com.backend.before.exception.BadRequestException;
import com.backend.before.exception.ErrorMessages;
import com.backend.meeting.domain.meeting.entity.QMeeting;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.function.Function;

public enum FilteringType {
    BY_TITLE(QMeeting.meeting.title::containsIgnoreCase),
    BY_HASHTAG(value -> QMeeting.meeting.meetingHashtags.any().hashtag.name.containsIgnoreCase(value));

    private final Function<String, BooleanExpression> whereClauseFunction;

    FilteringType(Function<String, BooleanExpression> whereClauseFunction) {
        this.whereClauseFunction = whereClauseFunction;
    }

    public BooleanExpression createWhereClause(String value) {
        return whereClauseFunction.apply(value);
    }

    public static FilterCriteria determineFilterCriteria(String title, String hashtag) {
        if ((title == null) == (hashtag == null)) {
            throw new BadRequestException(ErrorMessages.KEYWORD_NULL);
        }

        return title != null
                ? new FilterCriteria(BY_TITLE, title)
                : new FilterCriteria(BY_HASHTAG, hashtag);
    }
}