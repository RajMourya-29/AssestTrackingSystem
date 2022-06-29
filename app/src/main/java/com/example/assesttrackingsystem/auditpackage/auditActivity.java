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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.assesttrackingsystem.Global;
import com.example.assesttrackingsystem.R;
import com.example.assesttrackingsystem.assignassest.AssestTrackingActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.seuic.uhf.UHFService;

import java.util.ArrayList;
import java.util.List;

public class auditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText barcode,rfid;
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
        barcode=findViewById(R.id.barcode);
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
                    }
                })
                .setNegativeButton("RFID", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        barcode.setEnabled(false);
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

                    int r = handler.insertalldata(str_barcode, "null", str_loc, str_subloc, str_dept);
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
                        AuditModelArrayList.add(new AuditModel(str_barcode, str_rfid, str_loc, str_subloc, str_dept));
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

                        int bank = 2;
                        int address = 0;
                        int length = 12;
                        String str_password = "00000000";
                        String Epc = rfid.getText().toString();

                        byte[] btPassword = new byte[16];
                        BaseUtil.getHexByteArray(str_password, btPassword, btPassword.length);
                        byte[] buffer = new byte[MAX_LEN];
                        if (length > MAX_LEN) {
                            buffer = new byte[length];
                        }

                        if (!mDevice.readTagData(BaseUtil.getHexByteArray(Epc), btPassword, bank, address, length, buffer)) {
                            Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                            String data = BaseUtil.getHexString(buffer, length);
                            rfid.setText(data);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "scan rfid tag", Toast.LENGTH_SHORT).show();
                    }
                }

                return false;
            }
        });


    }

    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



}