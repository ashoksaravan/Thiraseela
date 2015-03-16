package com.ashoksm.thiraseela;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


public class EventsDetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_detail);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        setSupportActionBar(toolbar);

        String name = getIntent().getStringExtra(EventsListActivity.EXTRA_EVENT_NAME);
        getSupportActionBar().setTitle(name);
        ImageView performerImage = (ImageView) findViewById(R.id.performer_img);
        new DownloadImageTask(performerImage).execute("http://thiraseela.com/users/info@thiraseela.com/events/1932/banner.jpg");
    }
}
