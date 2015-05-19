package com.ashoksm.thiraseela.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashoksm.thiraseela.R;
import com.ashoksm.thiraseela.wsclient.WSClient;


public class AboutUSActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        setSupportActionBar(toolbar);
        final TextView about = (TextView) findViewById(R.id.about_us);

        new AsyncTask<Void, Void, Void>() {

            LinearLayout progressLayout = (LinearLayout) findViewById(R.id.progressLayout);
            CardView contentLayout = (CardView) findViewById(R.id.contentLayout);
            String response = "";

            @Override
            protected void onPreExecute() {
                // SHOW THE SPINNER WHILE LOADING FEEDS
                progressLayout.setVisibility(View.VISIBLE);
                contentLayout.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                String response = WSClient.execute("", "http://thiraseela.com/thiraandroidapp/aboutusservice.php");
                Log.d("response", response);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                about.setText(response);
                // HIDE THE SPINNER WHILE LOADING FEEDS
                progressLayout.setVisibility(View.GONE);
                contentLayout.setVisibility(View.VISIBLE);
            }
        }.execute();

    }
}
