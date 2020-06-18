package com.makers.tech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferService;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class TransferActivity extends AppCompatActivity {

    public static final String TAG = TransferActivity.class.getSimpleName();
    private ProgressBar progressBar;
    private TextView statusTextView;
    private TextView percentTextView;
    private int progressStatus = 0;
    private Handler progressHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        progressBar = findViewById(R.id.progress_horizontal);
        statusTextView = findViewById(R.id.status_text);
        percentTextView = findViewById(R.id.percent_text);


    }

    private void uploadWithTransferUtility() {
        BasicAWSCredentials credentials = new BasicAWSCredentials("AKIA5EAMDYV75SGLV24I","7s9mlFiMRjCYBeeE9k4vmVg+vUl9jDEAnENfjTHq");
        AmazonS3Client s3Client = new AmazonS3Client(credentials);

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(s3Client)
                        .build();

        String Fol = new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getPath());
        sb.append("CDS1/images");
        sb.append(".jpg");
        String imagePath = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("public/");
        sb2.append(Fol);
        sb2.append("18.04,22.405");
        TransferObserver uploadObserver = transferUtility.upload(sb2.toString(),new File(imagePath));
        uploadObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {

            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int)percentDonef;
                StringBuilder sb = new StringBuilder();
                sb.append("BytesCurrent: ");
                sb.append(bytesCurrent);
                sb.append("PercentDone: ");
                sb.append(percentDone);
                sb.append("%");
                percentTextView.setText(sb.toString());
            }

            @Override
            public void onError(int id, Exception ex) {
                    ex.printStackTrace();
            }

        });



    }

    public void upload(View view) {
        getApplicationContext().startService(new Intent(getApplicationContext(),TransferService.class));
        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails result) {
                StringBuilder sb = new StringBuilder();
                sb.append(" ");
                sb.append(result.getUserState());
                statusTextView.setText(sb.toString());
                uploadWithTransferUtility();
            }

            @Override
            public void onError(Exception e) {
                    String s = "Error"+e.toString();
                    statusTextView.setText(s);
            }
        });
    }

    public void zip(View view){
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getPath());
        sb.append("/CDS1/");
        String inputPath = sb.toString();
        StringBuilder s2 = new StringBuilder();
        s2.append(inputPath);
        s2.append("/images.jpg");
    }

    public void zip(String[] _files, String zipFileName) {
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFileName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            byte data[] = new byte[BUFFER];

            for (int i = 0; i < _files.length; i++) {
                Log.v("Compress", "Adding: " + _files[i]);
                FileInputStream fi = new FileInputStream(_files[i]);
                origin = new BufferedInputStream(fi, BUFFER);

                ZipEntry entry = new ZipEntry(_files[i].substring(_files[i].lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;

                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void progress(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                while (progressStatus < 100) {
                    progressStatus +=10;
                    //Update the progress bar and display
                    //the status
                    progressHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            statusTextView.setText(progressStatus+"/"+progressBar.getMax());
                        }
                    });
                    try {
                        //Sleep for 200 milliseconds
                        Thread.sleep(200);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }


}