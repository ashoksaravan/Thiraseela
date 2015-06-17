package com.ashoksm.thiraseela.ui;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.ashoksm.thiraseela.adapter.ArtistListAdapter;
import com.ashoksm.thiraseela.dto.ArtistListDTO;
import com.ashoksm.thiraseela.utils.ImageDownloader;
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
    private boolean networkAvailable = true;
    private static final String URL = "http://thiraseela.com/thiraandroidapp/images/inner_bg.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_list);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigation_arrow_back);
        setSupportActionBar(toolbar);

        EditText searchText = (EditText) findViewById(R.id.search_bar);
        final RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.artist_list_view);
        final TextView emptyView = (TextView) findViewById(R.id.empty_view);
        Button retryButton = (Button) findViewById(R.id.retryButton);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDetails(mRecyclerView, emptyView);
            }
        });

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        loadDetails(mRecyclerView, emptyView);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getApplicationContext(), ArtistDetailActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ArtistListDTO artistListDTO = adapter.getFilteredArtistListDTOs().get(position);
                        int i = ARTIST_LIST_VOS.indexOf(artistListDTO);
                        intent.putExtra(EXTRA_PERFORMER_NAME, String.valueOf(i));
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
        ImageView innerBG = (ImageView) findViewById(R.id.inner_bg);
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ImageDownloader downloader = ImageDownloader.getInstance(am.getMemoryClass());
        Bitmap bitmap = downloader.getBitmapFromMemCache(URL);
        if (bitmap != null) {
            innerBG.setImageBitmap(bitmap);
        } else {
            downloader.download(URL, innerBG, null, null);
        }
    }

    private void loadDetails(final RecyclerView mRecyclerView, final TextView emptyView) {
        new AsyncTask<Void, Void, Void>() {
            LinearLayout progressLayout = (LinearLayout) findViewById(R.id.progressLayout);
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
                if (ARTIST_LIST_VOS.size() == 0) {
                    ARTIST_LIST_VOS.clear();
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        String response = WSClient.execute("", "http://thiraseela.com/thiraandroidapp/performerlistservice.php");
                        Log.d("response", response);
                        List<ArtistListDTO> temp = mapper.readValue(response,
                                TypeFactory.defaultInstance().constructCollectionType(List.class, ArtistListDTO.class));
                        ARTIST_LIST_VOS.addAll(temp);
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
                    adapter = new ArtistListAdapter(ARTIST_LIST_VOS, ArtistListActivity.this, mRecyclerView, emptyView);
                    mRecyclerView.setAdapter(adapter);
                    // HIDE THE SPINNER WHILE LOADING FEEDS
                    progressLayout.setVisibility(View.GONE);
                    contentLayout.setVisibility(View.VISIBLE);
                    timeoutLayout.setVisibility(View.GONE);
                } else {
                    timeoutLayout.setVisibility(View.VISIBLE);
                    progressLayout.setVisibility(View.GONE);
                    contentLayout.setVisibility(View.GONE);
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
