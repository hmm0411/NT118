package course.examples.cinepople.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import course.examples.cinepople.R;
import course.examples.cinepople.domain.Seat;
import course.examples.cinepople.viewmodel.SelectSeatsViewModel;

public class SeatsAdapter extends RecyclerView.Adapter<SeatsAdapter.SeatViewHolder> {

    private List<Seat> seats = new ArrayList<>();
    private List<String> selectedIds = new ArrayList<>(); // Danh sách ID đang chọn để đổi màu
    private SelectSeatsViewModel viewModel;

    public SeatsAdapter(SelectSeatsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void submitList(List<Seat> newSeats) {
        this.seats = newSeats;
        notifyDataSetChanged();
    }

    // Cập nhật danh sách đang chọn từ ViewModel để render lại màu
    public void updateSelectedIds(List<String> ids) {
        this.selectedIds = ids;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seat, parent, false);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        Seat seat = seats.get(position);
        holder.bind(seat);
    }

    @Override
    public int getItemCount() {
        return seats.size();
    }

    class SeatViewHolder extends RecyclerView.ViewHolder {
        TextView tvSeat;

        public SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSeat = itemView.findViewById(R.id.tv_seat_code);
        }

        void bind(Seat seat) {
            tvSeat.setText(seat.getCode());

            if (seat.isSold()) {
                // Ghế đã bán: Màu xám, không click được
                tvSeat.setBackgroundResource(R.drawable.bg_seat_chosen);
                tvSeat.setTextColor(Color.WHITE);
                tvSeat.setEnabled(false);
            } else if (selectedIds.contains(seat.getCode())) {
                // Ghế đang chọn: Màu chủ đạo
                tvSeat.setBackgroundResource(R.drawable.bg_seat_occupied);
                tvSeat.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.text_color_dark));
                tvSeat.setEnabled(true);
            } else {
                // Ghế trống: Viền xám
                tvSeat.setBackgroundResource(R.drawable.bg_seat_available);
                tvSeat.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.text_color));
                tvSeat.setEnabled(true);
            }

            tvSeat.setOnClickListener(v -> {
                viewModel.toggleSeatSelection(seat);
            });
        }
    }
}