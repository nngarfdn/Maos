package com.presidev.maos.catatanku.target;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.presidev.maos.R;
import com.presidev.maos.databinding.ActivityAddUpdateTargetBinding;

import static com.presidev.maos.utils.AppUtils.getFixText;
import static com.presidev.maos.utils.AppUtils.showToast;
import static com.presidev.maos.utils.Constants.EXTRA_TARGET;

public class AddUpdateTargetActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityAddUpdateTargetBinding binding;
    private Target target;
    private TargetViewModel targetViewModel;

    private boolean isUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddUpdateTargetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        isUpdate = intent.hasExtra(EXTRA_TARGET);
        if (isUpdate){
            target = intent.getParcelableExtra(EXTRA_TARGET);
            binding.toolbar.setTitle("Edit Target");

            binding.edtPagesRead.setText(String.valueOf(target.getPagesRead()));
            binding.edtBookTitle.setText(target.getBookTitle());
            binding.edtTotalPages.setText(String.valueOf(target.getTotalPages()));
            binding.edtDailyPages.setText(String.valueOf(target.getDailyPages()));
        } else {
            target = new Target();
            binding.toolbar.setTitle("Tambah Target");

            binding.edtPagesRead.setText("0");
        }

        binding.btnAdd.setOnClickListener(this);
        binding.btnRemove.setOnClickListener(this);
        binding.btnSave.setOnClickListener(this);

        targetViewModel = new ViewModelProvider(this).get(TargetViewModel.class);

        setEditTextValidationListener();
    }

    private void setEditTextValidationListener() {
        binding.edtPagesRead.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int totalPages = 0;
                if (!getFixText(binding.edtTotalPages).isEmpty())
                    totalPages = Integer.parseInt(getFixText(binding.edtTotalPages));

                int pagesRead = 0;
                if (!String.valueOf(charSequence).isEmpty())
                    pagesRead = Integer.parseInt(String.valueOf(charSequence));

                if (pagesRead > totalPages){
                    binding.edtPagesRead.setText(String.valueOf(totalPages));
                    showToast(AddUpdateTargetActivity.this,
                            "Progres baca tidak boleh lebih dari jumlah halaman");
                }
            }

            @Override public void afterTextChanged(Editable editable) {
                if (!getFixText(binding.edtPagesRead).isEmpty()){
                    if (Integer.parseInt(getFixText(binding.edtPagesRead)) < 0){
                        binding.edtPagesRead.setText("0");
                    }
                }
            }
        });

        binding.edtDailyPages.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int totalPages = 0;
                if (!getFixText(binding.edtTotalPages).isEmpty())
                    totalPages = Integer.parseInt(getFixText(binding.edtTotalPages));

                int dailyPages = 0;
                if (!String.valueOf(charSequence).isEmpty())
                    dailyPages = Integer.parseInt(String.valueOf(charSequence));

                if (dailyPages > totalPages){
                    binding.edtDailyPages.setText(String.valueOf(totalPages));
                    showToast(AddUpdateTargetActivity.this,
                            "Target harian tidak boleh lebih dari jumlah halaman");
                }
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add:
                int pagesRead1 = 0;
                if (!getFixText(binding.edtPagesRead).isEmpty())
                    pagesRead1 = Integer.parseInt(getFixText(binding.edtPagesRead));
                binding.edtPagesRead.setText(String.valueOf(pagesRead1 + 1));
                break;

            case R.id.btn_remove:
                int pagesRead2 = 0;
                if (!getFixText(binding.edtPagesRead).isEmpty())
                    pagesRead2 = Integer.parseInt(getFixText(binding.edtPagesRead));
                binding.edtPagesRead.setText(String.valueOf(pagesRead2 - 1));
                break;

            case R.id.btn_save:
                String bookTitle = getFixText(binding.edtBookTitle);
                String totalPages = getFixText(binding.edtTotalPages);
                String dailyPages = getFixText(binding.edtDailyPages);

                if (bookTitle.isEmpty() || totalPages.isEmpty() || dailyPages.isEmpty()){
                    if (bookTitle.isEmpty()) binding.edtBookTitle.setError("Masukkan judul buku");
                    if (totalPages.isEmpty()) binding.edtTotalPages.setError("Masukkan jumlah halaman buku");
                    if (dailyPages.isEmpty()) binding.edtDailyPages.setError("Masukkan target halaman per hari");
                    showToast(this, "Pastikan data yang diisi lengkap");
                    return;
                }

                target.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                target.setBookTitle(bookTitle);
                target.setTotalPages(Integer.parseInt(totalPages));
                target.setDailyPages(Integer.parseInt(dailyPages));

                int pagesRead = 0;
                if (!getFixText(binding.edtPagesRead).isEmpty())
                    pagesRead = Integer.parseInt(getFixText(binding.edtPagesRead));

                target.setPagesRead(pagesRead);
                target.setProgress(((double) pagesRead/(double) target.getTotalPages()) * 100);

                if (isUpdate){
                    targetViewModel.update(target);
                    showToast(this, "Target berhasil diedit");
                } else {
                    targetViewModel.insert(target);
                    showToast(this, "Target berhasil ditambah");
                }

                onBackPressed();
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}