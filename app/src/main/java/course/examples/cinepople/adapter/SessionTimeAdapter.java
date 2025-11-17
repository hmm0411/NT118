package course.examples.cinepople.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import course.examples.cinepople.R;

public class SessionTimeAdapter extends RecyclerView.Adapter<SessionTimeAdapter.TimeViewHolder> {

    public interface OnTimeClickListener {
        void onTimeClick(String time);
    }

    private List<String> timeList;
    private OnTimeClickListener listener;

    public SessionTimeAdapter(List<String> timeList, OnTimeClickListener listener) {
        this.timeList = timeList;
        this.listener = listener;
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
        holder.bind(time, listener);
    }

    @Override
    public int getItemCount() {
        return (timeList != null) ? timeList.size() : 0;
    }

    class TimeViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;

        public TimeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_session_time);
        }

        public void bind(final String time, final OnTimeClickListener listener) {
            tvTime.setText(time);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTimeClick(time);
                }
            });
        }
    }
}