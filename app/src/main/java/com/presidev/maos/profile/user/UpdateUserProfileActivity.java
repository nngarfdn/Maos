package com.presidev.maos.profile.user;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.presidev.maos.R;
import com.presidev.maos.customview.LoadingDialog;
import com.presidev.maos.location.model.Location;
import com.presidev.maos.location.viewmodel.LocationViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.presidev.maos.utils.AppUtils.getFixText;
import static com.presidev.maos.utils.AppUtils.isValidPhone;
import static com.presidev.maos.utils.AppUtils.loadImageFromUrl;
import static com.presidev.maos.utils.AppUtils.loadProfilePicFromUrl;
import static com.presidev.maos.utils.AppUtils.showToast;
import static com.presidev.maos.utils.Constants.EXTRA_USER;
import static com.presidev.maos.utils.Constants.FOLDER_ID_CARD;
import static com.presidev.maos.utils.Constants.FOLDER_PROFILE;

public class UpdateUserProfileActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final int RC_PROFILE_IMAGE = 100;
    private static final int RC_ID_CARD_IMAGE = 200;

    private LoadingDialog loadingDialog;
    private LocationViewModel locationViewModel;
    private User user;
    private UserViewModel userViewModel;

    private Button btnSave;
    private ImageView imgPhoto, imgIdCard;
    private EditText edtName, edtWhatsApp, edtAddress;
    private Spinner spProvinces, spRegencies, spDistricts;
    private Uri uriIdCard;

    private ArrayList<Location> provinceList, regencyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadingDialog = new LoadingDialog(this, false);

        Button btnPhoto = findViewById(R.id.btn_photo_uup);
        Button btnIdCard = findViewById(R.id.btn_id_card_uup);
        btnSave = findViewById(R.id.btn_save_uup);
        btnPhoto.setOnClickListener(this);
        btnIdCard.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnSave.setEnabled(false);

        imgPhoto = findViewById(R.id.img_photo_uup);
        imgIdCard = findViewById(R.id.img_id_card_uup);
        edtName = findViewById(R.id.edt_name_uup);
        edtWhatsApp = findViewById(R.id.edt_whatsapp_uup);
        edtAddress = findViewById(R.id.edt_address_uup);

        spProvinces = findViewById(R.id.sp_provinces_uup);
        spRegencies = findViewById(R.id.sp_regencies_uup);
        spDistricts = findViewById(R.id.sp_districts_uup);
        spProvinces.setOnItemSelectedListener(this);
        spRegencies.setOnItemSelectedListener(this);
        spDistricts.setOnItemSelectedListener(this);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_USER)){
            user = intent.getParcelableExtra(EXTRA_USER);
            loadProfilePicFromUrl(imgPhoto, user.getPhoto());
            loadImageFromUrl(imgIdCard, user.getIdCard());
            edtName.setText(user.getName());
            edtWhatsApp.setText(user.getWhatsApp());
            edtAddress.setText(user.getAddress());
        }

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        setLocationViewModelGetData();
        locationViewModel.queryProvinces();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_photo_uup:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Pilih foto profil"), RC_PROFILE_IMAGE);
                break;

            case R.id.btn_id_card_uup:
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "Ambil foto identitas");
                values.put(MediaStore.Images.Media.DESCRIPTION, "Menggunakan kamera");
                uriIdCard = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intentIdCard = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentIdCard.putExtra(MediaStore.EXTRA_OUTPUT, uriIdCard);
                startActivityForResult(intentIdCard, RC_ID_CARD_IMAGE);
                break;

            case R.id.btn_save_uup:
                String name = getFixText(edtName);
                String whatsApp = getFixText(edtWhatsApp);
                String address = getFixText(edtAddress);

                if (name.isEmpty() || whatsApp.isEmpty() || address.isEmpty()){
                    if (name.isEmpty()) edtName.setError("Masukkan nama lengkapmu");
                    if (whatsApp.isEmpty()) edtWhatsApp.setError("Masukkan nomor WhatsApp");
                    if (address.isEmpty()) edtAddress.setError("Masukkan alamat");
                    return;
                }

                if (!isValidPhone(whatsApp)){
                    edtWhatsApp.setError("Awali nomor WhatsApp dengan 62");
                    return;
                }

                user.setName(name);
                user.setWhatsApp(whatsApp);
                user.setAddress(address);

                user.setProvince(spProvinces.getSelectedItem().toString());
                user.setRegency(spRegencies.getSelectedItem().toString());
                user.setDistrict(spDistricts.getSelectedItem().toString());

                userViewModel.update(user);
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

                    Uri uriProfileImage = data.getData();
                    loadProfilePicFromUrl(imgPhoto, uriProfileImage.toString());

                    String fileName = user.getId() + ".jpeg";
                    userViewModel.uploadImage(this, uriProfileImage, FOLDER_PROFILE, fileName, imageUrl -> {
                        user.setPhoto(imageUrl);
                        loadingDialog.dismiss();
                    });
                }
            }
        } else if (requestCode == RC_ID_CARD_IMAGE){
            if (resultCode == Activity.RESULT_OK){
                try {
                    loadingDialog.show();

                    loadImageFromUrl(imgIdCard, uriIdCard.toString());

                    String fileName = user.getId() + ".jpeg";
                    userViewModel.uploadImage(this, uriIdCard, FOLDER_ID_CARD, fileName, imageUrl -> {
                        user.setIdCard(imageUrl);
                        loadingDialog.dismiss();
                    });
                } catch (Exception e){
                    e.printStackTrace();
                    showToast(getApplicationContext(), "Gagal mengunggah kartu identitas");
                }
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()){
            case R.id.sp_provinces_uup:
                btnSave.setEnabled(false);
                spRegencies.setAdapter(null);
                spDistricts.setAdapter(null);

                int idProvince = provinceList.get(position).getId();
                locationViewModel.queryRegencies(idProvince);
                break;

            case R.id.sp_regencies_uup:
                btnSave.setEnabled(false);
                spDistricts.setAdapter(null);

                int idRegency = regencyList.get(position).getId();
                locationViewModel.queryDistricts(idRegency);
                break;

            case R.id.sp_districts_uup:
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
            spProvinces.setSelection(adapter.getPosition(user.getProvince()));
        });

        locationViewModel.getRegencies().observe(this, result -> {
            regencyList = result;
            ArrayAdapter<String> adapter = getArrayAdapter(result);
            spRegencies.setAdapter(adapter);
            spRegencies.setSelection(adapter.getPosition(user.getRegency()));
        });

        locationViewModel.getDistricts().observe(this, result -> {
            //districtList = result;
            ArrayAdapter<String> adapter = getArrayAdapter(result);
            spDistricts.setAdapter(adapter);
            spDistricts.setSelection(adapter.getPosition(user.getDistrict()));
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