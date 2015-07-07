package com.ashoksm.thiraseela.adapter;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ashoksm.thiraseela.R;
import com.ashoksm.thiraseela.ui.EventsListActivity;
import com.ashoksm.thiraseela.utils.ImageDownloader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class EventsPagerAdapter extends PagerAdapter {

    LayoutInflater mLayoutInflater;
    Context context;
    ImageDownloader downloader;
    Bitmap placeHolderImage;
    DateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

    public EventsPagerAdapter(Context contextIn) {
        this.context = contextIn;
        mLayoutInflater = (LayoutInflater) contextIn.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ActivityManager am = (ActivityManager) contextIn.getSystemService(Context.ACTIVITY_SERVICE);
        downloader = ImageDownloader.getInstance(am.getMemoryClass());
        placeHolderImage = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.ic_launcher);
    }

    @Override
    public int getCount() {
        return EventsListActivity.EVENT_LIST_DTOS.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final View itemView = mLayoutInflater.inflate(R.layout.events_pager_item, container, false);

        TextView eventName = (TextView) itemView.findViewById(R.id.event_name);
        TextView category = (TextView) itemView.findViewById(R.id.category);
        TextView by = (TextView) itemView.findViewById(R.id.by);
        TextView venue = (TextView) itemView.findViewById(R.id.venue);
        TextView city = (TextView) itemView.findViewById(R.id.city);
        TextView date = (TextView) itemView.findViewById(R.id.date);
        TextView time = (TextView) itemView.findViewById(R.id.time);
        TextView contactName = (TextView) itemView.findViewById(R.id.contact_name);
        TextView phone = (TextView) itemView.findViewById(R.id.phone);
        TextView mobile = (TextView) itemView.findViewById(R.id.mobile);
        TextView email = (TextView) itemView.findViewById(R.id.email);
        TextView web = (TextView) itemView.findViewById(R.id.web);
        LinearLayout phoneLayout = (LinearLayout) itemView.findViewById(R.id.phone_layout);
        LinearLayout mobileLayout = (LinearLayout) itemView.findViewById(R.id.mobile_layout);
        LinearLayout emailLayout = (LinearLayout) itemView.findViewById(R.id.email_layout);
        LinearLayout webLayout = (LinearLayout) itemView.findViewById(R.id.web_layout);
        ImageView performerImg = (ImageView) itemView.findViewById(R.id.performer_img);

        if (EventsListActivity.EVENT_LIST_DTOS.get(position).getLogo() != null
                && EventsListActivity.EVENT_LIST_DTOS.get(position).getLogo().trim().length() > 0) {
            String url = "http://thiraseela.com/" + EventsListActivity.EVENT_LIST_DTOS.get(position).getLogo();
            Bitmap bitmap = downloader.getBitmapFromMemCache(url);
            if (bitmap == null) {
                downloader.download(url, performerImg, context.getResources(), placeHolderImage);
            } else {
                performerImg.setImageBitmap(bitmap);
            }
        }

        eventName.setText(EventsListActivity.EVENT_LIST_DTOS.get(position).getName());
        category.setText(EventsListActivity.EVENT_LIST_DTOS.get(position).getCategory());
        by.setText(EventsListActivity.EVENT_LIST_DTOS.get(position).getArtistName());
        venue.setText(EventsListActivity.EVENT_LIST_DTOS.get(position).getVenue());
        city.setText(EventsListActivity.EVENT_LIST_DTOS.get(position).getDistrict());

        if (!EventsListActivity.EVENT_LIST_DTOS.get(position).getStart().equals(
                EventsListActivity.EVENT_LIST_DTOS.get(position).getEnd())) {
            date.setText("Date : " + df.format(EventsListActivity.EVENT_LIST_DTOS.get(position).getStart()) + " - "
                    + df.format(EventsListActivity.EVENT_LIST_DTOS.get(position).getEnd()));
        } else {
            date.setText("On : " + df.format(EventsListActivity.EVENT_LIST_DTOS.get(position).getStart()));
        }
        time.setText("Time : " + EventsListActivity.EVENT_LIST_DTOS.get(position).getFromTime() + " - "
                + EventsListActivity.EVENT_LIST_DTOS.get(position).getToTime());
        contactName.setText(EventsListActivity.EVENT_LIST_DTOS.get(position).getContactName());
        if (EventsListActivity.EVENT_LIST_DTOS.get(position).getPhone() != null
                && EventsListActivity.EVENT_LIST_DTOS.get(position).getPhone().trim().length() > 0) {
            phone.setText(EventsListActivity.EVENT_LIST_DTOS.get(position).getPhone());
            phoneLayout.setVisibility(View.VISIBLE);
        } else {
            phoneLayout.setVisibility(View.GONE);
        }
        if (EventsListActivity.EVENT_LIST_DTOS.get(position).getMobile() != null
                && EventsListActivity.EVENT_LIST_DTOS.get(position).getMobile().trim().length() > 0) {
            mobile.setText(EventsListActivity.EVENT_LIST_DTOS.get(position).getMobile());
            mobileLayout.setVisibility(View.VISIBLE);
        } else {
            mobileLayout.setVisibility(View.GONE);
        }
        if (EventsListActivity.EVENT_LIST_DTOS.get(position).getEmail() != null
                && EventsListActivity.EVENT_LIST_DTOS.get(position).getEmail().trim().length() > 0) {
            email.setText(EventsListActivity.EVENT_LIST_DTOS.get(position).getEmail());
            emailLayout.setVisibility(View.VISIBLE);
        } else {
            emailLayout.setVisibility(View.GONE);
        }
        if (EventsListActivity.EVENT_LIST_DTOS.get(position).getWeb() != null
                && EventsListActivity.EVENT_LIST_DTOS.get(position).getWeb().trim().length() > 0) {
            web.setText(EventsListActivity.EVENT_LIST_DTOS.get(position).getWeb());
            webLayout.setVisibility(View.VISIBLE);
        } else {
            webLayout.setVisibility(View.GONE);
        }

        container.addView(itemView);
        return itemView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ScrollView) object);
    }
}
