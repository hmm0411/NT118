//package course.examples.cinepople.adapter;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//import course.examples.cinepople.R;
//
//public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {
//
//    // Interface để gửi sự kiện click ra Fragment
//    public interface OnDateClickListener {
//        void onDateClick(Date date, int position);
//    }
//
//    private List<Date> dateList;
//    private OnDateClickListener listener;
//
//    public DateAdapter(List<Date> dateList, OnDateClickListener listener) {
//        this.dateList = dateList;
//        this.listener = listener;
//    }
//
//    @NonNull
//    @Override
//    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_date, parent, false);
//        return new DateViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
//        Date date = dateList.get(position);
//        holder.bind(date, position, listener);
//    }
//
//    @Override
//    public int getItemCount() {
//        return dateList.size();
//    }
//
//    // --- ViewHolder ---
//    static class DateViewHolder extends RecyclerView.ViewHolder {
//
//        ConstraintLayout rootLayout;
//        TextView tvDayOfWeek;
//        TextView tvDate;
//
//        public DateViewHolder(@NonNull View itemView) {
//            super(itemView);
//            rootLayout = itemView.findViewById(R.id.date_root_layout);
//            tvDayOfWeek = itemView.findViewById(R.id.tv_day_of_week);
//            tvDate = itemView.findViewById(R.id.tv_date);
//        }
//
//        public void bind(final Date date, final int position, final OnDateClickListener listener) {
//            tvDayOfWeek.setText(date.getDayOfWeek());
//            tvDate.setText(date.getDate());
//
//            // Thay đổi giao diện dựa trên trạng thái "isSelected"
//            // (Bạn cần tạo file drawable/selector_date_background.xml
//            // và color/selector_date_text_color.xml)
//            rootLayout.setSelected(date.isSelected());
//            tvDayOfWeek.setSelected(date.isSelected());
//            tvDate.setSelected(date.isSelected());
//
//            // Gán sự kiện click
//            itemView.setOnClickListener(v -> {
//                listener.onDateClick(date, position);
//            });
//        }
//    }
//}