package com.maitriinfosoft.bar_code_app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.maitriinfosoft.bar_code_app.database.DBManager;
import com.maitriinfosoft.bar_code_app.database.DatabaseHandler;
import com.maitriinfosoft.bar_code_app.database.SQLiteAdapter;
import com.maitriinfosoft.bar_code_app.services.ApiConstant;
import com.maitriinfosoft.bar_code_app.services.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.maitriinfosoft.bar_code_app.database.DatabaseHandler.BATCH_NUMBER;

public class Scanning_Activity extends AppCompatActivity {

    ImageView btnScanBarcode;
    public static TextView txtResultsHeader;

    String Location_name;
    TextView tv_locationName;
    public static final String TABLE_NAME = "SCANDATA";


    String Draft_No,str_dataBase;
    EditText et_draftNo, et_qty;
    CheckBox cb_edit;
    RecyclerView recycler_item,recycler_item1;
    Scandata_Adapter scandata_adapter,scandata_adapter1;
    ScanData scanData;
    ProgressDialog progressDialog;
    ProgressDialog progressDialog1;

    private DBManager dbManager;
    private DatabaseHandler db;

    private SQLiteDatabase database;


    final String[] from = new String[] { DatabaseHandler.ITEM_CODE, DatabaseHandler.ITEM_NAME,
            DatabaseHandler.QUANTITY, BATCH_NUMBER, DatabaseHandler.BATCH_QUANTITY };

    //final int[] to = new int[] { R.id.id, R.id.title, R.id.desc };



    private List<ScanData> mList = new ArrayList<>();

    ArrayList<String> ItemCode_Array = new ArrayList<String>();
    ArrayList<String> ItemName_Array = new ArrayList<String>();
    ArrayList<String> Quantity_Array = new ArrayList<String>();
    ArrayList<String> DraftNumber_Array = new ArrayList<String>();
    ArrayList<String> DocNum_Array = new ArrayList<String>();
    ArrayList<String> BatchNum_Array = new ArrayList<String>();
    ArrayList<String> LineNum_Array = new ArrayList<String>();
    ArrayList<String> BatchQty_Array = new ArrayList<String>();


    ArrayList<String> DOCEntry_Array = new ArrayList<String>();
    ArrayList<String> DOCNum_Array = new ArrayList<String>();



    String Quantity, ItemCode, DraftNumber, LineNum, ItemName,BatchNumber,Batch_Quantity;

    SharedPreferences sharedPreferences;
    String str_locationName, str_BarCodeNo, str_addQTY,str_selectedDraftNum;
    Button btn_add1,btn_submit,btn_delete,btn_update;
    TextView tv_Quantity;
    ArrayList<ScanData> datalist = new ArrayList<>();
    ArrayList<ScanData> datalist1 = new ArrayList<>();

    ArrayList<ScanData> datalist_new = new ArrayList<>();
    Spinner spinner_draftNo;

    SQLiteAdapter mySQLiteAdapter;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        btnScanBarcode = findViewById(R.id.btnScanBarcode);
        txtResultsHeader = findViewById(R.id.txtResultsHeader);
        tv_locationName = findViewById(R.id.tv_locationName);
        et_draftNo = findViewById(R.id.et_draftNo);
        cb_edit = findViewById(R.id.cb_edit);
        et_qty = findViewById(R.id.et_qty);
        btn_add1 = findViewById(R.id.btn_add1);
        btn_submit = findViewById(R.id.btn_submit);
        btn_delete = findViewById(R.id.btn_delete);
        btn_update = findViewById(R.id.btn_update);
        tv_Quantity = findViewById(R.id.tv_Quantity);
        spinner_draftNo = findViewById(R.id.spinner_draftNo);

        recycler_item = findViewById(R.id.recycler_item);
        recycler_item1 = findViewById(R.id.recycler_item1);

        db = new DatabaseHandler(this);



        database = db.getWritableDatabase();


        txtResultsHeader.setEnabled(false);


        dbManager = new DBManager(this);
        dbManager.open();
        //Cursor cursor = dbManager.fetch();

        sharedPreferences = getSharedPreferences("USER_CREDENTIALS", MODE_PRIVATE);
        str_locationName = sharedPreferences.getString("LOCATION_NAME", "");
        str_dataBase = sharedPreferences.getString("Databasec", "");


        tv_locationName.setText(str_locationName);


        getDraftNumber();

        spinner_draftNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {



                //str_selectedDraftNum = parent.getItemAtPosition(pos).toString();
                str_selectedDraftNum = DOCNum_Array.get(pos).toString();


                if(str_selectedDraftNum.equals("--Select--")){

                }else{


                    mySQLiteAdapter = null;
                    if (mySQLiteAdapter == null) {
                        mySQLiteAdapter = new SQLiteAdapter(Scanning_Activity.this);
                        mySQLiteAdapter.openToWrite();
                        //Toast.makeText(Scanning_Activity.this,"Data Base Open",Toast.LENGTH_LONG).show();
                    }

                    if(datalist_new.size() > 0){
                        datalist_new.clear();


                        datalist_new.addAll(mySQLiteAdapter.getdraftNoWisedata(str_selectedDraftNum));

                       // recycler_item.setAdapter(null);

                        // datalist_new.addAll(mySQLiteAdapter.allData());

                        if(datalist_new.size() > 0 ){



                            recycler_item.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                            scandata_adapter = new Scandata_Adapter( Scanning_Activity.this,datalist_new);
                            recycler_item.setAdapter(scandata_adapter);

                            scandata_adapter.notifyDataSetChanged();

                        }else{

                            getDraftNumber1();

                        }


                    }else{



                        datalist_new.addAll(mySQLiteAdapter.getdraftNoWisedata(str_selectedDraftNum));

                       // recycler_item.setAdapter(null);

                        // datalist_new.addAll(mySQLiteAdapter.allData());

                        if(datalist_new.size() > 0 ){



                            recycler_item.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                            scandata_adapter = new Scandata_Adapter( Scanning_Activity.this,datalist_new);
                            recycler_item.setAdapter(scandata_adapter);

                            scandata_adapter.notifyDataSetChanged();

                        }else{

                            getDraftNumber1();

                        }


                    }





                }










            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mySQLiteAdapter = null;
                if (mySQLiteAdapter == null) {
                    mySQLiteAdapter = new SQLiteAdapter(Scanning_Activity.this);
                    mySQLiteAdapter.openToWrite();
                    //Toast.makeText(Scanning_Activity.this,"Data Base Open",Toast.LENGTH_LONG).show();
                }

                String new_batchNumber = txtResultsHeader.getText().toString().trim();
                String new_batchQuantity = tv_Quantity.getText().toString().trim();

                mySQLiteAdapter.deleteBatchNo(new_batchNumber,new_batchQuantity);
                mySQLiteAdapter.close();


                txtResultsHeader.setText("");
                tv_Quantity.setText("");




            }
        });





        btn_add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                 if(TextUtils.isEmpty(txtResultsHeader.getText().toString().trim())){
                    txtResultsHeader.setError("Please scan product");
                    txtResultsHeader.requestFocus();
                    return;
                }

                else {

                     String s_draftNumber = spinner_draftNo.getSelectedItem().toString();
                     String s_batchNo = txtResultsHeader.getText().toString().trim();
                     String s_scanQty = tv_Quantity.getText().toString().trim();

                     mySQLiteAdapter = null;
                     if (mySQLiteAdapter == null) {
                         mySQLiteAdapter = new SQLiteAdapter(Scanning_Activity.this);
                         mySQLiteAdapter.openToWrite();

                     }


                     mySQLiteAdapter.UpdateScanQuantity(s_scanQty, s_batchNo, s_draftNumber);

                     if (datalist_new.size() > 0) {
                         datalist_new.clear();

                         datalist_new.addAll(mySQLiteAdapter.getdraftNoWisedata(str_selectedDraftNum));

                         if (datalist_new.size() > 0) {

                             recycler_item.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                             scandata_adapter = new Scandata_Adapter(Scanning_Activity.this, datalist_new);
                             recycler_item.setAdapter(scandata_adapter);

                             scandata_adapter.notifyDataSetChanged();


                         }
                     }
                 }






            }
        });

        if (ActivityCompat.checkSelfPermission(Scanning_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) Scanning_Activity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            return;


        }




            btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgressDialog();
                updateBatch();
            }
        });



        cb_edit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                               @Override
                                               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

          txtResultsHeader.setEnabled(true);

           txtResultsHeader.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

              @Override
               public void onTextChanged(CharSequence s, int start, int before, int count) {


//                  str_BarCodeNo = txtResultsHeader.getText().toString().trim();
//                  str_addQTY = tv_Quantity.getText().toString().trim();

                  if (s.length() == 0) {
                     // Toast.makeText(getApplicationContext(), "Scan BarCode", Toast.LENGTH_LONG).show();
                  } else {


                      mySQLiteAdapter = null;
                      if (mySQLiteAdapter == null) {
                          mySQLiteAdapter = new SQLiteAdapter(Scanning_Activity.this);
                          mySQLiteAdapter.openToWrite();

                      }


                           //mySQLiteAdapter.checkBarCodeNo1(s.toString());


                          String batchQty =  mySQLiteAdapter.getbatchquantity(s.toString());

                          tv_Quantity.setText(batchQty);




                      mySQLiteAdapter.close();


                      if (BatchNum_Array.contains(str_BarCodeNo)) {
                          Toast.makeText(getApplicationContext(), "Bar code no. is available !", Toast.LENGTH_SHORT).show();

                          int index = (int) BatchNum_Array.indexOf(str_BarCodeNo);

                          Quantity = (Quantity_Array.get(index));

                          ItemCode = (ItemCode_Array.get(index));
                          Batch_Quantity = (BatchQty_Array.get(index));
                          tv_Quantity.setText(Batch_Quantity);

                          ItemName = (ItemName_Array.get(index));
                          BatchNumber = (BatchNum_Array.get(index));
                          DraftNumber = (DraftNumber_Array.get(index));
                          LineNum = LineNum_Array.get(index);

                          str_addQTY = tv_Quantity.getText().toString().trim();


                      }



                  }
              }
                            @Override public void afterTextChanged(Editable s) {
            }

           });
         }
        }
        );



        sharedPreferences = getSharedPreferences("USER_CREDENTIALS", MODE_PRIVATE);
        Location_name = sharedPreferences.getString("LOCATION_NAME", "");

        tv_locationName.setText(Location_name);


        btnScanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Scanning_Activity.this, Scan_Product_Activity.class);
                startActivity(intent);
            }
        });
    }







    @Override
    protected void onStart() {
        super.onStart();


        if (!TextUtils.isEmpty(txtResultsHeader.getText().toString().trim())) {
            str_BarCodeNo = txtResultsHeader.getText().toString().trim();


            if (BatchNum_Array != null) {

                if (BatchNum_Array.contains(str_BarCodeNo)) {
                    Toast.makeText(getApplicationContext(), "Bar code no. is available !", Toast.LENGTH_SHORT).show();


                } else {
                    // Toast.makeText(this, "Bar code no. is not available", Toast.LENGTH_SHORT).show();
                }

            } else {

                Toast.makeText(this, "Data not found", Toast.LENGTH_SHORT).show();
            }
        }


    }


    private void getDraftNumber1() {

        progressDialog1 = new ProgressDialog(this);
        progressDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog1.setMessage("Loading...");
        progressDialog1.setCanceledOnTouchOutside(false);
        progressDialog1.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiConstant.Draft_data1,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        progressDialog1.dismiss();
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            boolean error = jsonObject.getBoolean("Error");

                            if (error) {

                                 progressDialog1.dismiss();
                                //Toast.makeText(Scanning_Activity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();

                            } else {

                                progressDialog1.dismiss();

//                                if (datalist.size() > 0) {
//                                    datalist.clear();
//                                }

                                Toast.makeText(Scanning_Activity.this, "Draft No. Found !!!", Toast.LENGTH_SHORT).show();

                                JSONArray jsonArray = jsonObject.getJSONArray("draftdata");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                                    DraftNumber_Array.add(jsonObject1.getString("DocEntry"));
                                    DocNum_Array.add(jsonObject1.getString("DocNum"));
                                    ItemName_Array.add(jsonObject1.getString("ItemName"));
                                    ItemCode_Array.add(jsonObject1.getString("ItemCode"));
                                    Quantity_Array.add(jsonObject1.getString("Quantity"));
                                    BatchNum_Array.add(jsonObject1.getString("BatchNum"));
                                    LineNum_Array.add(jsonObject1.getString("LineNum"));
                                    BatchQty_Array.add(jsonObject1.getString("BatchQty"));

                                    String str_DraftNumber = jsonObject1.getString("DocEntry");
                                    String str_ItemCode = jsonObject1.getString("ItemCode");
                                    String str_ItemName = jsonObject1.getString("ItemName");
                                    String str_Quality = jsonObject1.getString("Quantity");
                                    String str_BatchNumber = jsonObject1.getString("BatchNum");
                                    String str_BatchQuantity = jsonObject1.getString("BatchQty");
                                    String str_ScanQuantity = null;


                                    scanData= new ScanData(str_DraftNumber,str_ItemCode,str_ItemName,str_Quality,str_BatchNumber,str_BatchQuantity);

                                    mySQLiteAdapter = null;
                                    if (mySQLiteAdapter == null) {
                                        mySQLiteAdapter = new SQLiteAdapter(Scanning_Activity.this);
                                        mySQLiteAdapter.openToWrite();
                                        //Toast.makeText(Scanning_Activity.this,"Data Base Open",Toast.LENGTH_LONG).show();
                                    }

                                   // mySQLiteAdapter.checkValue(str_addQTY);

                                    String args[] = {str_DraftNumber,str_ItemCode,str_ItemName,str_Quality,str_BatchNumber,str_BatchQuantity,str_ScanQuantity};


                                    mySQLiteAdapter.insertData(args);

                                    mySQLiteAdapter.close();

                                    datalist.add(scanData);

                                    sharedPreferences.edit().putString("jsonArray", jsonArray.toString()).apply();
                                    sharedPreferences.edit().putString("ItemName_Array", ItemName_Array.toString()).apply();
                                    sharedPreferences.edit().putString("ItemCode_Array", ItemCode_Array.toString()).apply();
                                    sharedPreferences.edit().putString("Quantity_Array", Quantity_Array.toString()).apply();
                                    sharedPreferences.edit().putString("DocEntry_Array", Quantity_Array.toString()).apply();
                                    sharedPreferences.edit().putString("DocNum_Array", DocNum_Array.toString()).apply();


                                }
                                recycler_item.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                                scandata_adapter = new Scandata_Adapter( Scanning_Activity.this,datalist);
                                recycler_item.setAdapter(scandata_adapter);

                                scandata_adapter.notifyDataSetChanged();

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog1.dismiss();
                            Toast.makeText(Scanning_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog1.dismiss();
                String mesaage = null;
                if (error instanceof NetworkError) {
                    Toast.makeText(Scanning_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    //mesaage = "Cannot connect to Internet. Please check your connection!";
                } else if (error instanceof ServerError) {
                    Toast.makeText(Scanning_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    //  mesaage = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {

                    Toast.makeText(Scanning_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    // mesaage = "Cannot connect to Internet. Please check your connection!";

                } else if (error instanceof NoConnectionError) {

                    // Toast.makeText(FetchRoom_Activity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
                    mesaage = "Cannot connect to Internet. Please check your connection!";
                    Toast.makeText(Scanning_Activity.this, mesaage, Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(Scanning_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    // mesaage = "Connection TimeOut! Please check your internet connection.";
                } else if (error instanceof ParseError) {
                    Toast.makeText(Scanning_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    // mesaage = "Indicates that the server response could not be parsed.";
                } else {
                    Toast.makeText(Scanning_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    // mesaage = "Something went wrong. Try later";
                }

                Toast.makeText(Scanning_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("DocNumber", str_selectedDraftNum);
                params.put("databasec", str_dataBase);
                return params;
            }

        };
        int MY_SOCKET_TIMEOUT_MS = 30000;
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(stringRequest);


    }


    private void getDraftNumber() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiConstant.Draft_data,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        //progressDialog.dismiss();
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            boolean error = jsonObject.getBoolean("Error");

                            if (error) {

                                // progressDialog.dismiss();
                                //Toast.makeText(Scanning_Activity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();

                            } else {

                                //Toast.makeText(Scanning_Activity.this, "Draft No. Found !!!", Toast.LENGTH_SHORT).show();

                                JSONArray jsonArray = jsonObject.getJSONArray("draftdata");

                                DOCNum_Array.add("--Select--");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    DOCEntry_Array.add(jsonObject1.getString("DocEntry"));
                                    DOCNum_Array.add(jsonObject1.getString("DocNum"));




                                }

                                ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<>(Scanning_Activity.this,
                                        R.layout.spinner_layout, R.id.tv_spinnerText, DOCNum_Array);
                                spinnerArrayAdapter1.setDropDownViewResource(R.layout.spinner_layout);
                                spinner_draftNo.setAdapter(spinnerArrayAdapter1);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            // progressDialog.dismiss();
                            Toast.makeText(Scanning_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //progressDialog.dismiss();
                String mesaage = null;
                if (error instanceof NetworkError) {
                    Toast.makeText(Scanning_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    //mesaage = "Cannot connect to Internet. Please check your connection!";
                } else if (error instanceof ServerError) {
                    Toast.makeText(Scanning_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    //  mesaage = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {

                    Toast.makeText(Scanning_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    // mesaage = "Cannot connect to Internet. Please check your connection!";

                } else if (error instanceof NoConnectionError) {

                    // Toast.makeText(FetchRoom_Activity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
                    mesaage = "Cannot connect to Internet. Please check your connection!";
                    Toast.makeText(Scanning_Activity.this, mesaage, Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(Scanning_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    // mesaage = "Connection TimeOut! Please check your internet connection.";
                } else if (error instanceof ParseError) {
                    Toast.makeText(Scanning_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    // mesaage = "Indicates that the server response could not be parsed.";
                } else {
                    Toast.makeText(Scanning_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    // mesaage = "Something went wrong. Try later";
                }

                Toast.makeText(Scanning_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("databasec", str_dataBase);
                return params;
            }

        };
        int MY_SOCKET_TIMEOUT_MS = 30000;
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(stringRequest);


    }


    private void updateBatch() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiConstant.Update_Batch,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            boolean error = jsonObject.getBoolean("Error");

                            if (error) {
                                progressDialog.dismiss();
                                Toast.makeText(Scanning_Activity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();

                            } else {
                                  progressDialog.dismiss();
                               // recycler_item.setAdapter(null);


                                Toast.makeText(Scanning_Activity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                                et_qty.setText("");

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(Scanning_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                String mesaage = null;
                if (error instanceof NetworkError) {
                    Toast.makeText(Scanning_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    //mesaage = "Cannot connect to Internet. Please check your connection!";
                } else if (error instanceof ServerError) {
                    Toast.makeText(Scanning_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    //  mesaage = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {

                    Toast.makeText(Scanning_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    // mesaage = "Cannot connect to Internet. Please check your connection!";

                } else if (error instanceof NoConnectionError) {

                    // Toast.makeText(FetchRoom_Activity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
                    mesaage = "Cannot connect to Internet. Please check your connection!";
                    Toast.makeText(Scanning_Activity.this, mesaage, Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(Scanning_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    // mesaage = "Connection TimeOut! Please check your internet connection.";
                } else if (error instanceof ParseError) {
                    Toast.makeText(Scanning_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    // mesaage = "Indicates that the server response could not be parsed.";
                } else {
                    Toast.makeText(Scanning_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    // mesaage = "Something went wrong. Try later";
                }

                Toast.makeText(Scanning_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Draftkey", DraftNumber);
                params.put("linenumber", LineNum);
                params.put("iteamcode", ItemCode);
                params.put("qty", Quantity);
                params.put("batchnumber",BatchNumber );
                params.put("bqty", str_addQTY);
                return params;
            }

        };
        int MY_SOCKET_TIMEOUT_MS = 30000;
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(stringRequest);


    }


    private void showProgressDialog() {
        progressDialog = new ProgressDialog( this,R.style.CustomDialog);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait.");
        progressDialog.show();
    }

}



