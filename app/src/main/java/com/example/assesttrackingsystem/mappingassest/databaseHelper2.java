package com.example.assesttrackingsystem.mappingassest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

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



    public databaseHelper2(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL( "create table mapping_table " +
                "(id integer primary key, barcode text,rfid text,date text,time text)"
        );


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS inentory_table");
        onCreate(db);
    }

    public int insertmappingdata (String barcode, String rfid,String date,String currentTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        String QUERY = "SELECT * FROM  "+MAPPING + " WHERE BARCODE='"+barcode+"' OR RFID='"+rfid+"';";
        Cursor cursor = db.rawQuery(QUERY, null);
        if (cursor.getCount() > 0){
            return 0;
        }
        else
        {

            ContentValues contentValues = new ContentValues();
            String timeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss",
                    Locale.getDefault()).format(new Date());

            contentValues.put("barcode", barcode);
            contentValues.put("rfid", rfid);
            contentValues.put("date", timeStamp);

            db.insert("mapping_table", null, contentValues);
            return 1;
        }
     }


    public ArrayList<String[]> getMappingdata()
    {
        ArrayList<String[]> outputlist = new ArrayList();
        String selectQuery =  "SELECT * FROM  "+MAPPING;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToNext())
        {
            do
            {
                String barcode = (cursor.getString(cursor.getColumnIndex(BARCODE)));
                String rfid = (cursor.getString(cursor.getColumnIndex(RFID)));
//                String date =  (cursor.getString(cursor.getColumnIndex(SCANNED_DATE)));
//                String currentTime =  (cursor.getString(cursor.getColumnIndex(SCANNED_TIME)));

                outputlist.add(new String[]{barcode,rfid});
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





