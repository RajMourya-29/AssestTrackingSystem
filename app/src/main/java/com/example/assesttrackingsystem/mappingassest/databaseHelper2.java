package com.example.assesttrackingsystem.mappingassest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.assesttrackingsystem.auditpackage.AuditModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class databaseHelper2 extends SQLiteOpenHelper {
    public static   String MAPPING = "mapping_table";
    public static   String DATABASE_NAME = "asahi2";
    private static final String ID="id";
    private static final String BARCODE = "barcode";
    private static final String RFID = "rfid";
    private static final String SCANNED_DATE = "date";
    private static final String SCANNED_TIME = "time";
    private static final String USERNAME = "username";
    private static final String USERCODE = "usercode";



    public databaseHelper2(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL( "create table mapping_table " +
                "(id integer primary key, barcode text,rfid text,username text,usercode text)"
        );


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS inentory_table");
        onCreate(db);
    }

    public int insertmappingdata (String barcode, String rfid,String username,String usercode) {
        SQLiteDatabase db = this.getWritableDatabase();
        String QUERY = "SELECT * FROM  "+MAPPING + " WHERE BARCODE='"+barcode+"' OR RFID='"+rfid+"';";
        Cursor cursor = db.rawQuery(QUERY, null);
        if (cursor.getCount() > 0){
            return 0;
        }
        else
        {

            ContentValues contentValues = new ContentValues();
            contentValues.put("barcode", barcode);
            contentValues.put("rfid", rfid);
            contentValues.put("username", username);
            contentValues.put("usercode", usercode);

            db.insert("mapping_table", null, contentValues);
            return 1;
        }
     }


//    public ArrayList<String[]> getMappingdata()
//    {
//        ArrayList<String[]> outputlist = new ArrayList();
//        String selectQuery =  "SELECT * FROM  "+MAPPING;
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        if(cursor.moveToNext())
//        {
//            do
//            {
//                String barcode = (cursor.getString(cursor.getColumnIndex(BARCODE)));
//                String rfid = (cursor.getString(cursor.getColumnIndex(RFID)));
//                String username =  (cursor.getString(cursor.getColumnIndex(USERNAME)));
//                String usercode =  (cursor.getString(cursor.getColumnIndex(USERCODE)));
//
//                outputlist.add(new String[]{barcode,rfid});
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//
//        return outputlist;
//    }


    public ArrayList<MappingModel> getMappingdata()
    {
        ArrayList<MappingModel> outputlist = new ArrayList();
        String selectQuery =  "SELECT * FROM  "+MAPPING;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToNext())
        {
            do
            {
                String barcode = (cursor.getString(cursor.getColumnIndex(BARCODE)));
                String rfid = (cursor.getString(cursor.getColumnIndex(RFID)));
                String username =  (cursor.getString(cursor.getColumnIndex(USERNAME)));
                String usercode =  (cursor.getString(cursor.getColumnIndex(USERCODE)));

                outputlist.add(new MappingModel(barcode,rfid,username,usercode));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return outputlist;
    }



    public void deleteRecords(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MAPPING,null,null);
        db.close();
    }

}





