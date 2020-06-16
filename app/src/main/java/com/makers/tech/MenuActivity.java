package com.makers.tech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void toUpload(View view) {
        Intent upIntent = new Intent(MenuActivity.this,UploadActivity.class);
        startActivity(upIntent);
    }

    public void toScan(View view) {
        Intent scIntent = new Intent(MenuActivity.this,ScanActivity.class);
        startActivity(scIntent);
    }


}
