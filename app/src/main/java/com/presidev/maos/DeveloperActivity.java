package com.presidev.maos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.presidev.maos.login.view.RegisterActivity;
import com.presidev.maos.subscribe.view.AddMemberCardActivity;
import com.presidev.maos.utils.Constants;

import static com.presidev.maos.utils.AppUtils.showToast;
import static com.presidev.maos.utils.Constants.LEVEL_MITRA;

public class DeveloperActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);

        Button btnRegisterMitra = findViewById(R.id.btn_register_mitra_dev);
        Button btnAddMemberCard = findViewById(R.id.btn_add_member_card_dev);
        btnRegisterMitra.setOnClickListener(this);
        btnAddMemberCard.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register_mitra_dev:
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser == null){
                    Intent intent = new Intent(this, RegisterActivity.class);
                    intent.putExtra(Constants.EXTRA_LEVEL, LEVEL_MITRA);
                    startActivity(intent);
                } else showToast(this, "Kamu sudah masuk");
                break;

            case R.id.btn_add_member_card_dev:
                Intent intent = new Intent(this, AddMemberCardActivity.class);
                startActivity(intent);
                break;
        }
    }
}