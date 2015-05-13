package com.ashoksm.thiraseela;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ashoksm.thiraseela.adapter.ArtistListAdapter;
import com.ashoksm.thiraseela.dto.ArtistListDTO;
import com.ashoksm.thiraseela.vo.ArtistListVO;

import java.util.ArrayList;
import java.util.List;


public class AcademyListActivity extends ActionBarActivity {

    public static final String EXTRA_ACADEMY_NAME = "EXTRA_ACADEMY_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academy_list);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        setSupportActionBar(toolbar);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.academy_list_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //set adapter
        final List<ArtistListDTO> list = new ArrayList<>();
       ArtistListAdapter adapter = new ArtistListAdapter(list, this);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ArtistListDTO item = list.get(position);
                        Intent intent = new Intent(getApplicationContext(), AcademyDetailActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(EXTRA_ACADEMY_NAME, item.getName());
                        getApplicationContext().startActivity(intent);
                    }
                })
        );
    }
}
