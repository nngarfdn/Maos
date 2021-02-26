package com.presidev.maos.location.view;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.presidev.maos.location.model.Location;
import com.presidev.maos.location.response.Attributes;
import com.presidev.maos.location.response.Districts;
import com.presidev.maos.location.response.Provinces;
import com.presidev.maos.location.response.Regencies;
import com.presidev.maos.location.rest.ApiClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationViewModel extends ViewModel {
    private final String TAG = getClass().getSimpleName();

    private final ApiClient client = new ApiClient();

    private final MutableLiveData<ArrayList<Location>> provincesLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Location>> regenciesLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Location>> districtsLiveData = new MutableLiveData<>();

    public LiveData<ArrayList<Location>> getProvinces(){
        return provincesLiveData;
    }
    public LiveData<ArrayList<Location>> getRegencies(){
        return regenciesLiveData;
    }
    public LiveData<ArrayList<Location>> getDistricts(){
        return districtsLiveData;
    }

    public void queryProvinces(){
        client.getService().getProvinces().enqueue(new Callback<Provinces>() {
            @Override
            public void onResponse(@NonNull Call<Provinces> call, @NonNull Response<Provinces> response) {
                try {
                    if (response.isSuccessful()){
                        ArrayList<Location> provinceList = new ArrayList<>();
                        Provinces result = response.body();
                        for (Attributes attributes : result.getProvinces()){
                            if (attributes.getId() == 31) provinceList.add(new Location(attributes.getId(), "DKI Jakarta"));
                            else if (attributes.getId() == 34) provinceList.add(new Location(attributes.getId(), "DI Yogyakarta"));
                            else provinceList.add(new Location(attributes.getId(), attributes.getName()));
                        }
                        provincesLiveData.postValue(provinceList);
                    }
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

    public void queryRegencies(int idProvince){
        client.getService().getRegencies(idProvince).enqueue(new Callback<Regencies>() {
            @Override
            public void onResponse(@NonNull Call<Regencies> call, @NonNull Response<Regencies> response) {
                try {
                    if (response.isSuccessful()){
                        ArrayList<Location> regencyList = new ArrayList<>();
                        Regencies result = response.body();
                        for (Attributes attributes : result.getRegencies()){
                            regencyList.add(new Location(attributes.getId(), attributes.getName()));
                        }
                        regenciesLiveData.postValue(regencyList);
                    }
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

    public void queryDistricts(int idRegency){
        client.getService().getDistricts(idRegency).enqueue(new Callback<Districts>() {
            @Override
            public void onResponse(@NonNull Call<Districts> call, @NonNull Response<Districts> response) {
                try {
                    if (response.isSuccessful()){
                        ArrayList<Location> districtList = new ArrayList<>();
                        Districts result = response.body();
                        for (Attributes attributes : result.getDistricts()){
                            districtList.add(new Location(attributes.getId(), attributes.getName()));
                        }
                        districtsLiveData.postValue(districtList);
                    }
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
