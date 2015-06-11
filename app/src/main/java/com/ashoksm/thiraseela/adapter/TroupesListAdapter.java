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
import com.ashoksm.thiraseela.dto.TroupeListDTO;
import com.ashoksm.thiraseela.utils.ImageDownloader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TroupesListAdapter extends RecyclerView.Adapter<TroupesListAdapter.ViewHolder> implements Filterable {

    private List<TroupeListDTO> troupeListDTOs;
    private List<TroupeListDTO> filteredTroupeListDTOs = new ArrayList<>();
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
        public TextView location;
        public ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            txtFooter = (TextView) v.findViewById(R.id.secondLine);
            imageView = (ImageView) v.findViewById(R.id.icon);
            location = (TextView) v.findViewById(R.id.thirdLine);
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public TroupesListAdapter(List<TroupeListDTO> troupeListDTOsIn, Context contextIn, RecyclerView recyclerViewIn, TextView emptyViewIn) {
        this.troupeListDTOs = troupeListDTOsIn;
        filteredTroupeListDTOs.addAll(troupeListDTOsIn);
        context = contextIn;
        placeHolderImage = BitmapFactory.decodeResource(contextIn.getResources(),
                R.mipmap.ic_launcher);
        ActivityManager am = (ActivityManager)  contextIn.getSystemService(Context.ACTIVITY_SERVICE);
        int memClassBytes = am.getMemoryClass();
        imageDownloader = new ImageDownloader(memClassBytes);
        recyclerView = recyclerViewIn;
        emptyView = emptyViewIn;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.troupes_list_layout, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final TroupeListDTO troupeListDTO = filteredTroupeListDTOs.get(position);
        holder.txtHeader.setText(troupeListDTO.getName());
        holder.txtFooter.setText(troupeListDTO.getPrgrmName());
        holder.location.setText(troupeListDTO.getPlace() + ", " + troupeListDTO.getCity());
        String url = "http://thiraseela.com/gleimo/Troupes/images/truopes" + troupeListDTO.getId() + "/thumb/logo.jpeg";
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
        return filteredTroupeListDTOs.size();
    }

    @Override
    public Filter getFilter() {
        return new TroupesFilter(this, troupeListDTOs);
    }

    private static class TroupesFilter extends Filter {

        private final TroupesListAdapter adapter;

        private final List<TroupeListDTO> originalList;

        private final List<TroupeListDTO> filteredList;

        private TroupesFilter(TroupesListAdapter adapter, List<TroupeListDTO> originalList) {
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

                for (final TroupeListDTO troupeListDTO : originalList) {
                    if (troupeListDTO.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(troupeListDTO);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.filteredTroupeListDTOs.clear();
            adapter.filteredTroupeListDTOs.addAll((ArrayList<TroupeListDTO>) results.values);
            if(adapter.filteredTroupeListDTOs.size() == 0) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }
            adapter.notifyDataSetChanged();
        }
    }

    public List<TroupeListDTO> getFilteredTroupeListDTOs() {
        return filteredTroupeListDTOs;
    }
}
