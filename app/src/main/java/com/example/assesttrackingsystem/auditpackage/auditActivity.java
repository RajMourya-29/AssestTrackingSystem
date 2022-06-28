package com.example.assesttrackingsystem.auditpackage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.assesttrackingsystem.Global;
import com.example.assesttrackingsystem.R;
import com.example.assesttrackingsystem.assignassest.AssestTrackingActivity;

import java.util.ArrayList;

public class auditActivity extends AppCompatActivity {
EditText barcode,rfid;
private Databasehelper handler;
 ArrayList<String[]> subloc, loc,dept;
 String location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit);
        barcode=findViewById(R.id.barcode);
        rfid=findViewById(R.id.rfid);
        handler = new Databasehelper(getApplicationContext());


     loc= (ArrayList<String[]>) handler.getLoc();
     subloc= (ArrayList<String[]>) handler.getSubloc(location);
        dept= (ArrayList<String[]>) handler.getDepartment();



        if (loc.size() == 0){
            Toast.makeText(auditActivity.this, "location not present", Toast.LENGTH_SHORT).show();
        } else {
            for (String[] str : loc) {
                String log = str[0];
                Log.d("loc  TABLE", "DATA: " + log);
            }
        }

            if (subloc.size() == 0){
                Toast.makeText(auditActivity.this, "sub location not present", Toast.LENGTH_SHORT).show();
            } else {
                for (String[] str : subloc) {
                    String log = str[0];
                    Log.d("loc  TABLE", "DATA: " + log);
                }
            }

                if (dept.size() == 0){
                    Toast.makeText(auditActivity.this, "department not present", Toast.LENGTH_SHORT).show();
                } else {
                    for (String[] str : subloc) {
                        String log = str[0];
                        Log.d("loc  TABLE", "DATA: " + log);
                    }
                }


        new AlertDialog.Builder(auditActivity.this)
                .setMessage("Select scan Type")
                .setCancelable(false)
                .setPositiveButton("Barcode", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        rfid.setEnabled(false);
                    }
                })
                .setNegativeButton("RFID", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        barcode.setEnabled(false);
                    }
                })
                .show();
    }


}