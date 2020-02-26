package com.maitriinfosoft.bar_code_app.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.widget.Toast;

import com.maitriinfosoft.bar_code_app.ScanData;

import java.util.ArrayList;
import java.util.List;


public class SQLiteAdapter {

    public static final String MYDATABASE_NAME = "scanData.db";

    public static final String CUSTOMER_TABLE = "scanData";

    public static final int MYDATABASE_VERSION = 1;

    public static final String KEY_ID = "_id";
    public static final String ITEM_CODE = "itemcode";
    public static final String ITEM_NAME = "itemname";
    public static final String QUANTITY = "quantity";
    public static final String BATCH_NUMBER = "batchnumber11";
    public static final String BATCH_QUANTITY = "batchquantity";
    public static final String DRAFT_NUMBER = "draftnumber";
    public static final String SCAN_QUANTITY = "scanquantity";



    String colname[]=
            {
                    "draftnumber","","itemname","quantity","batchnumber","batchquantity","scanquantity"
            };


    private static  String cuaromer_table ="";
    //,productnamename text not null,source text not null);";

    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;

    private Context context;


    public SQLiteAdapter(Context c){
        context = c;
        String s="";
        for(int i=0;i<colname.length;i++)
        {
            s=s+colname[i]+" text no null";
            if(i!=colname.length-1)s=s+",";
        }
        //String text="create table customer ("+s+");";
        String text="create table " +CUSTOMER_TABLE+" ("+s+");";

        cuaromer_table=text;

    }

    public SQLiteAdapter openToRead() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        return this;
    }
    public SQLiteDatabase getReadable() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        return sqLiteDatabase;
    }
    public SQLiteAdapter openToWrite() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        sqLiteHelper.close();
    }

    public void getData()
    {

    }

    public long insertData(String args[])
    {
        ContentValues contentValues = new ContentValues();
        for(int i=0;i<colname.length;i++)
        {
            //////System.out.println("@@@Name: "+colname[i]+" = "+args[i]);
            contentValues.put(colname[i], args[i]);
        }
        return sqLiteDatabase.insert(CUSTOMER_TABLE, null, contentValues);
    }





    public List<ScanData> allData() {
        List<ScanData> scanDataList = new ArrayList<>();

        String select = "Select * from "+CUSTOMER_TABLE;
        Cursor c = sqLiteDatabase.rawQuery(select, null);

        if (c.moveToFirst()) {
            do {
                ScanData scanData = new ScanData();
                scanData.setItemCode(c.getString(0));
                scanData.setDescription(c.getString(1));
                scanData.setQuality(c.getString(2));
                scanData.setBatchNo(c.getString(3));
                scanData.setQuality(c.getString(4));
                scanDataList.add(scanData);
            } while (c.moveToNext());
        }

        return scanDataList;
    }


    public List<ScanData> getdraftNoWisedata( String draftnumber ) {
        List<ScanData> scanDataList = new ArrayList<>();

         String select = "Select *,rowid from "+CUSTOMER_TABLE+" where draftnumber = '" + draftnumber +"'";
        //String select = "Select * from " + CUSTOMER_TABLE + " where draftnumber = '" + draftnumber  ;
        Cursor c = sqLiteDatabase.rawQuery(select, null);

        if (c.moveToFirst()) {
            do {
                ScanData scanData = new ScanData();
                scanData.setDraftNumber(c.getString(0));
                scanData.setItemCode(c.getString(1));
                scanData.setDescription(c.getString(2));
                scanData.setQuality(c.getString(3));
                scanData.setBatchNo(c.getString(4));
                scanData.setBatchQty(c.getString(5));
                scanData.setScan_qlt(c.getString(6));
                scanDataList.add(scanData);
            } while (c.moveToNext());
        }

        return scanDataList;
    }



    public String checkValue(String s)
    {
        if(s.length()==0 || s==null)
        {
            s="";
        }
        return s;
    }


    public Cursor checkBarCodeNo1(String batchnumber1){

        String query = "Select batchquantity  from " + CUSTOMER_TABLE + " where batchnumber = " + "'" + batchnumber1 +"'";

        Cursor c = sqLiteDatabase.rawQuery(query, null);

        if(c.getCount() <= 0)
        {
            Toast.makeText(context,"Bar code no. is not available !",Toast.LENGTH_SHORT).show();
            return c;
        }
        else
        {
            Toast.makeText(context,"Bar code no. is  available !",Toast.LENGTH_SHORT).show();
            return c;
        }

    }


    public String getbatchquantity(String bactchNo)
    {
        String rv = "";
        String select = "Select batchquantity from "+CUSTOMER_TABLE+" where batchnumber ='"+ bactchNo+"'";

        Cursor c = sqLiteDatabase.rawQuery(select, null);
        if (c.moveToFirst()) {
            rv = c.getString(0);
        }
        c.close();
        return rv;
    }




    public void deleteBatchNo(String t,String s)
    {
        String where = "batchnumber=? and batchquantity=?";
        String[] whereArgs = {t,s};
        sqLiteDatabase.delete(CUSTOMER_TABLE, where, whereArgs);


        //sqLiteDatabase.delete(CUSTOMER_TABLE, "name" + "=" + id, null);
    }

    public void UpdateScanQuantity(String scanquntity1 , String batchNo, String s_draftNo)
    {
//
//        String select = " update " + CUSTOMER_TABLE + " set  scanquantity = '" + scanquntity1 + "'" +
//                " where batchnumber  ='" + batchNo + "' & draftnumber = '" + s_draftNo + "'";

        String select = " update " + CUSTOMER_TABLE + " set  scanquantity = '" + scanquntity1 + "'" +
                " where batchnumber  ='" + batchNo  + "'";

        sqLiteDatabase.execSQL(select);

    }





    public Cursor getbf()
    {
        String select = "Select * from "+CUSTOMER_TABLE;
        Cursor c = sqLiteDatabase.rawQuery(select, null);
        return c;
    }

    public Cursor getbf1()
    {
        String select = "Select rowid, * from "+CUSTOMER_TABLE;
        Cursor c = sqLiteDatabase.rawQuery(select, null);
        return c;
    }

    public Cursor getUCC(String query)
    {


        String select = query;
        Cursor c = sqLiteDatabase.rawQuery(select, null);
        return c;
    }
    public Cursor getDraftNumberWiseData(String draftnumber)
    {
        String select = "Select *,rowid from "+CUSTOMER_TABLE+" where draftnumber='"+ draftnumber+"'";
        Cursor c = sqLiteDatabase.rawQuery(select, null);
        return c;
    }

    public Cursor getAllData(String s)
    {

        String select = "Select * from "+CUSTOMER_TABLE+" where rowid='"+s+"'";
        Cursor c = sqLiteDatabase.rawQuery(select, null);
        return c;
    }



    public Cursor getFavStatus(String id,String id1, String s)
    {
        String select = "Select * from "+CUSTOMER_TABLE+" where favstatus='"+s+"' and topicname='"+id+"' and subtopicname='"+id1+"'";
        Cursor c = sqLiteDatabase.rawQuery(select, null);
        return c;
    }




    public Cursor queueAll(){
        //String[] columns = new String[]{KEY_ID, KEY_CONTENT};
        //Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns,
        //  null, null, null, null, null);
        String select = "Select * from leadinitialization";
        Cursor c = sqLiteDatabase.rawQuery(select, null);
        return c;


    }

    public class SQLiteHelper extends SQLiteOpenHelper {

        public SQLiteHelper(Context context, String name,
                            CursorFactory factory, int version) {

            super(context, name, factory, version);
            //super(context, Environment.getExternalStorageDirectory()+"/" + name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            //////System.out.println("cuaromer_table"+cuaromer_table);
            db.execSQL(cuaromer_table);
            //db.execSQL(leadcreation);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub

        }

    }


}
