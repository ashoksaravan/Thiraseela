package com.ashoksm.thiraseela.ui;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ashoksm.thiraseela.R;
import com.ashoksm.thiraseela.utils.ImageDownloader;


public class MainActivity extends AppCompatActivity {

    private static final String URL = "http://thiraseela.com/thiraandroidapp/images/home_bg.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button artists = (Button) findViewById(R.id.artists);
        Button troupes = (Button) findViewById(R.id.troupes);
        Button academy = (Button) findViewById(R.id.academy);
        Button events = (Button) findViewById(R.id.events);
        Button aboutUS = (Button) findViewById(R.id.about_us);

        ImageView homeBG = (ImageView) findViewById(R.id.home_bg);
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ImageDownloader downloader = ImageDownloader.getInstance(am.getMemoryClass());
        Bitmap bitmap = downloader.getBitmapFromMemCache(URL);
        if (bitmap != null) {
            homeBG.setImageBitmap(bitmap);
        } else {
            downloader.download(URL, homeBG, null, null);
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        artists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ArtistListActivity.class);
            }
        });

        troupes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(TroupesListActivity.class);
            }
        });

        academy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AcademyListActivity.class);
            }
        });

        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(EventsListActivity.class);
            }
        });

        aboutUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AboutUSActivity.class);
            }
        });
    }

    private void startActivity(Class clazz) {
        Intent intent = new Intent(getApplicationContext(), clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_out_left, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.slide_in_left, 0);
    }
}
