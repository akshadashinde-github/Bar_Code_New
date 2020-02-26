package com.maitriinfosoft.bar_code_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Menu_Activity extends AppCompatActivity {

    Spinner spinner_ModuleType;
    Button btn_boxScanning,btn_select;
    TextView tv_Path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        spinner_ModuleType = findViewById(R.id.spinner_ModuleType);
        btn_boxScanning = findViewById(R.id.btn_boxScanning);
        btn_select = findViewById(R.id.btn_select);
        tv_Path = findViewById(R.id.tv_Path);

        final List<String> list_ornaments= new ArrayList<String>();

        list_ornaments.add("--Select--");
        list_ornaments.add("batch scan");
//        list_ornaments.add("Piking Scan");
//        list_ornaments.add("Receipt Scan");
//        list_ornaments.add("Customer Scan");

        ArrayAdapter<String> adapter_ornaments = new ArrayAdapter<String>(this,
                R.layout.spinner_layout,R.id.tv_spinnerText, list_ornaments);
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_ModuleType.setAdapter(adapter_ornaments);






        btn_boxScanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu_Activity.this, Scanning_Activity.class);
                startActivity(intent);

            }
        });

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 7);

            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 7:

                if (resultCode == RESULT_OK) {

                    String PathHolder = data.getData().getPath();
                    tv_Path.setText(PathHolder);
                    Toast.makeText(Menu_Activity.this, PathHolder, Toast.LENGTH_LONG).show();

                }
                break;

        }
    }

}
