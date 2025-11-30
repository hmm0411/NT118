package course.examples.cinepople.data.repository;

import androidx.lifecycle.MutableLiveData;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import course.examples.cinepople.domain.Region;

public class RegionRepository {

    private final FirebaseFirestore db;
    private static final String REGION_COLLECTION = "regions"; // Tên Collection Firestore

    public RegionRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    /**
     * Lấy danh sách Regions từ Firestore.
     * @param regions LiveData để cập nhật danh sách Regions.
     * @param errorMessage LiveData để cập nhật lỗi.
     */
    public void fetchRegions(MutableLiveData<List<Region>> regions, MutableLiveData<String> errorMessage) {
        db.collection(REGION_COLLECTION)
                // Tùy chọn: Sắp xếp theo tên
                .orderBy("name")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Region> regionList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Tự động map Document sang đối tượng Region
                            Region region = document.toObject(Region.class);
                            regionList.add(region);
                        }
                        // Cập nhật LiveData khi thành công
                        regions.postValue(regionList);
                    } else {
                        // Cập nhật LiveData lỗi
                        errorMessage.postValue("Failed to load regions: " + task.getException().getMessage());
                    }
                });
    }
}