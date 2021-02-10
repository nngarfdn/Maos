package com.presidev.maos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.presidev.maos.login.preference.AccountPreference;
import com.presidev.maos.mitramanagement.view.KatalogMitraActivity;

import static com.presidev.maos.utils.AppUtils.showToast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnKatalogMitra = findViewById(R.id.btn_katalog_mitra);
        btnKatalogMitra.setOnClickListener(v -> {
            Intent intent = new Intent(this, KatalogMitraActivity.class);
            startActivity(intent);
        });

        Button btnDeveloper = findViewById(R.id.btn_developer_main);
        btnDeveloper.setOnClickListener(v -> {
            Intent intent = new Intent(this, DeveloperActivity.class);
            startActivity(intent);
        });

        Button btnLevel = findViewById(R.id.btn_level_main);
        btnLevel.setOnClickListener(view -> {
            AccountPreference accountPreference = new AccountPreference(this);
            showToast(this, accountPreference.getEmail() + ": " + accountPreference.getLevel());
        });

        Button btnAppMain = findViewById(R.id.btn_app);
        btnAppMain.setOnClickListener(view -> {
            Intent intent = new Intent(this, App.class);
            startActivity(intent);
        });
    }
}