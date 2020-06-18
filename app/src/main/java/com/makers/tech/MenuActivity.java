package com.makers.tech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.SignInUIOptions;
import com.amazonaws.mobile.client.UserState;
import com.amazonaws.mobile.client.UserStateDetails;

public class MenuActivity extends AppCompatActivity {

    public static final String TAG = MenuActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void toUpload(View view) {
        Intent upIntent = new Intent(MenuActivity.this,TransferActivity.class);
        startActivity(upIntent);
    }

    public void toScan(View view) {
        Intent scIntent = new Intent(MenuActivity.this,ScanActivity.class);
        startActivity(scIntent);
    }

    public void toSignOut(View view) {
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
            AWSMobileClient.getInstance().showSignIn(this, SignInUIOptions.builder().nextActivity(AuthenticationActivity.class).build());
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
