//package course.examples.cinepople.ui.seats;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.DiffUtil;
//import androidx.recyclerview.widget.ListAdapter;
//import androidx.recyclerview.widget.RecyclerView;
//
//import course.examples.cinepople.R;
//
//public class SeatsAdapter extends ListAdapter<Seat, SeatsAdapter.SeatViewHolder> {
//
//    // Interface để gửi sự kiện click ngược về Activity/ViewModel
//    private OnSeatClickListener clickListener;
//
//    public SeatsAdapter(OnSeatClickListener listener) {
//        super(DIFF_CALLBACK);
//        this.clickListener = listener;
//    }
//
//    @NonNull
//    @Override
//    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.list_item_seat, parent, false);
//        return new SeatViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
//        Seat seat = getItem(position);
//        holder.bind(seat, clickListener);
//    }
//
//    /**
//     * ViewHolder chứa logic "vẽ" cho từng ô ghế
//     */
//    static class SeatViewHolder extends RecyclerView.ViewHolder {
//        TextView tvSeatName;
//        Context context;
//
//        public SeatViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvSeatName = itemView.findViewById(R.id.tv_seat_name);
//            context = itemView.getContext();
//        }
//
//        public void bind(final Seat seat, final OnSeatClickListener listener) {
//            tvSeatName.setText(seat.getId());
//
//            // Logic tô màu quan trọng
//            switch (seat.getStatus()) {
//                case AVAILABLE:
//                    tvSeatName.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.seat_available_bg));
//                    tvSeatName.setTextColor(ContextCompat.getColor(context, R.color.seat_available_text));
//                    break;
//                case OCCUPIED:
//                    tvSeatName.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.seat_occupied_bg));
//                    tvSeatName.setTextColor(ContextCompat.getColor(context, R.color.seat_occupied_text));
//                    break;
//                case CHOSEN:
//                    tvSeatName.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.seat_chosen_bg));
//                    tvSeatName.setTextColor(ContextCompat.getColor(context, R.color.seat_chosen_text));
//                    break;
//            }
//
//            // Gán sự kiện click
//            itemView.setOnClickListener(v -> listener.onSeatClick(seat));
//        }
//    }
//
//    /**
//     * Interface click listener
//     */
//    public interface OnSeatClickListener {
//        void onSeatClick(Seat seat);
//    }
//
//    /**
//     * DiffUtil giúp RecyclerView cập nhật hiệu quả
//     */
//    private static final DiffUtil.ItemCallback<Seat> DIFF_CALLBACK = new DiffUtil.ItemCallback<Seat>() {
//        @Override
//        public boolean areItemsTheSame(@NonNull Seat oldItem, @NonNull Seat newItem) {
//            return oldItem.getId().equals(newItem.getId());
//        }
//
//        @Override
//        public boolean areContentsTheSame(@NonNull Seat oldItem, @NonNull Seat newItem) {
//            return oldItem.equals(newItem); // Dùng hàm equals() ta đã viết
//        }
//    };
//}