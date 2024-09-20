package com.backend.before.util.etc;

import com.backend.before.config.properties.S3Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class S3Util {
    private final S3Properties s3Properties;

    public String createImageUrl(String fileName) {
        return s3Properties.getEndpoint() + "/" + s3Properties.getBucket() + "/" + fileName;
    }

    public String getBucket() {
        return s3Properties.getBucket();
    }
}
