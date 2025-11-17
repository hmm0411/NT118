package course.examples.cinepople.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import course.examples.cinepople.R;
import course.examples.cinepople.domain.SeatModel;
import course.examples.cinepople.viewmodel.SelectSeatsViewModel;

public class SeatsAdapter extends ListAdapter<SeatModel, SeatsAdapter.SeatViewHolder> {

    private final SelectSeatsViewModel viewModel;

    public SeatsAdapter(SelectSeatsViewModel viewModel) {
        super(DIFF_CALLBACK);
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_seat, parent, false);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        SeatModel seat = getItem(position);
        holder.bind(seat, viewModel);
    }

    static class SeatViewHolder extends RecyclerView.ViewHolder {
        TextView tvSeatName;

        public SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSeatName = itemView.findViewById(R.id.tv_seat_name);
        }

        public void bind(final SeatModel seat, final SelectSeatsViewModel viewModel) {
            tvSeatName.setText(seat.getId());

            int bgColorRes;
            int textColorRes;
            boolean isClickable;

            if (seat.isOccupied()) {
                bgColorRes = R.color.seat_occupied_bg;
                textColorRes = R.color.seat_occupied_text;
                isClickable = false;
            } else if (seat.isChosen()) {
                bgColorRes = R.color.main_color;
                textColorRes = R.color.btn_text_color;
                isClickable = true;
            } else {
                bgColorRes = R.color.seat_available_bg;
                textColorRes = R.color.seat_available_text;
                isClickable = true;
            }

            tvSeatName.setBackgroundTintList(ContextCompat.getColorStateList(itemView.getContext(), bgColorRes));
            tvSeatName.setTextColor(ContextCompat.getColor(itemView.getContext(), textColorRes));

            itemView.setClickable(isClickable);
            if (isClickable) {
                itemView.setOnClickListener(v -> viewModel.onSeatClicked(seat));
            } else {
                itemView.setOnClickListener(null);
            }
        }
    }

    private static final DiffUtil.ItemCallback<SeatModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<SeatModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull SeatModel oldItem, @NonNull SeatModel newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull SeatModel oldItem, @NonNull SeatModel newItem) {
            // So sánh cả hai trạng thái boolean để xác định item đã thay đổi
            return oldItem.getId().equals(newItem.getId()) &&
                    oldItem.isOccupied() == newItem.isOccupied() &&
                    oldItem.isChosen() == newItem.isChosen();
        }
    };
}