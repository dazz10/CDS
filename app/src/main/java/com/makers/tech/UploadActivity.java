package com.makers.tech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;

public class UploadActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView statusTextView;
    private int progressStatus = 0;
    private Handler progressHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        progressBar = findViewById(R.id.progress_horizontal);
        statusTextView = findViewById(R.id.status_text);



    }

    public void upload(View view) {
        getApplicationContext().startService(new Intent(getApplicationContext(),UploadActivity.class));

        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails result) {
                StringBuilder sb = new StringBuilder();
                sb.append(" ");
                sb.append(result.getUserState());
                Toast.makeText(getApplicationContext(),sb.toString(),Toast.LENGTH_SHORT).show();
                uploadWithTransferUtility();
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    private void uploadWithTransferUtility() {

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
