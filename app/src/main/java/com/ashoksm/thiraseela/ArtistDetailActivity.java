package com.ashoksm.thiraseela;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ashoksm.thiraseela.dto.ArtistDetailDTO;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        setSupportActionBar(toolbar);

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
        loadDetails();
        // Detect touched area
        detector = new SimpleGestureFilter(this, this);
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

            @Override
            protected void onPreExecute() {
                if (getSupportActionBar() != null && ArtistListActivity.ARTIST_LIST_VOS.get(i) != null) {
                    getSupportActionBar().setTitle(ArtistListActivity.ARTIST_LIST_VOS.get(i).getName());
                }
                // SHOW THE SPINNER WHILE LOADING FEEDS
                progressLayout.setVisibility(View.VISIBLE);
                contentLayout.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                ObjectMapper mapper = new ObjectMapper();
                String response = WSClient.execute(String.valueOf(ArtistListActivity.ARTIST_LIST_VOS.get(i).getId()), "http://thiraseela.com/thiraandroidapp/performerdetailservice.php");
                Log.d("response", response);

                try {
                    artistDetailDTO = mapper.readValue(response, ArtistDetailDTO.class);
                } catch (IOException e) {
                    Log.e("ArtistDetailActivity", e.getLocalizedMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                about.setText(artistDetailDTO.getAbout());
                performerImage.setImageResource(R.mipmap.ic_launcher);
                designation.setText(ArtistListActivity.ARTIST_LIST_VOS.get(i).getTitle());
                location.setText(artistDetailDTO.getPlace() + ", " + artistDetailDTO.getCity());
                if (artistDetailDTO.getAddress() != null && artistDetailDTO.getAddress().trim().length() > 0) {
                    address.setText(artistDetailDTO.getAddress());
                } else {
                    address.setVisibility(View.GONE);
                }
                if (artistDetailDTO.getMobile() != null && artistDetailDTO.getMobile().trim().length() > 0) {
                    mobile.setText(artistDetailDTO.getMobile());
                } else {
                    mobile.setVisibility(View.GONE);
                }
                if (artistDetailDTO.getPhone() != null && artistDetailDTO.getPhone().trim().length() > 0) {
                    phone.setText(artistDetailDTO.getPhone());
                } else {
                    phone.setVisibility(View.GONE);
                }
                if (artistDetailDTO.getEmail() != null && artistDetailDTO.getEmail().trim().length() > 0) {
                    email.setText(artistDetailDTO.getEmail());
                } else {
                    email.setVisibility(View.GONE);
                }
                if (artistDetailDTO.getWebsite() != null && artistDetailDTO.getWebsite().trim().length() > 0) {
                    web.setText(artistDetailDTO.getWebsite());
                } else {
                    web.setVisibility(View.GONE);
                }
                new DownloadImageTask(performerImage).execute("http://thiraseela.com/gleimo/performers/images/perfomr" + ArtistListActivity.ARTIST_LIST_VOS.get(i).getId() + "/Perfmr_img.jpeg");
                // HIDE THE SPINNER WHILE LOADING FEEDS
                progressLayout.setVisibility(View.GONE);
                contentLayout.setVisibility(View.VISIBLE);
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
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
