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
import com.ashoksm.thiraseela.dto.AcademyDetailDTO;
import com.ashoksm.thiraseela.dto.AcademyListDTO;
import com.ashoksm.thiraseela.utils.ImageDownloader;
import com.ashoksm.thiraseela.wsclient.WSClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class AcademyPagerAdapter extends PagerAdapter {

    List<AcademyListDTO> items;
    LayoutInflater mLayoutInflater;
    Context context;
    ImageDownloader downloader;
    Bitmap placeHolderImage;

    public AcademyPagerAdapter(List<AcademyListDTO> itemsIn, Context contextIn) {
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
        final View itemView = mLayoutInflater.inflate(R.layout.academy_pager_item, container, false);

        final ImageView performerImage = (ImageView) itemView.findViewById(R.id.performer_img);
        final TextView academyType = (TextView) itemView.findViewById(R.id.acadType);
        final TextView address = (TextView) itemView.findViewById(R.id.address);
        final TextView mobile = (TextView) itemView.findViewById(R.id.mobile);
        final TextView phone = (TextView) itemView.findViewById(R.id.phone);
        final TextView email = (TextView) itemView.findViewById(R.id.email);
        final TextView web = (TextView) itemView.findViewById(R.id.web);
        final TextView performerProfile = (TextView) itemView.findViewById(R.id.performer_profile);
        final RelativeLayout addressLayout = (RelativeLayout) itemView.findViewById(R.id.addressLayout);
        final CardView aboutView = (CardView) itemView.findViewById(R.id.aboutView);

        loadDetails(position, itemView, performerImage, academyType, address, mobile, phone, email,
                web, performerProfile, addressLayout, aboutView);

        Button retryButton = (Button) itemView.findViewById(R.id.retryButton);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDetails(position, itemView, performerImage, academyType, address, mobile, phone, email,
                        web, performerProfile, addressLayout, aboutView);
            }
        });

        container.addView(itemView);
        return itemView;
    }

    private void loadDetails(final int position, final View itemView, final ImageView performerImage,
                             final TextView academyType, final TextView address,
                             final TextView mobile, final TextView phone, final TextView email, final TextView web,
                             final TextView performerProfile, final RelativeLayout addressLayout,
                             final CardView aboutView) {
        new AsyncTask<Void, Void, Void>() {
            AcademyDetailDTO academyDetailDTO = null;
            ObjectMapper mapper = new ObjectMapper();
            ScrollView contentView = (ScrollView) itemView.findViewById(R.id.contentLayout);
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
                            "http://thiraseela.com/thiraandroidapp/academydetailservice.php");
                    Log.d("response", response);
                    academyDetailDTO = mapper.readValue(response, AcademyDetailDTO.class);
                } catch (IOException e) {
                    Log.e("MyPagerAdapter", e.getLocalizedMessage());
                    networkAvailable = false;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (networkAvailable) {
                    String url = "http://thiraseela.com/gleimo/Academy/images/academy"
                            + items.get(position).getId() + "/thumb/logo.jpeg";
                    Bitmap bitmap = downloader.getBitmapFromMemCache(url);
                    if (bitmap == null) {
                        downloader.download(url, performerImage, context.getResources(), placeHolderImage);
                    } else {
                        performerImage.setImageBitmap(bitmap);
                    }

                    if (academyDetailDTO != null) {
                        if (academyDetailDTO.getAbout() != null && academyDetailDTO.getAbout().trim().length() > 0) {
                            aboutView.setVisibility(View.VISIBLE);
                            performerProfile.setText(
                                    academyDetailDTO.getAbout().replaceAll("\\\\\"", "\"").replaceAll("\\\\'", "'"));
                        } else {
                            aboutView.setVisibility(View.GONE);
                        }

                        academyType.setText(academyDetailDTO.getAcadType());
                        if (academyDetailDTO.getAddress() != null &&
                                academyDetailDTO.getAddress().trim().length() > 0) {
                            addressLayout.setVisibility(View.VISIBLE);
                            address.setText(
                                    academyDetailDTO.getAddress().replaceAll("\\\\\"", "\"").replaceAll("\\\\'", "'"));
                        } else {
                            addressLayout.setVisibility(View.GONE);
                        }
                        if (academyDetailDTO.getMobile() != null && academyDetailDTO.getMobile().trim().length() > 0) {
                            mobile.setVisibility(View.VISIBLE);
                            mobile.setText(academyDetailDTO.getMobile());
                        } else {
                            mobile.setVisibility(View.GONE);
                        }
                        if (academyDetailDTO.getPhone() != null && academyDetailDTO.getPhone().trim().length() > 0) {
                            phone.setVisibility(View.VISIBLE);
                            phone.setText(academyDetailDTO.getPhone());
                        } else {
                            phone.setVisibility(View.GONE);
                        }
                        if (academyDetailDTO.getEmail() != null && academyDetailDTO.getEmail().trim().length() > 0) {
                            email.setVisibility(View.VISIBLE);
                            email.setText(academyDetailDTO.getEmail());
                        } else {
                            email.setVisibility(View.GONE);
                        }
                        if (academyDetailDTO.getWebsite() != null &&
                                academyDetailDTO.getWebsite().trim().length() > 0) {
                            web.setVisibility(View.VISIBLE);
                            web.setText(academyDetailDTO.getWebsite());
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
