package com.presidev.maos.auth.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.AuthCredential;
import com.presidev.maos.auth.model.Account;
import com.presidev.maos.auth.repository.AuthRepository;

public class AuthViewModel extends AndroidViewModel {
    private final AuthRepository authRepository;

    private final LiveData<Account> userLiveData;

    public AuthViewModel (@NonNull Application application){
       super(application);
       authRepository = new AuthRepository(application);
       userLiveData = authRepository.getUserLiveData();
    }

    public LiveData<Account> getUserLiveData(){
        return userLiveData;
    }

    public void authWithGoogle(AuthCredential authCredential){
        authRepository.authWithGoogle(authCredential);
    }

    public void registerWithEmail(String name, String email, String password, String level){
        authRepository.registerWithEmail(name, email, password, level);
    }

    public void loginWithEmail(String email, String password){
        authRepository.loginWithEmail(email, password);
    }

    public void sendPasswordReset(String email){
        authRepository.sendPasswordReset(email);
    }

    public void logout(){
        authRepository.logout();
    }
}
