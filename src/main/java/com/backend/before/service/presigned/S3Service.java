package com.backend.before.service.presigned;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.backend.before.dto.presigned.ImageRequestList;
import com.backend.before.dto.presigned.ImageResponse;
import com.backend.before.dto.presigned.ImageResponseList;
import com.backend.before.util.etc.S3Util;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3Client amazonS3Client;
    private final S3Util s3Util;

    @Transactional(readOnly = true)
    public ImageResponseList createPresignedUrls(ImageRequestList request) {
        List<ImageResponse> urlResponses = request.imageList()
                .stream()
                .map(imageInfo -> {
                    String fileName = createFileName(imageInfo.fileName());
                    String fileType = imageInfo.fileType();
                    URL presignedUrl = createPresignedUrl(fileName, fileType);

                    String imageUrl = s3Util.createImageUrl(fileName);
                    return new ImageResponse(presignedUrl, imageUrl);
                })
                .toList();


        return new ImageResponseList(urlResponses);
    }

    private String createFileName(String originalFileName) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_" + originalFileName;
    }

    private URL createPresignedUrl(String fileName, String fileType) {
        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + 1000 * 60 * 60);
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(s3Util.getBucket(), fileName)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(expiration);

        generatePresignedUrlRequest.addRequestParameter(Headers.S3_CANNED_ACL, CannedAccessControlList.PublicRead.toString());
        generatePresignedUrlRequest.setContentType(fileType);

        return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
    }
}
