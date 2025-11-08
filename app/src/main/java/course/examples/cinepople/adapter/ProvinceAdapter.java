package course.examples.cinepople.adapter;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import course.examples.cinepople.data.Province;
import course.examples.cinepople.databinding.ItemProvinceBinding;

public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.ViewHolder> {
    private List<Province> provinces;
    private OnProvinceClickListener listener;

    public interface OnProvinceClickListener {
        void onClick(Province province);
    }

    public ProvinceAdapter(List<Province> provinces, OnProvinceClickListener listener) {
        this.provinces = provinces;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProvinceBinding binding = ItemProvinceBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Province province = provinces.get(position);
        holder.binding.textProvinceName.setText(province.getName());
        holder.itemView.setOnClickListener(v -> listener.onClick(province));
    }

    @Override
    public int getItemCount() { return provinces.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ItemProvinceBinding binding;
        ViewHolder(ItemProvinceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}