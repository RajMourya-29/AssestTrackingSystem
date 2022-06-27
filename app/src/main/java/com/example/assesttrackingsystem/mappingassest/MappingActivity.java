package com.example.assesttrackingsystem.mappingassest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.assesttrackingsystem.ApiInterface;
import com.example.assesttrackingsystem.Global;
import com.example.assesttrackingsystem.R;
import com.example.assesttrackingsystem.Responsee;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.seuic.sleduhf.EPC;
import com.seuic.sleduhf.UhfCallback;
import com.seuic.sleduhf.UhfDevice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MappingActivity extends AppCompatActivity   {

    EditText barcode, rfid;
    String bartag, rfidtag;
    Button button;
    private databaseHelper2 handler;
    ArrayList<String[]> SaveBoxData;
    private UhfDevice mDevice;
    private String model, strEPC;
    private SoundPool soundPool;
    private ArrayList<MappingModel> MappingModelArraylist;
    private ArrayList<String[]> barcodata;
    private int soundId;
    public static final int MAX_LEN = 64;
    String data;
    RecyclerView user_list;
    MappingAdapter mappingAdapter;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping);

        handler = new databaseHelper2(getApplicationContext());

        barcode = findViewById(R.id.edt_barcode);
        rfid = findViewById(R.id.edt_rfid);
        button = findViewById(R.id.save);

        mDevice = UhfDevice.getInstance(this);
        mDevice.inventoryStop();

        bartag = barcode.getText().toString();
        rfidtag = rfid.getText().toString();

        MappingModelArraylist = new ArrayList<>();
        barcodata = new ArrayList<String[]>();
        builder = new AlertDialog.Builder(this);

        user_list = findViewById(R.id.user_list);

        mappingAdapter = new MappingAdapter(MappingActivity.this, MappingModelArraylist);
        user_list.setAdapter(mappingAdapter);

        barcode.requestFocus();
        barcodata.addAll(handler.getMappingdata());

        SaveBoxData = handler.getMappingdata();

        button.setOnClickListener(v -> {

            GlobalProgressDialog.showProgress(this, "Saving barcode details please wait...");

            if (SaveBoxData.isEmpty()){
                if (GlobalProgressDialog.isProgressShowing()) {
                    GlobalProgressDialog.dismissProgress();
                }
                Toast.makeText(MappingActivity.this, "List Empty", Toast.LENGTH_SHORT).show();
            } else {
                JSONObject jsonObject = new JSONObject();
                try {
                    JSONArray inventoryBarcodeJsonArray = new JSONArray(SaveBoxData.toString());
                    jsonObject.put("SaveBoxData", inventoryBarcodeJsonArray);
                    Log.e("Json: ", jsonObject.toString());
                } catch (JSONException exception){
                    if (GlobalProgressDialog.isProgressShowing()) {
                        GlobalProgressDialog.dismissProgress();
                    }
                    exception.printStackTrace();
                }

                RetrofitInstance.getInstance().getApiInterface().saveAllBarcode(jsonObject.toString())
                        .enqueue(new Callback<SaveInventoryBarcodeResponse>() {
                            @Override
                            public void onResponse(Call<SaveInventoryBarcodeResponse> call, Response<SaveInventoryBarcodeResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                              //      new Logger(MappingActivity.this).saveLog("MappingActivity Activity", "Success Response: " + response.isSuccessful());
                                    if (response.body().getMessage().equals("Data Inserted Successfully")) {

                                        new MaterialAlertDialogBuilder(MappingActivity.this)
                                                .setTitle("message")
                                                .setIcon(R.drawable.ic_launcher_background)
                                                .setCancelable(false)
                                                .setMessage("Data Inserted Successfully")
                                                .setPositiveButton("Okay", (dialog, which) -> {
                                                    SaveBoxData.clear();
                                                    handler.deleteRecords();
                                                    finish();
                                                    startActivity(getIntent());
                                                    dialog.cancel();
                                                }).create().show();
                                    } else {
                                        new MaterialAlertDialogBuilder(MappingActivity.this)
                                                .setTitle("message")
                                                .setIcon(R.drawable.ic_launcher_background)
                                                .setCancelable(false)
                                                .setMessage(response.body().getMessage())
                                                .setPositiveButton("Okay", (dialog, which) -> {
                                                    finish();
                                                    startActivity(getIntent());
                                                    dialog.cancel();
                                                }).create().show();
                                        Toast.makeText(MappingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                  //  new Logger(MappingActivity.this).saveLog("MappingActivity Activity", "Error Response: " + response.errorBody());

                                    Toast.makeText(MappingActivity.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                                }
                                if (GlobalProgressDialog.isProgressShowing()) {
                                    GlobalProgressDialog.dismissProgress();
                                }
                            }

                            @Override
                            public void onFailure(Call<SaveInventoryBarcodeResponse> call, Throwable t) {
                                if (GlobalProgressDialog.isProgressShowing()) {
                                    GlobalProgressDialog.dismissProgress();
                                }
                        //        new Logger(MappingActivity.this).saveLog("MappingActivity Activity", "Failure Response: " + t.getLocalizedMessage());

                                Toast.makeText(MappingActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }

        });


        barcode.setOnKeyListener((v, keyCode, event) -> {

            if ((event.getAction() == KeyEvent.ACTION_DOWN)
                    && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                bartag=barcode.getText().toString();
                barcode.post(new Runnable() {
                    @Override
                    public void run() {
                        rfid.setText("");
                        rfid.requestFocus();
                    }
                });
            }

            return false;
        });

        rfid.setOnKeyListener((v, keyCode, event) -> {

            if ((event.getAction() == KeyEvent.ACTION_DOWN)
                    && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                bartag = barcode.getText().toString();
                rfidtag = rfid.getText().toString();

                EPC epc = new EPC();
                if (mDevice.inventoryOnce(epc,100)) {
                    String id = epc.getId();
                    String epcid = id.substring(4);
                    byte[] Epc = epc.id;
                    byte[] btPassword = new byte[16];
                    BaseUtil.getHexByteArray("00000000", btPassword, btPassword.length);
                    byte[] buffer = new byte[MAX_LEN];
                    int length = 12;
                    if (length > MAX_LEN) {
                        buffer = new byte[length];
                    }
                    //
                    if (!mDevice.readTagData(Epc, btPassword, 2, 0, length, buffer)) {
                        Toast.makeText(MappingActivity.this, "readTagData_faild", Toast.LENGTH_SHORT).show();
                    } else {
                        //  Toast.makeText(getContext(), R.string.readTagData_sucess, Toast.LENGTH_SHORT).show();
                        data = BaseUtil.getHexString(buffer, length);
                        rfidtag = "H" + data;
                        Log.e("datadata", "" + data);
                    }
                    //
                    strEPC = "H" + epcid;
                    rfid.setText("EPC: " + strEPC + "\nTID: " + rfidtag);
                    String verifyTid = rfidtag.substring(rfidtag.length() - 11, rfidtag.length() - 1);
                    if (verifyTid.equals("0000000000")) {
                        new AlertDialog.Builder(MappingActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Warning")
                                .setMessage("Invalid tag id")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        rfid.setText("");
                                        barcode.setText("");

                                    }
                                })
                                .show();
                    }
                }

                Log.e("mDevice", "" + mDevice);
                if (bartag.isEmpty() || rfidtag.isEmpty()) {
                    Toast.makeText(MappingActivity.this, "Something Missing", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("data", "" + data);

                    int r = handler.insertmappingdata(bartag, rfidtag, "", "");
                    if (r == 0) {
                        builder.setMessage("Barcode And RFID already present")
                                .setIcon(R.drawable.rounded_button)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        barcode.setText("");
                                        rfid.setText("");
                                        barcode.requestFocus();
                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.setTitle("ERROR");
                        alert.show();
                    } else {
                        MappingModelArraylist.clear();
                        SaveBoxData = handler.getMappingdata();
                        for (String[] str : SaveBoxData) {
                     //       String log = str[0] + "|" + str[1] + "|" + str[2];

                            Toast.makeText(MappingActivity.this, "successfully", Toast.LENGTH_SHORT).show();
                            MappingModelArraylist.add(new MappingModel(str[0], str[1]));
                            mappingAdapter.notifyDataSetChanged();
                            rfid.post(new Runnable() {
                                @Override
                                public void run() {
                                    barcode.setText("");
                                    rfid.setText("");
                                    barcode.requestFocus();
                                }
                            });
                        }
                    }
                }
            }


            return false;
        });


        UhfCallback mUhfCallback = new UhfCallback() {

            @Override
            public void onGetBtInfo(BluetoothDevice bluetoothDevice) {
                mDevice.connect(bluetoothDevice.getAddress(), 1000);

            }

            @Override
            public void onGetConnectStatus(int i) {
                if (i == 0)
                    Toast.makeText(MappingActivity.this, "Device connected", Toast.LENGTH_LONG).show();
                else if (i == -1)
                    Toast.makeText(MappingActivity.this, "Device disconnected", Toast.LENGTH_LONG).show();
                else if (i == -2)
                    Toast.makeText(MappingActivity.this, "connection timeout", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onGetTagsId(List<EPC> list) {

            }

            @Override
            public void onGetScanKeyMode(int i) {

            }
        };
        mDevice.registerUhfCallback(mUhfCallback);
        mDevice.setSledParam(UhfDevice.PARAM_FASTID, 1);

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        if (soundPool == null) {
            Log.e("as3992", "Open sound failed");
        }
        soundId = soundPool.load("/system/media/audio/ui/VideoRecord.ogg", 0);
        Log.w("as3992_6C", "id is " + soundId);

        boolean rv = mDevice.setPower(10);
    }

    @Override
    protected void onStop() {
        Log.w("stop", "im stopping");

        mDevice.inventoryStop();
        soundPool.release();
        super.onStop();
    }
}