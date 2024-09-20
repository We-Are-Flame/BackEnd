package com.backend.before.dto.meeting.response;

import com.backend.before.dto.meeting.response.output.TimeOutput;
import java.time.LocalDateTime;

public enum TimeStatus {
    FINISHED,
    IN_PROGRESS,
    NOT_STARTED;

    public static TimeStatus determineTimeStatus(TimeOutput timeOutput) {
        LocalDateTime startTime = timeOutput.getStartTime();
        LocalDateTime endTime = timeOutput.getEndTime();

        LocalDateTime now = LocalDateTime.now();

        if (endTime.isBefore(now)) {
            return TimeStatus.FINISHED;
        } else if (startTime.isBefore(now) && endTime.isAfter(now)) {
            return TimeStatus.IN_PROGRESS;
        } else {
            return TimeStatus.NOT_STARTED;
        }
    }
}
