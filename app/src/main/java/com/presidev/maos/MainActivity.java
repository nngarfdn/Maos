package com.presidev.maos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.presidev.maos.mitra.view.KatalogMitraActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "Commit", Toast.LENGTH_SHORT).show();

        Button btnKatalogMitra = findViewById(R.id.btn_katalog_mitra);
        btnKatalogMitra.setOnClickListener(v -> {
            Intent intent = new Intent(this, KatalogMitraActivity.class);
            startActivity(intent);
        });
    }
}