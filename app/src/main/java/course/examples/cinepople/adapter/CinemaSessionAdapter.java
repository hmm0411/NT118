//package course.examples.cinepople.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast; // Thêm import
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//// SỬA: Import Chip và ChipGroup
//import com.google.android.material.chip.Chip;
//import com.google.android.material.chip.ChipGroup;
//
//import java.util.List;
//
//import course.examples.cinepople.R;
//
//public class CinemaSessionAdapter extends RecyclerView.Adapter<CinemaSessionAdapter.CinemaViewHolder> {
//
//    private Context context;
//    //private List<CinemaSession> cinemaList;
//
////    public CinemaSessionAdapter(Context context, List<CinemaSession> cinemaList) {
////        this.context = context;
////        this.cinemaList = cinemaList;
////    }
//
//    @NonNull
//    @Override
//    public CinemaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context)
//                .inflate(R.layout.item_cinema_session, parent, false);
//        return new CinemaViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull CinemaViewHolder holder, int position) {
//        CinemaSession cinemaSession = cinemaList.get(position);
//        holder.bind(cinemaSession);
//    }
//
//    @Override
//    public int getItemCount() {
//        return cinemaList.size();
//    }
//
//    // --- ViewHolder (Đã sửa) ---
//    class CinemaViewHolder extends RecyclerView.ViewHolder {
//
//        TextView tvCinemaName, tvCinemaAddress, tvDistance;
//        // SỬA: Đổi RecyclerView thành ChipGroup
//        ChipGroup chipGroupTimes;
//
//        public CinemaViewHolder(@NonNull View itemView) {
//            super(itemView);
////            tvCinemaName = itemView.findViewById(R.id.tv_cinema_name);
////            tvCinemaAddress = itemView.findViewById(R.id.tv_cinema_address);
////            tvDistance = itemView.findViewById(R.id.tv_distance);
////            // SỬA: Tìm ID của ChipGroup
////            chipGroupTimes = itemView.findViewById(R.id.chip_group_times);
//        }
//
//        public void bind(CinemaSession session) {
//            tvCinemaName.setText(session.getCinemaName());
//            tvCinemaAddress.setText(session.getCinemaAddress());
//            tvDistance.setText(session.getDistance());
//
//            // --- SỬA LẠI HOÀN TOÀN LOGIC BIND GIỜ CHIẾU ---
//
//            // 1. Xóa các Chip cũ (rất quan trọng khi tái sử dụng ViewHolder)
//            chipGroupTimes.removeAllViews();
//
//            // 2. Lấy Context
//            Context context = chipGroupTimes.getContext();
//
//            // 3. Lặp qua danh sách giờ chiếu và tạo Chip
//            for (String time : session.getSessionTimes()) {
//                // Tạo một Chip mới
//                // Bạn cần tạo file style "Widget.App.Chip" trong res/values/styles.xml
//                // Hoặc dùng style có sẵn: com.google.android.material.R.style.Widget_MaterialComponents_Chip_Action
//                Chip chip = new Chip(context);
//
//                // Gán style cho chip (style này mô phỏng file item_session_time.xml của bạn)
//                chip.setChipBackgroundColorResource(android.R.color.transparent);
//                chip.setChipStrokeColorResource(R.color.red);
//                chip.setChipStrokeWidth(1.5f);
//                chip.setTextColor(context.getResources().getColor(R.color.red));
//                chip.setText(time);
//
//                // Bắt sự kiện click
//                chip.setOnClickListener(v -> {
//                    Toast.makeText(context, "Chọn suất: " + time, Toast.LENGTH_SHORT).show();
//                    // TODO: Chuyển sang màn hình Chọn Ghế
//                });
//
//                // 4. Thêm Chip vào nhóm
//                chipGroupTimes.addView(chip);
//            }
//            // (Bạn không cần SessionTimeAdapter và FlexboxLayoutManager nữa)
//        }
//    }
//}