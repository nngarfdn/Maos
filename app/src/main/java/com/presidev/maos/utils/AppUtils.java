package com.presidev.maos.utils;

import android.content.Context;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.presidev.maos.R;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class AppUtils {
    public static Locale locale = new Locale("in", "ID");

    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    // Mendapatkan string tanpa spasi berlebihan
    public static String getFixText(EditText editText){
        return (editText.getText().toString().trim()).replaceAll("\\s+", " ");
    }

    public static void loadProfilePicFromUrl(ImageView imageView, String url){
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.ic_no_profile_pic)
                .error(R.drawable.ic_no_profile_pic)
                .into(imageView);
    }
}
