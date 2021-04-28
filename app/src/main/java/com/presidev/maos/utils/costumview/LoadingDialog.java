package com.presidev.maos.utils.costumview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.presidev.maos.R;

public class LoadingDialog {
    private final AlertDialog dialog;

    @SuppressLint("InflateParams")
    public LoadingDialog(Activity activity, boolean isCancelable){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_loading, null));
        builder.setCancelable(isCancelable);
        dialog = builder.create();
    }

    public void show(){
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }
}