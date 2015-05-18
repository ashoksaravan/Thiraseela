package com.ashoksm.thiraseela;

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

import com.ashoksm.thiraseela.adapter.EventsListAdapter;
import com.ashoksm.thiraseela.dto.EventListDTO;
import com.ashoksm.thiraseela.wsclient.WSClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class EventsListActivity extends AppCompatActivity {

    public static final String EXTRA_EVENT_NAME = "EXTRA_EVENT_NAME";

    public static final List<EventListDTO> EVENT_LIST_DTOS = new ArrayList<>();

    private EventsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        setSupportActionBar(toolbar);

        EditText searchText = (EditText) findViewById(R.id.search_bar);
        final RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.events_list_view);

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
                if (EVENT_LIST_DTOS.size() == 0) {
                    EVENT_LIST_DTOS.clear();
                    ObjectMapper mapper = new ObjectMapper();
                    String response = WSClient.execute("", "http://thiraseela.com/thiraandroidapp/eventservice.php");
                    Log.d("response", response);
                    try {
                        List<EventListDTO> temp = mapper.readValue(response,
                                TypeFactory.defaultInstance().constructCollectionType(List.class, EventListDTO.class));
                        EVENT_LIST_DTOS.addAll(temp);
                    } catch (IOException e) {
                        Log.e("ArtistListActivity", e.getLocalizedMessage());
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                adapter = new EventsListAdapter(EVENT_LIST_DTOS);
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
                        Intent intent = new Intent(getApplicationContext(), EventsDetailActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        EventListDTO eventListDTO = adapter.getFilteredArtistListDTOs().get(position);
                        int i = EVENT_LIST_DTOS.indexOf(eventListDTO);
                        intent.putExtra(EXTRA_EVENT_NAME, String.valueOf(i));
                        getApplicationContext().startActivity(intent);
                    }
                })
        );

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (adapter != null) {
                    adapter.getFilter().filter(s.toString());
                }
            }
        });
    }
}
