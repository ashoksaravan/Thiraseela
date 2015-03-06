package com.ashoksm.thiraseela;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ashoksm.thiraseela.adapter.ArtistListAdapter;
import com.ashoksm.thiraseela.adapter.TroupesListAdapter;
import com.ashoksm.thiraseela.vo.ArtistListVO;
import com.ashoksm.thiraseela.vo.TroupesListVO;

import java.util.ArrayList;
import java.util.List;


public class TroupesListActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troupes_list);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        setSupportActionBar(toolbar);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.troupes_list_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //set adapter
        List<TroupesListVO> list = new ArrayList<>();
        list.add(new TroupesListVO("Trivandrum Brothers", "Panchavadyam", "kowadiyar, Trivandrum", "http://thiraseela.com/gleimo/Troupes/images/truopes32/thumb/logo.jpeg"));
        list.add(new TroupesListVO("Dreams Thiruvananthapuram", "Mimics", "Thampanoor, Trivandrum", "http://thiraseela.com/gleimo/Troupes/images/truopes48/thumb/logo.jpeg"));
        list.add(new TroupesListVO("Thengumvila bhagavathi kalasamithi", "Sinkarimelam", "Chirayinkeezhu,Trivandrum", "http://thiraseela.com/gleimo/Troupes/images/truopes41/thumb/logo.jpeg"));
        TroupesListAdapter adapter = new TroupesListAdapter(list, this);
        mRecyclerView.setAdapter(adapter);
    }
}