package com.example.assesttrackingsystem;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.assesttrackingsystem.assignassest.AssestTrackingActivity;
import com.example.assesttrackingsystem.auditpackage.auditActivity;
import com.example.assesttrackingsystem.loginpage.LoginPageActivity;
import com.example.assesttrackingsystem.mappingassest.MappingActivity;
import com.example.assesttrackingsystem.transferassest.TransferActivity;
import com.example.assesttrackingsystem.utils.SharedPref;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity {

    LinearLayout inventory_layout,dispatch_layout,Receive_layout,pikingsheet_layout,missingrack_layout,barcodereplaced_layout,retrofyt1,appeove;
    String  name,plantid,Post;
    CardView inventory_card,dispatch_card,receive_card,picking_card,missing_card,replace_card,rectfy_card,appeovecard;

    String  usename,usercode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usename= new SharedPref(MainActivity.this).getString("username", "");
        Log.e("TAG","username : "+usename);
        usercode = new SharedPref(MainActivity.this).getString("usercode","");
        Log.e("TAG","usercode : "+usercode);


        OnBackPressedCallback backPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new MaterialAlertDialogBuilder(MainActivity.this)
                        .setTitle("Exit")
                        .setIcon(R.drawable.worningimaage)
                        .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Yes", (dialog, which) -> MainActivity.this.finishAffinity())
                        .setNegativeButton("No", (dialog, which) -> dialog.cancel()).create().show();
            }
        };

        MainActivity.this.getOnBackPressedDispatcher().addCallback(this, backPressedCallback);
        findViewById(R.id.inventory_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MappingActivity.class));
            }
        });

        findViewById(R.id.Receive_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AssestTrackingActivity.class));
            }
        });

        findViewById(R.id.dispatch_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TransferActivity.class));
            }
        });
        findViewById(R.id.pikingsheet_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, auditActivity.class));
            }
        });
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);



    }

    public boolean onCreateOptionsMenu( Menu menu ) {

        getMenuInflater().inflate(R.menu.addmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {

        switch (item.getItemId()){

            case R.id.Logout:
                new SharedPref(MainActivity.this).clearString("username");
                new SharedPref(MainActivity.this).clearString("plantid");
                startActivity(new Intent(MainActivity.this, LoginPageActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}