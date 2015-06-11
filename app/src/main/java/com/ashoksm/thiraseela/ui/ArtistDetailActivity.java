package com.ashoksm.thiraseela.ui;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ashoksm.thiraseela.R;
import com.ashoksm.thiraseela.dto.ArtistDetailDTO;
import com.ashoksm.thiraseela.utils.ImageDownloader;
import com.ashoksm.thiraseela.utils.SimpleGestureFilter;
import com.ashoksm.thiraseela.wsclient.WSClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;


public class ArtistDetailActivity extends AppCompatActivity implements SimpleGestureFilter.SimpleGestureListener {

    private SimpleGestureFilter detector;
    private int i;
    private TextView about;
    private ImageView performerImage;
    private TextView designation;
    private TextView location;
    private TextView address;
    private TextView mobile;
    private TextView phone;
    private TextView email;
    private TextView web;
    private Bitmap placeHolderImage;
    private CardView aboutView;
    private RelativeLayout addressLayout;
    private boolean networkAvailable = true;
    private ImageDownloader imageDownloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigation_arrow_back);
        setSupportActionBar(toolbar);
        placeHolderImage = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);

        String name = getIntent().getStringExtra(ArtistListActivity.EXTRA_PERFORMER_NAME);
        i = Integer.valueOf(name);
        about = (TextView) findViewById(R.id.performer_profile);
        performerImage = (ImageView) findViewById(R.id.performer_img);
        designation = (TextView) findViewById(R.id.designation);
        location = (TextView) findViewById(R.id.location);
        address = (TextView) findViewById(R.id.address);
        mobile = (TextView) findViewById(R.id.mobile);
        phone = (TextView) findViewById(R.id.phone);
        email = (TextView) findViewById(R.id.email);
        web = (TextView) findViewById(R.id.web);
        aboutView = (CardView) findViewById(R.id.aboutView);
        addressLayout = (RelativeLayout) findViewById(R.id.addressLayout);
        loadDetails();
        // Detect touched area
        detector = new SimpleGestureFilter(this, this);
        Button retryButton = (Button) findViewById(R.id.retryButton);
        retryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                loadDetails();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent me) {
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public void onSwipe(int direction) {

        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT:
                if (i != 0) {
                    i--;
                    loadDetails();
                }
                break;
            case SimpleGestureFilter.SWIPE_LEFT:
                if (i != ArtistListActivity.ARTIST_LIST_VOS.size() - 1) {
                    i++;
                    loadDetails();
                }
                break;
        }
    }

    private void loadDetails() {
        new AsyncTask<Void, Void, Void>() {
            ArtistDetailDTO artistDetailDTO = new ArtistDetailDTO();
            LinearLayout progressLayout = (LinearLayout) findViewById(R.id.progressLayout);
            ScrollView contentLayout = (ScrollView) findViewById(R.id.contentLayout);
            LinearLayout retryLayout = (LinearLayout) findViewById(R.id.timeoutLayout);

            @Override
            protected void onPreExecute() {
                if (getSupportActionBar() != null && ArtistListActivity.ARTIST_LIST_VOS.get(i) != null) {
                    getSupportActionBar().setTitle(ArtistListActivity.ARTIST_LIST_VOS.get(i).getName());
                }
                // SHOW THE SPINNER WHILE LOADING FEEDS
                progressLayout.setVisibility(View.VISIBLE);
                contentLayout.setVisibility(View.GONE);
                retryLayout.setVisibility(View.GONE);
                networkAvailable = true;
            }

            @Override
            protected Void doInBackground(Void... params) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    String response = WSClient.execute(String.valueOf(ArtistListActivity.ARTIST_LIST_VOS.get(i).getId()), "http://thiraseela.com/thiraandroidapp/performerdetailservice.php");
                    Log.d("response", response);
                    artistDetailDTO = mapper.readValue(response, ArtistDetailDTO.class);
                } catch (IOException e) {
                    networkAvailable = false;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if(networkAvailable) {
                    if (artistDetailDTO.getAbout() != null && artistDetailDTO.getAbout().trim().length() > 0) {
                        aboutView.setVisibility(View.VISIBLE);
                        about.setText(artistDetailDTO.getAbout().replaceAll("\\\\\"", "\"").replaceAll("\\\\'", "'"));
                    } else {
                        aboutView.setVisibility(View.GONE);
                    }
                    performerImage.setImageResource(R.mipmap.ic_launcher);
                    designation.setText(ArtistListActivity.ARTIST_LIST_VOS.get(i).getTitle());
                    location.setText(artistDetailDTO.getPlace() + ", " + artistDetailDTO.getCity());
                    if (artistDetailDTO.getAddress() != null && artistDetailDTO.getAddress().trim().length() > 0) {
                        addressLayout.setVisibility(View.VISIBLE);
                        address.setText(artistDetailDTO.getAddress().replaceAll("\\\\\"", "\"").replaceAll("\\\\'", "'"));
                    } else {
                        addressLayout.setVisibility(View.GONE);
                    }
                    if (artistDetailDTO.getMobile() != null && artistDetailDTO.getMobile().trim().length() > 0) {
                        mobile.setVisibility(View.VISIBLE);
                        mobile.setText(artistDetailDTO.getMobile());
                    } else {
                        mobile.setVisibility(View.GONE);
                    }
                    if (artistDetailDTO.getPhone() != null && artistDetailDTO.getPhone().trim().length() > 0) {
                        phone.setVisibility(View.VISIBLE);
                        phone.setText(artistDetailDTO.getPhone());
                    } else {
                        phone.setVisibility(View.GONE);
                    }
                    if (artistDetailDTO.getEmail() != null && artistDetailDTO.getEmail().trim().length() > 0) {
                        email.setVisibility(View.VISIBLE);
                        email.setText(artistDetailDTO.getEmail());
                    } else {
                        email.setVisibility(View.GONE);
                    }
                    if (artistDetailDTO.getWebsite() != null && artistDetailDTO.getWebsite().trim().length() > 0) {
                        web.setVisibility(View.VISIBLE);
                        web.setText(artistDetailDTO.getWebsite());
                    } else {
                        web.setVisibility(View.GONE);
                    }
                    String url = "http://thiraseela.com/gleimo/performers/images/perfomr" + ArtistListActivity.ARTIST_LIST_VOS.get(i).getId() + "/Perfmr_img.jpeg";
                    if(imageDownloader == null) {
                        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                        int memClassBytes = am.getMemoryClass();
                        imageDownloader = new ImageDownloader(memClassBytes);
                    }
                    Bitmap bitmap = imageDownloader.getBitmapFromMemCache(url);
                    if (bitmap == null) {
                        imageDownloader.download(url, performerImage, getResources(), placeHolderImage);
                    } else {
                        performerImage.setImageBitmap(bitmap);
                    }
                    // HIDE THE SPINNER WHILE LOADING FEEDS
                    progressLayout.setVisibility(View.GONE);
                    contentLayout.setVisibility(View.VISIBLE);
                    retryLayout.setVisibility(View.GONE);
                } else {
                    progressLayout.setVisibility(View.GONE);
                    contentLayout.setVisibility(View.GONE);
                    retryLayout.setVisibility(View.VISIBLE);
                }
            }
        }.execute();
    }

    @Override
    public void onDoubleTap() {
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
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                getApplicationContext().startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
