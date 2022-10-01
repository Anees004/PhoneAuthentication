package com.example.phoneauthetication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.phoneauthetication.model.AuthenticationRepository;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends AndroidViewModel {


    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<Boolean> getLoggedStatus() {
        return loggedStatus;
    }

    private AuthenticationRepository repository;
private MutableLiveData<FirebaseUser> userMutableLiveData;
private MutableLiveData<Boolean> loggedStatus;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        repository = new AuthenticationRepository(application);
        userMutableLiveData =repository.getFirebaseUserMutableLiveData();
        loggedStatus = repository.getUserLoggedstatusMutableLive();
    }

    public void register(String phNum)
    {
        repository.sendVerificationCode(phNum);
    }

    public void signOut()
    {
        repository.signOut();
    }

    public void verifyCode(String enteredCode)
    {
        repository.verifyCode(enteredCode);
    }

}
