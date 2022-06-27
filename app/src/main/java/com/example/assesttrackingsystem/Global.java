package com.example.assesttrackingsystem;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import dmax.dialog.SpotsDialog;

public class Global {

    public static String BASE_URL="http://assettrack.rajkamalbarscan.co.in/WebService1.asmx/";

    public static boolean isOnline(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static void showsnackbar(Activity activity, String message){
        Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static void showtoast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static SpotsDialog showdailog(Context context){
        SpotsDialog progressDialog;
        progressDialog = new SpotsDialog(context, R.style.Custom);
        progressDialog.show();
        return progressDialog;
    }

}
