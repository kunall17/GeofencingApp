package com.example.sameer.geofencingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class Search extends AppCompatActivity {

    String id;
    EditText editText;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        id=getIntent().getExtras().getString("id");
        editText=findViewById(R.id.editText);
        editText.setText(id+" coupons");
        webView=findViewById(R.id.webView);

    }

    public void buGo(View view) {
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://www.google.co.in/search?q="+editText.getText().toString());
    }
}
