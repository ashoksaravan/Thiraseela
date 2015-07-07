package com.ashoksm.thiraseela.ui;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.ashoksm.thiraseela.R;
import com.ashoksm.thiraseela.adapter.TroupesPagerAdapter;
import com.ashoksm.thiraseela.utils.DepthPageTransformer;
import com.ashoksm.thiraseela.utils.ImageDownloader;


public class TroupesDetailActivity extends AppCompatActivity {

    private static final String URL = "http://thiraseela.com/thiraandroidapp/images/inner_bg.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troupes_detail);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigation_arrow_back);
        setSupportActionBar(toolbar);

        String troupeId = getIntent().getStringExtra(TroupesListActivity.EXTRA_TROUPE_ID);
        int i = Integer.valueOf(troupeId);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(TroupesListActivity.TROUPE_LIST_DTOS.get(i).getName());
        }

        ImageView innerBG = (ImageView) findViewById(R.id.inner_bg);
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ImageDownloader imageDownloader = ImageDownloader.getInstance(am.getMemoryClass());
        Bitmap bitmap = imageDownloader.getBitmapFromMemCache(URL);
        if (bitmap != null) {
            innerBG.setImageBitmap(bitmap);
        } else {
            imageDownloader.download(URL, innerBG, null, null);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.troupes_view_pager);
        TroupesPagerAdapter adapter = new TroupesPagerAdapter(TroupesListActivity.TROUPE_LIST_DTOS, this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(i);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(TroupesListActivity.TROUPE_LIST_DTOS.get(position).getName());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        viewPager.setPageTransformer(true, new DepthPageTransformer());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, 0);
    }
}
