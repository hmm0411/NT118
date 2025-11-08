package course.examples.cinepople.ui.session;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

//import course.examples.cinepople.adapter.DateAdapter;
import course.examples.cinepople.databinding.FragmentSessionSelectBinding;

public class SessionSelectFragment extends Fragment {
    public interface OnLocationFilterClickListener {
    }

    // --- Interface để giao tiếp với Activity ---
//    public interface OnLocationFilterClickListener {
//        void onLocationFilterClicked();
//    }
    private OnLocationFilterClickListener listener;
    // ---

    private FragmentSessionSelectBinding binding;
    private String movieId;
    private String provinceName;

    // Adapters
    //private DateAdapter dateAdapter;
    //private CinemaSessionAdapter cinemaAdapter;

    // Data lists
    //private List<Date> dateList = new ArrayList<>();
    //private List<CinemaSession> cinemaList = new ArrayList<>();

    // "Constructor" của Fragment
    public static SessionSelectFragment newInstance(String movieId, String provinceName) {
        SessionSelectFragment fragment = new SessionSelectFragment();
        Bundle args = new Bundle();
        args.putString("MOVIE_ID", movieId);
        args.putString("PROVINCE_NAME", provinceName);
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
        if (getArguments() != null) {
            movieId = getArguments().getString("MOVIE_ID");
            provinceName = getArguments().getString("PROVINCE_NAME");
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

        // 1. Cài đặt nút lọc tỉnh/TP
        binding.btnLocationFilter.setText(provinceName);
        binding.btnLocationFilter.setOnClickListener(v -> {
//            if (listener != null) {
//                listener.onLocationFilterClicked();
//            }
        });

        // 2. Cài đặt RecyclerViews
        //setupDateRecyclerView();
        //setupCinemaRecyclerView();

//        // 3. Tải dữ liệu (giả)
//        loadDates();
//        loadCinemaSessions();
    }

//    private void setupDateRecyclerView() {
//        dateAdapter = new DateAdapter(dateList, (date, position) -> {
//            // TODO: Xử lý khi chọn ngày mới
//            // 1. Cập nhật trạng thái "selected" cho dateList
//            // 2. Gọi dateAdapter.notifyDataSetChanged()
//            // 3. Tải lại danh sách rạp (loadCinemaSessions())
//        });
//        binding.recyclerDates.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//        binding.recyclerDates.setAdapter(dateAdapter);
//    }
//
//    private void setupCinemaRecyclerView() {
//        cinemaAdapter = new CinemaSessionAdapter(getContext(), cinemaList);
//        binding.recyclerCinemas.setLayoutManager(new LinearLayoutManager(getContext()));
//        // Tắt tính năng cuộn lồng của RecyclerView này vì nó nằm trong NestedScrollView
//        binding.recyclerCinemas.setNestedScrollingEnabled(false);
//        binding.recyclerCinemas.setAdapter(cinemaAdapter);
//    }

//    private void loadDates() {
//        // TODO: Tải dữ liệu ngày thật
//        dateList.clear();
//        dateList.add(new Date("Today", "Oct 5", true)); // true = được chọn
//        dateList.add(new Date("Mon", "Oct 6", false));
//        dateList.add(new Date("Tue", "Oct 7", false));
//        dateList.add(new Date("Wed", "Oct 8", false));
//        dateList.add(new Date("Thu", "Oct 9", false));
//        dateAdapter.notifyDataSetChanged();
//    }

//    private void loadCinemaSessions() {
//        // TODO: Tải dữ liệu rạp từ Firestore/API
//        // Dựa trên 'movieId', 'provinceName', và ngày được chọn
//        cinemaList.clear();
//
//        // Dữ liệu giả
//        List<String> times1 = List.of("14:00", "15:40", "16:40", "17:40");
//        cinemaList.add(new CinemaSession("Cine Pople 1", "Thu Duc, " + provinceName, "1.0km", times1));
//
//        List<String> times2 = List.of("17:40", "18:40", "19:40");
//        cinemaList.add(new CinemaSession("Cine Pople 2", "Quan 1, " + provinceName, "3.5km", times2));
//
//        //cinemaAdapter.notifyDataSetChanged();
//    }


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