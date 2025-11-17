package course.examples.cinepople.fragment.tickets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import course.examples.cinepople.domain.Booking;
import course.examples.cinepople.databinding.FragmentMainTicketsBinding;

public class TicketsFragment extends Fragment {

    private static final String TAG = "TicketsFragment";
    private FragmentMainTicketsBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String currentUserId;

    private List<Booking> paidTicketsList = new ArrayList<>();

    private List<Booking> unpaidTicketsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainTicketsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (mAuth.getCurrentUser() != null) {
            currentUserId = mAuth.getCurrentUser().getUid();
        } else {
        }
    }

//    private void setupRecyclerViews() {
//        // --- 1. Vé Đã Thanh Toán (PAID) ---
//        paidTicketAdapter = new TicketAdapter(getContext(), paidTicketsList, booking -> {
//            // TODO: Mở TicketPaidActivity
//        });
//        binding.recyclerPaidTickets.setLayoutManager(new LinearLayoutManager(getContext()));
//        binding.recyclerPaidTickets.setAdapter(paidTicketAdapter);
//
//        // --- 2. Vé Chưa Thanh Toán (UNPAID) ---
//        unpaidTicketAdapter = new TicketAdapter(getContext(), unpaidTicketsList, booking -> {
//            // TODO: Mở TicketUnpaidActivity
//        });
//        binding.recyclerUnpaidTickets.setLayoutManager(new LinearLayoutManager(getContext()));
//        binding.recyclerUnpaidTickets.setAdapter(unpaidTicketAdapter);
//    }

//    private void loadTickets() {
//        if (currentUserId == null) return;
//
//        // Tải vé đã thanh toán
//        loadPaidTickets();
//
//        // Tải vé chưa thanh toán
//        loadUnpaidTickets();
//    }

//    private void loadPaidTickets() {
//        // Truy vấn Firebase: Lọc theo userId và status = "paid"
//        db.collection("bookings")
//                .whereEqualTo("userId", currentUserId)
//                .whereEqualTo("status", "paid")
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    paidTicketsList.clear();
//                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
//                        Booking booking = doc.toObject(Booking.class);
//                        if (booking != null) {
//                            paidTicketsList.add(booking);
//                        }
//                    }
//                    paidTicketAdapter.notifyDataSetChanged();
//                })
//                .addOnFailureListener(e -> {
//
//                });
//    }

//    private void loadUnpaidTickets() {
//        // Truy vấn Firebase: Lọc theo userId và status = "unpaid"
//        db.collection("bookings")
//                .whereEqualTo("userId", currentUserId)
//                .whereEqualTo("status", "unpaid")
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    unpaidTicketsList.clear();
//                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
//                        Booking booking = doc.toObject(Booking.class);
//                        if (booking != null) {
//                            unpaidTicketsList.add(booking);
//                        }
//                    }
//                    unpaidTicketAdapter.notifyDataSetChanged();
//                })
//                .addOnFailureListener(e -> {
//                    // TODO: Xử lý lỗi tải vé chưa thanh toán
//                });
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}