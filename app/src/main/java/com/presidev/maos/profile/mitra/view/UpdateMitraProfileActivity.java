package com.presidev.maos.profile.mitra.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.material.chip.Chip;
import com.presidev.maos.R;
import com.presidev.maos.customview.LoadingDialog;
import com.presidev.maos.location.model.Location;
import com.presidev.maos.location.view.LocationViewModel;
import com.presidev.maos.profile.mitra.model.Mitra;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.presidev.maos.utils.AppUtils.getFixText;
import static com.presidev.maos.utils.AppUtils.isValidPhone;
import static com.presidev.maos.utils.AppUtils.loadImageFromUrl;
import static com.presidev.maos.utils.AppUtils.loadProfilePicFromUrl;
import static com.presidev.maos.utils.AppUtils.scrollableListener;
import static com.presidev.maos.utils.AppUtils.showToast;
import static com.presidev.maos.utils.Constants.EXTRA_MITRA;
import static com.presidev.maos.utils.Constants.FOLDER_BANNER;
import static com.presidev.maos.utils.Constants.FOLDER_PROFILE;

public class UpdateMitraProfileActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final int RC_PROFILE_IMAGE = 100;
    private static final int RC_BANNER_IMAGE = 300;

    private LoadingDialog loadingDialog;
    private LocationViewModel locationViewModel;
    private Mitra mitra;
    private MitraViewModel mitraViewModel;

    private Button btnSave;
    private Chip chipCOD, chipKirimLuarKota;
    private ImageView imgLogo, imgBanner;
    private EditText edtName, edtDescription, edtWhatsApp, edtAddress, edtRules;
    private Spinner spProvinces, spRegencies, spDistricts;

    private ArrayList<Location> provinceList, regencyList;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_mitra_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        loadingDialog = new LoadingDialog(this, false);

        Button btnLogo = findViewById(R.id.btn_logo_ump);
        Button btnBanner = findViewById(R.id.btn_banner_ump);
        btnSave = findViewById(R.id.btn_save_ump);
        btnLogo.setOnClickListener(this);
        btnBanner.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnSave.setEnabled(false);

        chipCOD = findViewById(R.id.chip_cod_ump);
        chipKirimLuarKota = findViewById(R.id.chip_kirim_luar_kota_ump);
        imgLogo = findViewById(R.id.img_logo_ump);
        imgBanner = findViewById(R.id.img_banner_ump);
        edtName = findViewById(R.id.edt_name_ump);
        edtDescription = findViewById(R.id.edt_description_ump);
        edtWhatsApp = findViewById(R.id.edt_whatsapp_ump);
        edtAddress = findViewById(R.id.edt_address_ump);
        edtRules = findViewById(R.id.edt_rules_ump);

        spProvinces = findViewById(R.id.sp_provinces_ump);
        spRegencies = findViewById(R.id.sp_regencies_ump);
        spDistricts = findViewById(R.id.sp_districts_ump);
        spProvinces.setOnItemSelectedListener(this);
        spRegencies.setOnItemSelectedListener(this);
        spDistricts.setOnItemSelectedListener(this);

        edtDescription.setOnTouchListener(scrollableListener);
        edtRules.setOnTouchListener(scrollableListener);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_MITRA)){
            mitra = intent.getParcelableExtra(EXTRA_MITRA);
            chipCOD.setChecked(mitra.isCOD());
            chipKirimLuarKota.setChecked(mitra.isKirimLuarKota());
            loadProfilePicFromUrl(imgLogo, mitra.getLogo());
            loadImageFromUrl(imgBanner, mitra.getBanner());
            edtName.setText(mitra.getName());
            edtDescription.setText(mitra.getDescription());
            edtWhatsApp.setText(mitra.getWhatsApp());
            edtAddress.setText(mitra.getAddress());
            edtRules.setText(mitra.getRules());
            chipCOD.setChecked(mitra.isCOD());
            chipKirimLuarKota.setChecked(mitra.isKirimLuarKota());
        }

        mitraViewModel = new ViewModelProvider(this).get(MitraViewModel.class);

        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        setLocationViewModelGetData();
        locationViewModel.queryProvinces();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_logo_ump:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Pilih foto profil"), RC_PROFILE_IMAGE);
                break;

            case R.id.btn_banner_ump:
                Intent intentBanner = new Intent(Intent.ACTION_PICK);
                intentBanner.setType("image/*");
                startActivityForResult(Intent.createChooser(intentBanner, "Pilih latar belakang"), RC_BANNER_IMAGE);
                break;

            case R.id.btn_save_ump:
                String name = getFixText(edtName);
                String description = edtDescription.getText().toString().trim();
                String whatsApp = getFixText(edtWhatsApp);
                String address = getFixText(edtAddress);
                String rules = edtRules.getText().toString().trim();

                if (name.isEmpty() || description.isEmpty() || whatsApp.isEmpty() ||
                        address.isEmpty() || rules.isEmpty()){
                    if (name.isEmpty()) edtName.setError("Masukkan nama lengkapmu");
                    if (description.isEmpty()) edtDescription.setError("Masukkan deskripsi");
                    if (whatsApp.isEmpty()) edtWhatsApp.setError("Masukkan nomor WhatsApp/nama pengguna Instagram");
                    if (address.isEmpty()) edtAddress.setError("Masukkan alamat");
                    if (rules.isEmpty()) edtRules.setError("Masukkan peraturan peminjaman");
                    showToast(this, "Pastikan data yang diisi lengkap");
                    return;
                }

                boolean isWA = TextUtils.isDigitsOnly(whatsApp);
                if (isWA && isValidPhone(whatsApp)){
                    edtWhatsApp.setError("Awali nomor WhatsApp dengan 62");
                    showToast(this, "Awali nomor WhatsApp dengan 62");
                    return;
                }

                mitra.setName(name);
                mitra.setDescription(description);
                mitra.setWhatsApp(whatsApp);
                mitra.setAddress(address);
                mitra.setRules(rules);

                mitra.setProvince(spProvinces.getSelectedItem().toString());
                mitra.setRegency(spRegencies.getSelectedItem().toString());
                mitra.setDistrict(spDistricts.getSelectedItem().toString());
                mitra.setCOD(chipCOD.isChecked());
                mitra.setKirimLuarKota(chipKirimLuarKota.isChecked());

                mitraViewModel.update(mitra);
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PROFILE_IMAGE){
            if (resultCode == Activity.RESULT_OK){
                if (data != null) if (data.getData() != null){
                    loadingDialog.show();

                    Uri uriImage = data.getData();
                    loadProfilePicFromUrl(imgLogo, uriImage.toString());

                    String fileName = mitra.getId() + ".jpeg";
                    mitraViewModel.uploadImage(this, uriImage, FOLDER_PROFILE, fileName, imageUrl -> {
                        mitra.setLogo(imageUrl);
                        loadingDialog.dismiss();
                    });
                }
            }
        } else if (requestCode == RC_BANNER_IMAGE){
            if (resultCode == Activity.RESULT_OK){
                if (data != null) if (data.getData() != null){
                    loadingDialog.show();

                    Uri uriImage = data.getData();
                    loadImageFromUrl(imgBanner, uriImage.toString());

                    String fileName = mitra.getId() + ".jpeg";
                    mitraViewModel.uploadImage(this, uriImage, FOLDER_BANNER, fileName, imageUrl -> {
                        mitra.setBanner(imageUrl);
                        loadingDialog.dismiss();
                    });
                }
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()){
            case R.id.sp_provinces_ump:
                btnSave.setEnabled(false);
                spRegencies.setAdapter(null);
                spDistricts.setAdapter(null);

                int idProvince = provinceList.get(position).getId();
                locationViewModel.queryRegencies(idProvince);
                break;

            case R.id.sp_regencies_ump:
                btnSave.setEnabled(false);
                spDistricts.setAdapter(null);

                int idRegency = regencyList.get(position).getId();
                locationViewModel.queryDistricts(idRegency);
                break;

            case R.id.sp_districts_ump:
                //int idDistrict = districtList.get(position).getId();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    private void setLocationViewModelGetData() {
        locationViewModel.getProvinces().observe(this, result -> {
            provinceList = result;
            ArrayAdapter<String> adapter = getArrayAdapter(result);
            spProvinces.setAdapter(adapter);
            spProvinces.setSelection(adapter.getPosition(mitra.getProvince()));
        });

        locationViewModel.getRegencies().observe(this, result -> {
            regencyList = result;
            ArrayAdapter<String> adapter = getArrayAdapter(result);
            spRegencies.setAdapter(adapter);
            spRegencies.setSelection(adapter.getPosition(mitra.getRegency()));
        });

        locationViewModel.getDistricts().observe(this, result -> {
            //districtList = result;
            ArrayAdapter<String> adapter = getArrayAdapter(result);
            spDistricts.setAdapter(adapter);
            spDistricts.setSelection(adapter.getPosition(mitra.getDistrict()));
            btnSave.setEnabled(true);
        });
    }

    private ArrayAdapter<String> getArrayAdapter(ArrayList<Location> locationList){
        List<String> itemList = new ArrayList<>();
        for (Location location : locationList) itemList.add(location.getName());
        return new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemList);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}