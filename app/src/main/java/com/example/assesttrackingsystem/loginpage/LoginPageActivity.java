package com.example.assesttrackingsystem.loginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.assesttrackingsystem.ApiInterface;
import com.example.assesttrackingsystem.Global;
import com.example.assesttrackingsystem.MainActivity;
import com.example.assesttrackingsystem.R;
import com.example.assesttrackingsystem.Responsee;
import com.example.assesttrackingsystem.mappingassest.MappingActivity;
import com.google.android.material.snackbar.Snackbar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        userid = findViewById(R.id.userid);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressbar);
        login = findViewById(R.id.login);

        retrofit = new Retrofit.Builder()
                .baseUrl(Global.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        login.setOnClickListener(view -> {

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
}