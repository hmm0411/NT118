package course.examples.cinepople.fragment.auth.signup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import course.examples.cinepople.activity.auth.SignUpActivity;
import course.examples.cinepople.databinding.FragmentAuthSignUpCreateProfileBinding;

public class SignUpCreateProfileFragment extends Fragment {

    private FragmentAuthSignUpCreateProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAuthSignUpCreateProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getActivity() instanceof SignUpActivity) {
                    ((SignUpActivity) getActivity())
                            .loadFragment(new SignUpSuccessLoadingFragment(), true);
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}