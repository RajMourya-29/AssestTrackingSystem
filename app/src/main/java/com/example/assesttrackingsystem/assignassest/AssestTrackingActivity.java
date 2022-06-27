package com.example.assesttrackingsystem.assignassest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assesttrackingsystem.ApiInterface;
import com.example.assesttrackingsystem.Global;
import com.example.assesttrackingsystem.R;
import com.example.assesttrackingsystem.Responsee;
import com.example.assesttrackingsystem.assignassest.pojoclasses.EmployeeName;
import com.example.assesttrackingsystem.assignassest.pojoclasses.LocationDetail;
import com.example.assesttrackingsystem.assignassest.pojoclasses.SubLocationDetail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AssestTrackingActivity extends AppCompatActivity {

    String setbarcode,setrfid;
    LinearLayout linear1,linear2;
    EditText barcode,rfid,ondate;
    Spinner empname,location,subloaction;
    Button assign,clear;
    TextView showondate;

    private int mYear,mMonth,mDay;

    Retrofit retrofit;
    SpotsDialog spotsDialog;
    List<EmployeeName> responseeArrayList;
    List<LocationDetail> locationDetailList;
    List<SubLocationDetail> subLocationDetailList;

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assest_tracking);

        barcode = findViewById(R.id.barcode);
        rfid = findViewById(R.id.rfid);
        empname = findViewById(R.id.empname);
        ondate = findViewById(R.id.ondate);
        location = findViewById(R.id.location);
        subloaction = findViewById(R.id.subloaction);
        showondate = findViewById(R.id.showondate);
        linear1 = findViewById(R.id.linear1);
        linear2 = findViewById(R.id.linear2);
        assign = findViewById(R.id.assign);
        clear = findViewById(R.id.clear);

        responseeArrayList = new ArrayList<com.example.assesttrackingsystem.assignassest.pojoclasses.EmployeeName>();
        locationDetailList = new ArrayList<>();
        subLocationDetailList = new ArrayList<>();
        retrofit = new Retrofit.Builder()
                .baseUrl(Global.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate1 = df1.format(c.getTime());
        ondate.setText(formattedDate1);

        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate2 = df2.format(c.getTime());
        showondate.setText(formattedDate2);
        
        new AlertDialog.Builder(AssestTrackingActivity.this)
                .setMessage("Select Assest Type")
                .setCancelable(false)
                .setPositiveButton("Barcode", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setbarcode="0";
                        linear1.requestFocus();
                        rfid.setEnabled(false);
                        linear2.setAlpha(0.2f);
                        if (!Global.isOnline(getApplicationContext())) {
                            Global.showsnackbar(AssestTrackingActivity.this, "No Internet Connection");
                        } else {
                            loadata();
                        }
                    }
                })
                .setNegativeButton("RFID", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setrfid="0";
                        linear2.requestFocus();
                        barcode.setEnabled(false);
                        linear1.setAlpha(0.2f);
                        if (!Global.isOnline(getApplicationContext())) {
                            Global.showsnackbar(AssestTrackingActivity.this,"No Internet Connection");
                        } else {
                            loadata();
                        }
                    }
                })
                .show();

        ondate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AssestTrackingActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                ondate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                showondate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(barcode.getText().toString().trim().equals("0"))   {
                    Global.showsnackbar(AssestTrackingActivity.this,"Please Scan Barcode");
                }else if(rfid.getText().toString().trim().equals("0"))   {
                    Global.showsnackbar(AssestTrackingActivity.this,"Please Scan RFID");
                }else {
                    spotsDialog = Global.showdailog(AssestTrackingActivity.this);
                    executor.execute(() -> {
                        savedata();
                    });
                }

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                startActivity(getIntent());

            }
        });

    }

    private void loadata() {

        ApiInterface myAPICall = retrofit.create(ApiInterface.class);
        Call<Responsee> call = myAPICall.getempdata();

        call.enqueue(new Callback<Responsee>() {
            @Override
            public void onResponse(Call<Responsee> call, Response<Responsee> response) {

                Responsee data = response.body();
                if(data.getResponse().equals("true")){

                    responseeArrayList=data.getEmployeeName();

                    List itemcode = new ArrayList<>();
                    for (int i=0; i<responseeArrayList.size(); i++) {
                        itemcode.add(responseeArrayList.get(i).getUserName());
                    }

                    ArrayAdapter aa = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,itemcode);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    empname.setAdapter(aa);
                    loadlocationdata();

                }else {
//                    spotsDialog.dismiss();
                    Global.showsnackbar(AssestTrackingActivity.this,"No Data Found");
                }


            }

            @Override
            public void onFailure(Call<Responsee> call, Throwable t) {

            }
        });


    }

    private void loadlocationdata() {

        ApiInterface myAPICall = retrofit.create(ApiInterface.class);
        Call<Responsee> call = myAPICall.getlocationdata();

        call.enqueue(new Callback<Responsee>() {
            @Override
            public void onResponse(Call<Responsee> call, Response<Responsee> response) {

                Responsee data = response.body();
                if(data.getResponse().equals("true")){

                    locationDetailList=data.getLocationDetails();

                    List itemcode = new ArrayList<>();
                    for (int i=0; i<locationDetailList.size(); i++) {
                        itemcode.add(locationDetailList.get(i).getLocationname());
                    }

                    ArrayAdapter aa = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,itemcode);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    location.setAdapter(aa);

                    location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            loadsublocationdata();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                }else {
//                    spotsDialog.dismiss();
                    Global.showsnackbar(AssestTrackingActivity.this,"No Data Found");
                }


            }

            @Override
            public void onFailure(Call<Responsee> call, Throwable t) {

            }
        });


    }

    private void loadsublocationdata() {

        ApiInterface myAPICall = retrofit.create(ApiInterface.class);
        Call<Responsee> call = myAPICall.getsublocationdata(location.getSelectedItem().toString());

        call.enqueue(new Callback<Responsee>() {
            @Override
            public void onResponse(Call<Responsee> call, Response<Responsee> response) {

                Responsee data = response.body();
                if(data.getResponse().equals("true")){

                    subLocationDetailList=data.getSubLocationDetails();

                    List itemcode = new ArrayList<>();
                    for (int i=0; i<subLocationDetailList.size(); i++) {
                        itemcode.add(subLocationDetailList.get(i).getSublocation());
                    }

                    ArrayAdapter aa = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,itemcode);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    subloaction.setAdapter(aa);

                }else {
//                    spotsDialog.dismiss();
                    Global.showsnackbar(AssestTrackingActivity.this,"No Data Found");
                }


            }

            @Override
            public void onFailure(Call<Responsee> call, Throwable t) {

            }
        });


    }

    private void savedata() {

        String getbarcode = barcode.getText().toString();
        String getempname = empname.getSelectedItem().toString();
        String getlocation = location.getSelectedItem().toString();
        String getsublocation = subloaction.getSelectedItem().toString();
        String getondate = showondate.getText().toString();

        ApiInterface myAPICall = retrofit.create(ApiInterface.class);
        Call<Responsee> call = myAPICall.savedata(getbarcode,getempname,getlocation,getsublocation, getondate);

        call.enqueue(new Callback<Responsee>() {
            @Override
            public void onResponse(Call<Responsee> call, retrofit2.Response<Responsee> response) {
                if(response.isSuccessful()) {
                    Responsee checkResponse = response.body();

                    if (checkResponse.getResponse().equals("data saved suceessfully")) {
                        Global.showsnackbar(AssestTrackingActivity.this,"Data Saved Suceessfully");
                        spotsDialog.cancel();
                    }
                }else {
                    Global.showsnackbar(AssestTrackingActivity.this,"Something Went Wrong");
                    spotsDialog.cancel();
                }
            }

            @Override
            public void onFailure(Call<Responsee> call, Throwable t) {

            }
        });

    }

}