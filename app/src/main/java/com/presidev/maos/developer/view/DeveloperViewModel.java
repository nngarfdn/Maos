package com.presidev.maos.developer.view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.presidev.maos.developer.repository.DeveloperRepository;

import java.util.ArrayList;

public class DeveloperViewModel extends ViewModel {
    private final DeveloperRepository repository = new DeveloperRepository();

    public LiveData<ArrayList<String>> getQuoteBackgroundListLiveData(){
        return repository.getQuoteBackgroundListLiveData();
    }

    public void queryQuoteBackground(){
        repository.queryQuoteBackground();
    }
}
