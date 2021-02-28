package com.presidev.maos.catatanku.quotes.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.presidev.maos.R;
import com.presidev.maos.catatanku.quotes.model.Quote;
import com.presidev.maos.catatanku.quotes.view.photoeditor.EditingToolsAdapter;
import com.presidev.maos.catatanku.quotes.view.photoeditor.TextEditorDialogFragment;
import com.presidev.maos.catatanku.quotes.view.photoeditor.ToolType;
import com.presidev.maos.customview.LoadingDialog;
import com.presidev.maos.databinding.ActivityAddQuoteBinding;
import com.presidev.maos.developer.view.DeveloperViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.SaveSettings;
import ja.burhanrashid52.photoeditor.TextStyleBuilder;
import ja.burhanrashid52.photoeditor.ViewType;

import static com.presidev.maos.utils.AppUtils.showToast;

public class AddQuoteActivity extends AppCompatActivity implements View.OnClickListener, EditingToolsAdapter.OnItemSelected, OnPhotoEditorListener, BackgroundQuoteAdapter.OnQuoteBackgroundSelectCallback {
    public static final String FILE_PROVIDER_AUTHORITY = "com.presidev.maos.fileprovider";
    private static final int RC_BACKGROUND_IMAGE = 100;

    private ActivityAddQuoteBinding binding;
    private BackgroundQuoteAdapter adapter;
    //private EditingToolsAdapter mEditingToolsAdapter = new EditingToolsAdapter(this);
    private FirebaseUser firebaseUser;
    private LoadingDialog loadingDialog;
    private PhotoEditor mPhotoEditor;
    private QuoteViewModel quoteViewModel;

    @Nullable @VisibleForTesting private Uri mSaveImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddQuoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        loadingDialog = new LoadingDialog(this, false);

        binding.rvBackground.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvBackground.setHasFixedSize(true);
        adapter = new BackgroundQuoteAdapter(this);
        binding.rvBackground.setAdapter(adapter);
        DeveloperViewModel developerViewModel = new ViewModelProvider(this).get(DeveloperViewModel.class);
        developerViewModel.queryQuoteBackground();
        developerViewModel.getQuoteBackgroundListLiveData().observe(this, result -> {
            adapter.setData(result);
            setQuoteBackground(result.get(0));
        });

        mPhotoEditor = new PhotoEditor.Builder(this, binding.photoEditorView)
                .setPinchTextScalable(true) // set flag to make text scalable when pinch
                //.setDefaultTextTypeface(mTextRobotoTf)
                //.setDefaultEmojiTypeface(mEmojiTypeFace)
                .build(); // build photo editor sdk
        mPhotoEditor.setOnPhotoEditorListener(this);

        binding.imgUndo.setOnClickListener(this);
        binding.imgRedo.setOnClickListener(this);
        binding.imgShare.setOnClickListener(this);
        binding.btnSimpanQuote.setOnClickListener(this);
        binding.edtQuote.setOnClickListener(this);

        quoteViewModel = new ViewModelProvider(this).get(QuoteViewModel.class);
    }

    private void setQuoteBackground(String url){
        //binding.photoEditorView.getSource().setScaleType(ImageView.ScaleType.CENTER_CROP);
        //binding.photoEditorView.getSource().setAdjustViewBounds(true);
        Picasso.get()
                .load(url)
                //.fit().centerCrop()
                //.into(binding.photoEditorView.getSource());
                .into(new Target() {
                    @Override public void onPrepareLoad(Drawable placeHolderDrawable) {}
                    @Override public void onBitmapFailed(Exception e, Drawable errorDrawable) {}
                    @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        //binding.photoEditorView.getSource().setScaleType(ImageView.ScaleType.CENTER_CROP);
                        binding.photoEditorView.getSource().setBackground(new BitmapDrawable(getResources(), bitmap));
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edt_quote: onToolSelected(ToolType.TEXT); break;
            case R.id.btn_simpan_quote: saveImage(); break;
            case R.id.imgUndo: mPhotoEditor.undo(); break;
            case R.id.imgRedo: mPhotoEditor.redo(); break;
            case R.id.imgShare: shareImage(); break;
        }
    }

    private void shareImage() {
        new AlertDialog.Builder(this)
                .setTitle("Simpan gambar dulu")
                .setMessage("Setelah klik simpan, kamu bisa swipe kutipan yang sudah kamu simpan untuk dibagikan.")
                .setPositiveButton("Oke", null).create().show();
    }

    private Uri buildFileProviderUri(@NonNull Uri uri) {
        return FileProvider.getUriForFile(this,
                FILE_PROVIDER_AUTHORITY,
                new File(uri.getPath()));
    }

    @Override
    public void onToolSelected(ToolType toolType) {
        if (toolType == ToolType.TEXT) {
            TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show(this);
            textEditorDialogFragment.setOnTextEditorListener((inputText, colorCode) -> {
                final TextStyleBuilder styleBuilder = new TextStyleBuilder();
                styleBuilder.withTextColor(colorCode);
                styleBuilder.withGravity(Gravity.CENTER);
                styleBuilder.withTextFont(ResourcesCompat.getFont(this, R.font.creteround_italic));

                mPhotoEditor.addText(inputText, styleBuilder);
            });
        }
    }

    @SuppressLint("MissingPermission")
    private void saveImage() {
        loadingDialog.show();

        SaveSettings saveSettings = new SaveSettings.Builder()
                .setClearViewsEnabled(true)
                .setTransparencyEnabled(true)
                .build();

        mPhotoEditor.saveAsBitmap(saveSettings, new OnSaveBitmap() {
            @Override
            public void onBitmapReady(Bitmap saveBitmap) {
                Quote quote = new Quote();
                quote.setUserId(firebaseUser.getUid());
                quote.setId(quoteViewModel.getReference().document(quote.getUserId())
                        .collection("quotes").document().getId());

                String fileName = quote.getId() + ".jpeg";
                quoteViewModel.uploadImage(getApplicationContext(), saveBitmap,
                        firebaseUser.getUid(), fileName, imageUrl -> {
                    quote.setUrl(imageUrl);
                    quoteViewModel.insert(quote);

                    loadingDialog.dismiss();
                    showToast(getApplicationContext(), "Berhasil menyimpan kutipan");
                    onBackPressed();
                });
            }

            @Override
            public void onFailure(Exception e) {
                loadingDialog.dismiss();
                showToast(getApplicationContext(), "Gagal menyimpan kutipan, mohon coba lagi");
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onEditTextChangeListener(View rootView, String text, int colorCode) {
        TextEditorDialogFragment textEditorDialogFragment =
                TextEditorDialogFragment.show(this, text, colorCode);
        textEditorDialogFragment.setOnTextEditorListener((inputText, colorCode1) -> {
            final TextStyleBuilder styleBuilder = new TextStyleBuilder();
            styleBuilder.withTextColor(colorCode1);

            mPhotoEditor.editText(rootView, inputText, styleBuilder);
        });
    }

    @Override public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {}
    @Override public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {}
    @Override public void onStartViewChangeListener(ViewType viewType) {}
    @Override public void onStopViewChangeListener(ViewType viewType) {}

    @Override
    public void onSelected(String bgUrl) {
        setQuoteBackground(bgUrl);
    }

    @Override
    public void onPickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Pilih latar belakang"), RC_BACKGROUND_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_BACKGROUND_IMAGE){
            if (resultCode == Activity.RESULT_OK){
                if (data != null) if (data.getData() != null){
                    Uri uriImage = data.getData();
                    adapter.addData(uriImage.toString());
                    setQuoteBackground(uriImage.toString());
                }
            }
        }
    }
}