package course.examples.cinepople.fragment.tickets;


import android.content.Intent;

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
import course.examples.cinepople.utility.SessionManager;

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
        checkLoginStatus();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLoginStatus();
    }

    private void checkLoginStatus() {
        if (binding == null || getActivity() == null) return;

        boolean isLoggedIn = SessionManager.isLoggedIn(getActivity());

        if (isLoggedIn) {
            binding.loggedInView.setVisibility(View.VISIBLE);
            binding.loggedOutView.setVisibility(View.GONE);
        } else {
            binding.loggedInView.setVisibility(View.GONE);
            binding.loggedInView.setVisibility(View.VISIBLE);

            binding.btnGoToLogin.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            });
        }
    }


    private void setupRecyclerViews() {
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