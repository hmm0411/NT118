package course.examples.cinepople.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import course.examples.cinepople.R;
import course.examples.cinepople.domain.Session;

public class CinemaAdapter extends RecyclerView.Adapter<CinemaAdapter.CinemaViewHolder> {

    public interface OnTimeClickListener {
        void onTimeClick(Session session, String time);
    }

    private Context context;
    private List<Session> sessionList;
    private OnTimeClickListener timeClickListener;

    public CinemaAdapter(Context context, List<Session> sessionList, OnTimeClickListener listener) {
        this.context = context;
        this.sessionList = sessionList;
        this.timeClickListener = listener;
    }

    @NonNull
    @Override
    public CinemaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_cinema_session, parent, false);
        return new CinemaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CinemaViewHolder holder, int position) {
        Session session = sessionList.get(position);
        holder.bind(session, timeClickListener);
    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    class CinemaViewHolder extends RecyclerView.ViewHolder {

        TextView tvCinemaName, tvCinemaAddress, tvDistance;
        RecyclerView recyclerTimeSlots;

        public CinemaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCinemaName = itemView.findViewById(R.id.text_cinema_name);
            tvCinemaAddress = itemView.findViewById(R.id.text_cinema_address);
            tvDistance = itemView.findViewById(R.id.text_cinema_distance);
            recyclerTimeSlots = itemView.findViewById(R.id.recycler_time_slots);
        }

        public void bind(final Session session, final OnTimeClickListener listener) {
            tvCinemaName.setText(session.getCinemaName());
            tvCinemaAddress.setText(session.getCinemaAddress());

            SessionTimeAdapter timeAdapter = new SessionTimeAdapter(session.getShowtimes(), time -> {
                if (listener != null) {
                    listener.onTimeClick(session, time);
                }
            });

            GridLayoutManager layoutManager = new GridLayoutManager(itemView.getContext(), 3);

            recyclerTimeSlots.setLayoutManager(layoutManager);
            recyclerTimeSlots.setAdapter(timeAdapter);
        }
    }
}