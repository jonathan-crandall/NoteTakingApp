package com.crandallj.notetaking.repositories;

import androidx.lifecycle.MutableLiveData;

import com.crandallj.notetaking.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserRepository {

    FirebaseAuth auth;

    public static interface OnSuccessListener {
        public void onSuccess(User user);
    }
    public static interface OnFailureListener {
        public void onFailure(Exception e);
    }

    public UserRepository(){
        auth = FirebaseAuth.getInstance();
    }

    public static interface OnAuthStateChanged {
        public void onAuthStateChanged(User user);
    }

    public void setAuthStateChangedListener(OnAuthStateChanged listener) {
        auth.addAuthStateListener(auth -> {
            listener.onAuthStateChanged(getCurrentUser());
        });
    }
    public User getCurrentUser() {
        User user = new User();
        FirebaseUser fbUser = auth.getCurrentUser();
        if (fbUser == null) return null;

        user.uid = fbUser.getUid();
        user.email = fbUser.getEmail();
        return user;
    }

    public void signIn(String email, String password, OnSuccessListener onSuccessListener, OnFailureListener onFailureListener){

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                AuthResult result = task.getResult();
                User newUser = new User();
                newUser.email = result.getUser().getEmail();
                newUser.uid = result.getUser().getUid();
                onSuccessListener.onSuccess(newUser);
            } else {
                onFailureListener.onFailure(task.getException());
            }
        });

    }

    public void signUp(String email, String password, OnSuccessListener onSuccessListener, OnFailureListener onFailureListener) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AuthResult result = task.getResult();
                User newUser = new User();
                newUser.email = email;
                newUser.uid = result.getUser().getUid();
                onSuccessListener.onSuccess(newUser);
            } else {
                onFailureListener.onFailure(task.getException());
            }
        });

    }
    public void logout(){
        auth.signOut();
    }



}
