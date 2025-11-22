package course.examples.cinepople.fragment.session;

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
import course.examples.cinepople.domain.Region;
import course.examples.cinepople.databinding.FragmentRegionSelectBinding;

public class RegionSelectFragment extends Fragment {

    private static final String TAG = "RegionSelectFragment";

    public interface OnProvinceSelectedListener {
        void onProvinceSelected(String regionId, String regionName);
    }

    private OnProvinceSelectedListener listener;
    private FragmentRegionSelectBinding binding;
    private RegionAdapter adapter;
    private List<Region> regionList = new ArrayList<>();
    private FirebaseFirestore db;

    public static RegionSelectFragment newInstance() {
        return new RegionSelectFragment();
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
        setupRecyclerView();
        loadRegionsFromFirebase();
    }

    private void setupRecyclerView() {
        adapter = new RegionAdapter(regionList, region -> {
            if (listener != null) {
                listener.onProvinceSelected(region.getId(), region.getName());
            }
        });
        binding.recyclerProvinces.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerProvinces.setAdapter(adapter);
    }

    private void loadRegionsFromFirebase() {
        binding.progressBar.setVisibility(View.VISIBLE);
        regionList.clear();

        db.collection("regions")
                .orderBy("name")
                .get()
                .addOnCompleteListener(task -> {
                    if (!isAdded()) return;
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