package course.examples.cinepople.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import course.examples.cinepople.domain.Seat;
import course.examples.cinepople.domain.Showtime;
import course.examples.cinepople.data.repository.ShowtimeRepository;

public class SelectSeatsViewModel extends ViewModel {

    private final ShowtimeRepository repository;

    // Dữ liệu hiển thị lên RecyclerView
    private final MutableLiveData<List<Seat>> seatList = new MutableLiveData<>();

    // Danh sách ID các ghế ĐANG CHỌN (VD: ["A1", "A2"])
    private final MutableLiveData<List<String>> selectedSeatsIds = new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<Double> totalPrice = new MutableLiveData<>(0.0);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public SelectSeatsViewModel() {
        this.repository = new ShowtimeRepository();
    }

    // --- Getters ---
    public LiveData<List<Seat>> getSeatList() { return seatList; }
    public LiveData<List<String>> getSelectedSeatsIds() { return selectedSeatsIds; }
    public LiveData<Double> getTotalPrice() { return totalPrice; }
    public LiveData<String> getErrorMessage() { return errorMessage; }

    // --- Logic ---

    public void loadSessionData(String sessionId) {
        MutableLiveData<Showtime> tempShowtime = new MutableLiveData<>();

        repository.getShowtimeDetail(sessionId, tempShowtime, errorMessage);

        // Quan sát dữ liệu trả về để xử lý SeatMap
        tempShowtime.observeForever(showtime -> {
            if (showtime != null && showtime.getSeatMap() != null) {
                processSeatMap(showtime.getSeatMap());
            }
        });
    }

// Trong SelectSeatsViewModel.java

    private void processSeatMap(Map<String, Seat> map) {
        if (map == null) return; // Kiểm tra map null

        List<Seat> list = new ArrayList<>(map.values());

        // Sắp xếp ghế: A1, A2... B1, B2...
        Collections.sort(list, new Comparator<Seat>() {
            @Override
            public int compare(Seat s1, Seat s2) {
                // 🟢 1. Kiểm tra Null an toàn cho ROW
                String row1 = s1.getRow() != null ? s1.getRow() : "";
                String row2 = s2.getRow() != null ? s2.getRow() : "";

                int rowCompare = row1.compareTo(row2);
                if (rowCompare != 0) return rowCompare;

                // 🟢 2. So sánh COL (int thì không lo null, nhưng cứ cẩn thận)
                return Integer.compare(s1.getCol(), s2.getCol());
            }
        });

        seatList.postValue(list);
    }

    public void toggleSeatSelection(Seat seat) {
        if (seat.isSold()) return; // Không làm gì nếu ghế đã bán

        List<String> currentSelection = selectedSeatsIds.getValue();
        if (currentSelection == null) currentSelection = new ArrayList<>();

        if (currentSelection.contains(seat.getCode())) {
            // Bỏ chọn
            currentSelection.remove(seat.getCode());
            updatePrice(-seat.getPrice());
        } else {
            // Chọn mới
            currentSelection.add(seat.getCode());
            updatePrice(seat.getPrice());
        }

        selectedSeatsIds.setValue(currentSelection);
    }

    private void updatePrice(double amount) {
        double current = totalPrice.getValue() != null ? totalPrice.getValue() : 0.0;
        totalPrice.setValue(current + amount);
    }
}