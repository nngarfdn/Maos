package com.presidev.maos.catatanku.quotes.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.presidev.maos.R;
import com.presidev.maos.catatanku.quotes.model.Quote;
import com.presidev.maos.databinding.FragmentQuotesBinding;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.presidev.maos.utils.AppUtils.showToast;

public class QuotesFragment extends Fragment {
    private static final int RC_WRITE_EXTERNAL_STORAGE = 100;

    private FragmentQuotesBinding binding;

    public QuotesFragment() {}

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentQuotesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        binding.rvQuotes.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvQuotes.setHasFixedSize(true);
        QuoteAdapter adapter = new QuoteAdapter();
        binding.rvQuotes.setAdapter(adapter);

        QuoteViewModel quoteViewModel = new ViewModelProvider(this).get(QuoteViewModel.class);
        quoteViewModel.getQuoteListLiveData().observe(getViewLifecycleOwner(), adapter::setData);
        quoteViewModel.query(firebaseUser.getUid());
        quoteViewModel.addSnapshotListener(firebaseUser.getUid());

        new MySwipeHelper(requireContext(), binding.rvQuotes, 240) {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MySwipeHelper.MyButton> buffer) {
                buffer.add(new MyButton(requireContext(),
                        "Hapus",
                        32,
                        R.drawable.ic_delete,
                        Color.parseColor("#F46060"),
                        pos -> new AlertDialog.Builder(requireContext())
                                .setTitle("Hapus kutipan")
                                .setMessage("Apakah kamu yakin ingin menghapusnya?")
                                .setNegativeButton("Batal", null)
                                .setPositiveButton("Ya", (dialogInterface, i) -> {
                                    Quote quote = adapter.getData().get(pos);
                                    quoteViewModel.delete(quote);
                                    showToast(requireContext(), "Kutipan berhasil dihapus");
                                }).create().show()
                ));
                buffer.add(new MyButton(requireContext(),
                        "Bagikan",
                        32,
                        R.drawable.ic_share,
                        Color.parseColor("#F4A260"),
                        pos -> new AlertDialog.Builder(requireContext())
                                .setTitle("Bagikan kutipan")
                                .setMessage("Bagikan kutipan ini?")
                                .setNegativeButton("Batal", null)
                                .setPositiveButton("Ya", (dialogInterface, i) -> {
                                    Quote quote = adapter.getData().get(pos);
                                    shareQuote(quote);
                                }).create().show()
                ));
            }
        };

        binding.fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddQuoteActivity.class);
            startActivity(intent);
        });
    }

    private void shareQuote(Quote quote){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (requireContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_WRITE_EXTERNAL_STORAGE);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (requireContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                showToast(requireContext(), "Maos tidak punya izin untuk menyimpan foto");
                return;
            }
        }

        try {
            Bitmap bitmap = ((BitmapDrawable) ((ImageView) getView().findViewById(R.id.img_quote)).getDrawable()).getBitmap();

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(
                    requireContext().getContentResolver(), bitmap,
                    quote.getId(), null);
            Uri imageUri = Uri.parse(path);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
            startActivity(Intent.createChooser(intent, "Bagikan kutipan"));
        } catch (ClassCastException e) {
            e.printStackTrace();
            showToast(requireContext(), "Gagal membagikan kutipan, mohon coba lagi");
        }
    }
}