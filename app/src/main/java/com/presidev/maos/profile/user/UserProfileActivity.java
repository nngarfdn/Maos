package com.presidev.maos.profile.user;

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
import static com.presidev.maos.utils.Constants.EXTRA_USER;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private User user;
    private UserViewModel userViewModel;

    private Button btnUpdate;
    private ImageView imgPhoto;
    private TextView tvName, tvEmail, tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        btnUpdate = findViewById(R.id.btn_update_up);
        btnUpdate.setOnClickListener(this);

        imgPhoto = findViewById(R.id.img_photo_up);
        tvName = findViewById(R.id.tv_name_up);
        tvEmail = findViewById(R.id.tv_email_up);
        tvAddress = findViewById(R.id.tv_address_up);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUserLiveData().observe(this, user -> {
            this.user = user;
            loadProfilePicFromUrl(imgPhoto, user.getPhoto());
            tvName.setText(user.getName());
            tvEmail.setText(user.getEmail());
            tvAddress.setText(user.getAddress());
        });
        userViewModel.query("C6EOiwB3MqfMYRDECIvgQMYK6SF2");

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_update_up:
                if (user == null) return;
                Intent intent = new Intent(this, UpdateUserProfileActivity.class);
                intent.putExtra(EXTRA_USER, user);
                startActivity(intent);
                break;
        }
    }
}