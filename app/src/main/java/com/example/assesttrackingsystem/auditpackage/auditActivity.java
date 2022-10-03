package com.example.assesttrackingsystem.auditpackage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.assesttrackingsystem.Global;
import com.example.assesttrackingsystem.MainActivity;
import com.example.assesttrackingsystem.R;
import com.example.assesttrackingsystem.assignassest.AssestTrackingActivity;
import com.example.assesttrackingsystem.blank.BaseUtil;
import com.example.assesttrackingsystem.mappingassest.GlobalProgressDialog;
import com.example.assesttrackingsystem.mappingassest.MappingActivity;
import com.example.assesttrackingsystem.mappingassest.RetrofitInstance;
import com.example.assesttrackingsystem.mappingassest.SaveInventoryBarcodeResponse;
import com.example.assesttrackingsystem.utils.SharedPref;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.seuic.uhf.UHFService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class auditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button submit;
    EditText barcode,rfid;
    String  usename,usercode;
    Spinner LocSpinner,SblocSpinner,DptSpinner;
    private Databasehelper handler;
    String str_loc ,str_subloc,str_dept;
    String str_barcode,str_rfid;
    private ArrayList<AuditModel> AuditModelArrayList;
    AuditAdapter auditAdapter;
    List<String> sublocation;
    RecyclerView audit_list;

    public static final int MAX_LEN = 64;
    private UHFService mDevice;
    ArrayList<String>  location,department;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit);


        usename= new SharedPref(auditActivity.this).getString("username", "");
        Log.e("TAG","username : "+usename);
        usercode = new SharedPref(auditActivity.this).getString("usercode","");
        Log.e("TAG","usercode : "+usercode);


        barcode=findViewById(R.id.barcode);
        submit=findViewById(R.id.submit);
        rfid=findViewById(R.id.rfid);
        audit_list=(RecyclerView) findViewById(R.id.recycle);
        LocSpinner = findViewById(R.id.loc);
        SblocSpinner = findViewById(R.id.sbloc);
        DptSpinner = findViewById(R.id.dpt);
        AuditModelArrayList  = new ArrayList<>();
        handler = new Databasehelper(getApplicationContext());

        mDevice = UHFService.getInstance();

        auditAdapter = new AuditAdapter(auditActivity.this, AuditModelArrayList);
        audit_list.setLayoutManager(new LinearLayoutManager(auditActivity.this));
        audit_list.setItemAnimator(null);
        audit_list.setAdapter(auditAdapter);

        AuditModelArrayList.addAll(handler.getalldata());

        if (AuditModelArrayList.size() > 0){
            auditAdapter.notifyItemChanged(AuditModelArrayList.size());
        }


        location= (ArrayList<String>) handler.getLoc();
        department= (ArrayList<String>) handler.getDepartment();

        LocSpinner.setOnItemSelectedListener(this);
        DptSpinner.setOnItemSelectedListener(this);
        SblocSpinner.setOnItemSelectedListener(this);

        ArrayAdapter loc = new ArrayAdapter(this, android.R.layout.simple_spinner_item, location);
        loc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        LocSpinner.setAdapter(loc);

        ArrayAdapter dept = new ArrayAdapter(this, android.R.layout.simple_spinner_item, department);
        dept.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DptSpinner.setAdapter(dept);

        LocSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l)
            {
                str_loc= location.get(position);
                Log.e("location ", ""+str_loc);
                sublocation=  handler.getSubloc(str_loc);

                ArrayAdapter subloc = new ArrayAdapter(auditActivity.this, android.R.layout.simple_spinner_item, sublocation);
                subloc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SblocSpinner.setAdapter(subloc);

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SblocSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l)
            {
                str_subloc=sublocation.get(position);

                Log.e("sub location ", ""+str_subloc);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        DptSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l)
            {
                str_dept=department.get(position);
                Log.e("department ", ""+str_dept);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        new AlertDialog.Builder(auditActivity.this)
                .setMessage("Select scan Type")
                .setCancelable(false)
                .setPositiveButton("Barcode", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        rfid.setEnabled(false);
                        barcode.requestFocus();
                    }
                })
                .setNegativeButton("RFID", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        barcode.setEnabled(false);
                        rfid.requestFocus();
                    }
                })
                .show();

        barcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_ENTER))
                {

                    str_barcode = barcode.getText().toString();

                    int r = handler.insertalldata(str_barcode, "NA", str_loc, str_subloc, str_dept,usename,usercode);
                    if (r == 0) {
                        new MaterialAlertDialogBuilder(auditActivity.this)
                                .setTitle("message")
                                .setIcon(R.drawable.ic_baseline_error_24)
                                .setCancelable(false)
                                .setMessage(" Already Scanned")
                                .setPositiveButton("Okay", (dialog, which) -> {
                                    barcode.setText("");
                                    barcode.requestFocus();
                                    dialog.cancel();
                                }).create().show();
                    } else {
                        AuditModelArrayList.add(new AuditModel(str_barcode, "NA", str_loc, str_subloc, str_dept,usename,usercode));
                        auditAdapter.notifyItemChanged(AuditModelArrayList.size());

                        barcode.post(new Runnable() {
                            @Override
                            public void run() {
                                barcode.setText("");
                                barcode.requestFocus();
                            }
                        });

                    }

                }
                return false;
            }
        });
        rfid.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    if (!rfid.getText().toString().equals("")) {

                        int bank = 1;
                        int address = 0;
                        int length = 3;
                        String str_password = "00000000";
                        String Epc = rfid.getText().toString().trim();

                        byte[] btPassword = new byte[16];

                        BaseUtil.getHexByteArray(str_password, btPassword, btPassword.length);

                        byte[] buffer = new byte[MAX_LEN];

                        if (length > MAX_LEN) {
                            buffer = new byte[length];
                        }

                        if (!mDevice.readTagData(BaseUtil.getHexByteArray(Epc), btPassword, bank, address, length, buffer)) {
                               new MaterialAlertDialogBuilder(auditActivity.this)
                                    .setTitle("message")
                                    .setIcon(R.drawable.ic_baseline_error_24)
                                    .setCancelable(false)
                                    .setMessage("failed to Scanned rfid Please Scan properly")
                                    .setPositiveButton("Okay", (dialog, which) -> {
                                        rfid.setText("");
                                        rfid.requestFocus();
                                        dialog.cancel();
                                    }).create().show();
                            Log.e("data: ",rfid.getText().toString());
                        } else {
                            Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                            String data = BaseUtil.getHexString(buffer, length);
                            rfid.setText(data);
                            str_rfid=data;
                            int r = handler.insertalldata("NA", str_rfid, str_loc, str_subloc, str_dept,usename,usercode);
                            if (r == 0) {
                                new MaterialAlertDialogBuilder(auditActivity.this)
                                        .setTitle("message")
                                        .setIcon(R.drawable.ic_baseline_error_24)
                                        .setCancelable(false)
                                        .setMessage(" Already Scanned")
                                        .setPositiveButton("Okay", (dialog, which) -> {
                                            rfid.setText("");
                                            rfid.requestFocus();
                                            dialog.cancel();
                                        }).create().show();
                            } else {
                                AuditModelArrayList.add(new AuditModel("NA", str_rfid, str_loc, str_subloc, str_dept,usename,usercode));
                                auditAdapter.notifyItemChanged(AuditModelArrayList.size());

                                rfid.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        rfid.setText("");
                                        rfid.requestFocus();
                                    }
                                });
                            }
                        }



                        } else {
                        Toast.makeText(getApplicationContext(), "scan rfid tag", Toast.LENGTH_SHORT).show();
                   }
                }

                return true;
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                GlobalProgressDialog.showProgress(auditActivity.this, "Saving bar tag details please wait...");

                if (AuditModelArrayList.isEmpty()){
                    if (GlobalProgressDialog.isProgressShowing()) {
                        GlobalProgressDialog.dismissProgress();
                    }
                    Toast.makeText(auditActivity.this, "List Empty", Toast.LENGTH_SHORT).show();
                } else if (!Global.isOnline(auditActivity.this)) {
                    if (GlobalProgressDialog.isProgressShowing()) {
                        GlobalProgressDialog.dismissProgress();
                    }
                    new MaterialAlertDialogBuilder(auditActivity.this)
                            .setTitle("message")
                            .setIcon(R.drawable.ic_baseline_error_24)
                            .setCancelable(false)
                            .setMessage("No internet")
                            .setPositiveButton("Okay", (dialog, which) -> {
                                dialog.cancel();
                            }).create().show();
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        JSONArray inventoryBarcodeJsonArray = new JSONArray(AuditModelArrayList.toString());
                        jsonObject.put("Auditdata", inventoryBarcodeJsonArray);
                        Log.e("AuditDATA: ", jsonObject.toString());
                    } catch (JSONException exception) {
                        if (GlobalProgressDialog.isProgressShowing()) {
                            GlobalProgressDialog.dismissProgress();
                        }
                        exception.printStackTrace();
                    }

                    RetrofitInstance.getInstance().getApiInterface().SaveAuditdataDetail(jsonObject.toString())
                            .enqueue(new Callback<SaveAduditResponse>() {
                                @Override
                                public void onResponse(Call<SaveAduditResponse> call, Response<SaveAduditResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        if (response.body().getMessage().equals("Data Inserted Successfully")) {

                                            new MaterialAlertDialogBuilder(auditActivity.this)
                                                    .setTitle("message")
                                                    .setIcon(R.drawable.ic_baseline_check_circle_24)
                                                    .setCancelable(false)
                                                    .setMessage("Data Inserted Successfully")
                                                    .setPositiveButton("Okay", (dialog, which) -> {
                                                        AuditModelArrayList.clear();
                                                        handler.deletealldata();
                                                        finish();
                                                        startActivity(getIntent());
                                                        dialog.cancel();
                                                    }).create().show();
                                        } else {
                                            new MaterialAlertDialogBuilder(auditActivity.this)
                                                    .setTitle("message")
                                                    .setIcon(R.drawable.ic_baseline_error_24)
                                                    .setCancelable(false)
                                                    .setMessage(response.body().getMessage())
                                                    .setPositiveButton("Okay", (dialog, which) -> {
                                                        finish();
                                                        startActivity(getIntent());
                                                        dialog.cancel();
                                                    }).create().show();
                                            Toast.makeText(auditActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                    } else {

                                        Toast.makeText(auditActivity.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                    if (GlobalProgressDialog.isProgressShowing()) {
                                        GlobalProgressDialog.dismissProgress();
                                    }
                                }

                                @Override
                                public void onFailure(Call<SaveAduditResponse> call, Throwable t) {
                                    if (GlobalProgressDialog.isProgressShowing()) {
                                        GlobalProgressDialog.dismissProgress();
                                    }
                                    //        new Logger(MappingActivity.this).saveLog("MappingActivity Activity", "Failure Response: " + t.getLocalizedMessage());

                                    Toast.makeText(auditActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }}
            });
    }


    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



}