package com.presidev.maos.profile.mitra;

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
import static com.presidev.maos.utils.Constants.EXTRA_MITRA;

public class UpdateMitraProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Mitra mitra;
    private MitraViewModel mitraViewModel;

    private Button btnLogo, btnSave;
    private CheckBox cbCOD, cbKirimLuarKota;
    private ImageView imgLogo;
    private EditText edtName, edtDescription, edtWhatsApp, edtAddress, edtRules;
    private Spinner spProvince, spRegency, spDistrict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_mitra_profile);

        btnLogo = findViewById(R.id.btn_logo_ump);
        btnSave = findViewById(R.id.btn_save_ump);
        btnLogo.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        cbCOD = findViewById(R.id.cb_cod_ump);
        cbKirimLuarKota = findViewById(R.id.cb_kirim_luar_kota_ump);
        imgLogo = findViewById(R.id.img_logo_ump);
        edtName = findViewById(R.id.edt_name_ump);
        edtDescription = findViewById(R.id.edt_description_ump);
        edtWhatsApp = findViewById(R.id.edt_whatsapp_ump);
        edtAddress = findViewById(R.id.edt_address_ump);
        edtRules = findViewById(R.id.edt_rules_ump);
        spProvince = findViewById(R.id.sp_provinces_ump);
        spRegency = findViewById(R.id.sp_regencies_ump);
        spDistrict = findViewById(R.id.sp_districts_ump);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_MITRA)){
            mitra = intent.getParcelableExtra(EXTRA_MITRA);
            cbCOD.setChecked(mitra.isCOD());
            cbKirimLuarKota.setChecked(mitra.isKirimLuarKota());
            loadProfilePicFromUrl(imgLogo, mitra.getLogo());
            edtName.setText(mitra.getName());
            edtDescription.setText(mitra.getDescription());
            edtWhatsApp.setText(mitra.getWhatsApp());
            edtAddress.setText(mitra.getAddress());
            edtRules.setText(mitra.getRules());
        }

        mitraViewModel = new ViewModelProvider(this).get(MitraViewModel.class);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_logo_ump:
                break;

            case R.id.btn_save_ump:
                break;
        }
    }
}