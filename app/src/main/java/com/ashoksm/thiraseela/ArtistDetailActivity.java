package com.ashoksm.thiraseela;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ArtistDetailActivity extends ActionBarActivity implements SimpleGestureFilter.SimpleGestureListener{

    private SimpleGestureFilter detector;

    private int i;

    private TextView performerName;

    private ImageView performerImage;

    private TextView designation;

    private TextView location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        setSupportActionBar(toolbar);

        String name = getIntent().getStringExtra(ArtistListActivity.EXTRA_PERFORMER_NAME);
        i = Integer.valueOf(name);
        performerName = (TextView) findViewById(R.id.performer_profile);
        performerImage = (ImageView) findViewById(R.id.performer_img);
        designation = (TextView) findViewById(R.id.designation);
        location = (TextView) findViewById(R.id.location);
        loadDetails();
        // Detect touched area
        detector = new SimpleGestureFilter(this, this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public void onSwipe(int direction) {
        String str = "";

        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT:
                str = "Swipe Right";
                if(i != 0) {
                    i--;
                    loadDetails();
                }
                break;
            case SimpleGestureFilter.SWIPE_LEFT:
                str = "Swipe Left";
                if(i != ArtistListActivity.ARTIST_LIST_VOS.size()-1) {
                    i++;
                    loadDetails();
                }
                break;
        }
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private void loadDetails() {
        performerName.setText(ArtistListActivity.ARTIST_LIST_VOS.get(i).getAbout());
        performerImage.setImageResource(R.mipmap.ic_launcher);
        designation.setText(ArtistListActivity.ARTIST_LIST_VOS.get(i).getDesignation());
        location.setText(ArtistListActivity.ARTIST_LIST_VOS.get(i).getLocation());
        getSupportActionBar().setTitle(ArtistListActivity.ARTIST_LIST_VOS.get(i).getName());
        new DownloadImageTask(performerImage).execute(ArtistListActivity.ARTIST_LIST_VOS.get(i).getPerformerImage());
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
