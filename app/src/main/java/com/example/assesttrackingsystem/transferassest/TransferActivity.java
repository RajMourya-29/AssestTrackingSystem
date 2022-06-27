package com.example.assesttrackingsystem.transferassest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
import com.example.assesttrackingsystem.assignassest.AssestTrackingActivity;
import com.example.assesttrackingsystem.assignassest.pojoclasses.EmployeeName;
import com.example.assesttrackingsystem.assignassest.pojoclasses.LocationDetail;
import com.example.assesttrackingsystem.assignassest.pojoclasses.SubLocationDetail;
import com.example.assesttrackingsystem.transferassest.adapter.IssueAssestDetailAdapter;
import com.example.assesttrackingsystem.transferassest.pojoclass.AssetDetail;
import com.example.assesttrackingsystem.transferassest.pojoclass.BasicAssetDetail;
import com.example.assesttrackingsystem.transferassest.pojoclass.IssueAssetDetail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

public class TransferActivity extends AppCompatActivity {

    EditText barcode,ondate;
    LinearLayout linearLayout,linearLayout2;
    TextView serialno,type,category,subcategory,desc,location1,sublocation1,show,show2,showondate;
    Button transfer;
    Spinner empname,location,subloaction;
    Retrofit retrofit;
    Boolean isshow;
    SpotsDialog spotsDialog;
    List<EmployeeName> responseeArrayList;
    List<BasicAssetDetail> assetDetails;
    RecyclerView recyclerView;

    ArrayList<IssueAssetDetail> issueAssetDetailArrayList;
    IssueAssestDetailAdapter detailAdapter;

    List<LocationDetail> locationDetailList;
    List<SubLocationDetail> subLocationDetailList;

    private int mYear,mMonth,mDay;
    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        serialno = findViewById(R.id.serialno);
        type = findViewById(R.id.type);
        category = findViewById(R.id.category);
        subcategory = findViewById(R.id.subcategory);
        desc = findViewById(R.id.desc);
        location1 = findViewById(R.id.location1);
        sublocation1 = findViewById(R.id.sublocation1);
        transfer = findViewById(R.id.transfer);
        empname = findViewById(R.id.empname);
        linearLayout = findViewById(R.id.linear);
        linearLayout2 = findViewById(R.id.linear2);
        barcode = findViewById(R.id.barcode);
        show = findViewById(R.id.show);
        show2 = findViewById(R.id.show2);
        recyclerView = findViewById(R.id.recycler);
        ondate = findViewById(R.id.ondate);
        location = findViewById(R.id.location);
        subloaction = findViewById(R.id.subloaction);
        showondate = findViewById(R.id.showondate);

        responseeArrayList = new ArrayList<com.example.assesttrackingsystem.assignassest.pojoclasses.EmployeeName>();
        assetDetails = new ArrayList<>();
        locationDetailList = new ArrayList<>();
        subLocationDetailList = new ArrayList<>();
        issueAssetDetailArrayList = new ArrayList<>();

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

        barcode.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {

                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    if(barcode.getText().toString().trim().length() == 0) {
                        Toast.makeText(getApplicationContext(), "Please Scan A Barcode", Toast.LENGTH_LONG).show();
                    }else {
                        loadassetdetail();
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                    return true;
                }

                return false;
            }
        });

        ondate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(TransferActivity.this,
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

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(issueAssetDetailArrayList.size()==0) {
                    Global.showsnackbar(TransferActivity.this, "Assign a Asset");
                }else {
                    spotsDialog = Global.showdailog(TransferActivity.this);
                    executor.execute(() -> {
                        savedata();
                    });
                }
            }
        });

    }

    private void loadempname() {

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

                    empname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                       //     getempname.setText(empname.getSelectedItem().toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                }else {
//                    spotsDialog.dismiss();
                    Global.showsnackbar(TransferActivity.this,"No Data Found");
                }


            }

            @Override
            public void onFailure(Call<Responsee> call, Throwable t) {

            }
        });

    }

    private void loadassetdetail() {

        ApiInterface myAPICall = retrofit.create(ApiInterface.class);
        Call<AssetDetail> call = myAPICall.getassetdetail(barcode.getText().toString());

        call.enqueue(new Callback<AssetDetail>() {
            @Override
            public void onResponse(Call<AssetDetail> call, Response<AssetDetail> response) {

                AssetDetail data = response.body();
                if(data.getResponse().equals("true")){

                    serialno.setText(data.getBasicAssetDetails().get(0).getColumn1());
                    type.setText(data.getBasicAssetDetails().get(0).getColumn2());
                    category.setText(data.getBasicAssetDetails().get(0).getColumn3());
                    subcategory.setText(data.getBasicAssetDetails().get(0).getColumn4());
                    desc.setText(data.getBasicAssetDetails().get(0).getColumn5());
                    location1.setText(data.getBasicAssetDetails().get(0).getColumn6());
                    sublocation1.setText(data.getBasicAssetDetails().get(0).getColumn7());

                    if(serialno.getText().toString().equals("")){
                        linearLayout.setVisibility(View.GONE);
                        show2.setVisibility(View.VISIBLE);
                    }else {
                        linearLayout.setVisibility(View.VISIBLE);
                        show2.setVisibility(View.GONE);
                        loadempname();
                        loadlocationdata();
                        loadissueassetdetail();
                    }

                }else {
                    Global.showsnackbar(TransferActivity.this,"No Data Found");
                }

            }

            @Override
            public void onFailure(Call<AssetDetail> call, Throwable t) {

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
                    Global.showsnackbar(TransferActivity.this,"No Data Found");
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
                    Global.showsnackbar(TransferActivity.this,"No Data Found");
                }


            }

            @Override
            public void onFailure(Call<Responsee> call, Throwable t) {

            }
        });


    }

    private void loadissueassetdetail() {

        ApiInterface myAPICall = retrofit.create(ApiInterface.class);
        Call<AssetDetail> call = myAPICall.getissueassetdetail(barcode.getText().toString());

        call.enqueue(new Callback<AssetDetail>() {
            @Override
            public void onResponse(Call<AssetDetail> call, Response<AssetDetail> response) {

                AssetDetail data = response.body();

                if(data.getResponse().equals("true")) {

                    issueAssetDetailArrayList = new ArrayList<>(Arrays.asList(data.getIssueAssetDetails()));
                    detailAdapter=new IssueAssestDetailAdapter(issueAssetDetailArrayList,getApplicationContext());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(detailAdapter);
                    show.setVisibility(View.GONE);
                    isshow=false;
                }else {
                    Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_LONG).show();
                    show.setVisibility(View.VISIBLE);
                    isshow=true;
                }
            }

            @Override
            public void onFailure(Call<AssetDetail> call, Throwable t) {

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
                        Global.showsnackbar(TransferActivity.this,"Data Saved Suceessfully");
                        spotsDialog.cancel();
                    }
                }else {
                    Global.showsnackbar(TransferActivity.this,"Something Went Wrong");
                    spotsDialog.cancel();
                }
            }

            @Override
            public void onFailure(Call<Responsee> call, Throwable t) {

            }
        });

    }

}