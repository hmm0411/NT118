package course.examples.cinepople.ui.session; // Thay đổi package của bạn

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import course.examples.cinepople.adapter.ProvinceAdapter;
import course.examples.cinepople.data.Province;
import course.examples.cinepople.databinding.FragmentProvinceSelectBinding;

public class ProvinceSelectFragment extends Fragment {

//    // --- Interface để giao tiếp với Activity ---
//    public interface OnProvinceSelectedListener {
//        void onProvinceSelected(String provinceName);
//    }
//    private OnProvinceSelectedListener listener;
//    // ---
//
//    private FragmentProvinceSelectBinding binding;
//    private ProvinceAdapter adapter;
//    private List<Province> provinceList = new ArrayList<>();
//    private String currentProvinceName;
//
//    // "Constructor" của Fragment
//    public static ProvinceSelectFragment newInstance(String currentProvinceName) {
//        ProvinceSelectFragment fragment = new ProvinceSelectFragment();
//        Bundle args = new Bundle();
//        args.putString("CURRENT_PROVINCE", currentProvinceName);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        // Kiểm tra xem Activity có implement interface không
//        if (context instanceof OnProvinceSelectedListener) {
//            listener = (OnProvinceSelectedListener) context;
//        } else {
//            throw new RuntimeException(context.toString() + " must implement OnProvinceSelectedListener");
//        }
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            currentProvinceName = getArguments().getString("CURRENT_PROVINCE");
//        }
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        binding = FragmentProvinceSelectBinding.inflate(inflater, container, false);
//        return binding.getRoot();
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        // Hiển thị tỉnh/TP hiện tại ở trên cùng
//        binding.textCurrentCity.setText(currentProvinceName);
//
//        setupRecyclerView();
//        loadProvinces(); // Tải dữ liệu (giả)
//    }
//
//    private void setupRecyclerView() {
//        adapter = new ProvinceAdapter(provinceList, province -> {
//            // Khi một tỉnh/TP được nhấn
//            if (listener != null) {
//                listener.onProvinceSelected(province.getName());
//            }
//        });
//        binding.recyclerProvinces.setLayoutManager(new LinearLayoutManager(getContext()));
//        binding.recyclerProvinces.setAdapter(adapter);
//    }
//
//    private void loadProvinces() {
//        // TODO: Thay thế bằng dữ liệu thật từ Firestore/API
//        provinceList.clear();
//        provinceList.add(new Province("Gia Lai"));
//        provinceList.add(new Province("Vinh Long"));
//        provinceList.add(new Province("Ba Ria - Vung Tau"));
//        provinceList.add(new Province("Tay Ninh"));
//        provinceList.add(new Province("Da Nang"));
//        provinceList.add(new Province("Ha Noi"));
//        adapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null; // Tránh memory leak
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        listener = null;
//    }
}