package com.ashoksm.thiraseela.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashoksm.thiraseela.DownloadImageTask;
import com.ashoksm.thiraseela.R;
import com.ashoksm.thiraseela.dto.ArtistListDTO;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ArtistListAdapter extends RecyclerView.Adapter<ArtistListAdapter.ViewHolder> implements Filterable {

    private List<ArtistListDTO> artistListDTOs;

    private List<ArtistListDTO> filteredArtistListDTOs = new ArrayList<>();

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
        public ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            txtFooter = (TextView) v.findViewById(R.id.secondLine);
            imageView = (ImageView) v.findViewById(R.id.icon);
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public ArtistListAdapter(List<ArtistListDTO> artistListVOsIn) {
        artistListDTOs = artistListVOsIn;
        filteredArtistListDTOs.addAll(artistListVOsIn);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_list_layout, parent, false);
        // set the view's size, margins, padding and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final ArtistListDTO artistListDTO = filteredArtistListDTOs.get(position);
        holder.txtHeader.setText(artistListDTO.getName());
        holder.txtFooter.setText(artistListDTO.getTitle());
        holder.imageView.setImageResource(R.mipmap.ic_launcher);
        new DownloadImageTask(holder.imageView).execute("http://thiraseela.com/gleimo/performers/images/perfomr" + artistListDTO.getId() + "/thumb/Perfmr_img.jpeg");
        //setAnimation(holder.view, position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return filteredArtistListDTOs.size();
    }

    @Override
    public Filter getFilter() {
        return new ArtistFilter(this, artistListDTOs);
    }

    private static class ArtistFilter extends Filter {

        private final ArtistListAdapter adapter;

        private final List<ArtistListDTO> originalList;

        private final List<ArtistListDTO> filteredList;

        private ArtistFilter(ArtistListAdapter adapter, List<ArtistListDTO> originalList) {
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

                for (final ArtistListDTO artistListDTO : originalList) {
                    if (artistListDTO.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(artistListDTO);
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
            adapter.filteredArtistListDTOs.addAll((ArrayList<ArtistListDTO>) results.values);
            adapter.notifyDataSetChanged();
        }
    }

    public List<ArtistListDTO> getFilteredArtistListDTOs() {
        return filteredArtistListDTOs;
    }

}
