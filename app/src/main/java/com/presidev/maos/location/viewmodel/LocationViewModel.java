package com.presidev.maos.location.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.presidev.maos.location.response.Districts;
import com.presidev.maos.location.response.Provinces;
import com.presidev.maos.location.response.Regencies;
import com.presidev.maos.location.rest.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationViewModel extends ViewModel {
    private final String TAG = getClass().getSimpleName();

    private final ApiClient client = new ApiClient();

    private final MutableLiveData<Provinces> provincesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Regencies> regenciesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Districts> districtsLiveData = new MutableLiveData<>();

    public LiveData<Provinces> getProvinces(){
        return provincesLiveData;
    }
    public LiveData<Regencies> getRegencies(){
        return regenciesLiveData;
    }
    public LiveData<Districts> getDistricts(){
        return districtsLiveData;
    }

    public void loadProvinces(){
        final Provinces[] itemFound = {new Provinces()};
        client.getService().getProvinces().enqueue(new Callback<Provinces>() {
            @Override
            public void onResponse(@NonNull Call<Provinces> call, @NonNull Response<Provinces> response) {
                try {
                    if (response.isSuccessful()) itemFound[0] = response.body();
                    provincesLiveData.postValue(itemFound[0]);
                } catch (Exception e){
                    Log.d(TAG, "Exception: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Provinces> call, @NonNull Throwable t) {
                Log.d(TAG, "Failure: " + t.getMessage());
            }
        });
    }

    public void loadRegencies(int idProvince){
        final Regencies[] itemFound = {new Regencies()};
        client.getService().getRegencies(idProvince).enqueue(new Callback<Regencies>() {
            @Override
            public void onResponse(@NonNull Call<Regencies> call, @NonNull Response<Regencies> response) {
                try {
                    if (response.isSuccessful()) itemFound[0] = response.body();
                    regenciesLiveData.postValue(itemFound[0]);
                } catch (Exception e){
                    Log.d(TAG, "Exception: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Regencies> call, @NonNull Throwable t) {
                Log.d(TAG, "Failure: " + t.getMessage());
            }
        });
    }

    public void loadDistricts(int idRegency){
        final Districts[] itemFound = {new Districts()};
        client.getService().getDistricts(idRegency).enqueue(new Callback<Districts>() {
            @Override
            public void onResponse(@NonNull Call<Districts> call, @NonNull Response<Districts> response) {
                try {
                    if (response.isSuccessful()) itemFound[0] = response.body();
                    districtsLiveData.postValue(itemFound[0]);
                } catch (Exception e){
                    Log.d(TAG, "Exception: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Districts> call, @NonNull Throwable t) {
                Log.d(TAG, "Failure: " + t.getMessage());
            }
        });
    }
}
