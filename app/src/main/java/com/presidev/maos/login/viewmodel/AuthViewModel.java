package com.presidev.maos.login.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.AuthCredential;
import com.presidev.maos.login.model.User;
import com.presidev.maos.login.repository.AuthRepository;

public class AuthViewModel extends AndroidViewModel {
    private final AuthRepository authRepository;

    private final LiveData<User> userLiveData;

    public AuthViewModel (@NonNull Application application){
       super(application);
       authRepository = new AuthRepository(application);
        userLiveData = authRepository.getUserLiveData();
    }

    public LiveData<User> getUserLiveData(){
        return userLiveData;
    }

    public void authWithGoogle(AuthCredential authCredential){
        authRepository.authWithGoogle(authCredential);
    }

    public void registerWithEmail(String name, String email, String password){
        authRepository.registerWithEmail(name, email, password);
    }

    public void loginWithEmail(String email, String password){
        authRepository.loginWithEmail(email, password);
    }

    public void sendPasswordReset(String email){
        authRepository.sendPasswordReset(email);
    }
}
