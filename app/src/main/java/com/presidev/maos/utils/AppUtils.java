package com.presidev.maos.utils;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

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
}
