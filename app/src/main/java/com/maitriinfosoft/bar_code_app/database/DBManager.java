package com.maitriinfosoft.bar_code_app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DatabaseHandler dbHelper;

    private Context context;

    private SQLiteDatabase database;


    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHandler(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String itemCode, String itemName,String quantity, String batchNumber,String batchQty) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHandler.ITEM_CODE, itemCode);
        contentValue.put(DatabaseHandler.ITEM_NAME, itemName);
        contentValue.put(DatabaseHandler.QUANTITY, quantity);
        contentValue.put(DatabaseHandler.BATCH_NUMBER, batchNumber);
        contentValue.put(DatabaseHandler.BATCH_QUANTITY, batchQty);

        database.insert(DatabaseHandler.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHandler.ITEM_CODE, DatabaseHandler.ITEM_NAME ,
                DatabaseHandler.QUANTITY , DatabaseHandler.BATCH_NUMBER };
        Cursor cursor = database.query(DatabaseHandler.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update( String _id,String itemCode, String itemName,String quantity,String batchNumber,String batchQty) {
        ContentValues contentValues = new ContentValues();

       // contentValues.put(DatabaseHandler._ID, _id);
        contentValues.put(DatabaseHandler.ITEM_CODE, itemCode);
        contentValues.put(DatabaseHandler.ITEM_NAME, itemName);
        contentValues.put(DatabaseHandler.QUANTITY, quantity);
        contentValues.put(DatabaseHandler.BATCH_NUMBER, batchNumber);
        contentValues.put(DatabaseHandler.BATCH_QUANTITY, batchQty);
        int i = database.update(DatabaseHandler.TABLE_NAME, contentValues, DatabaseHandler.BATCH_NUMBER + " = " + batchNumber, null);
        return i;
    }

    public void delete(long batchNumber) {
        database.delete(DatabaseHandler.TABLE_NAME, DatabaseHandler.BATCH_NUMBER + "=" + batchNumber, null);
    }



}
