package com.presidev.maos.profile.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.presidev.maos.R;

import static com.presidev.maos.utils.AppUtils.loadProfilePicFromUrl;
import static com.presidev.maos.utils.Constants.EXTRA_USER;

public class UpdateUserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private User user;
    private UserViewModel userViewModel;

    private Button btnPhoto, btnIdCard, btnSave;
    private ImageView imgPhoto, imgIdCard;
    private EditText edtName, edtDescription, edtAddress;
    private Spinner spProvince, spRegency, spDistrict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_profile);

        btnPhoto = findViewById(R.id.btn_photo_uup);
        btnIdCard = findViewById(R.id.btn_id_card_uup);
        btnSave = findViewById(R.id.btn_save_uup);
        btnPhoto.setOnClickListener(this);
        btnIdCard.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        imgPhoto = findViewById(R.id.img_photo_uup);
        imgIdCard = findViewById(R.id.img_id_card_uup);
        edtName = findViewById(R.id.edt_name_uup);
        edtAddress = findViewById(R.id.edt_address_uup);
        spProvince = findViewById(R.id.sp_provinces_uup);
        spRegency = findViewById(R.id.sp_regencies_uup);
        spDistrict = findViewById(R.id.sp_districts_uup);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_USER)){
            user = intent.getParcelableExtra(EXTRA_USER);
            loadProfilePicFromUrl(imgPhoto, user.getPhoto());
            loadProfilePicFromUrl(imgIdCard, user.getIdCard());
            edtName.setText(user.getName());
            edtAddress.setText(user.getAddress());
        }

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_photo_uup:
                break;

            case R.id.btn_id_card_uup:
                break;

            case R.id.btn_save_uup:
                break;
        }
    }
}