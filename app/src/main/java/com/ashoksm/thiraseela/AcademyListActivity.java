package com.ashoksm.thiraseela;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ashoksm.thiraseela.adapter.ArtistListAdapter;
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
        final List<ArtistListVO> list = new ArrayList<>();
        list.add(new ArtistListVO("Bharatanjali Academy for Indian Dances", "Vanchiyoor,Trivandrum", "http://thiraseela.com/gleimo/Academy/images/academy9/thumb/logo.jpeg", "", "", ""));
        list.add(new ArtistListVO("Nishrinkala Dance Academy", "Hyderabad,Andhra Pradesh", "http://thiraseela.com/gleimo/Academy/images/academy43/thumb/logo.jpeg", "", "", ""));
        list.add(new ArtistListVO("Murali Sangeeth Music Ackademy", "Kochi,Ernakulam", "http://thiraseela.com/gleimo/Academy/images/academy21/thumb/logo.jpeg", "", "", ""));
        list.add(new ArtistListVO("Sree Sankara School of Dance", "Kalady,Ernakulam", "http://thiraseela.com/gleimo/Academy/images/academy33/thumb/logo.jpeg", "", "", ""));
        list.add(new ArtistListVO("Akshaya Dance School", "West Fort,Trivandrum", "http://thiraseela.com/gleimo/Academy/images/academy65/thumb/logo.jpeg", "", "", ""));
        list.add(new ArtistListVO("Kerala Folklore Akademi", "Chirakkal,Kannur", "http://thiraseela.com/gleimo/Academy/images/academy12/thumb/logo.jpeg", "", "", ""));
        list.add(new ArtistListVO("Margi Theatre", "East Fort,Trivandrum", "http://thiraseela.com/gleimo/Academy/images/academy4/thumb/logo.jpeg", "", "", ""));
        list.add(new ArtistListVO("Kerala Kalamandalam", "Cheruthuruthy,Thrissur", "http://thiraseela.com/gleimo/Academy/images/academy3/thumb/logo.jpeg", "", "", ""));
        list.add(new ArtistListVO("Kalabharathi Foundation for Indian Culture and Heritage", "Thrissur,Thrissur", "http://thiraseela.com/gleimo/Academy/images/academy68/thumb/logo.jpeg", "", "", ""));
        ArtistListAdapter adapter = new ArtistListAdapter(list, this);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ArtistListVO item = list.get(position);
                        Intent intent = new Intent(getApplicationContext(), AcademyDetailActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(EXTRA_ACADEMY_NAME, item.getName());
                        getApplicationContext().startActivity(intent);
                    }
                })
        );
    }
}
