package com.example.sameer.geofencingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DealsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deals);

        String id = getIntent().getStringExtra("ID");

    }
}
