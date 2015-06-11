package com.ashoksm.thiraseela.adapter;

import android.app.ActivityManager;
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

import com.ashoksm.thiraseela.R;
import com.ashoksm.thiraseela.dto.AcademyListDTO;
import com.ashoksm.thiraseela.utils.ImageDownloader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AcademyListAdapter extends RecyclerView.Adapter<AcademyListAdapter.ViewHolder> implements Filterable {

    private List<AcademyListDTO> academyListDTOs;
    private List<AcademyListDTO> filteredAcademyListDTOs = new ArrayList<>();
    private Context context;
    private Bitmap placeHolderImage;
    private ImageDownloader imageDownloader;
    private static RecyclerView recyclerView;
    private static TextView emptyView;

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
    public AcademyListAdapter(List<AcademyListDTO> academyListDTOsIn, Context contextIn, RecyclerView recyclerViewIn, TextView emptyViewIn) {
        this.academyListDTOs = academyListDTOsIn;
        filteredAcademyListDTOs.addAll(academyListDTOsIn);
        context = contextIn;
        placeHolderImage = BitmapFactory.decodeResource(contextIn.getResources(),
                R.mipmap.ic_launcher);
        ActivityManager am = (ActivityManager) contextIn.getSystemService(Context.ACTIVITY_SERVICE);
        int memClassBytes = am.getMemoryClass();
        imageDownloader = new ImageDownloader(memClassBytes);
        recyclerView = recyclerViewIn;
        emptyView = emptyViewIn;
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
        final AcademyListDTO academyListDTO = filteredAcademyListDTOs.get(position);
        holder.txtHeader.setText(academyListDTO.getName());
        holder.txtFooter.setText(academyListDTO.getPlace() + ", " + academyListDTO.getCity());
        String url = "http://thiraseela.com/gleimo/Academy/images/academy" + academyListDTO.getId() + "/thumb/logo.jpeg";
        Bitmap bitmap = imageDownloader.getBitmapFromMemCache(url);
        if (bitmap == null) {
            imageDownloader.download(url, holder.imageView, context.getResources(), placeHolderImage);
        } else {
            holder.imageView.setImageBitmap(bitmap);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return filteredAcademyListDTOs.size();
    }

    @Override
    public Filter getFilter() {
        return new ArtistFilter(this, academyListDTOs);
    }

    private static class ArtistFilter extends Filter {

        private final AcademyListAdapter adapter;

        private final List<AcademyListDTO> originalList;

        private final List<AcademyListDTO> filteredList;

        private ArtistFilter(AcademyListAdapter adapter, List<AcademyListDTO> originalList) {
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

                for (final AcademyListDTO academyListDTO : originalList) {
                    if (academyListDTO.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(academyListDTO);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.filteredAcademyListDTOs.clear();
            adapter.filteredAcademyListDTOs.addAll((ArrayList<AcademyListDTO>) results.values);
            if (adapter.filteredAcademyListDTOs.size() == 0) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }
            adapter.notifyDataSetChanged();
        }
    }

    public List<AcademyListDTO> getFilteredAcademyListDTOs() {
        return filteredAcademyListDTOs;
    }
}
