package com.adrinofast.sa4;

import android.app.Activity;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

public class ProgressLoader {

    Activity activity;
    AlertDialog dialog;

    ProgressLoader(Activity myActivity)
    {
        this.activity = myActivity;
    }

    void StartProgressLoader()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progress_loader,null));
        builder.setCancelable(true);

        dialog=builder.create();
        dialog.show();


    }

    void stopProgresBar()
    {
        dialog.dismiss();
    }
}


