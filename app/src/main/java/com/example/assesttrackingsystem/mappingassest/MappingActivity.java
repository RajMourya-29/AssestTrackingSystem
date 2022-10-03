package com.example.assesttrackingsystem.mappingassest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.assesttrackingsystem.R;
import com.example.assesttrackingsystem.blank.BaseUtil;
import com.example.assesttrackingsystem.utils.SharedPref;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import com.seuic.uhf.UHFService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MappingActivity extends AppCompatActivity {

    EditText barcode, rfid;
    String bartag, rfidtag;
    Button button;
    String  username,usercode;
    private databaseHelper2 handler;
    ArrayList<String[]> SaveBoxData;

    private String model, strEPC;
    private SoundPool soundPool;
    private ArrayList<MappingModel> MappingModelArraylist;
    private ArrayList<MappingModel> barcodata;
    private int soundId;
    String data;
    RecyclerView user_list;
    MappingAdapter mappingAdapter;
    AlertDialog.Builder builder;
    public static final int MAX_LEN = 64;
    private UHFService mDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping);

        handler = new databaseHelper2(getApplicationContext());

        mDevice = UHFService.getInstance();

        barcode = findViewById(R.id.edt_barcode);
        rfid = findViewById(R.id.edt_rfid);
        button = findViewById(R.id.save);

        username= new SharedPref(MappingActivity.this).getString("username", "");
        Log.e("TAG","username : "+username);
        usercode = new SharedPref(MappingActivity.this).getString("usercode","");
        Log.e("TAG","usercode : "+usercode);


//        bartag = barcode.getText().toString();
//        rfidtag = rfid.getText().toString();

        MappingModelArraylist = new ArrayList<>();
        barcodata  = new ArrayList<>();
        builder = new AlertDialog.Builder(this);

        user_list = findViewById(R.id.user_list);

        barcode.requestFocus();



        mappingAdapter = new MappingAdapter(MappingActivity.this, barcodata);
        user_list.setLayoutManager(new LinearLayoutManager(MappingActivity.this));
        user_list.setItemAnimator(null);
        user_list.setAdapter(mappingAdapter);

        barcodata.addAll(handler.getMappingdata());

        if (barcodata.size() > 0){
            mappingAdapter.notifyItemChanged(barcodata.size());
        }

        button.setOnClickListener(v -> {

            GlobalProgressDialog.showProgress(this, "Saving bar tag details please wait...");

            if (barcodata.isEmpty()) {
                if (GlobalProgressDialog.isProgressShowing()) {
                    GlobalProgressDialog.dismissProgress();
                }
                Toast.makeText(MappingActivity.this, "List Empty", Toast.LENGTH_SHORT).show();
            } else {
                JSONObject jsonObject = new JSONObject();
                try {
                    JSONArray inventoryBarcodeJsonArray = new JSONArray(barcodata.toString());
                    jsonObject.put("SaveBoxData", inventoryBarcodeJsonArray);
                    Log.e("Json: ", jsonObject.toString());
                } catch (JSONException exception) {
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
                                    if (response.body().getMessage().equals("Data Inserted Successfully")) {

                                        handler.deleteRecords();
                                        barcodata.clear();
                                        if (response.body().getResponse() != null) {
                                            for (int i = 0; i < response.body().getResponse().size(); i++) {
                                                barcodata.add(new MappingModel(
                                                        response.body().getResponse().get(i).getBarcode(),
                                                        response.body().getResponse().get(i).getRfid(),
                                                        response.body().getResponse().get(i).getScanBy(),
                                                        response.body().getResponse().get(i).getUsercode()
                                                ));
                                            }

                                                Log.e("Size: ", "" + barcodata.size());

                                            }
                                        runOnUiThread(() ->
                                                mappingAdapter.notifyItemChanged(barcodata.size()));
                                    }
                                    new MaterialAlertDialogBuilder(MappingActivity.this)
                                            .setTitle("message")
                                            .setIcon(R.drawable.ic_baseline_check_circle_24)
                                            .setCancelable(false)
                                            .setMessage("Data Inserted Successfully")
                                            .setPositiveButton("Okay", (dialog, which) -> {
//                                                barcodata.clear();
//                                                handler.deleteRecords();

                                                dialog.cancel();
                                            }).create().show();
                                } else {
                                    new MaterialAlertDialogBuilder(MappingActivity.this)
                                            .setTitle("message")
                                            .setIcon(R.drawable.ic_baseline_error_24)
                                            .setCancelable(false)
                                            .setMessage(response.body().getMessage())
                                            .setPositiveButton("Okay", (dialog, which) -> {
                                                finish();
                                                startActivity(getIntent());
                                                dialog.cancel();
                                            }).create().show();
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
                bartag = barcode.getText().toString();
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


                if (bartag.isEmpty() || rfidtag.isEmpty()) {
                    Toast.makeText(MappingActivity.this, "Something Missing", Toast.LENGTH_SHORT).show();
                } else {

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
                        new MaterialAlertDialogBuilder(MappingActivity.this)
                                .setTitle("message")
                                .setIcon(R.drawable.ic_baseline_error_24)
                                .setCancelable(false)
                                .setMessage("failed to Scanned rfid Please Scan properly")
                                .setPositiveButton("Okay", (dialog, which) -> {
                                    rfid.setText("");
                                    rfid.requestFocus();
                                    dialog.cancel();
                                }).create().show();

                    } else {
                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                        String data = BaseUtil.getHexString(buffer, length);
                        rfid.setText(data);
                        rfidtag=data;

                        int r = handler.insertmappingdata(bartag, rfidtag,username , usercode);
                        if (r == 0) {
                            builder.setMessage("Barcode And RFID already present")
                                    .setIcon(R.drawable.ic_baseline_error_24)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // handler.deleteRecords();
                                            barcode.setText("");
                                            rfid.setText("");
                                            barcode.requestFocus();
                                        }
                                    });

                            AlertDialog alert = builder.create();
                            alert.setTitle("ERROR");
                            alert.show();
                        }
                        else{
                            barcodata.add(new MappingModel(bartag, rfidtag, username, usercode));
                            mappingAdapter.notifyItemChanged(barcodata.size());

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
    }
}