package com.crandallj.notetaking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crandallj.notetaking.databinding.FragmentSignUpBinding;
import com.crandallj.notetaking.viewmodels.UserViewModel;


public class SignUpFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentSignUpBinding binding = FragmentSignUpBinding.inflate(inflater, container, false);
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if(user != null){
                NavHostFragment.findNavController(this).navigate(R.id.action_signUpFragment_to_applicationFragment);
            }
        });

        userViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            binding.errorMessage2.setText(errorMessage);
        });

        binding.confirmSignUp.setOnClickListener(view -> {
            userViewModel.signUp(
                    binding.emailSignUp.getText().toString(),
                    binding.emailSignUpConfirmation.getText().toString(),
                    binding.passwordSignUp.getText().toString(),
                    binding.passwordSignUpConfirmation.getText().toString()
            );
        });

        return binding.getRoot();
    }

}