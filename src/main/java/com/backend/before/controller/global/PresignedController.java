package com.backend.before.controller.global;

import com.backend.before.dto.presigned.ImageRequestList;
import com.backend.before.dto.presigned.ImageResponseList;
import com.backend.before.service.presigned.S3Service;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "이미지 업로드 URL(Presigned URL) 제공 API", description = "이미지 저장 관련 API입니다. Object Stoage에 대한 Presigned URL 및 image_url을 제공합니다.")
@RestController
@RequestMapping("/api/presigned")
@RequiredArgsConstructor
public class PresignedController {
    private final S3Service s3Service;

    @PostMapping
    public ImageResponseList getPresignedUrls(@Valid @RequestBody ImageRequestList request) {
        return s3Service.createPresignedUrls(request);
    }
}
