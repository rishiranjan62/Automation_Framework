package com.rishi.utility;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.transcribe.AmazonTranscribe;
import com.amazonaws.services.transcribe.AmazonTranscribeClient;
import com.amazonaws.services.transcribe.model.*;
import java.io.File;


public class AwsHelper {
    public static final String AWS_ACCESS_KEY = "ASIAY35AGZPSYLVAKMHF";

    public static final String AWS_SECRET_KEY = "4UyKGiyqiVf7a/JeufdvMmaRoAetWQfMDdCCatgN";

    public static final String AWS_Session_Token="IQoJb3JpZ2luX2VjELj//////////wEaCXVzLWVhc3QtMSJGMEQCICxhVvQXcXOY3FPBEOZHUSPdi2DJivLy6vYemZz+yC7pAiB+puPdrEfZKm9PBHgw4b8V1unFzpsiVr19zicxMU0MnyqOAwjx//////////8BEAMaDDYwOTY4NDQ3NDg1MyIMSqG6s8RT1oCtzs4TKuICkLqllR67cLipJrZO3wToCAdI7/DTr6fTxIEkOdA1tkW87tyD9oVbq1ZPBHoRBUxmzOcpG7ElosUTWhmoYMMThpHORHtbg3Tio7tamJiiG1a76TVcvdj5E2okHHHCm+Xnk4CVG+pgQFq8xJ0eaDQyNaqlkgB0TbOT2XiJQoYZvvwr1V+zfLZJV3wJxBSxqgtUfhgUXqUQlftKCROZcxhZDt2zvRYKFg6ipMP/j5W2X8IgkI6aUW1+M5XAZscr4tSMCCo5cBimLYZkcCsyHoIesSRHyAQ7HRl+8hhvkqMpRE+Doh01mk2ClUgP7LDybuqpO3IhCwRe6hTvjUnU/D6M2VG49Gyyd757m/b0pTD378el/5zznBJkxy90vGGViMCVzeRS6Tb7x25QUhqPjR6PEmwsoj0Q4oFhYehIX8qRXbbuKeJ6BMO0fIz16gct5V6T2ahdVeF+zp/WGFWWoZxRmLDtMI677aMGOqcBS/H0I0CzQFZSwtC7ZvlxIw1ooW14rEJ/BnykkKECZX4T87qqIyjL1vvQx9/UWGQIjReCLDiu1Bv3bpjV1YPyp2cu9yvv3OK6DrVjQuU7fs/zO4QX5NZVJG24v18p6PIT88YtWGCL0b24et4S2kfoQbBvZFBlUFdR5fL2mE8Z6xHhEuMcf8gan6VDYO3bs7Ii+OhsRYjrxQ1lOym4lh2qlDVD3BJzbv4=";
    static{
        System.setProperty("aws.accessKeyId", AWS_ACCESS_KEY);
        System.setProperty("aws.secretKey"  , AWS_SECRET_KEY);
        System.setProperty("aws.sessionToken",AWS_Session_Token);
    }

    public static void main(String[] args) throws Exception {

        String bucketName = "ctt-recordings";
        String filePath= "s3://zuma-assets/voicemails/TheRetreatAtMountainBrook.mp3";
        AwsHelper aws=new AwsHelper();
      //  aws.uploadFileToS3Bucket(bucketName,filePath);
        String fileName="callrecord_2023-03-06_08-09_Ryan__Guarin_to_ 12059734678.wav";
       // String fileName="1685017454515.mp3";
        aws.transcribeAudio("transcript2",bucketName+"/MightyCallAudio",fileName);

      //  aws.transcribeAudio1(bucketName+"/MightyCallAudio",fileName);
       // s3://ctt-recordings/MightyCallAudio/callrecord_2023-03-06_08-09_Ryan__Guarin_to_ 12059734678.wav
       // aws.deleteFileFromS3Bucket(bucketName+"/MightyCallAudio",fileName);
    }


    public void uploadFileToS3Bucket(String bucketName,String filePath){
        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(getAwsCredentrial()))
                .withRegion(Regions.US_EAST_1)
                .build();
        s3client.putObject(
                bucketName,
                filePath,
                new File(filePath));
    }

    private AWSCredentials getAwsCredentrial(){
        AWSCredentials credentials = new BasicSessionCredentials(
                AWS_ACCESS_KEY,AWS_SECRET_KEY, AWS_Session_Token
        );
        return credentials;
    }

    public String transcribeAudio(String jobName,String bucketName,String audioFilePath) throws Exception {
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion("us-east-1").withClientConfiguration(new ClientConfiguration()).withCredentials(new DefaultAWSCredentialsProviderChain() ).build();
        AmazonTranscribe client = AmazonTranscribeClient.builder().withRegion("us-east-1").build();
        StartTranscriptionJobRequest request = new StartTranscriptionJobRequest();
        request.withLanguageCode(LanguageCode.EnUS);
        Media media = new Media();
        media.setMediaFileUri(s3.getUrl(bucketName, audioFilePath).toString());
        request.withMedia(media).withMediaSampleRateHertz(8000);
        request.setTranscriptionJobName(jobName);
        request.withMediaFormat("wav");
        client.startTranscriptionJob(request);
        GetTranscriptionJobRequest jobRequest = new GetTranscriptionJobRequest();
        jobRequest.setTranscriptionJobName(jobName);
        TranscriptionJob transJob ;
        do{
            Thread.sleep(5000);
            transJob= client.getTranscriptionJob(jobRequest).getTranscriptionJob();
            System.out.println("TRANSCRIPTION "+transJob.getTranscriptionJobStatus());
        }while(!transJob.getTranscriptionJobStatus().equals("COMPLETED"));
        String transcript = transJob.getTranscript().getTranscriptFileUri();
        return transcript;
    }

    public void deleteFileFromS3Bucket(String bucketName, String fileName){
        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(getAwsCredentrial()))
                .withRegion(Regions.US_EAST_1)
                .build();
        s3client.deleteObject(bucketName,fileName);
    }


}