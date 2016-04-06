package com.example.mipo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class ViewUserPic extends AppCompatActivity {

    private WebView user_image;
    private String picUrl;
    private Button user_pic_back_butt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_pic);

        user_pic_back_butt=(Button)findViewById(R.id.user_pic_back_butt);
        user_pic_back_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        picUrl=getIntent().getStringExtra("picUrl");
        user_image = (WebView) findViewById (R.id.user_pic_wv);
        user_image.setBackgroundColor(0xff000000);
        user_image.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        user_image.getSettings().setBuiltInZoomControls(true);
        user_image.getSettings().setSupportZoom(true);
        user_image.getSettings().setDisplayZoomControls(false);
        user_image.getSettings().setJavaScriptEnabled(true);
        user_image.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        String html="<!DOCTYPE HTML> <html> <head> <style type=\"text/css\"> html, body { width: 100%; height: 100%; margin: 0px; padding: 0px; } </style> </head> <body> <img src='{IMAGE_PLACEHOLDER}' alt=\"starting\" style=\"height:100%;width:100%\" /> </body> </html> ";
        html = html.replace("{IMAGE_PLACEHOLDER}", picUrl);
        user_image.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", "");
    }
}
