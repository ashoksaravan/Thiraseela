package com.ashoksm.thiraseela;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ashoksm.thiraseela.adapter.ArtistListAdapter;
import com.ashoksm.thiraseela.adapter.EventsListAdapter;
import com.ashoksm.thiraseela.vo.ArtistListVO;
import com.ashoksm.thiraseela.vo.EventsVO;

import java.util.ArrayList;
import java.util.List;


public class EventsListActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        setSupportActionBar(toolbar);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.events_list_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //set adapter
        List<EventsVO> list = new ArrayList<>();
        list.add(new EventsVO("Monthly Programs @ Changampuzha Park", "Performance  |  Changampuzha Park", "At Changampuzha Park, Edappally   |   Ernakulam", "Date :01-Mar   -   31-Mar", "Time 9AM   -  6 PM", "http://thiraseela.com/users/info@thiraseela.com/events/1932/logoth.jpg"));
        list.add(new EventsVO("Bhava Raga Tala", "Performance  |  International Arts & Cultural Foundation", "At Seva Sadan, Malleshwaram   |   Bangalore", "On 07-Mar", "Time 6PM   -  9 PM", "http://thiraseela.com/users/info@thiraseela.com/events/1913/logoth.jpg"));
        list.add(new EventsVO("Kalakshetra - Kathakali Festival 2015", "Festivals  |  Kalakshetra Foundation", "At Rukmini Arangam, Kalakshetra   |   Chennai", "Date :07-Mar   -   10-Mar", "Time 6PM   -  10 PM", "http://thiraseela.com/users/info@thiraseela.com/events/1934/logoth.jpg"));
        EventsListAdapter adapter = new EventsListAdapter(list, this);
        mRecyclerView.setAdapter(adapter);
    }
}
