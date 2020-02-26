package com.maitriinfosoft.bar_code_app;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.maitriinfosoft.bar_code_app.services.ApiConstant;
import com.maitriinfosoft.bar_code_app.services.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 111;
    Button btn_Login;
    private int MY_PERMISSIONS_REQUESTS = 111;

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;

    ProgressDialog progressDialog;

    EditText et_username,et_password;

    String str_username,str_password,str_databaseName;
    SharedPreferences sharedPreferences;
    Spinner spinner_databaseList;


    ArrayList<String> databaseList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_Login = findViewById(R.id.btn_Login);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        spinner_databaseList = findViewById(R.id.spinner_databaseList);

        sharedPreferences=getSharedPreferences("USER_CREDENTIALS",MODE_PRIVATE);


        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading Database...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        
        getdatabaseList();
        
        checkPermission(Manifest.permission.CAMERA,
                CAMERA_PERMISSION_CODE);



        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_username = et_username.getText().toString();
                str_password = et_password.getText().toString().trim();
                str_databaseName = spinner_databaseList.getSelectedItem().toString();

                if(TextUtils.isEmpty(str_username)){
                    et_username.setError("Please enter Username");
                    et_username.requestFocus();
                    return;
                }
                else if(TextUtils.isEmpty(str_password)){
                    et_password.setError("Please enter password");
                    et_password.requestFocus();
                    return;
                }

                else {

                    showProgressDialog();
                    getLogin();
                }




            }
        });
    }

    private void getdatabaseList() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiConstant.Fetch_DB,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try{

                            JSONObject jsonObject = new JSONObject(response);

                            boolean error = jsonObject.getBoolean("Error");

                            if (error) {

                                 progressDialog.dismiss();
                               Toast.makeText(MainActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();

                            }else{


                                if (databaseList.size() > 0) {
                                    databaseList.clear();
                                }


                                JSONArray jsonArray = jsonObject.getJSONArray("draftdata");
                                databaseList.add("--Select--");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    databaseList.add(jsonObject1.getString("U_DBName"));

                                }

                            }


                            ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<>(MainActivity.this,
                                    R.layout.spinner_layout, R.id.tv_spinnerText, databaseList);
                            spinnerArrayAdapter1.setDropDownViewResource(R.layout.spinner_layout);
                            spinner_databaseList.setAdapter(spinnerArrayAdapter1);


                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                String message = null;
                if (error instanceof NetworkError) {
                    Toast.makeText(MainActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {

                    Toast.makeText(MainActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet. Please check your connection!";
                    Toast.makeText(MainActivity.this,message, Toast.LENGTH_SHORT).show();

                } else if (error instanceof TimeoutError) {
                    Toast.makeText(MainActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

                } else if (error instanceof ParseError) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(MainActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });

        int MY_SOCKET_TIMEOUT_MS = 30000;
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(stringRequest);


    }







    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] { permission },
                    requestCode);
        }
        else {
            Toast.makeText(MainActivity.this,
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this,R.style.CustomDialog);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Logging in. Please wait.");
        progressDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(MainActivity.this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(MainActivity.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }




    private void getLogin() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiConstant.LOGIN,
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
                                Toast.makeText(MainActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();

                                String EmpID = jsonObject.getString("EmpID");
                                String Location_Name = jsonObject.getString("Name");
                                String U_User = jsonObject.getString("U_User");
                                String Databasec = jsonObject.getString("Databasec");

                                sharedPreferences.edit().putString("EmpID",EmpID).apply();
                                sharedPreferences.edit().putString("LOCATION_NAME",Location_Name).apply();
                                sharedPreferences.edit().putString("U_User",U_User).apply();
                                sharedPreferences.edit().putString("Databasec",Databasec).apply();


                                Intent intent = new Intent(MainActivity.this, Menu_Activity.class);
                                startActivity(intent);
                                finish();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                String mesaage = null;
                if (error instanceof NetworkError) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    //mesaage = "Cannot connect to Internet. Please check your connection!";
                } else if (error instanceof ServerError) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    //  mesaage = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {

                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    // mesaage = "Cannot connect to Internet. Please check your connection!";

                } else if (error instanceof NoConnectionError) {

                    // Toast.makeText(FetchRoom_Activity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
                    mesaage = "Cannot connect to Internet. Please check your connection!";
                    Toast.makeText(MainActivity.this, mesaage, Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    // mesaage = "Connection TimeOut! Please check your internet connection.";
                } else if (error instanceof ParseError) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    // mesaage = "Indicates that the server response could not be parsed.";
                } else {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    // mesaage = "Something went wrong. Try later";
                }

                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", str_username);
                params.put("password", str_password);
                params.put("databasec", str_databaseName);
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

}
