package com.presidev.maos.catatanku.target;

import androidx.appcompat.app.AlertDialog;
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
import com.presidev.maos.catatanku.UserPreference;
import com.presidev.maos.catatanku.helper.TargetReminder;
import com.presidev.maos.catatanku.helper.TimePickerFragment;
import com.presidev.maos.databinding.ActivityAddUpdateTargetBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.presidev.maos.catatanku.helper.ReminderHelper.cancelReminder;
import static com.presidev.maos.utils.AppUtils.getFixText;
import static com.presidev.maos.utils.AppUtils.LOCALE;
import static com.presidev.maos.utils.AppUtils.showToast;
import static com.presidev.maos.utils.Constants.EXTRA_TARGET;
import static com.presidev.maos.utils.DateUtils.TIME_FORMAT;
import static com.presidev.maos.utils.DateUtils.differenceOfDates;
import static com.presidev.maos.utils.DateUtils.getCurrentDate;

public class AddUpdateTargetActivity extends AppCompatActivity implements View.OnClickListener, TimePickerFragment.DialogTimeListener {
    private final String TIME_PICKER_REPEAT_TAG = "time_picker_repeat";

    private ActivityAddUpdateTargetBinding binding;
    private Target target;
    private TargetViewModel targetViewModel;
    private UserPreference preference;

    private boolean isUpdate;
    private int todayPagesRead;
    private String lastUpdatePagesRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddUpdateTargetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preference = new UserPreference(this);

        binding.btnAdd.setOnClickListener(this);
        binding.btnRemove.setOnClickListener(this);
        binding.edtTime.setOnClickListener(this);
        binding.btnSave.setOnClickListener(this);
        binding.btnDelete.setOnClickListener(this);

        binding.switchReminder.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            binding.edtTime.setEnabled(isChecked);
            for (int i = 0; i < binding.cgReminder.getChildCount(); i++) {
                binding.cgReminder.getChildAt(i).setEnabled(isChecked);
            }
        });

        Intent intent = getIntent();
        isUpdate = intent.hasExtra(EXTRA_TARGET);
        if (isUpdate){
            target = intent.getParcelableExtra(EXTRA_TARGET);
            binding.toolbar.setTitle("Edit Target");
            binding.btnDelete.setVisibility(View.VISIBLE);

            binding.edtPagesRead.setText(String.valueOf(target.getPagesRead()));
            binding.edtBookTitle.setText(target.getBookTitle());
            binding.edtTotalPages.setText(String.valueOf(target.getTotalPages()));
            binding.edtDailyPages.setText(String.valueOf(target.getDailyPages()));
            binding.switchReminder.setChecked(target.getIsReminderEnabled());
            binding.edtTime.setText(target.getReminderTime());

            todayPagesRead = preference.getTodayPagesRead(target.getId());
            lastUpdatePagesRead = preference.getLastUpdatePagesRead(target.getId());
        } else {
            target = new Target();
            binding.toolbar.setTitle("Tambah Target");
            binding.btnDelete.setVisibility(View.GONE);

            binding.edtPagesRead.setText("0");

            todayPagesRead = 0;
            lastUpdatePagesRead = getCurrentDate();
        }

        targetViewModel = new ViewModelProvider(this).get(TargetViewModel.class);

        setEditTextValidationListener();
        setHintProgressDaily();
    }

    private void setHintProgressDaily() {
        if (differenceOfDates(lastUpdatePagesRead, getCurrentDate()) < 0){
            // Terakhir update kemarin, reset
            todayPagesRead = 0;
            lastUpdatePagesRead = getCurrentDate();
        }

        int dailyPages = getDailyPagesFromEditText();
        if (todayPagesRead < dailyPages){
            binding.tvHintDailyProgress.setText("Kamu belum mencapai target harian (" + todayPagesRead + "/" + dailyPages + ")");
            binding.tvHintDailyProgress.setTextColor(getResources().getColor(R.color.red));
        } else {
            binding.tvHintDailyProgress.setText("Target harian terpenuhi (" + todayPagesRead + "/" + dailyPages + ")");
            binding.tvHintDailyProgress.setTextColor(getResources().getColor(R.color.green));
        }
    }

    private void setEditTextValidationListener() {
        binding.edtPagesRead.addTextChangedListener(new TextWatcher() {
            private int oldPagesRead;

            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                oldPagesRead = getPagesReadFromEditText();
            }

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int totalPages = getTotalPagesFromEditText();
                int pagesRead = getPagesReadFromEditText();
                if (pagesRead > totalPages){
                    todayPagesRead = totalPages;
                    binding.edtPagesRead.setText(String.valueOf(totalPages));
                    showToast(AddUpdateTargetActivity.this,
                            "Progres baca tidak boleh lebih dari jumlah halaman");
                } else if (pagesRead < totalPages){
                    todayPagesRead += (getPagesReadFromEditText() - oldPagesRead);
                }
                setHintProgressDaily();
            }

            @Override public void afterTextChanged(Editable editable) {}
        });

        binding.edtDailyPages.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int totalPages = getTotalPagesFromEditText();
                int dailyPages = getDailyPagesFromEditText();
                if (dailyPages > totalPages){
                    binding.edtDailyPages.setText(String.valueOf(totalPages));
                    showToast(AddUpdateTargetActivity.this,
                            "Target harian tidak boleh lebih dari jumlah halaman");
                }
                setHintProgressDaily();
            }
            @Override public void afterTextChanged(Editable editable) {}
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add:
                binding.edtPagesRead.setText(String.valueOf(getPagesReadFromEditText() + 1));
                break;

            case R.id.btn_remove:
                if (getPagesReadFromEditText() > 0){
                    binding.edtPagesRead.setText(String.valueOf(getPagesReadFromEditText() - 1));
                }
                break;

            case R.id.edt_time:
                TimePickerFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), TIME_PICKER_REPEAT_TAG);
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

                String time = getFixText(binding.edtTime);
                if (binding.switchReminder.isChecked()){
                    if (time.isEmpty()){
                        showToast(this, "Kamu belum mengatur waktu untuk pengingat");
                        return;
                    }
                }

                if (!isUpdate){
                    target.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    target.setId(targetViewModel.getReference().document(target.getUserId())
                            .collection("target").document().getId());
                }

                target.setBookTitle(bookTitle);
                target.setTotalPages(Integer.parseInt(totalPages));
                target.setDailyPages(Integer.parseInt(dailyPages));

                int pagesRead = getPagesReadFromEditText();
                target.setPagesRead(pagesRead);
                target.setProgress(((double) pagesRead/(double) target.getTotalPages()) * 100);

                target.setIsReminderEnabled(binding.switchReminder.isChecked());
                target.setReminderTime(time);

                preference.setData(target.getId(), lastUpdatePagesRead, todayPagesRead);

                if (target.getIsReminderEnabled()){
                    TargetReminder targetReminder = new TargetReminder();
                    targetReminder.setReminder(this, target);
                } else {
                    cancelReminder(this, target.getId().hashCode());
                }

                if (isUpdate){
                    targetViewModel.update(target);
                    showToast(this, "Target berhasil diedit");
                } else {
                    targetViewModel.insert(target);
                    showToast(this, "Target berhasil ditambah");
                }

                onBackPressed();
                break;

            case R.id.btn_delete:
                new AlertDialog.Builder(this)
                        .setTitle("Hapus target")
                        .setMessage("Apakah kamu yakin ingin menghapusnya?")
                        .setNegativeButton("Batal", null)
                        .setPositiveButton("Ya", (dialogInterface, i) -> {
                            targetViewModel.delete(target);
                            preference.removeData(target.getId());
                            cancelReminder(this, target.getId().hashCode());
                            showToast(view.getContext(), "Target berhasil dihapus");
                            onBackPressed();
                        }).create().show();
                break;
        }
    }

    @Override
    public void onDialogTimeSet(String tag, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT, LOCALE);
        if (TIME_PICKER_REPEAT_TAG.equals(tag)) {
            binding.edtTime.setText(dateFormat.format(calendar.getTime()));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private int getDailyPagesFromEditText(){
        int dailyPages = 0;
        if (!getFixText(binding.edtDailyPages).isEmpty())
            dailyPages = Integer.parseInt(getFixText(binding.edtDailyPages));
        return dailyPages;
    }

    private int getTotalPagesFromEditText(){
        int totalPages = 0;
        if (!getFixText(binding.edtTotalPages).isEmpty())
            totalPages = Integer.parseInt(getFixText(binding.edtTotalPages));
        return totalPages;
    }

    private int getPagesReadFromEditText(){
        int pagesRead = 0;
        if (!getFixText(binding.edtPagesRead).isEmpty())
            pagesRead = Integer.parseInt(getFixText(binding.edtPagesRead));
        return pagesRead;
    }
}