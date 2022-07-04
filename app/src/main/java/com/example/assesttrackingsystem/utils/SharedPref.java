package com.example.assesttrackingsystem.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    Context context;

    public SharedPref(Context context) {
        this.context = context;
    }

    public void putString(String name, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ASI", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, value);
        editor.apply();
    }

    public String getString(String name, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ASI", Context.MODE_PRIVATE);
        return sharedPreferences.getString(name, defaultValue);
    }

    public void putInt(String name, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ASI", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(name, value);
        editor.apply();
    }

    public int getInt(String name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ASI", Context.MODE_PRIVATE);
        return sharedPreferences.getInt(name,0);
    }

    public void putBoolean(String name, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ASI", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(name, value);
        editor.apply();
    }

    public boolean getBoolean(String name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ASI", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(name,false);
    }

    public void clearString(String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ASI",Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(name).apply();
    }

}
