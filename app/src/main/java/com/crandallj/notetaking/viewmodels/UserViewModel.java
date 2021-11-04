package com.crandallj.notetaking.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.crandallj.notetaking.models.User;
import com.crandallj.notetaking.repositories.UserRepository;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserViewModel extends ViewModel {
    UserRepository repository;
    MutableLiveData<User> user = new MutableLiveData<>();
    MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public UserViewModel(){
        errorMessage.setValue("");
        repository = new UserRepository();
        repository.setAuthStateChangedListener(user -> {
            this.user.postValue(user);
        });
    }

    public MutableLiveData<User> getUser(){
        return user;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void signIn(String email, String password){
        if(email == null || email.isEmpty()){
            errorMessage.postValue("Email cannot be empty, please enter your email");
            return;
        }
        if(password == null || password.isEmpty()){
            errorMessage.postValue("Password cannot be empty, please enter your password");
            return;
        }
        repository.signIn(
                email,
                password,
                user -> this.user.postValue(user),
                e -> errorMessage.postValue(e.getMessage()));
    }

    public void signUp(String email, String emailConfirmation, String password, String passwordConfirmation) {
        // Input checks
        if(email == null || email.isEmpty()){
            errorMessage.postValue("Email cannot be empty, please enter your email");
            return;
        }
        if(password == null || password.isEmpty()){
            errorMessage.postValue("Password cannot be empty, please enter your password");
            return;
        }
        if(password.length() < 9){
            errorMessage.postValue("Your password must be 9 characters or longer");
            return;
        }
        if(!password.equals(passwordConfirmation)){
            errorMessage.postValue("Passwords do not match, please try again");
            return;
        }
        if(!email.equals(emailConfirmation)){
            errorMessage.postValue("Emails do not match, please try again");
            return;
        }

        repository.signUp(
                email,
                password,
                user -> this.user.postValue(user),
                e -> errorMessage.postValue(e.getMessage()));

    }
    public void logout(){
        repository.logout();
    }




}
