package course.examples.cinepople.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import course.examples.cinepople.R;
import course.examples.cinepople.domain.Showtime;

public class CinemaAdapter extends RecyclerView.Adapter<CinemaAdapter.CinemaViewHolder> {

    private Context context;
    private List<Map.Entry<String, List<Showtime>>> groupedList;

    // 🟢 THÊM BIẾN: Map lưu địa chỉ (Key: Tên rạp, Value: Địa chỉ)
    private Map<String, String> addressMap = new HashMap<>();

    private OnTimeClickListener listener;

    public interface OnTimeClickListener {
        void onTimeClick(Showtime showtime, String time);
    }

    public CinemaAdapter(Context context, List<Map.Entry<String, List<Showtime>>> groupedList, OnTimeClickListener listener) {
        this.context = context;
        this.groupedList = groupedList;
        this.listener = listener;
    }

    // 🟢 HÀM MỚI: Cập nhật danh sách địa chỉ từ bên ngoài vào
    public void setAddressMap(Map<String, String> newAddressMap) {
        this.addressMap = newAddressMap;
        notifyDataSetChanged(); // Refresh lại giao diện để hiện địa chỉ
    }

    @NonNull
    @Override
    public CinemaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cinema, parent, false);
        return new CinemaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CinemaViewHolder holder, int position) {
        Map.Entry<String, List<Showtime>> entry = groupedList.get(position);
        String cinemaName = entry.getKey();
        List<Showtime> showtimes = entry.getValue();

        // 1. Tên rạp
        holder.tvName.setText(cinemaName);

        // 🟢 2. LẤY ĐỊA CHỈ TỪ MAP (Dựa vào tên rạp)
        String address = addressMap.get(cinemaName);
        if (address != null && !address.isEmpty()) {
            holder.tvAddress.setText(address);
            holder.tvAddress.setVisibility(View.VISIBLE);
        } else {
            holder.tvAddress.setText("Đang cập nhật...");
            // holder.tvAddress.setVisibility(View.GONE); // Hoặc ẩn đi nếu muốn
        }

        // 3. Khoảng cách (Giả lập)
        holder.tvDistance.setText("2.5km");

        // 4. Setup RecyclerView con
        TimeAdapter timeAdapter = new TimeAdapter(showtimes, listener);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
        holder.rvTimeSlots.setLayoutManager(layoutManager);
        holder.rvTimeSlots.setAdapter(timeAdapter);
    }

    @Override
    public int getItemCount() {
        return groupedList.size();
    }

    public static class CinemaViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress, tvDistance;
        RecyclerView rvTimeSlots;

        public CinemaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.text_cinema_name);
            tvAddress = itemView.findViewById(R.id.text_cinema_address);
            tvDistance = itemView.findViewById(R.id.text_cinema_distance);
            rvTimeSlots = itemView.findViewById(R.id.recycler_time_slots);
        }
    }

    // ... (TimeAdapter giữ nguyên) ...
    private class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeViewHolder> {
        private List<Showtime> showtimes;
        private OnTimeClickListener timeClickListener;

        public TimeAdapter(List<Showtime> showtimes, OnTimeClickListener listener) {
            this.showtimes = showtimes;
            this.timeClickListener = listener;
        }

        @NonNull
        @Override
        public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_slot, parent, false);
            return new TimeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TimeViewHolder holder, int position) {
            Showtime showtime = showtimes.get(position);
            holder.tvTime.setText(showtime.getTime());
            holder.itemView.setOnClickListener(v -> {
                if (timeClickListener != null) {
                    timeClickListener.onTimeClick(showtime, showtime.getTime());
                }
            });
        }

        @Override
        public int getItemCount() { return showtimes.size(); }

        class TimeViewHolder extends RecyclerView.ViewHolder {
            TextView tvTime;
            public TimeViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTime = itemView.findViewById(R.id.text_time_slot);
            }
        }
    }
}