package com.presidev.maos.subscribe.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.presidev.maos.R;
import com.presidev.maos.mitrabookcatalog.view.MitraBookCatalogActivity;
import com.presidev.maos.profile.mitra.Mitra;
import com.presidev.maos.profile.user.UserViewModel;

import static com.presidev.maos.utils.AppUtils.loadProfilePicFromUrl;
import static com.presidev.maos.utils.AppUtils.setFullAddress;

public class MembershipRegistrationActivity extends AppCompatActivity implements View.OnClickListener, SelectMitraFragment.SelectMitraListener {
    private Mitra mitra;

    private EditText edtName, edtEmail, edtAddress;
    private ImageView imgMitraLogo;
    private TextView tvMitraName, tvMitraAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_registration);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Button btnSubmit = findViewById(R.id.btn_submit_mr);
        CardView cvMitra = findViewById(R.id.cv_mitra_mr);
        btnSubmit.setOnClickListener(this);
        cvMitra.setOnClickListener(this);

        edtName = findViewById(R.id.edt_name_mr);
        edtEmail = findViewById(R.id.edt_email_mr);
        edtAddress = findViewById(R.id.edt_address_mr);
        imgMitraLogo = findViewById(R.id.img_mitra_logo_mr);
        tvMitraName = findViewById(R.id.tv_mitra_name_mr);
        tvMitraAddress = findViewById(R.id.tv_mitra_address_mr);

        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUserLiveData().observe(this, user -> {
            edtName.setText(user.getName());
            edtEmail.setText(user.getEmail());
            edtAddress.setText(setFullAddress(user.getAddress(), user.getProvince(), user.getRegency(), user.getDistrict()));
        });
        userViewModel.query(firebaseUser.getUid());
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cv_mitra_mr:
                if (mitra == null){
                     launchSelectMitra();
                } else {
                    PopupMenu menu = new PopupMenu(this, view);
                    MenuInflater inflater = menu.getMenuInflater();
                    inflater.inflate(R.menu.menu_select_mitra, menu.getMenu());
                    menu.show();

                    menu.setOnMenuItemClickListener(item -> {
                        switch (item.getItemId()){
                            case R.id.menu_open_mitra:
                                Intent intent;
                                intent = new Intent(this, MitraBookCatalogActivity.class);
                                startActivity(intent);
                                break;

                            case R.id.menu_select_mitra:
                                launchSelectMitra();
                                break;
                        }
                        return false;
                    });
                }
                break;

            case R.id.btn_submit_mr:
                break;
        }
    }

    private void launchSelectMitra() {
        SelectMitraFragment bottomSheet = new SelectMitraFragment();
        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
    }

    @Override
    public void receiveData(Mitra mitra) {
        this.mitra = mitra;
        loadProfilePicFromUrl(imgMitraLogo, mitra.getLogo());
        tvMitraName.setText(mitra.getName());
        tvMitraAddress.setText(mitra.getAddress());
    }
}