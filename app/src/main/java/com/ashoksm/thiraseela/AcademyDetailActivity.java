package com.ashoksm.thiraseela;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


public class AcademyDetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academy_detail);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        setSupportActionBar(toolbar);

        String name = getIntent().getStringExtra(AcademyListActivity.EXTRA_ACADEMY_NAME);
        getSupportActionBar().setTitle(name);
        TextView performerName = (TextView) findViewById(R.id.performer_profile);
        performerName.setText("Trivi Art Concerns is a collective of theatre artists, stage technicians and art managers who have been working together with different organizations and platforms for more than a decade.\n" +
                "\n" +
                "Nurturing new avenues and perspectives in art appreciation is one of the founding principles of Trivi. Providing a good ambience rich with sensible management, state of the art technology and discerning audience for art practitioners is our prime focus. Trivi is also involved in art in education programmes through workshops, seminars and art events. Members of Trivi are closely associated with some of the prestigious art events in the country like Hay Festival Kerala, International Film Festival of Kerala, International Theatre Festival of Kerala, Kritya Poetry Festival and International Documentary & Short Film Festival of Kerala. Apart from acclaimed individual performing arts practitioners, Trivi is extending support to theatre/performing arts groups of international repute functioning from Kerala in their administration, programming, fund raising, tour management and publicity outreach.");
        ImageView performerImage = (ImageView) findViewById(R.id.performer_img);
        new DownloadImageTask(performerImage).execute("http://thiraseela.com/gleimo/Academy/images/academy46/thumb/logo.jpeg");
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
