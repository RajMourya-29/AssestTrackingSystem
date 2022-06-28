package com.example.assesttrackingsystem.loginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

        userid = findViewById(R.id.userid);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressbar);
        login = findViewById(R.id.login);
        handler = new Databasehelper(getApplicationContext());
        retrofit = new Retrofit.Builder()
                .baseUrl(Global.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        login.setOnClickListener(view -> {
            startActivity(new Intent(LoginPageActivity.this, MainActivity.class));
            if(userid.getText().toString().trim().equals("")){
                Global.showsnackbar(LoginPageActivity.this,"Please Enter UserId");
            }else if(password.getText().toString().trim().equals("")){
                Global.showsnackbar(LoginPageActivity.this,"Please Enter Password");
            }else {
                login.setVisibility(View.INVISIBLE);
                getuserid = userid.getText().toString();
                getpassword = password.getText().toString();

                ApiInterface apiClient = retrofit.create(ApiInterface.class);
                Call<Responsee> call = apiClient.login(getuserid,getpassword);

                call.enqueue(new Callback<Responsee>() {
                    @Override
                    public void onResponse(Call<Responsee> call, Response<Responsee> response) {
                        if(response.isSuccessful()) {
                            Responsee checkResponse = response.body();

                            if (checkResponse.getResponse().equals("login successfull")) {
                                login.setVisibility(View.VISIBLE);
                                startActivity(new Intent(LoginPageActivity.this, MainActivity.class));
                            }else {
                                Global.showsnackbar(LoginPageActivity.this,"No User Found");
                                login.setVisibility(View.VISIBLE);
                            }
                        }else {
                            Global.showsnackbar(LoginPageActivity.this,"Something Went Wrong");
                            login.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Responsee> call, Throwable t) {

                    }
                });
            }

        });


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