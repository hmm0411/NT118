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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import course.examples.cinepople.adapter.CinemaAdapter;
import course.examples.cinepople.adapter.DateAdapter;
import course.examples.cinepople.domain.Cinema;
import course.examples.cinepople.domain.DateModel;
import course.examples.cinepople.domain.Showtime;
import course.examples.cinepople.databinding.FragmentSessionSelectBinding;
import course.examples.cinepople.activity.booking.SelectSeatsActivity;
import course.examples.cinepople.viewmodel.CinemaViewModel;
import course.examples.cinepople.viewmodel.ShowtimeViewModel;

public class CinemaSelectFragment extends Fragment
        implements DateAdapter.OnDateClickListener,
        CinemaAdapter.OnTimeClickListener {

    private static final String TAG = "CinemaSelectFragment";
    private static final String ARG_MOVIE_ID = "MOVIE_ID";
    private static final String ARG_REGION_ID = "REGION_ID";
    private static final String ARG_REGION_NAME = "REGION_NAME";

    public interface OnLocationFilterClickListener {
        void onLocationFilterClicked();
    }
    private OnLocationFilterClickListener listener;

    private FragmentSessionSelectBinding binding;
    private ShowtimeViewModel viewModel;
    private CinemaViewModel cinemaViewModel; // 🟢 Thêm ViewModel để lấy địa chỉ Rạp

    // Dữ liệu
    private String movieId, regionID, regionName, selectedDate;
    private DateAdapter dateAdapter;
    private CinemaAdapter cinemaAdapter;

    private List<DateModel> dateList = new ArrayList<>();
    private List<Showtime> allAvailableShowtimes = new ArrayList<>();
    private List<Map.Entry<String, List<Showtime>>> groupedCinemaSessions = new ArrayList<>();

    // Map lưu địa chỉ rạp (Tên -> Địa chỉ)
    private Map<String, String> cinemaAddressMap = new HashMap<>();

    public CinemaSelectFragment() {}

    public static CinemaSelectFragment newInstance(String movieId, String regionID) {
        CinemaSelectFragment fragment = new CinemaSelectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MOVIE_ID, movieId);
        args.putString(ARG_REGION_ID, regionID);
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

        // Khởi tạo cả 2 ViewModel
        viewModel = new ViewModelProvider(this).get(ShowtimeViewModel.class);
        cinemaViewModel = new ViewModelProvider(this).get(CinemaViewModel.class);

        if (getArguments() != null) {
            movieId = getArguments().getString(ARG_MOVIE_ID);
            regionID = getArguments().getString(ARG_REGION_ID);
            regionName = getArguments().getString(ARG_REGION_NAME);
        }
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
        Log.d(TAG, "onViewCreated");

        if (regionName != null) binding.btnLocationFilter.setText(regionName);
        binding.btnLocationFilter.setOnClickListener(v -> {
            if (listener != null) listener.onLocationFilterClicked();
        });

        setupRecyclerViews();

        // 🟢 1. Tạo lịch ngay lập tức
        processAvailableDates();

        // 🟢 2. Lắng nghe dữ liệu Showtime
        observeShowtimeViewModel();

        // 🟢 3. Lắng nghe và gọi API lấy thông tin Rạp (để có địa chỉ)
        observeCinemaViewModel();
        cinemaViewModel.fetchAllCinemas();

        // 🟢 4. Gọi API lấy Showtime
        loadAllAvailableSessions();
    }

    private void setupRecyclerViews() {
        // Date Adapter
        dateAdapter = new DateAdapter(dateList, this);
        binding.recyclerDates.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerDates.setAdapter(dateAdapter);

        // Cinema Adapter
        cinemaAdapter = new CinemaAdapter(getContext(), groupedCinemaSessions, this);
        binding.recyclerCinemas.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerCinemas.setNestedScrollingEnabled(false);
        binding.recyclerCinemas.setAdapter(cinemaAdapter);
    }

    // --- OBSERVER ---

    private void observeShowtimeViewModel() {
        viewModel.getShowtimes().observe(getViewLifecycleOwner(), showtimes -> {
            if (!isAdded()) return;
            binding.progressBar.setVisibility(View.GONE);
            binding.tvNoResults.setVisibility(View.GONE);

            allAvailableShowtimes.clear();
            if (showtimes != null && !showtimes.isEmpty()) {
                Log.d(TAG, "Showtime API: Nhận được " + showtimes.size() + " suất.");
                allAvailableShowtimes.addAll(showtimes);
            } else {
                Log.d(TAG, "Showtime API: Không có suất chiếu.");
            }

            // Lọc lại ngay khi có dữ liệu mới
            if (selectedDate != null) filterSessionsByDate(selectedDate);
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (!isAdded()) return;
            binding.progressBar.setVisibility(View.GONE);
            if (error != null) handleEmptyState("Lỗi tải dữ liệu: " + error);
        });
    }

    private void observeCinemaViewModel() {
        cinemaViewModel.getCinemas().observe(getViewLifecycleOwner(), cinemas -> {
            if (cinemas != null) {
                Log.d(TAG, "Cinema API: Nhận được " + cinemas.size() + " rạp.");
                cinemaAddressMap.clear();
                for (Cinema c : cinemas) {
                    if (c.getName() != null && c.getAddress() != null) {
                        cinemaAddressMap.put(c.getName(), c.getAddress());
                    }
                }
                // Cập nhật Map địa chỉ vào Adapter
                if (cinemaAdapter != null) {
                    cinemaAdapter.setAddressMap(cinemaAddressMap);
                }
            }
        });
    }

    // --- LOGIC ---

    private void loadAllAvailableSessions() {
        if (movieId == null || regionID == null) return;
        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.fetchShowtimes(movieId, regionID, null);
    }

    private void processAvailableDates() {
        dateList.clear();
        Calendar calendar = Calendar.getInstance();
        Locale locale = new Locale("vi", "VN");
        SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", locale);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM", locale);

        for (int i = 0; i < 10; i++) {
            String fullDate = apiFormat.format(calendar.getTime());
            boolean isSelected = (i == 0);

            String dayDisplay = dayFormat.format(calendar.getTime());
            if (dayDisplay.length() > 0)
                dayDisplay = dayDisplay.substring(0, 1).toUpperCase() + dayDisplay.substring(1);

            dateList.add(new DateModel(dayDisplay, dateFormat.format(calendar.getTime()), isSelected, fullDate));

            if (isSelected) selectedDate = fullDate;
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        dateAdapter.notifyDataSetChanged();
    }

    private void filterSessionsByDate(String dateString) {
        if (dateString == null) return;
        selectedDate = dateString;
        groupedCinemaSessions.clear();

        List<Showtime> filteredList = new ArrayList<>();

        // Lọc theo ngày
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            filteredList = allAvailableShowtimes.stream()
                    .filter(s -> dateString.equals(s.getDate()))
                    .collect(Collectors.toList());
        } else {
            for (Showtime s : allAvailableShowtimes) {
                if (dateString.equals(s.getDate())) filteredList.add(s);
            }
        }

        if (!filteredList.isEmpty()) {
            // Nhóm theo tên rạp
            Map<String, List<Showtime>> groupedMap = new LinkedHashMap<>();
            for (Showtime s : filteredList) {
                String cinemaName = s.getCinemaName();
                if (!groupedMap.containsKey(cinemaName)) {
                    groupedMap.put(cinemaName, new ArrayList<>());
                }
                groupedMap.get(cinemaName).add(s);
            }
            groupedCinemaSessions.addAll(groupedMap.entrySet());
            binding.tvNoResults.setVisibility(View.GONE);
        } else {
            binding.tvNoResults.setText("Không có suất chiếu cho ngày này");
            binding.tvNoResults.setVisibility(View.VISIBLE);
        }

        cinemaAdapter.notifyDataSetChanged();
    }

    private void handleEmptyState(String message) {
        binding.tvNoResults.setText(message);
        binding.tvNoResults.setVisibility(View.VISIBLE);
        groupedCinemaSessions.clear();
        cinemaAdapter.notifyDataSetChanged();
    }

    // --- CLICK EVENTS ---

    @Override
    public void onDateClick(DateModel date, int position) {
        Log.d(TAG, "Chọn ngày: " + date.getFullDate());
        filterSessionsByDate(date.getFullDate());
    }

    @Override
    public void onTimeClick(Showtime showtime, String time) {
        Intent intent = new Intent(getActivity(), SelectSeatsActivity.class);

        // 🟢 SỬA: Dùng hằng số từ SelectSeatsActivity để đảm bảo khớp Key
        intent.putExtra(SelectSeatsActivity.SESSION_ID_KEY, showtime.getId());
        intent.putExtra(SelectSeatsActivity.SESSION_TIME_KEY, time);

        // Các key này bạn đang hardcode string bên Activity, nên giữ nguyên
        intent.putExtra("CINEMA_NAME", showtime.getCinemaName());
        intent.putExtra("MOVIE_TITLE", showtime.getMovieTitle());
        intent.putExtra("MOVIE_ID", showtime.getMovieId());

        // Gửi thêm Room Name nếu cần
        intent.putExtra("ROOM_NAME", showtime.getRoomName());

        startActivity(intent);
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