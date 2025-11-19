package course.examples.cinepople.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import course.examples.cinepople.R;
import course.examples.cinepople.domain.Booking;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private Context context;
    private List<Booking> bookingList;
    private OnTicketClickListener listener;

    // Interface để xử lý sự kiện click vào vé
    public interface OnTicketClickListener {
        void onTicketClick(Booking booking);
    }

    // Constructor
    public TicketAdapter(Context context, List<Booking> bookingList, OnTicketClickListener listener) {
        this.context = context;
        this.bookingList = bookingList;
        this.listener = listener;
    }

    // Cập nhật dữ liệu mới
    public void updateData(List<Booking> newBookingList) {
        this.bookingList = newBookingList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        if (booking == null) return;

//        // 1. Bind dữ liệu Text (Sửa getter cho khớp với model Booking của bạn)
//        holder.tvMovieTitle.setText(booking.getMovieTitle());
//        holder.tvDateTime.setText(booking.getShowDate() + " • " + booking.getShowTime());
//        holder.tvCinema.setText(booking.getCinemaName());
//        holder.tvSeats.setText("Seats: " + booking.getSeats()); // Ví dụ: List seats chuyển thành String
//
//        // Xử lý màu sắc trạng thái
//        if ("paid".equalsIgnoreCase(booking.getStatus())) {
//            holder.tvStatus.setText("PAID");
//            holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
//        } else {
//            holder.tvStatus.setText("UNPAID");
//            holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
//        }
//
//        // 2. Bind hình ảnh Poster bằng Glide
//        Glide.with(context)
//                .load(booking.getPosterUrl())
//                .placeholder(R.drawable.ic_placeholder) // Hình chờ (tạo drawable nếu chưa có)
//                .error(R.drawable.ic_error)             // Hình lỗi (tạo drawable nếu chưa có)
//                .into(holder.imgPoster);

        // 3. Xử lý sự kiện Click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTicketClick(booking);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (bookingList != null) {
            return bookingList.size();
        }
        return 0;
    }

    // ViewHolder Class
    public static class TicketViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPoster;
        TextView tvMovieTitle, tvDateTime, tvCinema, tvSeats, tvStatus;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPoster = itemView.findViewById(R.id.img_ticket_poster);
            tvMovieTitle = itemView.findViewById(R.id.tv_ticket_movie_title);
            tvDateTime = itemView.findViewById(R.id.tv_ticket_date_time);
            tvCinema = itemView.findViewById(R.id.tv_ticket_cinema);
            tvSeats = itemView.findViewById(R.id.tv_ticket_seats);
            tvStatus = itemView.findViewById(R.id.tv_ticket_status);
        }
    }
}