package org.eugene.core.common;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.hadoop.fs.Path;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.eugene.ui.Notifier;

public class AWSS3Reader {
    public Path read(String bucketName, String keyName, String region, String accessKey, String secretKey){
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().
                withRegion(region).
                withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials)).
                build();
        try {
            S3Object object = s3.getObject(bucketName, keyName);
            InputStream inputStream = object.getObjectContent();
            String suffix = ".parquet";
            if (keyName.toLowerCase().endsWith(".orc")){
                suffix = ".orc";
            }else if (keyName.toLowerCase().endsWith(".avro")){
                suffix = ".avro";
            }
            File tmp = File.createTempFile("viewer-temp-", suffix);
            Files.copy(inputStream, tmp.toPath(), StandardCopyOption.REPLACE_EXISTING);
            inputStream.close();
            return new Path(tmp.toPath().toString());
        } catch (AmazonServiceException e) {
            e.printStackTrace();
            Notifier.errorWithException(e);
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Notifier.errorWithException(e);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            Notifier.errorWithException(e);
            return null;
        }
    }

}
