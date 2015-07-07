package com.ashoksm.thiraseela.adapter;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ashoksm.thiraseela.R;
import com.ashoksm.thiraseela.dto.ArtistDetailDTO;
import com.ashoksm.thiraseela.dto.ArtistListDTO;
import com.ashoksm.thiraseela.utils.ImageDownloader;
import com.ashoksm.thiraseela.wsclient.WSClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class ArtistPagerAdapter extends PagerAdapter {

    List<ArtistListDTO> items;
    LayoutInflater mLayoutInflater;
    Context context;
    ImageDownloader downloader;
    Bitmap placeHolderImage;

    public ArtistPagerAdapter(List<ArtistListDTO> itemsIn, Context contextIn) {
        this.items = itemsIn;
        this.context = contextIn;
        mLayoutInflater = (LayoutInflater) contextIn.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ActivityManager am = (ActivityManager) contextIn.getSystemService(Context.ACTIVITY_SERVICE);
        downloader = ImageDownloader.getInstance(am.getMemoryClass());
        placeHolderImage = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.ic_launcher);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final View itemView = mLayoutInflater.inflate(R.layout.artist_pager_item, container, false);

        final ImageView performerImage = (ImageView) itemView.findViewById(R.id.performer_img);
        final TextView location = (TextView) itemView.findViewById(R.id.location);
        final TextView designation = (TextView) itemView.findViewById(R.id.designation);
        final TextView address = (TextView) itemView.findViewById(R.id.address);
        final TextView mobile = (TextView) itemView.findViewById(R.id.mobile);
        final TextView phone = (TextView) itemView.findViewById(R.id.phone);
        final TextView email = (TextView) itemView.findViewById(R.id.email);
        final TextView web = (TextView) itemView.findViewById(R.id.web);
        final TextView performerProfile = (TextView) itemView.findViewById(R.id.performer_profile);
        final RelativeLayout addressLayout = (RelativeLayout) itemView.findViewById(R.id.addressLayout);
        final CardView aboutView = (CardView) itemView.findViewById(R.id.aboutView);

        loadDetails(position, itemView, performerImage, location, designation, address, mobile, phone, email,
                web, performerProfile, addressLayout, aboutView);

        Button retryButton = (Button) itemView.findViewById(R.id.retryButton);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDetails(position, itemView, performerImage, location, designation, address, mobile, phone, email,
                        web, performerProfile, addressLayout, aboutView);
            }
        });

        container.addView(itemView);
        return itemView;
    }

    private void loadDetails(final int position, final View itemView, final ImageView performerImage,
                             final TextView location, final TextView designation, final TextView address,
                             final TextView mobile, final TextView phone, final TextView email, final TextView web,
                             final TextView performerProfile, final RelativeLayout addressLayout,
                             final CardView aboutView) {
        new AsyncTask<Void, Void, Void>() {
            ArtistDetailDTO artistDetailDTO = null;
            ObjectMapper mapper = new ObjectMapper();
            ScrollView contentView = (ScrollView) itemView.findViewById(R.id.contentView);
            LinearLayout progressView = (LinearLayout) itemView.findViewById(R.id.progressView);
            LinearLayout timeoutLayout = (LinearLayout) itemView.findViewById(R.id.timeoutLayout);
            boolean networkAvailable = true;

            @Override
            protected void onPreExecute() {
                contentView.setVisibility(View.GONE);
                timeoutLayout.setVisibility(View.GONE);
                progressView.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    String response = WSClient.execute(String.valueOf(items.get(position).getId()),
                            "http://thiraseela.com/thiraandroidapp/performerdetailservice.php");
                    Log.d("response", response);
                    artistDetailDTO = mapper.readValue(response, ArtistDetailDTO.class);
                } catch (IOException e) {
                    Log.e("MyPagerAdapter", e.getLocalizedMessage());
                    networkAvailable = false;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (networkAvailable) {
                    String url =
                            "http://thiraseela.com/gleimo/performers/images/perfomr" + items.get(position).getId() +
                                    "/Perfmr_img.jpeg";
                    Bitmap bitmap = downloader.getBitmapFromMemCache(url);
                    if (bitmap == null) {
                        downloader.download(url, performerImage, context.getResources(), placeHolderImage);
                    } else {
                        performerImage.setImageBitmap(bitmap);
                    }

                    location.setText(items.get(position).getName());

                    if (artistDetailDTO != null) {
                        if (artistDetailDTO.getAbout() != null && artistDetailDTO.getAbout().trim().length() > 0) {
                            aboutView.setVisibility(View.VISIBLE);
                            performerProfile.setText(
                                    artistDetailDTO.getAbout().replaceAll("\\\\\"", "\"").replaceAll("\\\\'", "'"));
                        } else {
                            aboutView.setVisibility(View.GONE);
                        }

                        designation.setText(items.get(position).getTitle());
                        location.setText(artistDetailDTO.getPlace() + ", " + artistDetailDTO.getCity());
                        if (artistDetailDTO.getAddress() != null && artistDetailDTO.getAddress().trim().length() > 0) {
                            addressLayout.setVisibility(View.VISIBLE);
                            address.setText(
                                    artistDetailDTO.getAddress().replaceAll("\\\\\"", "\"").replaceAll("\\\\'", "'"));
                        } else {
                            addressLayout.setVisibility(View.GONE);
                        }
                        if (artistDetailDTO.getMobile() != null && artistDetailDTO.getMobile().trim().length() > 0) {
                            mobile.setVisibility(View.VISIBLE);
                            mobile.setText(artistDetailDTO.getMobile());
                        } else {
                            mobile.setVisibility(View.GONE);
                        }
                        if (artistDetailDTO.getPhone() != null && artistDetailDTO.getPhone().trim().length() > 0) {
                            phone.setVisibility(View.VISIBLE);
                            phone.setText(artistDetailDTO.getPhone());
                        } else {
                            phone.setVisibility(View.GONE);
                        }
                        if (artistDetailDTO.getEmail() != null && artistDetailDTO.getEmail().trim().length() > 0) {
                            email.setVisibility(View.VISIBLE);
                            email.setText(artistDetailDTO.getEmail());
                        } else {
                            email.setVisibility(View.GONE);
                        }
                        if (artistDetailDTO.getWebsite() != null && artistDetailDTO.getWebsite().trim().length() > 0) {
                            web.setVisibility(View.VISIBLE);
                            web.setText(artistDetailDTO.getWebsite());
                        } else {
                            web.setVisibility(View.GONE);
                        }
                    }

                    contentView.setVisibility(View.VISIBLE);
                    timeoutLayout.setVisibility(View.GONE);
                } else {
                    contentView.setVisibility(View.GONE);
                    timeoutLayout.setVisibility(View.VISIBLE);
                }
                progressView.setVisibility(View.GONE);
            }
        }.execute();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);
    }
}
