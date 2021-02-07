package com.presidev.maos.search.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.presidev.maos.R;
import com.presidev.maos.location.model.Location;
import com.presidev.maos.location.viewmodel.LocationViewModel;
import com.presidev.maos.search.model.MitraFilter;

import java.util.ArrayList;
import java.util.List;

import static com.presidev.maos.utils.Constants.EXTRA_MITRA_FILTER;

public class MitraFilterFragment extends BottomSheetDialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, CheckBox.OnCheckedChangeListener {
    private LocationViewModel locationViewModel;
    private MitraFilter filter;
    private MitraFilterListener listener;

    private Button btnApply;
    private CheckBox cbProvinces, cbRegencies, cbDistricts, cbOnlyCOD, cbOnlyKirimLuarKota;
    private Spinner spProvinces, spRegencies, spDistricts;

    private ArrayList<Location> provinceList, regencyList;

    public MitraFilterFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mitra_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cbProvinces = view.findViewById(R.id.cb_provinces_mf);
        cbRegencies = view.findViewById(R.id.cb_regencies_mf);
        cbDistricts = view.findViewById(R.id.cb_districts_mf);
        spProvinces = view.findViewById(R.id.sp_provinces_mf);
        spRegencies = view.findViewById(R.id.sp_regencies_mf);
        spDistricts = view.findViewById(R.id.sp_districts_mf);
        cbOnlyCOD = view.findViewById(R.id.cb_only_cod_mf);
        cbOnlyKirimLuarKota = view.findViewById(R.id.cb_only_kirim_luar_kota_mf);

        cbProvinces.setEnabled(true);
        cbRegencies.setEnabled(false);
        cbDistricts.setEnabled(false);
        spProvinces.setEnabled(false);
        spRegencies.setEnabled(false);
        spDistricts.setEnabled(false);

        cbProvinces.setOnCheckedChangeListener(this);
        cbRegencies.setOnCheckedChangeListener(this);
        cbDistricts.setOnCheckedChangeListener(this);
        spProvinces.setOnItemSelectedListener(this);
        spRegencies.setOnItemSelectedListener(this);
        spDistricts.setOnItemSelectedListener(this);

        btnApply = view.findViewById(R.id.btn_apply_mf);
        btnApply.setOnClickListener(this);

        btnApply.setEnabled(false);

        Bundle bundle = getArguments();
        if (bundle != null && !bundle.isEmpty()){
            filter = bundle.getParcelable(EXTRA_MITRA_FILTER);
            cbProvinces.setChecked(filter.isByProvince());
            cbRegencies.setChecked(filter.isByRegency());
            cbDistricts.setChecked(filter.isByDistrict());
            cbOnlyCOD.setChecked(filter.isOnlyCOD());
            cbOnlyKirimLuarKota.setChecked(filter.isOnlyKirimLuarKota());

            locationViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(LocationViewModel.class);
            locationViewModel.queryProvinces();
        }

        setLocationViewModelGetData();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_apply_mf:
                filter.setProvince(spProvinces.getSelectedItem().toString());
                filter.setRegency(spRegencies.getSelectedItem().toString());
                filter.setDistrict(spDistricts.getSelectedItem().toString());
                filter.setByProvince(cbProvinces.isChecked());
                filter.setByRegency(cbRegencies.isChecked());
                filter.setByDistrict(cbDistricts.isChecked());
                filter.setOnlyCOD(cbOnlyCOD.isChecked());
                filter.setOnlyKirimLuarKota(cbOnlyKirimLuarKota.isChecked());

                listener.receiveData(filter);
                dismiss();
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()){
            case R.id.sp_provinces_mf:
                btnApply.setEnabled(false);
                spRegencies.setAdapter(null);
                spDistricts.setAdapter(null);

                int idProvince = provinceList.get(position).getId();
                locationViewModel.queryRegencies(idProvince);
                break;

            case R.id.sp_regencies_mf:
                spDistricts.setAdapter(null);

                int idRegency = regencyList.get(position).getId();
                locationViewModel.queryDistricts(idRegency);
                break;

            case R.id.sp_districts_mf:
                //int idDistrict = districtList.get(position).getId();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()){
            case R.id.cb_provinces_mf:
                spProvinces.setEnabled(isChecked);
                cbRegencies.setEnabled(isChecked);
                if (!isChecked){
                    cbRegencies.setChecked(false);
                    cbDistricts.setChecked(false);
                }
                break;

            case R.id.cb_regencies_mf:
                spRegencies.setEnabled(isChecked);
                cbDistricts.setEnabled(isChecked);
                if (!isChecked) cbDistricts.setChecked(false);
                break;

            case R.id.cb_districts_mf:
                spDistricts.setEnabled(isChecked);
                break;
        }
    }

    private void setLocationViewModelGetData() {
        locationViewModel.getProvinces().observe(getViewLifecycleOwner(), result -> {
            provinceList = result;
            ArrayAdapter<String> adapter = getArrayAdapter(result);
            spProvinces.setAdapter(adapter);
            spProvinces.setSelection(adapter.getPosition(filter.getProvince()));
        });

        locationViewModel.getRegencies().observe(getViewLifecycleOwner(), result -> {
            regencyList = result;
            ArrayAdapter<String> adapter = getArrayAdapter(result);
            spRegencies.setAdapter(adapter);
            spRegencies.setSelection(adapter.getPosition(filter.getRegency()));
        });

        locationViewModel.getDistricts().observe(getViewLifecycleOwner(), result -> {
            //districtList = result;
            ArrayAdapter<String> adapter = getArrayAdapter(result);
            spDistricts.setAdapter(adapter);
            spDistricts.setSelection(adapter.getPosition(filter.getDistrict()));
            btnApply.setEnabled(true);
        });
    }

    private ArrayAdapter<String> getArrayAdapter(ArrayList<Location> locationList){
        List<String> itemList = new ArrayList<>();
        for (Location location : locationList) itemList.add(location.getName());
        return new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, itemList);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (MitraFilterFragment.MitraFilterListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement " + MitraFilterFragment.MitraFilterListener.class.getSimpleName());
        }
    }

    public interface MitraFilterListener{
        void receiveData(MitraFilter filter);
    }
}