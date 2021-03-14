package com.presidev.maos.developer.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.presidev.maos.R;
import com.presidev.maos.membership.view.MemberCardViewModel;
import com.presidev.maos.profile.mitra.model.Mitra;
import com.presidev.maos.profile.mitra.view.MitraViewModel;
import com.presidev.maos.profile.user.model.User;
import com.presidev.maos.profile.user.view.UserViewModel;
import com.presidev.maos.membership.model.MemberCard;

import java.util.Objects;

import static com.presidev.maos.utils.AppUtils.getMemberCardId;
import static com.presidev.maos.utils.AppUtils.getFixText;
import static com.presidev.maos.utils.AppUtils.showToast;
import static com.presidev.maos.utils.DateUtils.addDay;
import static com.presidev.maos.utils.DateUtils.differenceOfDates;
import static com.presidev.maos.utils.DateUtils.getCurrentDate;

public class AddMemberCardActivity extends AppCompatActivity implements View.OnClickListener {
    private MemberCard memberCard;
    private MemberCardViewModel memberCardViewModel;
    private Mitra mitra;
    private MitraViewModel mitraViewModel;
    private User user;
    private UserViewModel userViewModel;

    private EditText edtMitraEmail, edtUserEmail;
    private TextView tvMitraValidation, tvUserValidation;

    private boolean isMitraValid = false;
    private boolean isUserValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member_card);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Button btnMitraValidation = findViewById(R.id.btn_mitra_validation_amc);
        Button btnUserValidation = findViewById(R.id.btn_user_validation_amc);
        Button btnAdd = findViewById(R.id.btn_add_amc);
        btnMitraValidation.setOnClickListener(this);
        btnUserValidation.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        edtMitraEmail = findViewById(R.id.edt_mitra_email_amc);
        edtUserEmail = findViewById(R.id.edt_user_email_amc);
        tvMitraValidation = findViewById(R.id.tv_mitra_validation_amc);
        tvUserValidation = findViewById(R.id.tv_user_validation_amc);

        edtMitraEmail.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isMitraValid){
                    isMitraValid = false;
                    setMitraValidView(false);
                }
            }
        });

        edtUserEmail.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isUserValid){
                    isUserValid = false;
                    setUserValidView(false);
                }
            }
        });

        memberCardViewModel = new ViewModelProvider(this).get(MemberCardViewModel.class);

        mitraViewModel = new ViewModelProvider(this).get(MitraViewModel.class);
        mitraViewModel.getMitraLiveData().observe(this, mitra -> {
            if (mitra != null){
                this.mitra = mitra;
                isMitraValid = true;
            } else isMitraValid = false;
            setMitraValidView(isMitraValid);
        });

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUserLiveData().observe(this, user -> {
            if (user != null){
                this.user = user;
                isUserValid = true;
            } else isUserValid = false;
            setUserValidView(isUserValid);
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_mitra_validation_amc) {
            String mitraEmail = getFixText(edtMitraEmail);
            if (mitraEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mitraEmail).matches()) {
                edtMitraEmail.setError("Masukkan email mitra yang valid");
                return;
            }
            mitraViewModel.queryByEmail(mitraEmail);
        } else if (id == R.id.btn_user_validation_amc) {
            String userMail = getFixText(edtUserEmail);
            if (userMail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(userMail).matches()) {
                edtUserEmail.setError("Masukkan email pengguna yang valid");
                return;
            }
            userViewModel.queryByEmail(userMail);
        } else if (id == R.id.btn_add_amc) {
            if (!(isMitraValid && isUserValid)) {
                showToast(this, "Validasi email mitra dan pengguna terlebih dahulu");
                return;
            }

            memberCard = new MemberCard(
                    getMemberCardId(mitra.getId(), user.getId()),
                    user.getId(),
                    mitra.getId(),
                    getCurrentDate(),
                    addDay(getCurrentDate(), 30)
            );

            memberCardViewModel.queryByCardId(getMemberCardId(mitra.getId(), user.getId()));
            memberCardViewModel.getMemberCardLiveData().observe(this, oldMemberCard -> {
                if (oldMemberCard != null) {
                    Log.d(getClass().getSimpleName(), "Sisa hari: " + differenceOfDates(oldMemberCard.getExpDate(), getCurrentDate()));
                    if (differenceOfDates(oldMemberCard.getExpDate(), getCurrentDate()) >= 0) {
                        showToast(this, "Kartu member sudah ada dan masih berlaku");
                    } else {
                        memberCardViewModel.update(memberCard);
                        showToast(this, "Kartu member dengan id " + memberCard.getId() + " berhasil diperbarui");
                        onBackPressed();
                    }
                } else {
                    memberCardViewModel.insert(memberCard);
                    showToast(this, "Kartu member dengan id " + memberCard.getId() + " berhasil dibuat");
                    onBackPressed();
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void setMitraValidView(boolean isMitraValid){
        if (isMitraValid) tvMitraValidation.setText("VALID: Akun mitra ditemukan");
        else tvMitraValidation.setText("TIDAK VALID: Akun mitra tidak ditemukan");
    }

    @SuppressLint("SetTextI18n")
    private void setUserValidView(boolean isUserValid){
        if (isUserValid) tvUserValidation.setText("VALID: Akun pengguna ditemukan");
        else tvUserValidation.setText("TIDAK VALID: Akun pengguna tidak ditemukan");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}