package com.presidev.maos.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;

import com.presidev.maos.R;
import com.squareup.picasso.Picasso;

import java.util.Locale;
import java.util.UUID;

import jp.wasabeef.picasso.transformations.BlurTransformation;

public class AppUtils {
    public static final Locale LOCALE = new Locale("in", "ID");

    public static void hideStatusBar(Activity activity, ActionBar actionBar){
        if (actionBar != null) actionBar.hide();
        // Mengubah notif bar menjadi transparan
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    // Mendapatkan string tanpa spasi berlebihan
    public static String getFixText(EditText editText){
        return (editText.getText().toString().trim()).replaceAll("\\s+", " ");
    }

    public static boolean isValidPhone(String number){
        if (number.length() < 2) return false;
        else return number.charAt(0) == '6' && number.charAt(1) == '2';
    }

    public static void loadImageFromUrl(ImageView imageView, String url){
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.ic_no_pic)
                .error(R.drawable.ic_no_pic)
                .into(imageView);
    }

    public static void loadBlurImageFromUrl(Context context, ImageView imageView, String url){
        Picasso.get()
                .load(url)
                .transform(new BlurTransformation(context, 24, 1))
                .placeholder(R.color.gray)
                .error(R.color.gray)
                .into(imageView);
    }

    public static void loadProfilePicFromUrl(ImageView imageView, String url){
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.ic_no_profile_pic)
                .error(R.drawable.ic_no_profile_pic)
                .into(imageView);
    }

    public static String setFullAddress(String address, String province, String regency, String district){
        if (address == null) return district + ", " + regency + ", " + province;
        else return address + ", " + district + ", " + regency + ", " + province;
    }

    public static String getMemberCardId(String mitraId, String userId){
        return String.valueOf(UUID.nameUUIDFromBytes((mitraId + userId).getBytes()))
                .replace("-", "").toUpperCase().substring(16, 32);
    }

    public static String showMemberCardId(String id){
        return id.substring(0, 4) + " " + id.substring(4, 8) + " " +
                id.substring(8, 12) + " " + id.substring(12, 16);
    }

    @SuppressLint("ClickableViewAccessibility")
    public static final View.OnTouchListener scrollableListener = (view, event) -> {
        view.getParent().requestDisallowInterceptTouchEvent(true);
        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_SCROLL) {
            view.getParent().requestDisallowInterceptTouchEvent(false);
            return true;
        }
        return false;
    };
}
