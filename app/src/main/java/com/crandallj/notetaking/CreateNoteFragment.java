package com.crandallj.notetaking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crandallj.notetaking.databinding.FragmentApplicationBinding;
import com.crandallj.notetaking.databinding.FragmentCreateNoteBinding;
import com.crandallj.notetaking.models.Note;
import com.crandallj.notetaking.viewmodels.NotesViewModel;
import com.crandallj.notetaking.viewmodels.UserViewModel;

public class CreateNoteFragment extends Fragment {

    private boolean isSaving = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentCreateNoteBinding binding = FragmentCreateNoteBinding.inflate(inflater, container, false);
        NotesViewModel notesViewModel = new ViewModelProvider(getActivity()).get(NotesViewModel.class);
        UserViewModel userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        Note selectedNote = notesViewModel.getSelectedNote().getValue();
        if(selectedNote != null) {
            binding.editTextTitle.setText(selectedNote.getTitle().toString());
            binding.editTextBody.setText(selectedNote.getBody().toString());
            binding.save.setText("Update");
            binding.delete.setVisibility(View.VISIBLE);

            binding.delete.setOnClickListener(view -> {
                binding.delete.setEnabled(false);
                binding.save.setEnabled(false);
                notesViewModel.deleteNote(selectedNote);
            });

        }

        notesViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            binding.errorMessageNote.setText(errorMessage);
            binding.save.setEnabled(true);
            binding.delete.setEnabled(true);
        });

        notesViewModel.getSaving().observe(getViewLifecycleOwner(), saving -> {
            if (!isSaving && saving) {
                isSaving = true;
            } else if (isSaving && !saving)  {
                NavHostFragment.findNavController(this).navigateUp();
            }
        });

        userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) return;
            binding.save.setOnClickListener(view -> {
                binding.save.setEnabled(false);
                if (selectedNote == null) {
                    notesViewModel.createNote(
                            binding.editTextTitle.getText().toString(),
                            binding.editTextBody.getText().toString(),
                            user.uid
                    );
                } else {
                    notesViewModel.updateNote(
                            selectedNote,
                            binding.editTextTitle.getText().toString(),
                            binding.editTextBody.getText().toString()
                            );
                }
            });
            binding.cancel.setOnClickListener(view -> {
                NavHostFragment.findNavController(this).navigateUp();
                notesViewModel.clearError();
            });
        });


        return binding.getRoot();
    }
}