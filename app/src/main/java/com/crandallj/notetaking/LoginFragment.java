package com.crandallj.notetaking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crandallj.notetaking.databinding.FragmentLoginBinding;
import com.crandallj.notetaking.viewmodels.UserViewModel;


public class LoginFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentLoginBinding binding = FragmentLoginBinding.inflate(inflater, container, false);
        UserViewModel userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if(user != null){
                NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_applicationFragment);
            }
        });

        userViewModel.getErrorMessage().observe(getViewLifecycleOwner(), binding.errorMessage::setText);

        binding.signInButton.setOnClickListener(view -> {
            userViewModel.signIn(binding.editTextTextEmailAddress.getText().toString(), binding.editTextTextPassword.getText().toString());
        });
        binding.signUpButton.setOnClickListener(view -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_signUpFragment);
        });

        return binding.getRoot();
    }

}