package course.examples.cinepople.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import course.examples.cinepople.domain.SeatModel;
import course.examples.cinepople.domain.Session;

public class SelectSeatsViewModel extends ViewModel {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final MutableLiveData<List<SeatModel>> _seatMap = new MutableLiveData<>();
    public LiveData<List<SeatModel>> getSeatMap() { return _seatMap; }

    private final MutableLiveData<List<String>> _selectedSeatsIds = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<String>> getSelectedSeatsIds() { return _selectedSeatsIds; }

    private final MutableLiveData<Double> _totalPrice = new MutableLiveData<>(0.0);
    public LiveData<Double> getTotalPrice() { return _totalPrice; }

    private Session currentSession;
    private double seatPrice = 18.00;

    public void loadSessionData(String sessionId) {
        db.collection("sessions").document(sessionId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Session session = documentSnapshot.toObject(Session.class);
                    if (session != null) {
                        currentSession = session;
                        Set<String> bookedSeats = new HashSet<>(session.getBookedSeats());
                        initializeSeatMap(session.getSeatMap(), bookedSeats);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("SeatsViewModel", "Error loading session data: " + e.getMessage());
                });
    }

    private void initializeSeatMap(List<String> allSeats, Set<String> bookedSeats) {
        if (allSeats == null) return;
        List<SeatModel> map = new ArrayList<>();

        for (String seatId : allSeats) {
            // isOccupied được xác định bằng dữ liệu Firebase (bookedSeats)
            boolean isOccupied = bookedSeats.contains(seatId);

            // Khởi tạo tất cả ghế đều chưa được chọn (isChosen = false)
            map.add(new SeatModel(seatId, seatPrice, isOccupied, false));
        }
        _seatMap.setValue(map);
    }

    public void onSeatClicked(SeatModel clickedSeat) {
        // Không thể chọn/bỏ chọn ghế đã bị chiếm
        if (clickedSeat.isOccupied()) {
            return;
        }

        List<SeatModel> currentMap = _seatMap.getValue();
        List<String> currentSelected = _selectedSeatsIds.getValue();
        if (currentMap == null || currentSelected == null) return;

        if (clickedSeat.isChosen()) {
            // BỎ CHỌN
            clickedSeat.setChosen(false);
            currentSelected.remove(clickedSeat.getId());
        } else {
            // CHỌN GHẾ
            clickedSeat.setChosen(true);
            currentSelected.add(clickedSeat.getId());
        }

        _selectedSeatsIds.setValue(currentSelected);
        _seatMap.setValue(currentMap);
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        if (_selectedSeatsIds.getValue() != null) {
            _totalPrice.setValue((double) _selectedSeatsIds.getValue().size() * seatPrice);
        } else {
            _totalPrice.setValue(0.0);
        }
    }
}