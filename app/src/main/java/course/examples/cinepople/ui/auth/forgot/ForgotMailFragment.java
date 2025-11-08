package course.examples.cinepople.ui.auth.forgot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import course.examples.cinepople.databinding.FragmentAuthForgotMailBinding;
import course.examples.cinepople.databinding.FragmentAuthSignUpCreateAccountBinding;
import course.examples.cinepople.ui.auth.forgot.ForgotActivity;
import course.examples.cinepople.ui.auth.forgot.ForgotOTPFragment;

public class ForgotMailFragment extends Fragment {

    private FragmentAuthForgotMailBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAuthForgotMailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ForgotActivity) getActivity())
                        .loadFragment(new ForgotOTPFragment(), true);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}