package com.backend.before.dto.registration.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BulkApplyRequest {
    private List<Long> registrationIds;
}
