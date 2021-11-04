package com.crandallj.notetaking;

import android.os.Bundle;

import androidx.databinding.ObservableList;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crandallj.notetaking.adapters.NotesAdapter;
import com.crandallj.notetaking.databinding.FragmentApplicationBinding;
import com.crandallj.notetaking.databinding.FragmentLoginBinding;
import com.crandallj.notetaking.models.Note;
import com.crandallj.notetaking.viewmodels.NotesViewModel;
import com.crandallj.notetaking.viewmodels.UserViewModel;


public class ApplicationFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentApplicationBinding binding = FragmentApplicationBinding.inflate(inflater, container, false);
        NotesViewModel notesViewModel = new ViewModelProvider(getActivity()).get(NotesViewModel.class);
        UserViewModel userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);


        userViewModel.getUser().observe(getViewLifecycleOwner(), (user -> {
            if(user == null) {
                NavHostFragment.findNavController(this).navigate(R.id.action_applicationFragment_to_loginFragment);
                return;
            }
            binding.fab.setOnClickListener(view -> {
                notesViewModel.setSelectedNote(null);
                NavHostFragment.findNavController(this).navigate(R.id.action_applicationFragment_to_createNoteFragment);
            });
            binding.notes.setAdapter(
                    new NotesAdapter(notesViewModel.getNotes(user.uid), note -> {
                        notesViewModel.setSelectedNote(note);
                        NavHostFragment.findNavController(this).navigate(R.id.action_applicationFragment_to_createNoteFragment);
                    })
            );
            binding.logout.setOnClickListener(view -> {
                userViewModel.logout();
                getActivity().getViewModelStore().clear();
            });
            binding.notes.setLayoutManager(new LinearLayoutManager(getContext()));
        }));


        return binding.getRoot();
    }
}