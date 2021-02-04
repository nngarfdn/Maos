package com.presidev.maos.login.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.presidev.maos.MainActivity;
import com.presidev.maos.R;
import com.presidev.maos.customview.LoadingDialog;

import static com.presidev.maos.utils.AppUtils.showToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = getClass().getSimpleName();
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private LoadingDialog loadingDialog;

    private EditText edtEmail, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize
        loadingDialog = new LoadingDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize view
        edtEmail = findViewById(R.id.edt_email_login);
        edtPassword = findViewById(R.id.edt_password_login);

        Button btnLogin = findViewById(R.id.btn_email_login);
        Button btnGoogle = findViewById(R.id.btn_google_login);
        Button btnResetPassword = findViewById(R.id.btn_reset_password_login);
        Button btnRegister = findViewById(R.id.btn_register_login);

        btnLogin.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        btnResetPassword.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_email_login:
                loginWithEmail(edtEmail.getText().toString(), edtPassword.getText().toString());
                break;

            case R.id.btn_google_login:
                loginWithGoogle();
                break;

            case R.id.btn_reset_password_login:
                Intent intent = new Intent(this,ResetPasswordActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_register_login:
                Intent intentRegister = new Intent(this,RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e){
                // Google Sign In failed or user press back button
                Log.w(TAG, "Google sign in failed", e);
                //showSnackbar(getActivity().findViewById(R.id.activity_login), "Email gagal diautentikasi.");
            }
        }
    }

    private void loginWithGoogle(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account){
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        loadingDialog.show();

        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()){
                        // Sign in success, update UI with the signed-in user's informationZ
                        Log.d(TAG, "signInWithCredential: success");

                        // Jika pengguna baru/email baru saja terdaftar
                        if (task.getResult().getAdditionalUserInfo().isNewUser()){
                            Log.d(TAG, "register with Google account: success");
                        }

                        launchMain();
                    } else {
                        // If sign in fails, display a message to the user
                        Log.w(TAG, "signInWithCredential: failure", task.getException());
                        showToast(getApplicationContext(), "Email gagal diautentikasi.");
                    }

                    loadingDialog.dismiss();
                });
    }

    private void loginWithEmail(String email, String password){
        if (!validateForm(email, password)) return;
        Log.d(TAG, "signIn: " + email);

        loadingDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()){
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail: success");
                        launchMain();
                    } else {
                        // If sign in fails, display a message to the user
                        Log.w(TAG, "signInWithEmail: failure", task.getException());
                        showToast(getApplicationContext(),  "Kata sandi salah.");
                    }

                    loadingDialog.dismiss();
                });
    }

    private boolean validateForm(String email, String password){
        boolean valid = true;

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError("Masukkan email yang valid");
            valid = false;
        }

        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Masukkan kata sandi");
            valid = false;
        }

        return valid;
    }

    private void launchMain(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);  // Clear all previous activities
        startActivity(intent);
    }
}