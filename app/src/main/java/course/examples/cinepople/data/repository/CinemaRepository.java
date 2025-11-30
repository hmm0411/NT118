package course.examples.cinepople.repository;

import androidx.lifecycle.MutableLiveData;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.List;

import course.examples.cinepople.domain.Cinema;

public class CinemaRepository {

    private final FirebaseFirestore db;
    private final CollectionReference cinemaCollection;
    private static final String CINEMA_COLLECTION = "cinemas"; // Tên Collection Firestore

    public CinemaRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.cinemaCollection = db.collection(CINEMA_COLLECTION);
    }

    /**
     * Lấy danh sách tất cả các rạp chiếu phim từ Firestore.
     * * @param cinemas LiveData để cập nhật danh sách Cinema.
     * @param errorMessage LiveData để cập nhật lỗi.
     */
    public void fetchAllCinemas(MutableLiveData<List<Cinema>> cinemas, MutableLiveData<String> errorMessage) {
        cinemaCollection
                .orderBy("name") // Tùy chọn: Sắp xếp theo tên rạp
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Cinema> cinemaList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                // Tự động map Document sang đối tượng Cinema
                                Cinema cinema = document.toObject(Cinema.class);
                                cinemaList.add(cinema);
                            } catch (Exception e) {
                                // Ghi log nếu có lỗi mapping dữ liệu
                                System.err.println("Lỗi mapping Cinema: " + e.getMessage());
                            }
                        }
                        // Cập nhật LiveData khi thành công
                        cinemas.postValue(cinemaList);
                    } else {
                        // Cập nhật LiveData lỗi
                        String error = task.getException() != null ? task.getException().getMessage() : "Lỗi không xác định khi tải rạp.";
                        errorMessage.postValue("Lỗi tải rạp: " + error);
                    }
                });
    }

    /**
     * Lấy danh sách các rạp dựa trên danh sách ID.
     * (Hữu ích khi bạn đã có danh sách ID rạp từ một truy vấn khác, ví dụ: sessions).
     * * @param cinemaIds Danh sách ID rạp cần truy vấn.
     * @param cinemas LiveData để cập nhật danh sách Cinema.
     * @param errorMessage LiveData để cập nhật lỗi.
     */
    public void fetchCinemasByIds(List<String> cinemaIds, MutableLiveData<List<Cinema>> cinemas, MutableLiveData<String> errorMessage) {
        if (cinemaIds == null || cinemaIds.isEmpty()) {
            cinemas.postValue(new ArrayList<>());
            return;
        }

        // Firestore giới hạn 30 cho truy vấn 'in'
        cinemaCollection
                .whereIn(com.google.firebase.firestore.FieldPath.documentId(), cinemaIds.subList(0, Math.min(cinemaIds.size(), 30)))
                .orderBy("name")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Cinema> cinemaList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Cinema cinema = document.toObject(Cinema.class);
                            cinemaList.add(cinema);
                        }
                        cinemas.postValue(cinemaList);
                    } else {
                        String error = task.getException() != null ? task.getException().getMessage() : "Lỗi tải rạp theo ID.";
                        errorMessage.postValue("Lỗi tải rạp: " + error);
                    }
                });
    }
}