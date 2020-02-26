package com.maitriinfosoft.bar_code_app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.maitriinfosoft.bar_code_app.ScanData;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "scanData";

    // Table columns
    public static final String _ID = "_id";
    public static final String ITEM_CODE = "item_code";
    public static final String ITEM_NAME = "item_name";
    public static final String QUANTITY = "quantity";
    public static final String BATCH_NUMBER = "batch_number";
    public static final String BATCH_QUANTITY = "batch_QUANTITY";


    // Database Information
    static final String DB_NAME = "barCode.db";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ITEM_CODE + " TEXT NOT NULL, " +
            ITEM_NAME + "TEXT," + QUANTITY  +  "TEXT," + BATCH_NUMBER +   "TEXT,"  + BATCH_QUANTITY + " TEXT);";


    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    public DatabaseHandler(@Nullable Context context, @Nullable String name,
                           @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //db.execSQL("create table " + TABLE_NAME +" (ITEM_CODE TEXT,ITEM_NAME TEXT,QUANTITY TEXT,BATCH_NUMBER TEXT,BATCH_QUANTITY TEXT )");


        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }



    public boolean  insert(String itemCode, String itemName,String quantity, String batchNumber,String batchQty) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();

        contentValue.put(ITEM_CODE, itemCode);
        contentValue.put(ITEM_NAME, itemName);
        contentValue.put(QUANTITY, quantity);
        contentValue.put(BATCH_NUMBER, batchNumber);
        contentValue.put(BATCH_QUANTITY, batchQty);

        long result =db.insert(DatabaseHandler.TABLE_NAME, null, contentValue);

        if(result == -1)
            return false;
        else
            return true;


    }


    public int update( String _id,String itemCode, String itemName,String quantity,String batchNumber,String batchQty) {
        ContentValues contentValues = new ContentValues();

        SQLiteDatabase db = this.getWritableDatabase();

        // contentValues.put(DatabaseHandler._ID, _id);
        contentValues.put(DatabaseHandler.ITEM_CODE, itemCode);
        contentValues.put(DatabaseHandler.ITEM_NAME, itemName);
        contentValues.put(DatabaseHandler.QUANTITY, quantity);
        contentValues.put(DatabaseHandler.BATCH_NUMBER, batchNumber);
        contentValues.put(DatabaseHandler.BATCH_QUANTITY, batchQty);
        int i = db.update(DatabaseHandler.TABLE_NAME, contentValues, DatabaseHandler.BATCH_NUMBER + " = " + batchNumber, null);
        return i;
    }



    // code to add the new contact
    public void addData(ScanData contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ITEM_CODE, contact.getItemCode());
        values.put(ITEM_NAME, contact.getDescription());
        values.put(QUANTITY, contact.getQuality());
        values.put(BATCH_NUMBER, contact.getBatchNo());
        values.put(BATCH_QUANTITY, contact.getScan_qlt());

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }


    // code to get the single contact
    ScanData getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] {ITEM_CODE,
                        ITEM_NAME, QUANTITY,BATCH_NUMBER,BATCH_QUANTITY }, BATCH_NUMBER + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ScanData scanData1 = new ScanData(cursor.getString(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4));
        // return contact
        return scanData1;
    }


    // code to get all contacts in a list view
    public List<ScanData> getAllData() {
        List<ScanData> scanData_List = new ArrayList<ScanData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ScanData scanData = new ScanData();
                scanData.setItemCode(cursor.getString(0));
                scanData.setDescription(cursor.getString(1));
                scanData.setQuality(cursor.getString(2));
                scanData.setBatchNo(cursor.getString(3));
                scanData.setScan_qlt(cursor.getString(4));
                // Adding contact to list
                scanData_List.add(scanData);
            } while (cursor.moveToNext());
        }

        // return contact list
        return scanData_List;
    }

    // code to update the single contact
    public int updateSinglrData(ScanData scanData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ITEM_CODE, scanData.getItemCode());
        values.put(ITEM_NAME, scanData.getDescription());
        values.put(QUANTITY, scanData.getQuality());
        values.put(BATCH_NUMBER, scanData.getBatchNo());
        values.put(BATCH_QUANTITY, scanData.getScan_qlt());

        // updating row
        return db.update(TABLE_NAME, values, BATCH_NUMBER + " = ?",
                new String[] { String.valueOf(scanData.getBatchNo()) });
    }

    // Deleting single contact
    public void deleteSingleData(ScanData scanData) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, BATCH_NUMBER + " = ?",
                new String[] { String.valueOf(scanData.getBatchNo()) });
        db.close();
    }

    // Getting contacts Count
    public int getDataCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }




    public boolean checkdata(String batchNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_NAME + " where " + BATCH_NUMBER + " = " + "'"+ batchNumber +"'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0)
        {
            cursor.close();
            return false;
        }
        else
        {
            cursor.close();
            return true;
        }
    }



    public Cursor getData(){
        SQLiteDatabase db= this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query,null);
        return  data;
    }
}
