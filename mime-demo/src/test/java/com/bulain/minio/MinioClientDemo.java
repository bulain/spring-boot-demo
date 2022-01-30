package com.bulain.minio;

import io.minio.*;
import io.minio.messages.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.util.Date;
import java.util.List;

@Slf4j
public class MinioClientDemo {

    @Test
    public void testMinioClient() throws Exception {
        MinioClient client = MinioClient.builder()
                .endpoint("http://127.0.0.1:9000")
                .credentials("2QXZRL605WFR1RTU9BNE", "2vGGELyPnfyjV+4roYTMlvKMlK+KpaG7VGBku+GR")
                .build();

        List<Bucket> listBuckets = client.listBuckets();
        log.debug("{}", listBuckets);

        String bucketName = "mime";
        String fileName = "images/test.jpg";
        String extension = FilenameUtils.getExtension(fileName);
        String objectName = new Date().getTime() + "." + extension;
        File file = new ClassPathResource(fileName).getFile();
        String contentType = "image/jpeg";

        ObjectWriteResponse uploadObject = client.uploadObject(
                UploadObjectArgs.builder()
                        .contentType(contentType)
                        .bucket(bucketName)
                        .object(objectName)
                        .filename(file.getAbsolutePath())
                        .build());
        log.debug("{}", uploadObject);

        StatObjectResponse statObject = client.statObject(
                StatObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
        log.debug("{}", statObject);

        GetObjectResponse getObject = client.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .bucket(objectName)
                        .build());
        log.debug("{}", getObject);

    }

}
