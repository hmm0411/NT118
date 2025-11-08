package course.examples.cinepople.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import course.examples.cinepople.R;

public class SessionTimeAdapter extends RecyclerView.Adapter<SessionTimeAdapter.TimeViewHolder> {

    private List<String> timeList;

    public SessionTimeAdapter(List<String> timeList) {
        this.timeList = timeList;
    }

    @NonNull
    @Override
    public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_session_time, parent, false);
        return new TimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeViewHolder holder, int position) {
        String time = timeList.get(position);
        holder.bind(time);
    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }

    // --- ViewHolder ---
    class TimeViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;

        public TimeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_session_time);
        }

        public void bind(String time) {
            tvTime.setText(time);

            // TODO: Xử lý sự kiện khi người dùng CHỌN một giờ chiếu
            itemView.setOnClickListener(v -> {
                Toast.makeText(v.getContext(), "Chọn suất: " + time, Toast.LENGTH_SHORT).show();
                // (Chuyển sang màn hình Chọn Ghế)
            });
        }
    }
}