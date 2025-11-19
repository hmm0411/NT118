package course.examples.cinepople.fragment.tickets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import course.examples.cinepople.activity.auth.LoginActivity;
import course.examples.cinepople.activity.auth.SignUpActivity;
import course.examples.cinepople.adapter.TicketAdapter;
import course.examples.cinepople.domain.Booking;
import course.examples.cinepople.databinding.FragmentMainTicketsBinding;

public class TicketsFragment extends Fragment {

    private FragmentMainTicketsBinding binding;

    private List<Booking> paidTicketsList = new ArrayList<>();
    private List<Booking> unpaidTicketsList = new ArrayList<>();
    private TicketAdapter paidTicketAdapter;
    private TicketAdapter unpaidTicketAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainTicketsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Logic sẽ nằm trong onResume để tự cập nhật khi quay lại
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLoginStatus();
    }

    private void checkLoginStatus() {
//        if (getContext() == null || binding == null) return;
//
//        SharedPreferences sharedPref = getContext().getSharedPreferences(
//                LoginActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
//        String token = sharedPref.getString(LoginActivity.KEY_AUTH_TOKEN, null);
//
//        if (token != null && !token.isEmpty()) {
//            // --- ĐÃ ĐĂNG NHẬP ---
//            binding.loggedOutView.setVisibility(View.GONE);
//            binding.loggedInView.setVisibility(View.VISIBLE);
//
//            // Khởi tạo RecyclerView và load data (nếu chưa load)
//            setupRecyclerViews();
//            // loadTicketsData(); // (Hàm gọi API tải vé của bạn)
//
//        } else {
            // --- CHƯA ĐĂNG NHẬP ---
            binding.loggedInView.setVisibility(View.GONE);
            binding.loggedOutView.setVisibility(View.VISIBLE);

            // Set sự kiện click
            binding.btnGoToLogin.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            });

            binding.btnGoToSignup.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(), SignUpActivity.class));
            });
        //}
    }

    private void setupRecyclerViews() {
        // (Chỉ setup nếu adapter chưa được tạo để tránh tạo lại nhiều lần)
        if (paidTicketAdapter == null) {
            paidTicketAdapter = new TicketAdapter(getContext(), paidTicketsList, booking -> {});
            binding.recyclerPaidTickets.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.recyclerPaidTickets.setAdapter(paidTicketAdapter);
        }

        if (unpaidTicketAdapter == null) {
            unpaidTicketAdapter = new TicketAdapter(getContext(), unpaidTicketsList, booking -> {});
            binding.recyclerUnpaidTickets.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.recyclerUnpaidTickets.setAdapter(unpaidTicketAdapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}