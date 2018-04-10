package com.example.sameer.geofencingapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class RegisteredActivity extends AppCompatActivity {
    SQLiteDatabase sqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);

        ListView list
                 = findViewById(R.id.listView);
        sqLiteDatabase = openOrCreateDatabase("kajsda.db", MODE_PRIVATE, null);
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM register", null);
        List<String> asd = new ArrayList<String>();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,asd);
        c.moveToFirst();
        for(int i=0;i<c.getCount();i++) {
            arrayAdapter.add(c.getString(0));
            c.moveToNext();
        }
        list.setAdapter(arrayAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sqLiteDatabase.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sqLiteDatabase.close();
    }
}
