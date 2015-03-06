package com.ashoksm.thiraseela;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


public class ArtistDetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        setSupportActionBar(toolbar);

        String name = getIntent().getStringExtra(ArtistListActivity.EXTRA_PERFORMER_NAME);
        getSupportActionBar().setTitle(name);
        TextView performerName = (TextView) findViewById(R.id.performer_profile);
        performerName.setText("Bharathanatyam, the supreme Indian classical dance that skillfully embodies the three primary ingredients of Dance - Rhythm, Rhyme and Expression, fascinated Kirti Ramgopal at a tender age. With her passion, focus and rigorous training, she is acclaimed as one of the brightest talents in her generation. Kirti began her training under Smt.Padmini Ramachandran and has also been mentored and guided by Smt. Priyadarshini Govind and Sri.A. Lakshman. Currently she is undergoing training in Abhinaya under Smt. Bragha Bessell. With her keen ability to understand and imbibe the nuances of this beautiful art form and with her perseverance and dedication, she has traveled across India and internationally throughout US, Canada, UK, Europe, The Middle East & Bangladesh performing before discerning audiences and winning acclaim from connoisseurs of art and critics alike. Kirti creates incandescent Natyam with her abundance of lively skill, geometric aptness, nimbleness and grace and sensitivity in delivery. Her dancing has been described as vibrant, expressive and that which extends a spirit of Joy. It is her sincere effort to delve further into this wonderful art of Bharathanatyam and preserve and propagate its pristine beauty.");
        ImageView performerImage = (ImageView) findViewById(R.id.performer_img);
        new DownloadImageTask(performerImage).execute("http://thiraseela.com/gleimo/performers/images/perfomr92/Perfmr_img.jpeg");
    }

}
