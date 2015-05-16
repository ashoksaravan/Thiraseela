package com.ashoksm.thiraseela;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class EventsDetailActivity extends AppCompatActivity implements SimpleGestureFilter.SimpleGestureListener {

    private SimpleGestureFilter detector;
    private int i;
    private TextView eventName;
    private TextView category;
    private TextView by;
    private TextView venue;
    private TextView city;
    private TextView date;
    private TextView time;
    private TextView contactName;
    private TextView phone;
    private TextView mobile;
    private TextView email;
    private TextView web;
    private LinearLayout phoneLayout;
    private LinearLayout mobileLayout;
    private LinearLayout emailLayout;
    private LinearLayout webLayout;
    private ImageView performerImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_detail);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        setSupportActionBar(toolbar);

        String name = getIntent().getStringExtra(EventsListActivity.EXTRA_EVENT_NAME);
        i = Integer.parseInt(name);
        eventName = (TextView) findViewById(R.id.event_name);
        category = (TextView) findViewById(R.id.category);
        by = (TextView) findViewById(R.id.by);
        venue = (TextView) findViewById(R.id.venue);
        city = (TextView) findViewById(R.id.city);
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);
        contactName = (TextView) findViewById(R.id.contact_name);
        phone = (TextView) findViewById(R.id.phone);
        mobile = (TextView) findViewById(R.id.mobile);
        email = (TextView) findViewById(R.id.email);
        web = (TextView) findViewById(R.id.web);
        phoneLayout = (LinearLayout) findViewById(R.id.phone_layout);
        mobileLayout = (LinearLayout) findViewById(R.id.mobile_layout);
        emailLayout = (LinearLayout) findViewById(R.id.email_layout);
        webLayout = (LinearLayout) findViewById(R.id.web_layout);
        performerImg = (ImageView) findViewById(R.id.performer_img);
        // Detect touched area
        detector = new SimpleGestureFilter(this, this);
        loadDetails();
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
                if (i != EventsListActivity.EVENT_LIST_DTOS.size() - 1) {
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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(EventsListActivity.EVENT_LIST_DTOS.get(i).getName());
        }
        performerImg.setImageResource(R.mipmap.ic_launcher);
        eventName.setText(EventsListActivity.EVENT_LIST_DTOS.get(i).getName());
        category.setText(EventsListActivity.EVENT_LIST_DTOS.get(i).getCategory());
        by.setText(EventsListActivity.EVENT_LIST_DTOS.get(i).getArtistName());
        venue.setText(EventsListActivity.EVENT_LIST_DTOS.get(i).getVenue());
        city.setText(EventsListActivity.EVENT_LIST_DTOS.get(i).getDistrict());
        DateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        if(!EventsListActivity.EVENT_LIST_DTOS.get(i).getStart().equals(EventsListActivity.EVENT_LIST_DTOS.get(i).getEnd())) {
            date.setText("Date : " + df.format(EventsListActivity.EVENT_LIST_DTOS.get(i).getStart()) + " - " + df.format(EventsListActivity.EVENT_LIST_DTOS.get(i).getEnd()));
        } else {
            date.setText("On : " + df.format(EventsListActivity.EVENT_LIST_DTOS.get(i).getStart()));
        }
        time.setText("Time : " + EventsListActivity.EVENT_LIST_DTOS.get(i).getFromTime() + " - " + EventsListActivity.EVENT_LIST_DTOS.get(i).getToTime());
        contactName.setText(EventsListActivity.EVENT_LIST_DTOS.get(i).getContactName());
        if (EventsListActivity.EVENT_LIST_DTOS.get(i).getPhone() != null && EventsListActivity.EVENT_LIST_DTOS.get(i).getPhone().trim().length() > 0) {
            phone.setText(EventsListActivity.EVENT_LIST_DTOS.get(i).getPhone());
        } else {
            phoneLayout.setVisibility(View.GONE);
        }
        if (EventsListActivity.EVENT_LIST_DTOS.get(i).getMobile() != null && EventsListActivity.EVENT_LIST_DTOS.get(i).getMobile().trim().length() > 0) {
            mobile.setText(EventsListActivity.EVENT_LIST_DTOS.get(i).getMobile());
        } else {
            mobileLayout.setVisibility(View.GONE);
        }
        if (EventsListActivity.EVENT_LIST_DTOS.get(i).getEmail() != null && EventsListActivity.EVENT_LIST_DTOS.get(i).getEmail().trim().length() > 0) {
            email.setText(EventsListActivity.EVENT_LIST_DTOS.get(i).getEmail());
        } else {
            emailLayout.setVisibility(View.GONE);
        }
        if (EventsListActivity.EVENT_LIST_DTOS.get(i).getWeb() != null && EventsListActivity.EVENT_LIST_DTOS.get(i).getWeb().trim().length() > 0) {
            web.setText(EventsListActivity.EVENT_LIST_DTOS.get(i).getWeb());
        } else {
            webLayout.setVisibility(View.GONE);
        }
        if (EventsListActivity.EVENT_LIST_DTOS.get(i).getLogo() != null && EventsListActivity.EVENT_LIST_DTOS.get(i).getLogo().trim().length() > 0) {
            new DownloadImageTask(performerImg).execute("http://thiraseela.com/" + EventsListActivity.EVENT_LIST_DTOS.get(i).getLogo());
        }
    }
}
