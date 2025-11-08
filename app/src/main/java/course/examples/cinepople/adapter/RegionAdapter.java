package course.examples.cinepople.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import course.examples.cinepople.R;
import course.examples.cinepople.data.Region;

public class RegionAdapter extends RecyclerView.Adapter<RegionAdapter.ProvinceViewHolder> {

    public interface OnProvinceClickListener {
        void onProvinceClick(Region region);
    }

    private List<Region> regionList;
    private OnProvinceClickListener listener;

    public RegionAdapter(List<Region> provinceList, OnProvinceClickListener listener) {
        this.regionList = provinceList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProvinceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_region, parent, false);
        return new ProvinceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProvinceViewHolder holder, int position) {
        Region region = regionList.get(position);
        holder.bind(region, listener);
    }

    @Override
    public int getItemCount() {
        return regionList.size();
    }

    // --- ViewHolder ---
    static class ProvinceViewHolder extends RecyclerView.ViewHolder {
        TextView tvRegionName;

        public ProvinceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRegionName = itemView.findViewById(R.id.tv_region_name);
        }

        public void bind(final Region province, final OnProvinceClickListener listener) {
            tvRegionName.setText(province.getName());
            itemView.setOnClickListener(v -> listener.onProvinceClick(province));
        }
    }
}