package com.example.admin.gows;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

/**
 * Created by ADMIN on 12/5/2017.
 */

public class Company_edit extends Fragment {
     EditText company_name,company_address,company_place,company_postal,company_gst,company_phonenum,company_email,company_mobilenum,company_bill_prefix;
    MaterialBetterSpinner state_spin;
    private Button save,disable;
    private SQLiteDatabase db;
    String state;
    String[] code;
    String[] list;
    String enable = "1",com;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.company_edit, container, false);
        db = getActivity().openOrCreateDatabase("Master.db", Context.MODE_PRIVATE ,null);
        db.execSQL("create table if not exists Company (Code Integer primary key autoincrement,Company_name varchar,Address text,State varchar," +
                "Postal_code varchar,GST_no varchar,Phone bigint,Mobile_no bigint,Created_date date,Created_time time," +
                "Bill_prefix varchar);");
     //   user_name = view.findViewById(R.id.edit_user_name);
      //  pass_word = view.findViewById(R.id.edit_password);
        //company_select = view.findViewById(R.id.company_edit_select);
        company_name = view.findViewById(R.id.company_edit_name);
        company_address = view.findViewById(R.id.company_edit_address);
       // company_place = view.findViewById(R.id.company_edit_place);
        state_spin = view.findViewById(R.id.company_edit_state);
        company_postal = view.findViewById(R.id.company_edit_postal);
        company_gst = view.findViewById(R.id.company_edit_gst);
        company_phonenum = view.findViewById(R.id.company_edit_phone);
     //   company_email = view.findViewById(R.id.company_edit_email);
        company_mobilenum = view.findViewById(R.id.company_edit_mobile);
        company_bill_prefix = view.findViewById(R.id.company_edit_billpre);
        save = view.findViewById(R.id.edit_save);

//        InputFilter filter = new InputFilter() {
//            public CharSequence filter(CharSequence source, int start, int end,
//                                       Spanned dest, int dstart, int dend) {
//                for (int i = start; i < end; i++) {
//                    if (Character.isSpaceChar(source.charAt(i))) {
//                        return "";
//                    }
//                }
//                return null;
//            }
//        };
//        company_email.setFilters(new InputFilter[]{filter});
//
//        company_email.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                Validation.isEmailAddress(company_email, true);
//
//                //isValidEmail();
//            }
//        });
        list = getResources().getStringArray(R.array.state);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                            R.layout.spinner_items, list);
                    state_spin.setAdapter(adapter);

        state_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                state = adapterView.getItemAtPosition(i).toString();
            }
        });


//        state_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                state = parent.getItemAtPosition(position).toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


                String select = "SELECT * FROM Company ";
                Cursor c= db.rawQuery(select,null);
                while(c.moveToNext())
                {
                    db.execSQL("create table if not exists Company (Code Integer primary key autoincrement,Company_name varchar,Address text,State varchar," +
                            "Postal_code varchar,GST_no varchar,Phone bigint,Mobile_no bigint,Created_date date,Created_time time," +
                            "Bill_prefix varchar);");
                    String com_name=c.getString(1);
                    String addrs= c.getString(2);
                  //  String place=c.getString(3);
                    String state = c.getString(3);
            //        Toast.makeText(getActivity(), "Saved state :"+state, Toast.LENGTH_SHORT).show();

                    String postal_code=c.getString(4);
                    String gst_code=c.getString(5);
                    String phone=c.getString(6);
//                    String mail=c.getString(8);
                    String mob=c.getString(7);
                    String bill=c.getString(10);


                    company_name.setText(com_name);
                    company_name.setSelectAllOnFocus(true);
                    company_name.selectAll();
                    company_address.setText(addrs);
                    company_address.setSelectAllOnFocus(true);
                    company_address.selectAll();
//                    company_place.setText(place);
//                    company_place.setSelectAllOnFocus(true);
//                    company_place.selectAll();
                    company_postal.setText(postal_code);
                    company_postal.setSelectAllOnFocus(true);
                    company_postal.selectAll();
                    company_gst.setText(gst_code);
                    company_gst.setSelectAllOnFocus(true);
                    company_gst.selectAll();
                    company_phonenum.setText(phone);
                    company_phonenum.setSelectAllOnFocus(true);
                    company_phonenum.selectAll();
//                    company_email.setText(mail);
//                    company_email.setSelectAllOnFocus(true);
//                    company_email.selectAll();
                    company_mobilenum.setText(mob);
                    company_mobilenum.setSelectAllOnFocus(true);
                    company_mobilenum.selectAll();
                    company_bill_prefix.setText(bill);
                    company_bill_prefix.setSelectAllOnFocus(true);
                    company_bill_prefix.selectAll();
                    state_spin.setText(state);

//                  list = getResources().getStringArray(R.array.state);
//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
//                            R.layout.spinner_items, list);
//                    state_spin.setAdapter(adapter);
//                    for (int i = 0; i < state_spin.getCount(); i++) {
//                        if (state_spin.getItemAtPosition(i).equals(state)) {
//                            state_spin.setSelection(i);
//                            break;
//                        }
//                    }
                }
        save.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             String comp_name =  company_name.getText().toString().trim();
             String comp_add = company_address.getText().toString().trim();
//             String comp_place = company_place.getText().toString().trim();
             String post = company_postal.getText().toString().trim();
             String gst = company_gst.getText().toString().trim();
             String phonenum = company_phonenum.getText().toString().trim();
//             String email = company_email.getText().toString().trim();
             String mobile = company_mobilenum.getText().toString().trim();
             String bill = company_bill_prefix.getText().toString().trim();
             if( !comp_name.isEmpty()&& !comp_add.isEmpty()&&!post.isEmpty()&&!phonenum.isEmpty()&&!mobile.isEmpty()&&!bill.isEmpty()&&!state_spin.getText().toString().trim().isEmpty() )
             {
                 if(mobile.length()==10 && post.length()== 6) {
                         com = company_name.getText().toString().trim();
                         ContentValues values = new ContentValues();
                         values.put("Company_name", company_name.getText().toString().trim());
                         values.put("Address", company_address.getText().toString());
//                         values.put("Place", company_place.getText().toString());
                         values.put("State", state_spin.getText().toString() );
                         values.put("Postal_code", company_postal.getText().toString());
                         values.put("GST_no", company_gst.getText().toString());
                         values.put("Phone", company_phonenum.getText().toString());
//                         values.put("Email", company_email.getText().toString());
                         values.put("Mobile_no", company_mobilenum.getText().toString());
                         values.put("Bill_prefix", company_bill_prefix.getText().toString());
                         db.update("Company", values, null, null);
                    //     Toast.makeText(getActivity(), "Saved"+state_spin.getText().toString(), Toast.LENGTH_SHORT).show();

                         FragmentTransaction tx = getActivity().getSupportFragmentManager().beginTransaction();
                         tx.replace(R.id.content_frame, new Dashboard());
                         tx.commit();
                     }
                     else {
                             if(mobile.length()!=10){
                                 company_mobilenum.requestFocus();
                             }
                           else if(post.length()!= 6){
                                 company_postal.requestFocus();
                             }
                     }
             }
             else
             {
                 Toast.makeText(getActivity(), "Empty Fields Are Not Allowed", Toast.LENGTH_SHORT).show();
                 if ( company_name.getText().toString().trim().isEmpty()) {
                     company_name.requestFocus();
                 } else if (company_address.getText().toString().trim().isEmpty()) {
                     company_address.requestFocus();
                 }
//                 else if (company_place.getText().toString().trim().isEmpty()) {
//                     company_place.requestFocus();
//                 }
                 else if (state_spin.getText().toString().trim().isEmpty()) {
                     state_spin.requestFocus();
                 }
                 else if (company_postal.getText().toString().trim().isEmpty()) {
                     company_postal.requestFocus();
                 }
//                 else if (company_gst.getText().toString().trim().isEmpty()) {
//                     company_gst.requestFocus();
//                 }
                 else if (company_phonenum.getText().toString().trim().isEmpty()) {
                     company_phonenum.requestFocus();
                 }
                 else if (company_email.getText().toString().trim().isEmpty()) {
                     company_email.requestFocus();
                 }
                 else if (company_mobilenum.getText().toString().trim().isEmpty()) {
                     company_mobilenum.requestFocus();
                 }
                 else if (company_bill_prefix.getText().toString().trim().isEmpty()) {
                     company_bill_prefix.requestFocus();
                 }
             }

         }
     });
        return view;
    }
    private boolean checkValidation() {
        boolean ret = true;


        if (!Validation.isEmailAddress(company_email, true)) ret = false;


        return ret;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Profile");

    }
}
