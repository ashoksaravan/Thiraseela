package com.ashoksm.thiraseela;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ArtistDetailActivity extends ActionBarActivity {

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        setSupportActionBar(toolbar);

        // Gesture detection
        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.artist_detail_view);
        frameLayout.setOnTouchListener(gestureListener);

        String name = getIntent().getStringExtra(ArtistListActivity.EXTRA_PERFORMER_NAME);
        getSupportActionBar().setTitle(name);
        TextView performerName = (TextView) findViewById(R.id.performer_profile);
        performerName.setText("Bharathanatyam, the supreme Indian classical dance that skillfully embodies the three primary ingredients of Dance - Rhythm, Rhyme and Expression, fascinated Kirti Ramgopal at a tender age. With her passion, focus and rigorous training, she is acclaimed as one of the brightest talents in her generation. Kirti began her training under Smt.Padmini Ramachandran and has also been mentored and guided by Smt. Priyadarshini Govind and Sri.A. Lakshman. Currently she is undergoing training in Abhinaya under Smt. Bragha Bessell. With her keen ability to understand and imbibe the nuances of this beautiful art form and with her perseverance and dedication, she has traveled across India and internationally throughout US, Canada, UK, Europe, The Middle East & Bangladesh performing before discerning audiences and winning acclaim from connoisseurs of art and critics alike. Kirti creates incandescent Natyam with her abundance of lively skill, geometric aptness, nimbleness and grace and sensitivity in delivery. Her dancing has been described as vibrant, expressive and that which extends a spirit of Joy. It is her sincere effort to delve further into this wonderful art of Bharathanatyam and preserve and propagate its pristine beauty.");
        ImageView performerImage = (ImageView) findViewById(R.id.performer_img);
        new DownloadImageTask(performerImage).execute("http://thiraseela.com/gleimo/performers/images/perfomr92/Perfmr_img.jpeg");

        performerImage.setOnTouchListener(gestureListener);
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(ArtistDetailActivity.this, "Left Swipe", Toast.LENGTH_SHORT).show();
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(ArtistDetailActivity.this, "Right Swipe", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }

}
