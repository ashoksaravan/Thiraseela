package com.ashoksm.thiraseela.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashoksm.thiraseela.DownloadImageTask;
import com.ashoksm.thiraseela.R;
import com.ashoksm.thiraseela.vo.ArtistListVO;
import com.ashoksm.thiraseela.vo.TroupesListVO;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class TroupesListAdapter extends RecyclerView.Adapter<TroupesListAdapter.ViewHolder> {

	private List<TroupesListVO> artistListVOs;
	private int lastPosition = -1;
	private Context context;

	// Provide a reference to the views for each data item
	// Complex data items may need more than one view per item, and
	// you provide access to all the views for a data item in a view holder
	public class ViewHolder extends RecyclerView.ViewHolder {
		// each data item is just a string in this case
		public TextView txtHeader;
		public TextView txtFooter;
        public TextView location;
        public ImageView imageView;
		public View view;

		public ViewHolder(View v) {
			super(v);
			txtHeader = (TextView) v.findViewById(R.id.firstLine);
			txtFooter = (TextView) v.findViewById(R.id.secondLine);
            imageView = (ImageView) v.findViewById(R.id.icon);
            location = (TextView) v.findViewById(R.id.thirdLine);
			view = v;
		}
	}


	// Provide a suitable constructor (depends on the kind of dataset)
	public TroupesListAdapter(List<TroupesListVO> artistListVOsIn, Context contextIn) {
        artistListVOs = artistListVOsIn;
		context = contextIn;
	}

	// Create new views (invoked by the layout manager)
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// create a new view
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.troupes_list_layout, parent, false);
		// set the view's size, margins, paddings and layout parameters
		ViewHolder vh = new ViewHolder(v);
		return vh;
	}

	// Replace the contents of a view (invoked by the layout manager)
	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		// - get element from your dataset at this position
		// - replace the contents of the view with that element
		final TroupesListVO artistListVO = artistListVOs.get(position);
		holder.txtHeader.setText(artistListVO.getName());
        holder.txtFooter.setText(artistListVO.getDesignation());
        holder.location.setText(artistListVO.getLocation());
        new DownloadImageTask(holder.imageView).execute(artistListVO.getUrl());

        setAnimation(holder.view, position);
	}

	private void setAnimation(View viewToAnimate, int position) {
		// If the bound view wasn't previously displayed on screen, it's
		// animated
		Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom
				: R.anim.down_from_top);
		viewToAnimate.startAnimation(animation);
		lastPosition = position;
	}

	// Return the size of your dataset (invoked by the layout manager)
	@Override
	public int getItemCount() {
		return artistListVOs.size();
	}
}
