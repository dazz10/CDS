package com.makers.tech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.SignInUIOptions;
import com.amazonaws.mobile.client.UserState;
import com.amazonaws.mobile.client.UserStateDetails;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Button button;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }


    public void toSign(View view) {
        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails result) {
                Log.i(TAG,result.getUserState().toString());
                if(result.getUserState() == UserState.SIGNED_OUT) {
                    showSignOut();
                }
                AWSMobileClient.getInstance().signOut();
                showSignIn();
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG,e.toString());
            }
        });
    }

    public void showSignOut() {
        try {
            AWSMobileClient.getInstance().signOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSignIn() {
        try {
            AWSMobileClient.getInstance().showSignIn(this, SignInUIOptions.builder().nextActivity(MenuActivity.class).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
