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

import com.ashoksm.thiraseela.dto.TroupeDetailDTO;
import com.ashoksm.thiraseela.wsclient.WSClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;


public class TroupesDetailActivity extends AppCompatActivity implements SimpleGestureFilter.SimpleGestureListener {

    private SimpleGestureFilter detector;

    private int i;

    private TextView programName;

    private TextView address;

    private TextView mobile;

    private TextView phone;

    private TextView email;

    private TextView web;

    private TextView about;

    private ImageView performerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troupes_detail);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        setSupportActionBar(toolbar);

        String troupeId = getIntent().getStringExtra(TroupesListActivity.EXTRA_TROUPE_ID);
        i = Integer.valueOf(troupeId);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(TroupesListActivity.TROUPE_LIST_DTOS.get(i).getName());
        }

        programName = (TextView) findViewById(R.id.programName);
        address = (TextView) findViewById(R.id.address);
        mobile = (TextView) findViewById(R.id.mobile);
        phone = (TextView) findViewById(R.id.phone);
        email = (TextView) findViewById(R.id.email);
        web = (TextView) findViewById(R.id.web);
        about = (TextView) findViewById(R.id.performer_profile);
        performerImage = (ImageView) findViewById(R.id.performer_img);
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
                if (i != TroupesListActivity.TROUPE_LIST_DTOS.size() - 1) {
                    i++;
                    loadDetails();
                }
                break;
        }
    }

    @Override
    public void onDoubleTap() {
    }

    private void loadDetails() {
        new AsyncTask<Void, Void, Void>() {
            TroupeDetailDTO troupeDetailDTO = new TroupeDetailDTO();
            LinearLayout progressLayout = (LinearLayout) findViewById(R.id.progressLayout);
            ScrollView contentLayout = (ScrollView) findViewById(R.id.contentLayout);

            @Override
            protected void onPreExecute() {
                // SHOW THE SPINNER WHILE LOADING FEEDS
                progressLayout.setVisibility(View.VISIBLE);
                contentLayout.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                ObjectMapper mapper = new ObjectMapper();
                String response = WSClient.execute(String.valueOf(TroupesListActivity.TROUPE_LIST_DTOS.get(i).getId()), "http://thiraseela.com/thiraandroidapp/troupedetailservice.php");
                Log.d("response", response);

                try {
                    troupeDetailDTO = mapper.readValue(response, TroupeDetailDTO.class);
                } catch (IOException e) {
                    Log.e("TroupesDetailActivity", e.getLocalizedMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                about.setText(troupeDetailDTO.getAbout());
                programName.setText(TroupesListActivity.TROUPE_LIST_DTOS.get(i).getPrgrmName());
                performerImage.setImageResource(R.mipmap.ic_launcher);
                if (troupeDetailDTO.getAddress() != null && troupeDetailDTO.getAddress().trim().length() > 0) {
                    address.setText(troupeDetailDTO.getAddress());
                } else {
                    address.setVisibility(View.GONE);
                }
                if (troupeDetailDTO.getMobile() != null && troupeDetailDTO.getMobile().trim().length() > 0) {
                    mobile.setText(troupeDetailDTO.getMobile());
                } else {
                    mobile.setVisibility(View.GONE);
                }
                if (troupeDetailDTO.getPhone() != null && troupeDetailDTO.getPhone().trim().length() > 0) {
                    phone.setText(troupeDetailDTO.getPhone());
                } else {
                    phone.setVisibility(View.GONE);
                }
                if (troupeDetailDTO.getEmail() != null && troupeDetailDTO.getEmail().trim().length() > 0) {
                    email.setText(troupeDetailDTO.getEmail());
                } else {
                    email.setVisibility(View.GONE);
                }
                if (troupeDetailDTO.getWebsite() != null && troupeDetailDTO.getWebsite().trim().length() > 0) {
                    web.setText(troupeDetailDTO.getWebsite());
                } else {
                    web.setVisibility(View.GONE);
                }
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(TroupesListActivity.TROUPE_LIST_DTOS.get(i).getName());
                }
                new DownloadImageTask(performerImage).execute("http://thiraseela.com/gleimo/Troupes/images/truopes" + TroupesListActivity.TROUPE_LIST_DTOS.get(i).getId() + "/thumb/logo.jpeg");
                // HIDE THE SPINNER WHILE LOADING FEEDS
                progressLayout.setVisibility(View.GONE);
                contentLayout.setVisibility(View.VISIBLE);
            }
        }.execute();
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
