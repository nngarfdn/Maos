package com.presidev.maos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.presidev.maos.login.view.LoginActivity;
import com.presidev.maos.mitra.view.KatalogMitraActivity;

import static com.presidev.maos.utils.AppUtils.showToast;

public class MainActivity extends AppCompatActivity {
    private FirebaseUser firebaseUser;

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

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        Button btnLogin = findViewById(R.id.btn_login_main);
        btnLogin.setOnClickListener(v -> {
            if (firebaseUser == null){
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            } else showToast(this, "Kamu sudah masuk");
        });

        Button btnLogout = findViewById(R.id.btn_logout_main);
        btnLogout.setOnClickListener(view -> {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(view.getContext(), gso);

            // Google and Firebase sign out
            googleSignInClient.signOut(); // Ini juga, agar nanti saat login bisa pilih akun Google yang lain
            firebaseAuth.signOut();

            // Muat ulang
            firebaseUser = firebaseAuth.getCurrentUser();
            showToast(this, "Berhasil keluar akun");
        });
    }
}