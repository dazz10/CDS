package com.makers.tech;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
        progress();

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
