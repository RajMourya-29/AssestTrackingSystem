package com.example.assesttrackingsystem.blank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assesttrackingsystem.R;
import com.example.assesttrackingsystem.mappingassest.MappingActivity;
import com.example.assesttrackingsystem.mappingassest.MappingAdapter;
import com.example.assesttrackingsystem.mappingassest.MappingModel;
import com.seuic.uhf.EPC;
import com.seuic.uhf.UHFService;

import java.util.ArrayList;
import java.util.List;

public class BlankActivity extends AppCompatActivity {

    public static final int MAX_LEN = 64;
    private UHFService mDevice;
    EditText rfid;
    TextView id, bufferTxt;
    MappingAdapter mappingAdapter;
    private List<EPC> mEPCList;
    private ArrayList<MappingModel> MappingModelArraylist;
    RecyclerView user_list;
    AlertDialog.Builder builder;
    private static SoundPool mSoundPool;
    private static int soundID;


    static int m_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);

        mDevice = UHFService.getInstance();

        mEPCList = new ArrayList<EPC>();

        rfid =findViewById(R.id.rfid);
        id =findViewById(R.id.text_tag);
        bufferTxt =findViewById(R.id.text_buffer);
        user_list = findViewById(R.id.user_list);

        id.requestFocus();

        MappingModelArraylist = new ArrayList<>();
        mappingAdapter = new MappingAdapter(this, MappingModelArraylist);
        user_list.setAdapter(mappingAdapter);

    }

    public void scanRfid(View view) {
        EPC epc = new EPC();
        if (mDevice.inventoryOnce(epc, 100)) {
            String id = epc.getId();
            System.out.println("" + id);
            if (id != null && !"".equals(id)) {
                playSound();
                mEPCList.clear();
                mEPCList.add(epc);
                refreshData();
            }
            System.out.println("OK!!!");
        }
    }

    public void readRfid(View view) {
        int bank = 2;
        int address = 0;
        int length = 12;

        String str_password ="00000000";

        String Epc = mEPCList.get(0).getId();

        byte[] btPassword = new byte[16];

        BaseUtil.getHexByteArray(str_password, btPassword, btPassword.length);

        byte[] buffer = new byte[MAX_LEN];

        if (length > MAX_LEN) {
            buffer = new byte[length];
        }

        if (!mDevice.readTagData(BaseUtil.getHexByteArray(Epc), btPassword, bank, address, length, buffer)) {

            Toast.makeText(this, R.string.readTagData_faild, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.readTagData_sucess, Toast.LENGTH_SHORT).show();
            String data = BaseUtil.getHexString(buffer, length, " ");
            bufferTxt.setText(data);
            MappingModelArraylist.add(new MappingModel(id.getText().toString(), data, "",""));
            mappingAdapter.notifyDataSetChanged();
            id.setText("");
            bufferTxt.setText("");
        }
    }

    private void refreshData() {

        if (mEPCList != null) {
            // Gets the number inside the list of all labels
            int count = 0;
            for (EPC item : mEPCList) {
                count += item.count;
            }
            if (count > m_count) {
                playSound();
            }
            id.setText(mEPCList.get(0).getId());
            Log.e("List;", ""+mEPCList.size());
        }
    }

    private void playSound() {
        if (mSoundPool == null) {
            mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 20);
            soundID = mSoundPool.load(this, R.raw.scan, 1);// "/system/media/audio/notifications/Antimony.ogg"
        }
        mSoundPool.play(soundID, 1, 1, 0, 0, 1);
    }
}