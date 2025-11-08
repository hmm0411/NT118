package course.examples.cinepople.ui.session;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import course.examples.cinepople.adapter.RegionAdapter;
import course.examples.cinepople.data.Region;
import course.examples.cinepople.databinding.FragmentRegionSelectBinding;

public class RegionSelectFragment extends Fragment {

    private static final String TAG = "RegionSelectFragment";

    // --- Interface để giao tiếp với Activity ---
    public interface OnProvinceSelectedListener {
        void onProvinceSelected(String provinceName);
    }
    private OnProvinceSelectedListener listener;
    // ---

    // SỬA: Tên binding của bạn là "FragmentProvinceSelectBinding"
    private FragmentRegionSelectBinding binding;
    private RegionAdapter adapter;
    private List<Region> regionList = new ArrayList<>();

    private FirebaseFirestore db;

    /**
     * SỬA: Xóa tham số 'currentProvinceName'
     */
    public static RegionSelectFragment newInstance() {
        RegionSelectFragment fragment = new RegionSelectFragment();
        // Không cần Bundle args nữa
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnProvinceSelectedListener) {
            listener = (OnProvinceSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnProvinceSelectedListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        // SỬA: Đã xóa logic lấy 'currentProvinceName' từ getArguments()
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegionSelectBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // SỬA: Xóa logic cho 'textCurrentCity'
        // binding.textCurrentCity.setText(currentProvinceName);

        setupRecyclerView();
        loadRegionsFromFirebase();
    }

    private void setupRecyclerView() {
        adapter = new RegionAdapter(regionList, region -> {
            if (listener != null) {
                listener.onProvinceSelected(region.getName());
            }
        });
        // SỬA: Tên RecyclerView trong binding của bạn là "recyclerProvinces"
        binding.recyclerProvinces.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerProvinces.setAdapter(adapter);
    }

    private void loadRegionsFromFirebase() {
        // SỬA: Tên ProgressBar trong binding của bạn là "progressBar"
        binding.progressBar.setVisibility(View.VISIBLE);
        regionList.clear(); // Xóa list cũ trước khi tải

        db.collection("regions")
                .orderBy("name") // Vẫn sắp xếp theo tên
                // SỬA: Đã xóa 'whereNotEqualTo'
                .get()
                .addOnCompleteListener(task -> {
                    if (!isAdded() || getContext() == null) {
                        return;
                    }
                    binding.progressBar.setVisibility(View.GONE);

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Region region = document.toObject(Region.class);
                            regionList.add(region);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.w(TAG, "Error getting regions.", task.getException());
                        Toast.makeText(getContext(), "Failed to load regions.", Toast.LENGTH_SHORT).show();
                    }
                });
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