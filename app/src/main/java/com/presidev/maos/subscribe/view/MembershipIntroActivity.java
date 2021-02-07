package com.presidev.maos.subscribe.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.presidev.maos.R;

public class MembershipIntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_intro);

        Button btnRegister = findViewById(R.id.btn_register_mi);
        btnRegister.setOnClickListener(view -> {
            Intent intent = new Intent(this, MembershipRegistrationActivity.class);
            startActivity(intent);
        });
    }
}