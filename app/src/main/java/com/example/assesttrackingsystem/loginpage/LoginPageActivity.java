package com.example.assesttrackingsystem.loginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.assesttrackingsystem.ApiInterface;
import com.example.assesttrackingsystem.Global;
import com.example.assesttrackingsystem.MainActivity;
import com.example.assesttrackingsystem.R;
import com.example.assesttrackingsystem.Responsee;
import com.example.assesttrackingsystem.auditpackage.Databasehelper;
import com.example.assesttrackingsystem.auditpackage.GetDeptResponse;
import com.example.assesttrackingsystem.auditpackage.LocSublocResponse;
import com.example.assesttrackingsystem.mappingassest.GlobalProgressDialog;
import com.example.assesttrackingsystem.mappingassest.MappingActivity;
import com.example.assesttrackingsystem.mappingassest.RetrofitInstance;
import com.example.assesttrackingsystem.utils.SharedPref;
import com.google.android.material.snackbar.Snackbar;

import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginPageActivity extends AppCompatActivity {

    String getuserid,getpassword;
    EditText userid,password;
    ProgressBar progressBar;
    Button login;
    Retrofit retrofit;
    private Databasehelper handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        String username = new SharedPref(LoginPageActivity.this).getString("username","NA");
        Log.e("name: ", username);
        if (!username.equals("NA")){
            GlobalProgressDialog.showProgress(LoginPageActivity.this, "please wait...");
            startActivity(new Intent(LoginPageActivity.this,MainActivity.class));
        }else {
            userid = findViewById(R.id.userid);
            password = findViewById(R.id.password);
            progressBar = findViewById(R.id.progressbar);
            login = findViewById(R.id.login);
            handler = new Databasehelper(getApplicationContext());
            retrofit = new Retrofit.Builder()
                    .baseUrl(Global.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


            login.setOnClickListener(v -> {
              //  GlobalProgressDialog.showProgress(this, "Logging in please wait...");
                String user = userid.getText().toString();
                String pass = password.getText().toString();

                if (user.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(LoginPageActivity.this, "Somthing is missing ", Toast.LENGTH_SHORT).show();
                } else {
                    //GlobalProgressDialog.showProgress(this, "Logging in please wait...");
                    if (!Global.isOnline(LoginPageActivity.this)) {
                        if (GlobalProgressDialog.isProgressShowing()) {
                            GlobalProgressDialog.dismissProgress();
                        }
                        Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
                    } else {
                        RetrofitInstance.getInstance().getApiInterface().goLogin(user, pass)
                                .enqueue(new Callback<LoginResponse>() {
                                    @Override
                                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            if (response.body().getMessage().equals("Success")) {

                                                getDepartment();
                                                getLocSubloc();
                                                Toast.makeText(LoginPageActivity.this, response.body().getLogin().getId(), Toast.LENGTH_SHORT).show();
                                                new SharedPref(LoginPageActivity.this).putString("username", response.body().getLogin().getName());
                                                new SharedPref(LoginPageActivity.this).putString("usercode", response.body().getLogin().getId());
                                                startActivity(new Intent(LoginPageActivity.this, MainActivity.class));
                                            } else {
                                                Toast.makeText(LoginPageActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(LoginPageActivity.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                        if (GlobalProgressDialog.isProgressShowing()) {
                                            GlobalProgressDialog.dismissProgress();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                                        if (GlobalProgressDialog.isProgressShowing()) {
                                            GlobalProgressDialog.dismissProgress();
                                        }
                                        Toast.makeText(LoginPageActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }


            });
        }

    }

    private void getLocSubloc() {
        GlobalProgressDialog.showProgress(this, " please wait...");

        if (!Global.isOnline(LoginPageActivity.this)) {
            if (GlobalProgressDialog.isProgressShowing()) {
                GlobalProgressDialog.dismissProgress();
            }
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
        } else {

            RetrofitInstance.getInstance().getApiInterface().GetLocandsubloc()
                    .enqueue(new Callback<LocSublocResponse>() {
                        @Override
                        public void onResponse(Call<LocSublocResponse> call, Response<LocSublocResponse> response) {
                            if (response.isSuccessful() && response.body() != null){
                                if (response.body().getMessage().equals("Success")){
                                    if (response.body().getResponse() != null){
                                        for (int i = 0; i < response.body().getResponse().size(); i++){
                                            handler.insertlocation(response.body().getResponse().get(i).getLocationname(),response.body().getResponse().get(i).getSublocation());
                                        }

                                    }

                                } else {
                                    Toast.makeText(LoginPageActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginPageActivity.this, "Fail: "+response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (GlobalProgressDialog.isProgressShowing()) {
                                GlobalProgressDialog.dismissProgress();
                            }
                        }

                        @Override
                        public void onFailure(Call<LocSublocResponse> call, Throwable t) {
                            if (GlobalProgressDialog.isProgressShowing()) {
                                GlobalProgressDialog.dismissProgress();
                            }
                            Toast.makeText(LoginPageActivity.this, "Failure: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    private void getDepartment() {
        GlobalProgressDialog.showProgress(this, "please wait...");

        if (!Global.isOnline(LoginPageActivity.this)) {
            if (GlobalProgressDialog.isProgressShowing()) {
                GlobalProgressDialog.dismissProgress();
            }
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
        } else {

            RetrofitInstance.getInstance().getApiInterface().Getdepartment()
                    .enqueue(new Callback<GetDeptResponse>() {
                        @Override
                        public void onResponse(Call<GetDeptResponse> call, Response<GetDeptResponse> response) {
                            if (response.isSuccessful() && response.body() != null){
                                if (response.body().getMessage().equals("Success")){
                                    if (response.body().getResponse() != null){
                                        for (int i = 0; i < response.body().getResponse().size(); i++){
                                            handler.insertdepartment(response.body().getResponse().get(i).getDepartment());
                                        }

                                    }

                                } else {
                                    Toast.makeText(LoginPageActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginPageActivity.this, "Fail: "+response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (GlobalProgressDialog.isProgressShowing()) {
                                GlobalProgressDialog.dismissProgress();
                            }
                        }

                        @Override
                        public void onFailure(Call<GetDeptResponse> call, Throwable t) {
                            if (GlobalProgressDialog.isProgressShowing()) {
                                GlobalProgressDialog.dismissProgress();
                            }
                            Toast.makeText(LoginPageActivity.this, "Failure: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

}