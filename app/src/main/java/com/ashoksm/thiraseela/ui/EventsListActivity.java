package com.ashoksm.thiraseela.ui;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ashoksm.thiraseela.R;
import com.ashoksm.thiraseela.adapter.EventsListAdapter;
import com.ashoksm.thiraseela.dto.EventListDTO;
import com.ashoksm.thiraseela.utils.ImageDownloader;
import com.ashoksm.thiraseela.utils.RecyclerItemClickListener;
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
    private boolean networkAvailable = true;
    private static final String URL = "http://thiraseela.com/thiraandroidapp/images/inner_bg.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigation_arrow_back);
        setSupportActionBar(toolbar);

        EditText searchText = (EditText) findViewById(R.id.search_bar);
        final RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.events_list_view);
        final TextView emptyView = (TextView) findViewById(R.id.empty_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        loadDetails(mRecyclerView, emptyView);

        Button retryButton = (Button) findViewById(R.id.retryButton);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDetails(mRecyclerView, emptyView);
            }
        });

        ImageView innerBG = (ImageView) findViewById(R.id.inner_bg);
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ImageDownloader downloader = ImageDownloader.getInstance(am.getMemoryClass());
        Bitmap bitmap = downloader.getBitmapFromMemCache(URL);
        if (bitmap != null) {
            innerBG.setImageBitmap(bitmap);
        } else {
            downloader.download(URL, innerBG, null, null);
        }

        TextView info = (TextView) findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "info@thiraseela.com", null));
                startActivity(Intent.createChooser(intent, "Complete action using"));
            }
        });

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(),
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(getApplicationContext(), EventsDetailActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                EventListDTO eventListDTO = adapter.getFilteredArtistListDTOs().get(position);
                                int i = EVENT_LIST_DTOS.indexOf(eventListDTO);
                                intent.putExtra(EXTRA_EVENT_NAME, String.valueOf(i));
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_out_left, 0);
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

    private void loadDetails(final RecyclerView mRecyclerView, final TextView emptyView) {
        new AsyncTask<Void, Void, Void>() {

            LinearLayout progressLayout = (LinearLayout) findViewById(R.id.progressView);
            RelativeLayout contentLayout = (RelativeLayout) findViewById(R.id.contentLayout);
            LinearLayout timeoutLayout = (LinearLayout) findViewById(R.id.timeoutLayout);

            @Override
            protected void onPreExecute() {
                // SHOW THE SPINNER WHILE LOADING FEEDS
                progressLayout.setVisibility(View.VISIBLE);
                contentLayout.setVisibility(View.GONE);
                timeoutLayout.setVisibility(View.GONE);
                networkAvailable = true;
            }

            @Override
            protected Void doInBackground(Void... params) {
                if (EVENT_LIST_DTOS.size() == 0) {
                    EVENT_LIST_DTOS.clear();
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        String response =
                                WSClient.execute("", "http://thiraseela.com/thiraandroidapp/eventservice.php");
                        List<EventListDTO> temp = mapper.readValue(response,
                                TypeFactory.defaultInstance().constructCollectionType(List.class, EventListDTO.class));
                        EVENT_LIST_DTOS.addAll(temp);
                    } catch (IOException e) {
                        Log.e("ArtistListActivity", e.getLocalizedMessage());
                        networkAvailable = false;
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (networkAvailable) {
                    adapter = new EventsListAdapter(EVENT_LIST_DTOS, EventsListActivity.this, mRecyclerView, emptyView);
                    mRecyclerView.setAdapter(adapter);
                    // HIDE THE SPINNER WHILE LOADING FEEDS
                    progressLayout.setVisibility(View.GONE);
                    contentLayout.setVisibility(View.VISIBLE);
                    timeoutLayout.setVisibility(View.GONE);
                } else {
                    progressLayout.setVisibility(View.GONE);
                    contentLayout.setVisibility(View.GONE);
                    timeoutLayout.setVisibility(View.VISIBLE);
                }
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.slide_in_left, 0);
    }
}
