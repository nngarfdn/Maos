package com.presidev.maos.membership.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
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
import com.presidev.maos.profile.mitra.model.Mitra;
import com.presidev.maos.profile.user.view.UserViewModel;

import java.util.Objects;

import static com.presidev.maos.utils.AppUtils.getFixText;
import static com.presidev.maos.utils.AppUtils.loadBlurImageFromUrl;
import static com.presidev.maos.utils.AppUtils.loadProfilePicFromUrl;
import static com.presidev.maos.utils.AppUtils.setFullAddress;
import static com.presidev.maos.utils.AppUtils.showToast;
import static com.presidev.maos.utils.Constants.EXTRA_MITRA;

public class MembershipRegistrationActivity extends AppCompatActivity implements View.OnClickListener, SelectMitraFragment.SelectMitraListener {
    private static final String ADMIN_PHONE_NUMBER = "628990969269";

    private Mitra mitra;

    private EditText edtName, edtEmail;
    private ImageView imgMitraBanner, imgMitraLogo;
    private TextView tvMitraName, tvMitraAddress;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_registration);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Button btnSubmit = findViewById(R.id.btn_submit_mr);
        CardView cvMitra = findViewById(R.id.cv_mitra_mr);
        CardView cvPackage = findViewById(R.id.cv_package_mr);
        btnSubmit.setOnClickListener(this);
        cvMitra.setOnClickListener(this);
        cvPackage.setOnClickListener(this);

        edtName = findViewById(R.id.edt_name_mr);
        edtEmail = findViewById(R.id.edt_email_mr);
        imgMitraBanner = findViewById(R.id.img_mitra_banner_mr);
        imgMitraLogo = findViewById(R.id.img_mitra_logo_mr);
        tvMitraName = findViewById(R.id.tv_mitra_name_mr);
        tvMitraAddress = findViewById(R.id.tv_mitra_address_mr);

        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUserLiveData().observe(this, user -> {
            edtName.setText(user.getName());
            edtEmail.setText(user.getEmail());
        });
        assert firebaseUser != null;
        userViewModel.query(firebaseUser.getUid());
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cv_package_mr:
                new AlertDialog.Builder(this)
                        .setTitle("Info paket")
                        .setMessage("Paket berlangganan 30 hari Rp10.000 per penyedia buku berlaku setelah pembayaran dikonfirmasi oleh admin melalui WhatsApp.")
                        .setPositiveButton("Oke", null)
                        .create().show();
                break;

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
                                intent.putExtra(EXTRA_MITRA, mitra);
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
                if (mitra == null){
                    showToast(this, "Silakan pilih penyedia buku terlebih dahulu");
                    return;
                }

                String name = getFixText(edtName);
                String email = getFixText(edtEmail);
                String mitraName = mitra.getName();
                String mitraEmail = mitra.getEmail();

                if (name.isEmpty()){
                    edtName.setError("Masukkan nama lengkapmu");
                    showToast(this, "Pastikan kamu sudah mengisi namamu");
                    return;
                }

                new AlertDialog.Builder(this)
                        .setTitle("Ajukan pendaftaran")
                        .setMessage("Ajukan pendaftaran berlangganan member selama 30 hari?")
                        .setNegativeButton("Batal", null)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    Intent whatsappIntent = new Intent("android.intent.action.SEND");
                                    whatsappIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.ContactPicker"));
                                    whatsappIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(ADMIN_PHONE_NUMBER) + "@s.whatsapp.net");
                                    whatsappIntent.putExtra(Intent.EXTRA_TEXT,
                                            "Halo kak, saya ingin *berlangganan 30 hari*\n\n" +
                                                    "Berikut data saya\n" +
                                                    "Nama: " + name + "\n" +
                                                    "Email: " + email + "\n\n" +
                                                    "Dan data penyedia buku\n" +
                                                    "Nama: " + mitraName + "\n" +
                                                    "Email: " + mitraEmail + "\n\n" +
                                                    "Terima kasih kak");
                                    startActivity(whatsappIntent);
                                } catch (Exception e) {
                                    Log.e(getClass().getSimpleName(), "Error on sharing: " + e);
                                    showToast(view.getContext(), "Kamu belum punya aplikasi WhatsApp");
                                }
                            }
                        }).create().show();
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
        loadBlurImageFromUrl(this, imgMitraBanner, mitra.getBanner());
        loadProfilePicFromUrl(imgMitraLogo, mitra.getLogo());
        tvMitraName.setText(mitra.getName());
        tvMitraAddress.setText(setFullAddress(null, mitra.getProvince(), mitra.getRegency(), mitra.getDistrict()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}