package com.example.admin.gows;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CompanyActivity extends AppCompatActivity {
LinearLayout detail1,detail2;
Button next,create;
    private SQLiteDatabase db;
     EditText company_name,company_address,company_postal,company_gst,company_phonenum,company_mobilenum,company_bill_prefix;
    private MaterialBetterSpinner state_spin;
    private String blockCharacterSet = "~#^|$%&*!()@";
    String state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        db = getApplicationContext().openOrCreateDatabase("Master.db", Context.MODE_PRIVATE ,null);
        db.execSQL("create table if not exists Company (Code Integer primary key autoincrement,Company_name varchar,Address text,State varchar," +
                "Postal_code varchar,GST_no varchar,Phone bigint,Mobile_no bigint,Created_date date,Created_time time," +
                "Bill_prefix varchar);");


        detail1 = findViewById(R.id.detail1);
         detail2 = findViewById(R.id.detail2);
//details1
        company_name = findViewById(R.id.company_name);
        company_gst =findViewById(R.id.company_gst);
        company_bill_prefix = findViewById(R.id.company_billpre);
        next = findViewById(R.id.company_next);
   //details2
        company_address = findViewById(R.id.company_address);
        state_spin = findViewById(R.id.company_state);
        company_postal = findViewById(R.id.company_postal);
        company_phonenum = findViewById(R.id.company_phone);
        company_mobilenum = findViewById(R.id.company_mobile);
        create = findViewById(R.id.company_create);

        final String date = new SimpleDateFormat("dd-MM-yyyy").format(new java.util.Date());
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        final String time =  mdformat.format(calendar.getTime());
        String[] list = getResources().getStringArray(R.array.state);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.spinner_items, list);

        state_spin.setAdapter(adapter);
        state_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                state = parent.getItemAtPosition(position).toString();

            }
        });

         next.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String comp_name =  company_name.getText().toString().trim();
                 String gst = company_gst.getText().toString().trim();
                 String bill = company_bill_prefix.getText().toString().trim();
                 if(!comp_name.isEmpty() && !bill.isEmpty()){
                     detail1.setVisibility(View.GONE);
                     detail2.setVisibility(View.VISIBLE);
                 }
                 else{
                     Toast.makeText(getApplicationContext(),"Please enter all fields", Toast.LENGTH_SHORT).show();
                     if ( company_name.getText().toString().trim().isEmpty()) {
                         company_name.requestFocus();
                     }
                     else if (company_bill_prefix.getText().toString().trim().isEmpty()) {
                         company_bill_prefix.requestFocus();
                     }
                 }

             }
         });
         create.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String comp_add = company_address.getText().toString().trim();
                 String post = company_postal.getText().toString().trim();
                 String phonenum = company_phonenum.getText().toString().trim();
                 String mobile = company_mobilenum.getText().toString().trim();
                 if(!comp_add.isEmpty()&&!state_spin.getText().toString().trim().isEmpty()&&!post.isEmpty()&&!phonenum.isEmpty()&&!mobile.isEmpty()){
                     if(mobile.length()==10 && post.length()==6) {
                         ContentValues values = new ContentValues();
                         values.put("Company_name", company_name.getText().toString().trim());
                         values.put("Address", company_address.getText().toString());
                         values.put("State", state);
                      //   Toast.makeText(getApplicationContext(), "Saved state :"+state, Toast.LENGTH_SHORT).show();
                         values.put("Postal_code", company_postal.getText().toString());
                         values.put("GST_no", company_gst.getText().toString());
                         values.put("Phone", company_phonenum.getText().toString());
                         values.put("Mobile_no", company_mobilenum.getText().toString());
                         values.put("Created_date", date);
                         values.put("Created_time", time);
                         values.put("Bill_prefix", company_bill_prefix.getText().toString());
                         {
                             db.insert("Company", null, values);
                            // company_name.getText().clear();
                             company_address.getText().clear();
                             company_postal.getText().clear();
                          //   company_gst.getText().clear();
                             company_phonenum.getText().clear();
                             company_mobilenum.getText().clear();
                         //    company_bill_prefix.getText().clear();
                             Intent in = new Intent(CompanyActivity.this, SecondActivity.class);
                             startActivity(in);
                             finish();
                         }
                     }
                     else {

                         if(mobile.length()!=10) {
                             company_mobilenum.requestFocus();
                         }
                         else if(post.length()!=6){
                             company_postal.requestFocus();
                         }


                     }
                 }
                 else{
                     Toast.makeText(getApplicationContext(),"Please enter all fields", Toast.LENGTH_SHORT).show();
                     if (company_address.getText().toString().trim().isEmpty()) {
                         company_address.requestFocus();
                     }
                     else if (state_spin.getText().toString().trim().isEmpty()) {
                         state_spin.requestFocus();
                     }
                     else if (company_postal.getText().toString().trim().isEmpty()) {
                         company_postal.requestFocus();
                     }
                     else if (company_phonenum.getText().toString().trim().isEmpty()) {
                         company_phonenum.requestFocus();
                     }
                     else if (company_mobilenum.getText().toString().trim().isEmpty()) {
                         company_mobilenum.requestFocus();
                     }
                 }
             }
         });
    }
}
