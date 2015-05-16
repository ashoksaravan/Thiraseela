package com.ashoksm.thiraseela;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ashoksm.thiraseela.dto.AcademyDetailDTO;
import com.ashoksm.thiraseela.wsclient.WSClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;


public class AcademyDetailActivity extends AppCompatActivity implements SimpleGestureFilter.SimpleGestureListener {

    private SimpleGestureFilter detector;
    private int i;
    private TextView about;
    private ImageView performerImage;
    private TextView acadType;
    private TextView address;
    private TextView mobile;
    private TextView phone;
    private TextView email;
    private TextView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academy_detail);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        setSupportActionBar(toolbar);

        String name = getIntent().getStringExtra(AcademyListActivity.EXTRA_ACADEMY_ID);
        i = Integer.valueOf(name);
        about = (TextView) findViewById(R.id.performer_profile);
        performerImage = (ImageView) findViewById(R.id.performer_img);
        acadType = (TextView) findViewById(R.id.acadType);
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
                if (i != AcademyListActivity.ACADEMY_LIST_DTOS.size() - 1) {
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
            AcademyDetailDTO academyDetailDTO = new AcademyDetailDTO();
            LinearLayout progressLayout = (LinearLayout) findViewById(R.id.progressLayout);
            ScrollView contentLayout = (ScrollView) findViewById(R.id.contentLayout);

            @Override
            protected void onPreExecute() {
                if (getSupportActionBar() != null && AcademyListActivity.ACADEMY_LIST_DTOS.get(i) != null) {
                    getSupportActionBar().setTitle(AcademyListActivity.ACADEMY_LIST_DTOS.get(i).getName());
                }
                // SHOW THE SPINNER WHILE LOADING FEEDS
                progressLayout.setVisibility(View.VISIBLE);
                contentLayout.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                ObjectMapper mapper = new ObjectMapper();
                String response = WSClient.execute(String.valueOf(AcademyListActivity.ACADEMY_LIST_DTOS.get(i).getId()), "http://thiraseela.com/thiraandroidapp/academydetailservice.php");
                Log.d("response", response);

                try {
                    academyDetailDTO = mapper.readValue(response, AcademyDetailDTO.class);
                } catch (IOException e) {
                    Log.e("AcademyDetailActivity", e.getLocalizedMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                about.setText(academyDetailDTO.getAbout());
                performerImage.setImageResource(R.mipmap.ic_launcher);
                acadType.setText(academyDetailDTO.getAcadType());
                if (academyDetailDTO.getAddress() != null && academyDetailDTO.getAddress().trim().length() > 0) {
                    address.setText(academyDetailDTO.getAddress());
                } else {
                    address.setVisibility(View.GONE);
                }
                if (academyDetailDTO.getMobile() != null && academyDetailDTO.getMobile().trim().length() > 0) {
                    mobile.setText(academyDetailDTO.getMobile());
                } else {
                    mobile.setVisibility(View.GONE);
                }
                if (academyDetailDTO.getPhone() != null && academyDetailDTO.getPhone().trim().length() > 0) {
                    phone.setText(academyDetailDTO.getPhone());
                } else {
                    phone.setVisibility(View.GONE);
                }
                if (academyDetailDTO.getEmail() != null && academyDetailDTO.getEmail().trim().length() > 0) {
                    email.setText(academyDetailDTO.getEmail());
                } else {
                    email.setVisibility(View.GONE);
                }
                if (academyDetailDTO.getWebsite() != null && academyDetailDTO.getWebsite().trim().length() > 0) {
                    web.setText(academyDetailDTO.getWebsite());
                } else {
                    web.setVisibility(View.GONE);
                }
                new DownloadImageTask(performerImage).execute("http://thiraseela.com/gleimo/Academy/images/academy" + AcademyListActivity.ACADEMY_LIST_DTOS.get(i).getId() + "/thumb/logo.jpeg");
                // HIDE THE SPINNER WHILE LOADING FEEDS
                progressLayout.setVisibility(View.GONE);
                contentLayout.setVisibility(View.VISIBLE);
            }
        }.execute();
    }
}
