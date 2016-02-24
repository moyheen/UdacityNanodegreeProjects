package com.moyinoluwa.gdgngevents.Champion;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moyinoluwa.gdgngevents.GDGOrganizer;
import com.moyinoluwa.gdgngevents.R;

import java.util.List;

/**
 * Created by moyinoluwa on 2/14/16.
 */
public class GdgAdapter
        extends RecyclerView.Adapter<GdgAdapter.ViewHolder> {

    private final List<GDGOrganizer> gdgOrganizerList;

    public GdgAdapter(List<GDGOrganizer> gdgOrganizers) {
        this.gdgOrganizerList = gdgOrganizers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gdg_organizers_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.organizerName.setText(gdgOrganizerList.get(position).getOrganizer_name());
        holder.organizerEmail.setText(gdgOrganizerList.get(position).getOrganizer_email());
        holder.organizerGdg.setText(gdgOrganizerList.get(position).getGdg());

        // Hides the divider if the last item on the list is displayed
        if (position == getItemCount() - 1) {
            holder.divider.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return gdgOrganizerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView organizerName;
        private TextView organizerEmail;
        private TextView organizerGdg;
        private View divider;

        public ViewHolder(View view) {
            super(view);

            organizerName = (TextView) view.findViewById(R.id.organizer_name);
            organizerEmail = (TextView) view.findViewById(R.id.organizer_email);
            organizerGdg = (TextView) view.findViewById(R.id.organizer_gdg);
            divider = view.findViewById(R.id.divider);
        }
    }
}
