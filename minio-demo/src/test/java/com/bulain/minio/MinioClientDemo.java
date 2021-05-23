package com.bulain.minio;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.ServerSideEncryption;
import io.minio.messages.Bucket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MinioClientDemo {

    @Test
    public void testMinioClient() throws Exception {
        MinioClient client = new MinioClient("http://127.0.0.1:9000", "2QXZRL605WFR1RTU9BNE",
                "2vGGELyPnfyjV+4roYTMlvKMlK+KpaG7VGBku+GR");

        List<Bucket> listBuckets = client.listBuckets();
        log.debug("{}", listBuckets);

        String bucketName = "mime";
        String objectName = Long.toString(new Date().getTime());
        InputStream stream = new ClassPathResource("images/test.jpg").getInputStream();
        Long size = null;
        Map<String, String> headerMap = new HashMap<String, String>();
        ServerSideEncryption sse = null;
        String contentType = "image/jpeg";
        client.putObject(bucketName, objectName, stream, size, headerMap, sse, contentType);

        ObjectStat statObject = client.statObject(bucketName, objectName);
        log.debug("{}", statObject);
        
        InputStream object = client.getObject(bucketName, objectName);
        log.debug("{}", object);
        
    }
    
}
