package com.example.assesttrackingsystem.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Logger {

    Context context;

    public Logger(Context context) {
        this.context = context;
    }

    public void saveLog(String className, String msg){
        Log.e("Log: ", "Started");

        try {
            File pathToExternalStorage = Environment.getExternalStorageDirectory();
            File appDirectory = new File(pathToExternalStorage.getAbsolutePath() + "/ASI/Logs" + "/");
            if (!appDirectory.exists()) {
                boolean mkdir = appDirectory.mkdirs();
                if (mkdir){
                    Log.e("Created","Success");
                }
            }
            File file= new File(appDirectory, "log_"+new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date())+".txt");
            FileWriter writer = new FileWriter(file,true);
            writer.append(DateFormat.getDateTimeInstance().format(new Date())).append(": ").append(className+" - "+msg);
            writer.append("\n\r");
            writer.flush();
            writer.close();
            Log.e("Log: ", "Success");
            MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null, null);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Log: ", "Failed");
        }
    }
}
