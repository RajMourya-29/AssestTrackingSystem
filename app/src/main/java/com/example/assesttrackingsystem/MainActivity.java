package com.example.assesttrackingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.assesttrackingsystem.assignassest.AssestTrackingActivity;
import com.example.assesttrackingsystem.auditpackage.auditActivity;
import com.example.assesttrackingsystem.mappingassest.MappingActivity;
import com.example.assesttrackingsystem.transferassest.TransferActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.mapping).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MappingActivity.class));
            }
        });

        findViewById(R.id.assign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AssestTrackingActivity.class));
            }
        });

        findViewById(R.id.transfer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TransferActivity.class));
            }
        });
        findViewById(R.id.audit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, auditActivity.class));
            }
        });

    }
}