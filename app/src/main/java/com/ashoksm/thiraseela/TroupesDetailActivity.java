package com.ashoksm.thiraseela;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;


public class TroupesDetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troupes_detail);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        setSupportActionBar(toolbar);

        String name = getIntent().getStringExtra(TroupesListActivity.EXTRA_TROUPE_NAME);
        getSupportActionBar().setTitle(name);
        TextView performerName = (TextView) findViewById(R.id.performer_profile);
        performerName.setText("Sinkarimelam and fire escape .. For sinkari melam bookings please call 9656086926. We have been performing sinkari melam for the last 10 years.");
        ImageView performerImage = (ImageView) findViewById(R.id.performer_img);
        new DownloadImageTask(performerImage).execute("http://thiraseela.com/gleimo/Troupes/images/truopes41/thumb/logo.jpeg");
    }
}
