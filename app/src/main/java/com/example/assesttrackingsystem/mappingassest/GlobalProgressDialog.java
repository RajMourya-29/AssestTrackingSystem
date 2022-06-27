package com.example.assesttrackingsystem.mappingassest;

import android.app.ProgressDialog;
import android.content.Context;

public class GlobalProgressDialog {

    private static ProgressDialog progressDialog;

    public GlobalProgressDialog() {

    }

    public static boolean isProgressShowing() {
        return progressDialog != null && progressDialog.isShowing();
    }

    public static void showProgress(Context context, String message){

        try {
            if (progressDialog != null) {
                if (progressDialog.isShowing()) {
                    progressDialog.setMessage(message);
                    return;
                }
            }

            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            progressDialog.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean dismissProgress(){
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {

                try {
                    progressDialog.dismiss();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                progressDialog = null;
                return true;
            }
        }
        return false;
    }
}