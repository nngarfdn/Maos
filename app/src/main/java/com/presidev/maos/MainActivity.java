package com.presidev.maos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnDeveloper = findViewById(R.id.btn_developer_main);
        btnDeveloper.setOnClickListener(v -> {
            Intent intent = new Intent(this, DeveloperActivity.class);
            startActivity(intent);
        });

        Button btnAppMain = findViewById(R.id.btn_app);
        btnAppMain.setOnClickListener(view -> {
            Intent intent = new Intent(this, App.class);
            startActivity(intent);
        });
    }
}