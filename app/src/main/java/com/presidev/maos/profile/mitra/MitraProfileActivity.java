package com.presidev.maos.profile.mitra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.presidev.maos.R;

import static com.presidev.maos.utils.AppUtils.loadProfilePicFromUrl;
import static com.presidev.maos.utils.Constants.EXTRA_MITRA;

public class MitraProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Mitra mitra;
    private MitraViewModel mitraViewModel;

    private Button btnUpdate;
    private ImageView imgLogo;
    private TextView tvName, tvEmail, tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mitra_profile);

        btnUpdate = findViewById(R.id.btn_update_mp);
        btnUpdate.setOnClickListener(this);

        imgLogo = findViewById(R.id.img_logo_mp);
        tvName = findViewById(R.id.tv_name_mp);
        tvEmail = findViewById(R.id.tv_email_mp);
        tvAddress = findViewById(R.id.tv_address_mp);

        mitraViewModel = new ViewModelProvider(this).get(MitraViewModel.class);
        mitraViewModel.getMitraLiveData().observe(this, mitra -> {
            this.mitra = mitra;
            loadProfilePicFromUrl(imgLogo, mitra.getLogo());
            tvName.setText(mitra.getName());
            tvEmail.setText(mitra.getEmail());
            tvAddress.setText(mitra.getAddress());
        });
        mitraViewModel.query("rSAszlHMDjgarYuvZgbQmzZnrM52");
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_update_mp:
                if (mitra == null) return;
                Intent intent = new Intent(this, UpdateMitraProfileActivity.class);
                intent.putExtra(EXTRA_MITRA, mitra);
                startActivity(intent);
                break;
        }
    }
}