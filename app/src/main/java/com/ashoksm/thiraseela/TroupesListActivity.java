package com.ashoksm.thiraseela;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ashoksm.thiraseela.adapter.TroupesListAdapter;
import com.ashoksm.thiraseela.vo.TroupesListVO;

import java.util.ArrayList;
import java.util.List;


public class TroupesListActivity extends ActionBarActivity {

    public static final String EXTRA_TROUPE_NAME = "EXTRA_TROUPE_NAME";

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
        final List<TroupesListVO> list = new ArrayList<>();
        list.add(new TroupesListVO("Trivandrum Brothers", "Panchavadyam", "kowadiyar, Trivandrum", "http://thiraseela.com/gleimo/Troupes/images/truopes32/thumb/logo.jpeg"));
        list.add(new TroupesListVO("Dreams Thiruvananthapuram", "Mimics", "Thampanoor, Trivandrum", "http://thiraseela.com/gleimo/Troupes/images/truopes48/thumb/logo.jpeg"));
        list.add(new TroupesListVO("Thengumvila bhagavathi kalasamithi", "Sinkarimelam", "Chirayinkeezhu,Trivandrum", "http://thiraseela.com/gleimo/Troupes/images/truopes41/thumb/logo.jpeg"));
        TroupesListAdapter adapter = new TroupesListAdapter(list, this);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        TroupesListVO item = list.get(position);
                        Intent intent = new Intent(getApplicationContext(), TroupesDetailActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(EXTRA_TROUPE_NAME, item.getName());
                        getApplicationContext().startActivity(intent);
                    }
                })
        );
    }
}