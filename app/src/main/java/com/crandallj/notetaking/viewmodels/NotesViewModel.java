package com.crandallj.notetaking.viewmodels;

import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.crandallj.notetaking.models.Note;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Observable;

public class NotesViewModel extends ViewModel {

    ObservableArrayList<Note> notes;
    MutableLiveData<Boolean> saving = new MutableLiveData<>();
    MutableLiveData<Note> selectedNote = new MutableLiveData<>();
    MutableLiveData<String> errorMessage = new MutableLiveData<>();
    FirebaseFirestore db;


    public NotesViewModel(){
        db = FirebaseFirestore.getInstance();
        saving.setValue(false);
    }

    public MutableLiveData<Boolean> getSaving() {
        return saving;
    }

    public MutableLiveData<Note> getSelectedNote() {
        return selectedNote;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void setSelectedNote(Note selectedNote) {
        this.selectedNote.setValue(selectedNote);
    }

    public void createNote(String title, String body, String userID){
        if(title.isEmpty()) {
            errorMessage.postValue("Please enter a title for your note.");
        }
        else {
            saving.setValue(true);
            Note note = new Note(
                    title,
                    System.currentTimeMillis(),
                    body,
                    userID
            );
            db
                    .collection("notes")
                    .document(note.getUserID() + "_" + note.getTimestamp())
                    .set(note)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            notes.add(note);
                            errorMessage.postValue("");
                        } else {
                            errorMessage.postValue("Note not created correctly, something went wrong with Firebase");
                        }
                        saving.setValue(false);
                    });
        }
    }
    public void updateNote(Note note, String title, String body){
        if(title.isEmpty()) {
            errorMessage.postValue("Please enter a title for your note.");
        } else {
            saving.setValue(true);
            note.setTitle(title);
            note.setBody(body);
            db
                    .collection("notes")
                    .document(note.getUserID() + "_" + note.getTimestamp())
                    .set(note)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            int pos = notes.indexOf(note);
                            notes.set(pos, note);
                            errorMessage.postValue("");
                        } else {
                            errorMessage.postValue("Note not updated correctly, something went wrong with Firebase");
                        }
                        saving.setValue(false);
                    });
        }
    }

    public void deleteNote(Note note){
        saving.setValue(true);
        db.collection("notes")
                .document(note.getUserID() + "_" + note.getTimestamp())
                .delete()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        notes.remove(note);
                        errorMessage.postValue("");
                    } else {
                        errorMessage.postValue("Note not deleted correctly, something went wrong with Firebase");
                    }
                    saving.setValue(false);
                });
    }

    public void clearError(){
        errorMessage.postValue("");
    }

    public ObservableArrayList<Note> getNotes(String userID) {
        if (notes == null){
            notes = new ObservableArrayList<>();
            loadNotes(userID);
        }

        return notes;
    }

    private void loadNotes(String userID){
        db.collection("notes").whereEqualTo("userID", userID)
                .get()
                .addOnCompleteListener(task ->  {
                    if(task.isSuccessful()) {
                        notes.addAll(task.getResult().toObjects(Note.class));
                    } else {

                    }
                });
    }





}
