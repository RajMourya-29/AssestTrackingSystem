package com.example.assesttrackingsystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class CheckActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    Boolean Registerd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedpreferences = CheckActivity.this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Registerd = sharedpreferences.getBoolean("check",false);




    }

}