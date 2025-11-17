package course.examples.cinepople.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import course.examples.cinepople.R;
import course.examples.cinepople.domain.DateModel;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {

    public interface OnDateClickListener {
        void onDateClick(DateModel date, int position);
    }

    private List<DateModel> dateList;
    private OnDateClickListener listener;
    private int selectedPosition = 0;

    public DateAdapter(List<DateModel> dateList, OnDateClickListener listener) {
        this.dateList = dateList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_date_card, parent, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        DateModel date = dateList.get(position);

        date.setSelected(position == selectedPosition);

        holder.bind(date);
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    // --- ViewHolder ---
    class DateViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView cardRoot;
        TextView tvDayOfWeek;
        TextView tvDate;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            cardRoot = itemView.findViewById(R.id.card_root);
            tvDayOfWeek = itemView.findViewById(R.id.tv_day_of_week);
            tvDate = itemView.findViewById(R.id.tv_date);
        }

        public void bind(final DateModel date) {
            tvDayOfWeek.setText(date.getDayOfWeek());
            tvDate.setText(date.getDate());


            cardRoot.setSelected(date.isSelected());

            itemView.setOnClickListener(v -> {
                if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

                notifyItemChanged(selectedPosition);
                selectedPosition = getAdapterPosition();
                notifyItemChanged(selectedPosition);

                listener.onDateClick(date, selectedPosition);
            });
        }
    }
}