package com.ashoksm.thiraseela;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.ashoksm.thiraseela.adapter.ArtistListAdapter;
import com.ashoksm.thiraseela.vo.ArtistListVO;

import java.util.ArrayList;
import java.util.List;

public class ArtistListActivity extends ActionBarActivity {

    private static boolean scroll_down;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_list);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        setSupportActionBar(toolbar);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.artist_list_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //set adapter
        List<ArtistListVO> list = new ArrayList<>();
        list.add(new ArtistListVO("Anju K Chandran", "Classical Dancer", "http://thiraseela.com/gleimo/performers/images/perfomr471/thumb/Perfmr_img.jpeg"));
        list.add(new ArtistListVO("Lekshmi harilal", "Bhavakala", "http://thiraseela.com/gleimo/performers/images/perfomr468/thumb/Perfmr_img.jpeg"));
        list.add(new ArtistListVO("Janeeta Rose Thomas", "Bharatanatyam Dancer", "http://thiraseela.com/gleimo/performers/images/perfomr465/thumb/Perfmr_img.jpeg"));
        list.add(new ArtistListVO("Rani Khanam", "Kathak Dancer", "http://thiraseela.com/gleimo/performers/images/perfomr444/thumb/Perfmr_img.jpeg"));
        list.add(new ArtistListVO("Gayatri Asokan", "Hindustani and Ghazals & Playback Singer", "http://thiraseela.com/gleimo/performers/images/perfomr443/thumb/Perfmr_img.jpeg"));
        list.add(new ArtistListVO("Aditi Yadav", "Kathak dance Performer", "http://thiraseela.com/gleimo/performers/images/perfomr442/thumb/Perfmr_img.jpeg"));
        list.add(new ArtistListVO("Sangeeta Majumder", "Kathak Dancer", "http://thiraseela.com/gleimo/performers/images/perfomr441/thumb/Perfmr_img.jpeg"));
        list.add(new ArtistListVO("Srinjoy Mukherjee", "Sarod Artist", "http://thiraseela.com/gleimo/performers/images/perfomr440/thumb/Perfmr_img.jpeg"));
        list.add(new ArtistListVO("Emelee Ghosh", "Kathak Dancer", "http://thiraseela.com/gleimo/performers/images/perfomr439/thumb/Perfmr_img.jpeg"));
        ArtistListAdapter adapter = new ArtistListAdapter(list, this);
        mRecyclerView.setAdapter(adapter);
    }
}
