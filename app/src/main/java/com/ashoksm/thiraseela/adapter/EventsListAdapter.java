package com.ashoksm.thiraseela.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashoksm.thiraseela.utils.DownloadImageTask;
import com.ashoksm.thiraseela.R;
import com.ashoksm.thiraseela.dto.EventListDTO;
import com.ashoksm.thiraseela.utils.ImageDownloader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class EventsListAdapter extends RecyclerView.Adapter<EventsListAdapter.ViewHolder> implements Filterable {

    private List<EventListDTO> eventListDTOs;
    private List<EventListDTO> filteredArtistListDTOs = new ArrayList<>();
    private Context context;
    private Bitmap placeHolderImage;
    private ImageDownloader imageDownloader;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
        public TextView location;
        public TextView date;
        public TextView time;
        public ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            txtFooter = (TextView) v.findViewById(R.id.secondLine);
            imageView = (ImageView) v.findViewById(R.id.icon);
            location = (TextView) v.findViewById(R.id.thirdLine);
            date = (TextView) v.findViewById(R.id.fourthLine);
            time = (TextView) v.findViewById(R.id.fifthLine);
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public EventsListAdapter(List<EventListDTO> eventListDTOsIn, Context contextIn) {
        eventListDTOs = eventListDTOsIn;
        filteredArtistListDTOs.addAll(eventListDTOsIn);
        context = contextIn;
        placeHolderImage = BitmapFactory.decodeResource(contextIn.getResources(),
                R.mipmap.ic_launcher);
        imageDownloader = new ImageDownloader();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_list_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final EventListDTO eventListDTO = filteredArtistListDTOs.get(position);
        holder.txtHeader.setText(eventListDTO.getName());
        holder.txtFooter.setText(eventListDTO.getEventType() + " | " + eventListDTO.getArtistName());
        holder.location.setText(eventListDTO.getVenue());
        DateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        if(!eventListDTO.getStart().equals(eventListDTO.getEnd())) {
            holder.date.setText("Date : " + df.format(eventListDTO.getStart()) + " - " + df.format(eventListDTO.getEnd()));
        } else {
            holder.date.setText("On : " + df.format(eventListDTO.getStart()));
        }
        holder.time.setText("Time : " + eventListDTO.getFromTime() + " - " + eventListDTO.getToTime());
        if(eventListDTO.getTsimg() != null && eventListDTO.getTsimg().trim().length() > 0) {
            String url = "http://thiraseela.com/" + eventListDTO.getTsimg();
            imageDownloader.download(url, holder.imageView, context.getResources(), placeHolderImage);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return filteredArtistListDTOs.size();
    }

    @Override
    public Filter getFilter() {
        return new EventsFilter(this, eventListDTOs);
    }

    private static class EventsFilter extends Filter {

        private final EventsListAdapter adapter;

        private final List<EventListDTO> originalList;

        private final List<EventListDTO> filteredList;

        private EventsFilter(EventsListAdapter adapter, List<EventListDTO> originalList) {
            super();
            this.adapter = adapter;
            this.originalList = new LinkedList<>(originalList);
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final EventListDTO eventListDTO : originalList) {
                    if (eventListDTO.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(eventListDTO);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.filteredArtistListDTOs.clear();
            adapter.filteredArtistListDTOs.addAll((ArrayList<EventListDTO>) results.values);
            adapter.notifyDataSetChanged();
        }
    }

    public List<EventListDTO> getFilteredArtistListDTOs() {
        return filteredArtistListDTOs;
    }
}
