package com.example.assesttrackingsystem.auditpackage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Databasehelper extends SQLiteOpenHelper {

    public static   String LOCATION_TABLE = "location_table";
    public static   String SUB_LOCATION_TABLE = "sub_location_table";
    public static   String DEPARTMENT_TABLE = "department_table";
    public static   String ALL_DATA = "all_data";
    public static   String DATABASE_NAME = "asset";


    private static final String ID="id";
    private static final String LOCATION = "location";
    private static final String SUBLOCATION = "sublocation";
    private static final String DEPARTMENT = "department";
    private static final String  BARCODE= "barcode";
    private static final String  RFID= "rfid";


    public Databasehelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL( "create table location_table " +
                "(id integer primary key, location text,sublocation text)"
        );
//        db.execSQL( "create table sub_location_tbl " +
//                "(id integer primary key, sublocation text)"
//        );
        db.execSQL( "create table department_table " +
                "(id integer primary key,department text)"
        );

        db.execSQL( "create table all_data " +
                "(id integer primary key, location text,sublocation text,department text,barcode text)"
        );


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS location_table");
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS sub_location_table");
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS department_table");
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS all_data");
        onCreate(db);
    }
    public int insertlocation (String location,String sublocation) {
        SQLiteDatabase db = this.getWritableDatabase();
        String QUERY = "SELECT * FROM  "+LOCATION_TABLE + " WHERE LOCATION='"+location+"'; ";
        Cursor cursor = db.rawQuery(QUERY, null);
        if (cursor.getCount() > 0){
            return 0;
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("location", location);
            contentValues.put("sublocation", sublocation);
            db.insert("location_table", null, contentValues);
            return 1;
        }
    }

//    public int insertsublocation (String sublocation) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String QUERY = "SELECT * FROM  "+SUB_LOCATION_TABLE + " WHERE SUBLOCATION='"+sublocation+"'; ";
//        Cursor cursor = db.rawQuery(QUERY, null);
//        if (cursor.getCount() > 0){
//            return 0;
//        } else {
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("sublocation", sublocation);
//            db.insert("sub_location_table", null, contentValues);
//            return 1;
//        }
//    }

    public int insertdepartment (String department) {
        SQLiteDatabase db = this.getWritableDatabase();
        String QUERY = "SELECT * FROM  "+DEPARTMENT_TABLE + " WHERE DEPARTMENT='"+department+"'; ";
        Cursor cursor = db.rawQuery(QUERY, null);
        if (cursor.getCount() > 0){
            return 0;
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("department", department);
            db.insert("department_table", null, contentValues);
            return 1;
        }
    }

    public int insertalldata (String location,String sublocation,String department,String barcode ) {
        SQLiteDatabase db = this.getWritableDatabase();
        String QUERY = "SELECT * FROM  "+ALL_DATA + " WHERE BARCODE='"+barcode+"'; ";
        Cursor cursor = db.rawQuery(QUERY, null);
        if (cursor.getCount() > 0){
            return 0;
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("location", location);
            contentValues.put("sublocation", sublocation);
            contentValues.put("department", department);
            contentValues.put("barcode", barcode);
            db.insert("all_data", null, contentValues);
            return 1;
        }
    }


    public List<String> getLoc()
    {
      List<String> outputlist = new ArrayList();
        String selectQuery =   "SELECT DISTINCT LOCATION FROM " +LOCATION_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToNext())
        {
            do
            {
                String location = (cursor.getString(cursor.getColumnIndex(LOCATION)));

                outputlist.add(new String(location));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return outputlist;
    }


    public List<String> getSubloc(String location)
    {
        List<String> outputlist = new ArrayList();
        String selectQuery =   "SELECT  SUBLOCATION  FROM " +LOCATION_TABLE+
                " where  LOCATION='"+location+";" ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToNext())
        {
            do
            {
                String sublocation = (cursor.getString(cursor.getColumnIndex(SUBLOCATION)));

                outputlist.add(new String(sublocation));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return outputlist;
    }



    public List<String> getDepartment()
    {
        List<String> outputlist = new ArrayList();
        String selectQuery =   "SELECT DISTINCT DEPARTMENT FROM " +DEPARTMENT_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToNext())
        {
            do
            {
                String department = (cursor.getString(cursor.getColumnIndex(DEPARTMENT)));

                outputlist.add(new String(department));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return outputlist;
    }
}
