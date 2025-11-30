package course.examples.cinepople.fragment.session;

import android.content.Context;
import android.os.Bundle;
import android.util.Log; // 🟢 Import Log
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import course.examples.cinepople.adapter.RegionAdapter;
import course.examples.cinepople.domain.Region;
import course.examples.cinepople.databinding.FragmentRegionSelectBinding;

public class RegionSelectFragment extends Fragment {

    // 🟢 1. KHAI BÁO TAG
    private static final String TAG = "RegionSelectFragment";
    private static final String ARG_REGION_LIST = "region_list";

    public interface OnProvinceSelectedListener {
        void onProvinceSelected(String regionId, String regionName);
    }

    private OnProvinceSelectedListener listener;
    private FragmentRegionSelectBinding binding;
    private RegionAdapter adapter;
    private List<Region> regionList = new ArrayList<>();

    public static RegionSelectFragment newInstance(List<Region> regions) {
        RegionSelectFragment fragment = new RegionSelectFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_REGION_LIST, (Serializable) regions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: Fragment đã gắn vào Activity"); // 🟢 Log Lifecycle
        if (context instanceof OnProvinceSelectedListener) {
            listener = (OnProvinceSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnProvinceSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Đang khởi tạo giao diện"); // 🟢 Log Lifecycle
        binding = FragmentRegionSelectBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: Bắt đầu xử lý dữ liệu"); // 🟢 Log Lifecycle

        if (getArguments() != null) {
            List<Region> receivedRegions = (List<Region>) getArguments().getSerializable(ARG_REGION_LIST);

            if (receivedRegions != null && !receivedRegions.isEmpty()) {
                Log.d(TAG, "onViewCreated: Nhận được " + receivedRegions.size() + " vùng từ Activity."); // 🟢 Log số lượng nhận được
                regionList.clear();
                regionList.addAll(receivedRegions);
                binding.progressBar.setVisibility(View.GONE);
            } else {
                Log.e(TAG, "onViewCreated: Danh sách vùng nhận được bị Null hoặc Rỗng!"); // 🟢 Log Lỗi
                Toast.makeText(getContext(), "Không có dữ liệu vùng", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e(TAG, "onViewCreated: Arguments bị Null!"); // 🟢 Log Lỗi
        }

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        Log.d(TAG, "setupRecyclerView: Thiết lập Adapter với " + regionList.size() + " item."); // 🟢 Log Adapter

        adapter = new RegionAdapter(regionList, region -> {
            Log.d(TAG, "User click chọn vùng: " + region.getName() + " (ID: " + region.getId() + ")"); // 🟢 Log sự kiện click
            if (listener != null) {
                listener.onProvinceSelected(region.getId(), region.getName());
            }
        });
        binding.recyclerProvinces.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerProvinces.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        Log.d(TAG, "onDestroyView");
    }
}