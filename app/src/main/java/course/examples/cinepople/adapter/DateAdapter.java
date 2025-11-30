package course.examples.cinepople.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import course.examples.cinepople.R;
import course.examples.cinepople.domain.DateModel;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {

    private List<DateModel> dateList;
    private OnDateClickListener listener;

    // Biến để theo dõi vị trí đang được chọn (mặc định là 0 - ngày đầu tiên)
    private int selectedPosition = 0;

    // Interface để Fragment lắng nghe sự kiện click
    public interface OnDateClickListener {
        void onDateClick(DateModel date, int position);
    }

    public DateAdapter(List<DateModel> dateList, OnDateClickListener listener) {
        this.dateList = dateList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date_session, parent, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        DateModel dateModel = dateList.get(position);

        // 1. Gán dữ liệu text
        holder.tvDayOfWeek.setText(dateModel.getDayOfWeek());
        holder.tvDate.setText(dateModel.getDate());

        if (dateModel.isSelected()) {
            selectedPosition = position;
        }

        int mainRedColor = Color.parseColor("#990011"); // Màu đỏ đậm (như chữ Today)
        int grayBackgroundColor = Color.parseColor("#E0E0E0"); // Màu xám nhạt (nền Mon, Tue)

        if (selectedPosition == position) {
            // --- TRẠNG THÁI ĐƯỢC CHỌN
            holder.container.setStrokeColor(mainRedColor);
            holder.container.setStrokeWidth(3);

            holder.tvDate.setTextColor(mainRedColor);
            holder.tvDate.setBackgroundColor(Color.WHITE);

            holder.tvDayOfWeek.setTextColor(Color.WHITE);
            holder.tvDayOfWeek.setBackgroundColor(mainRedColor);

        } else {

            holder.container.setStrokeWidth(0);

            holder.tvDate.setTextColor(Color.BLACK);
            holder.tvDate.setBackgroundColor(Color.WHITE);

            holder.tvDayOfWeek.setTextColor(Color.BLACK);
            holder.tvDayOfWeek.setBackgroundColor(grayBackgroundColor);
        }

        // 3. Bắt sự kiện Click
        holder.itemView.setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();

            // Cập nhật trạng thái trong list dữ liệu (để đồng bộ)
            dateList.get(previousPosition).setSelected(false);
            dateList.get(selectedPosition).setSelected(true);

            // Thông báo cập nhật giao diện:
            // Chỉ reload 2 item bị thay đổi để tối ưu hiệu năng
            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);

            // Gửi callback về Fragment
            if (listener != null) {
                listener.onDateClick(dateModel, selectedPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }
            public static class DateViewHolder extends RecyclerView.ViewHolder {
            TextView tvDayOfWeek;
            TextView tvDate;

            // 🟢 SỬA LẠI: Đổi từ ConstraintLayout sang MaterialCardView
            MaterialCardView container;

            public DateViewHolder(@NonNull View itemView) {
                super(itemView);
                tvDayOfWeek = itemView.findViewById(R.id.tv_day_of_week);
                tvDate = itemView.findViewById(R.id.tv_date);

                // 🟢 ID này trong XML là MaterialCardView, nên biến container phải là MaterialCardView
                container = itemView.findViewById(R.id.item_date_session);
            }
        }
}