package course.examples.cinepople.fragment.session;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import course.examples.cinepople.adapter.CinemaAdapter;
import course.examples.cinepople.adapter.DateAdapter;
import course.examples.cinepople.domain.DateModel;
import course.examples.cinepople.domain.Session;
import course.examples.cinepople.databinding.FragmentSessionSelectBinding;
import course.examples.cinepople.activity.booking.SelectSeatsActivity;
// import course.examples.cinepople.ui.seats.SelectSeatsActivity;

// --- SỬA 1: Thêm lại Interface của CinemaAdapter ---
public class SessionSelectFragment extends Fragment
        implements DateAdapter.OnDateClickListener,
        CinemaAdapter.OnTimeClickListener { // <-- BẠN ĐÃ THIẾU DÒNG NÀY

    private static final String TAG = "SessionSelectFragment";

    public interface OnLocationFilterClickListener {
        void onLocationFilterClicked();
    }
    private OnLocationFilterClickListener listener;

    private FragmentSessionSelectBinding binding;
    private FirebaseFirestore db;

    private String movieId;
    private String regionID;

    private DateAdapter dateAdapter;
    private CinemaAdapter cinemaAdapter;

    private List<DateModel> dateList = new ArrayList<>();
    private List<String> availableDateStrings = new ArrayList<>();
    private List<Session> cinemaSessionList = new ArrayList<>();

    public static SessionSelectFragment newInstance(String movieId, String regionID) {
        SessionSelectFragment fragment = new SessionSelectFragment();
        Bundle args = new Bundle();
        args.putString("MOVIE_ID", movieId);
        args.putString("REGION_ID", regionID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnLocationFilterClickListener) {
            listener = (OnLocationFilterClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnLocationFilterClickListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        if (getArguments() != null) {
            movieId = getArguments().getString("MOVIE_ID");
            regionID = getArguments().getString("REGION_ID");
        }
        Log.d(TAG, "Dữ liệu nhận được (Fragment onCreate):");
        Log.d(TAG, "   movieID: " + movieId);
        Log.d(TAG, "   regionID: " + regionID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSessionSelectBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnLocationFilter.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLocationFilterClicked();
            }
        });

        setupDateRecyclerView();
        setupCinemaRecyclerView();
        loadAvailableDates();
    }

    private void setupDateRecyclerView() {
        dateAdapter = new DateAdapter(dateList, this);
        binding.recyclerDates.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerDates.setAdapter(dateAdapter);
    }

    private void setupCinemaRecyclerView() {
        cinemaAdapter = new CinemaAdapter(getContext(), cinemaSessionList, this);
        binding.recyclerCinemas.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerCinemas.setNestedScrollingEnabled(false);
        binding.recyclerCinemas.setAdapter(cinemaAdapter);
    }

    private void loadAvailableDates() {
        Log.d(TAG, "Đang chạy truy vấn loadAvailableDates...");
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.tvNoResults.setVisibility(View.GONE);

        db.collection("session")
                .whereEqualTo("movieId", movieId)
                .whereEqualTo("regionID", regionID)
                .get()
                .addOnCompleteListener(task -> {
                    if (!isAdded()) return;

                    if (task.isSuccessful()) {
                        Log.d(TAG, "Truy vấn thành công. Tìm thấy: " + task.getResult().size() + " suất chiếu (trước khi lọc ngày).");

                        Set<String> dateSet = new HashSet<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String date = document.getString("date");
                            if (date != null) {
                                dateSet.add(date);
                            }
                        }

                        availableDateStrings.clear();
                        availableDateStrings.addAll(dateSet);
                        Collections.sort(availableDateStrings);

                        dateList.clear();
                        boolean isFirst = true;
                        for (String dateString : availableDateStrings) {
                            dateList.add(new DateModel(
                                    getFormattedDay(dateString),
                                    getFormattedDate(dateString),
                                    isFirst
                            ));
                            isFirst = false;
                        }

                        dateAdapter.notifyDataSetChanged();
                        Log.d(TAG, "Đã cập nhật DateAdapter. Tổng số ngày duy nhất: " + dateList.size());

                        if (!availableDateStrings.isEmpty()) {
                            loadCinemaSessions(availableDateStrings.get(0));
                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                            Log.w(TAG, "Không tìm thấy ngày nào có sẵn.");
                            binding.tvNoResults.setText("Không tìm thấy suất chiếu");
                            binding.tvNoResults.setVisibility(View.VISIBLE);
                        }
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        Log.w(TAG, "Error getting available dates: ", task.getException());
                    }
                });
    }

    private void loadCinemaSessions(String dateString) {
        cinemaSessionList.clear();
        cinemaAdapter.notifyDataSetChanged();
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.tvNoResults.setVisibility(View.GONE);

        db.collection("session")
                .whereEqualTo("movieId", movieId)
                .whereEqualTo("regionID", regionID)
                .whereEqualTo("date", dateString)
                .get()
                .addOnCompleteListener(task -> {
                    if (!isAdded()) return;
                    binding.progressBar.setVisibility(View.GONE);

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Session session = document.toObject(Session.class);
                            cinemaSessionList.add(session);
                        }
                        cinemaAdapter.notifyDataSetChanged();

                        if (cinemaSessionList.isEmpty()) {
                            binding.tvNoResults.setText("Không có rạp nào cho ngày này");
                            binding.tvNoResults.setVisibility(View.VISIBLE);
                        }

                    } else {
                        Log.w(TAG, "Error getting cinema sessions: ", task.getException());
                        Toast.makeText(getContext(), "Failed to load sessions.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDateClick(DateModel date, int position) {
        if (position < availableDateStrings.size() && position >= 0) {
            String selectedDateString = availableDateStrings.get(position);
            loadCinemaSessions(selectedDateString);
        }
    }
    @Override
    public void onTimeClick(Session session, String time) {
        String uniqueSessionId = session.getId();

        Intent intent = new Intent(getActivity(), SelectSeatsActivity.class);

        intent.putExtra("SESSION_ID", uniqueSessionId);
        intent.putExtra("CINEMA_NAME", session.getCinemaName());
        intent.putExtra("MOVIE_ID", session.getMovieId());
        intent.putExtra("SESSION_TIME", time);

        startActivity(intent);
    }
    private String getFormattedDay(String inputDate) {
        try {
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            SimpleDateFormat formatter = new SimpleDateFormat("EEE", Locale.US); // "Wed"
            Date date = parser.parse(inputDate);
            return formatter.format(date);
        } catch (Exception e) { return "N/A"; }
    }

    private String getFormattedDate(String inputDate) {
        try {
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            SimpleDateFormat formatter = new SimpleDateFormat("MMM, d", Locale.US); // "Nov, 12"
            Date date = parser.parse(inputDate);
            return formatter.format(date);
        } catch (Exception e) { return "N/A"; }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}