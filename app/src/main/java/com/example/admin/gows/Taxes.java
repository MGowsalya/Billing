package com.example.admin.gows;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by ADMIN on 1/17/2018.
 */

public class Taxes extends Fragment {
    @Nullable

    List<String> na = new ArrayList<String>() ;
    List<String> pr = new ArrayList<String>() ;
    //    SQLiteDatabase db;
    EditText name,percent;
    Button add;//edit;
    SQLiteDatabase db;
    String names = "tax",concat;

    private static final String JSON_URL = "http://192.168.0.112/projects/samp/dummy.php";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.taxes, container, false);
        db = getActivity().openOrCreateDatabase("Master.db", Context.MODE_PRIVATE, null);
        db.execSQL("create table if not exists Taxes(Name text,Percentage varchar)");
        name = view.findViewById(R.id.tax_name);
        percent = view.findViewById(R.id.tax_percent);
        add = view.findViewById(R.id.tax_add);
        insert();

        // edit = view.findViewById(R.id.tax_button_edit);
//        listView_name = view.findViewById(R.id.listview);
//        listView_percent = view.findViewById(R.id.listview2);
        //  Linear = view.findViewById(R.id.linear);
        // imageView = view.findViewById(R.id.imageView);

//          edit.setOnClickListener(new View.OnClickListener() {
//              @Override
//              public void onClick(View view) {
//                  Fragment fragment = new Tax_edit();
//                  FragmentTransaction ft = getFragmentManager().beginTransaction();
//                  ft.replace(R.id.content_frame, fragment);
//                  //      ft.disallowAddToBackStack(getString(R.layout.bill_edit));
//                  //    ft.addToBackStack(getString(R.layout.bill));
//                  ft.addToBackStack(null);
//                  ft.commit();
//
//              }
//          });

        percent.setFilters(new InputFilter[] {
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 3, afterDecimal = 2;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = percent.getText() + source.toString();

                        if (temp.equals(".")) {
                            return "0.";
                        }
                        else if (temp.toString().indexOf(".") == -1) {
                            // no decimal point placed yet
                            if (temp.length() > beforeDecimal) {
                                return "";
                            }
                        } else {
                            temp = temp.substring(temp.indexOf(".") + 1);
                            if (temp.length() > afterDecimal) {
                                return "";
                            }
                        }

                        return super.filter(source, start, end, dest, dstart, dend);
                    }
                }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Default_values df = new Default_values();
//                df.insert_tax();
//                String table = "Item";
//                db.execSQL("drop table if exists "+table);
//                Toast.makeText(getContext(), "deleted!", Toast.LENGTH_SHORT).show();

//                File sd = Environment.getExternalStorageDirectory();
//                File data = Environment.getDataDirectory();
//                FileChannel source = null;
//                FileChannel destination = null;
//                String currentDBPath = "/data/" + "com.example.admin.gows" + "/databases/" + "Master.db";
//                String backupDBPath = "BillDemo.db";
//                File currentDB = new File(data, currentDBPath);
//                // File currentDB1 = new File(data,currentDBPath);
//                // File currentDB! = new File(data,currentDBPath);
//                File backupDB = new File(sd, backupDBPath);
//                try {
//                    source = new FileInputStream(currentDB).getChannel();
//                    destination = new FileOutputStream(backupDB).getChannel();
//                    destination.transferFrom(source, 0, source.size());
//                    source.close();
//                    destination.close();
//                    Toast.makeText(getContext(), "DB Exported!", Toast.LENGTH_LONG).show();
//                    } catch (IOException e) {
//                    e.printStackTrace();
//                    }

                String nam = name.getText().toString();
                String per = percent.getText().toString();
                //  String pp = 0;
                //  int pp= Integer.parseInt(percent.getText().toString());
                String[] n = nam.split("-");
                String[] p = per.split("-");
                if (!nam.isEmpty() && !per.isEmpty()) {
                    ContentValues cv = new ContentValues();
                    cv.put("Name", nam);
                    cv.put("Percentage", per);
                    Float pp = Float.valueOf(per);// percent.getText();
                    if(pp<= 100.0f) {
                        if (rowIdExists(name.getText().toString().trim())) {
                            db.insert("Taxes", null, cv);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Tax " + "" + nam + " has been saved");

                            //  builder.setMessage("saved");
                            builder.setPositiveButton("ok", null);
                            builder.show();
                            name.getText().clear();
                            percent.getText().clear();
                        } else {
                            Toast.makeText(getContext(), "Tax Name Already Used", Toast.LENGTH_SHORT).show();
                        }}
                    else {
                        Toast.makeText(getContext(), "Percentage should be between 1 to 100..", Toast.LENGTH_SHORT).show();

                    }
                    //   GetValues();
                }
                else

                {
                    Toast.makeText(getContext(), "Empty values not allowed", Toast.LENGTH_SHORT).show();
                }
            }

        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    public boolean rowIdExists(String name) {
        String select = "select Name from Taxes ";
        Cursor cursor = db.rawQuery(select, null);
        List<String> labels = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(0);
                labels.add(var);
                // Toast.makeText(getContext(),""+labels.get(1),Toast.LENGTH_SHORT).show();

            } while (cursor.moveToNext());
        } boolean allMatch = true;
        for (String string : labels) {
            if (string.equalsIgnoreCase(name)) {
                allMatch = false;
                break;
            }
        }
        return allMatch;
    }

    //    private void GetValues() {
//        RequestQueue queue = Volley.newRequestQueue(getContext().getApplicationContext());
//        Log.e("URL", JSON_URL);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new com.android.volley.Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    Log.e("Json_Response", jsonObject.toString());
//                    Toast.makeText(getContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new com.android.volley.Response.ErrorListener()
//
//        {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getContext().getApplicationContext(), "That didn't work :(", Toast.LENGTH_SHORT).show();
//            }
//        })
//
//        {
//            //adding parameters to the request
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//
//                params.put("Name", String.valueOf(name));
//                params.put("Percentage", percent.getText().toString());
//                Log.e("Params", params.toString());
//                return params;
//            }
//        };
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest);
//    }
    private void insert(){

        for (int n = 0; n < 5; n++) {
            concat = names + n;
            //   Toast.makeText(getApplicationContext(), "name:" + concat, Toast.LENGTH_SHORT).show();
            int percent = 2;
            //    Toast.makeText(getApplicationContext(), "percent:" + percent, Toast.LENGTH_SHORT).show();
            ContentValues cv = new ContentValues();
            cv.put("Name", concat);
            cv.put("Percentage", percent);
            if(rowIdExists(concat)) {
                db.insert("Taxes", null, cv);
            }
            else {
                //  Toast.makeText(getContext(), "Tax Name Already Used", Toast.LENGTH_SHORT).show();
            }
        }


    }

}
