package com.bulain.minio;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Slf4j
public class AwsS3Demo {

    @Test
    public void testS3() throws Exception {

        AWSCredentials credentials = new BasicAWSCredentials("13y7GNg1XUJNSy5UHT",
                "chXbnWNwOIpgkzNQq9FchbT146e16cZqDLiQ");
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setSignerOverride("AWSS3V4SignerType");

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration("http://localhost:8333", Regions.US_EAST_1.name()))
                .withPathStyleAccessEnabled(true).withClientConfiguration(clientConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();

        List<Bucket> listBuckets = s3Client.listBuckets();
        log.debug("{}", listBuckets);

        String bucketName = "mime";
        String file = "images/test.jpg";
        String key = "g00/" + new Date().getTime() + "." + FilenameUtils.getExtension(file);
        ClassPathResource resource = new ClassPathResource(file);
        InputStream input = resource.getInputStream();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(resource.contentLength());
        PutObjectResult putObject = s3Client.putObject(bucketName, key, input, metadata);
        log.debug("{}", putObject);

        ObjectMetadata objectMetadata = s3Client.getObjectMetadata(bucketName, key);
        log.debug("{}", objectMetadata);

        S3Object object = s3Client.getObject(bucketName, key);
        log.debug("{}", object);

    }

}
