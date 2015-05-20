package com.ashoksm.thiraseela.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ashoksm.thiraseela.R;
import com.ashoksm.thiraseela.adapter.ArtistListAdapter;
import com.ashoksm.thiraseela.dto.ArtistListDTO;
import com.ashoksm.thiraseela.utils.RecyclerItemClickListener;
import com.ashoksm.thiraseela.wsclient.WSClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArtistListActivity extends AppCompatActivity {

    public static final String EXTRA_PERFORMER_NAME = "EXTRA_PERFORMER_NAME";
    public static final List<ArtistListDTO> ARTIST_LIST_VOS = new ArrayList<>();
    private ArtistListAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_list);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        setSupportActionBar(toolbar);

        EditText searchText = (EditText) findViewById(R.id.search_bar);
        final RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.artist_list_view);
        final TextView emptyView = (TextView) findViewById(R.id.empty_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        new AsyncTask<Void, Void, Void>() {

            LinearLayout progressLayout = (LinearLayout) findViewById(R.id.progressLayout);
            RelativeLayout contentLayout = (RelativeLayout) findViewById(R.id.contentLayout);

            @Override
            protected void onPreExecute() {
                // SHOW THE SPINNER WHILE LOADING FEEDS
                progressLayout.setVisibility(View.VISIBLE);
                contentLayout.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                if (ARTIST_LIST_VOS.size() == 0) {
                    ARTIST_LIST_VOS.clear();
                    ObjectMapper mapper = new ObjectMapper();
                    String response = WSClient.execute("", "http://thiraseela.com/thiraandroidapp/performerlistservice.php");
                    Log.d("response", response);
                    try {
                        List<ArtistListDTO> temp = mapper.readValue(response,
                                TypeFactory.defaultInstance().constructCollectionType(List.class, ArtistListDTO.class));
                        ARTIST_LIST_VOS.addAll(temp);
                    } catch (IOException e) {
                        Log.e("ArtistListActivity", e.getLocalizedMessage());
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                adapter = new ArtistListAdapter(ARTIST_LIST_VOS, ArtistListActivity.this, mRecyclerView, emptyView);
                mRecyclerView.setAdapter(adapter);
                // HIDE THE SPINNER WHILE LOADING FEEDS
                progressLayout.setVisibility(View.GONE);
                contentLayout.setVisibility(View.VISIBLE);
            }
        }.execute();

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getApplicationContext(), ArtistDetailActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ArtistListDTO artistListDTO = adapter.getFilteredArtistListDTOs().get(position);
                        int i = ARTIST_LIST_VOS.indexOf(artistListDTO);
                        intent.putExtra(EXTRA_PERFORMER_NAME, String.valueOf(i));
                        getApplicationContext().startActivity(intent);
                    }
                })
        );

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(adapter != null) {
                    adapter.getFilter().filter(s.toString());
                }
            }
        });
    }
}
