package com.dimordovin.uploadservice.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.InputStream;

public class S3Service {

    private AmazonS3 amazonS3;
    public static final String S3BUCKET = System.getenv("AWS_S3_BUCKET");

    public S3Service() {
        String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
        String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");

        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        amazonS3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(System.getenv("AWS_REGION")).build();
        amazonS3.createBucket(S3BUCKET);
    }

    public void save(InputStream file, String filename) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(S3BUCKET, filename, file, null);
        putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
        amazonS3.putObject(putObjectRequest);
    }

    public void delete(String filename) {
        amazonS3.deleteObject(S3BUCKET, filename);
    }
}
