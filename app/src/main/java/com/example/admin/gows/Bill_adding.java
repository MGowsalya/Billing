package com.example.admin.gows;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ADMIN on 3/12/2018.
 */

public class Bill_adding extends Fragment {
    Spinner bill_category, bill_item;
    TextView qty;
    ImageButton plus, minus;
    Button add;
    int item_code, serial_num;
    int total_tax;
    int count = 1;
    int bl, bill;
    SQLiteDatabase db;
    float rate, rt, tax, tax_price, total;
    int enable = 1;
    String[] filter;
    Bill_adding.TextClicked mCallback;
    String fill, bill_cat_code;
    public static int billnumber;
    String date, time, f_t, f_tot, amtt, qt, product;

    private static final String JSON_URL = "http://192.168.0.112/projects/samp/dummy.php";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bill_add, container, false);
        db = getActivity().openOrCreateDatabase("Master.db", Context.MODE_PRIVATE, null);

        db.execSQL("create table if not exists Billing(ID integer primary key autoincrement,Bill_no  bigint(11),bdate date,pcode int," +
                "Product Varchar,Rate float,Tax int,Qty int,Amount float,Total float,Created_date Date,Created_time time,Enable int)");

        db.execSQL("create table if not exists Category (Category_Code Integer primary key autoincrement ,Name Varchar,Created_date Date,Created_time Time,Enable int)");


        date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        time = mdformat.format(calendar.getTime());


        bill_category = view.findViewById(R.id.bill_category);
        bill_item = view.findViewById(R.id.bill_item);
        qty = view.findViewById(R.id.qty);
        plus = view.findViewById(R.id.plus);
        minus = view.findViewById(R.id.minus);
        add = view.findViewById(R.id.bill_add);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                qty.setText("" + count);
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                if (count <= 1) {
                    count = 1;
                }
                qty.setText("" + count);
            }
        });


        List<String> lables = getAllLabels();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_items, lables);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.spinner_items);
        // attaching data adapter to spinner
        bill_category.setAdapter(dataAdapter);

        bill_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fill = parent.getItemAtPosition(position).toString();
                // filter = fil.split("-");
                String select = "SELECT  Category_Code FROM Category where  Name='" + fill + "'";
                Cursor cursor1 = db.rawQuery(select, null);
                if (cursor1.moveToFirst()) {
                    do {
                        //   category_code = cursor.getString(0);
                        bill_cat_code = cursor1.getString(0);//+" - "+cursor.getString(1);
                        //    Toast.makeText(getActivity(), "cc"+c_code, Toast.LENGTH_SHORT).show();
                    } while (cursor1.moveToNext());
                }
                List<String> type = getItem();
                ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_items, type);
                // Drop down layout style - list view with radio button
                dataAdapter1.setDropDownViewResource(R.layout.spinner_items);
                // attaching data adapter to spinner
                bill_item.setAdapter(dataAdapter1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        bill_item.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String it = bill_item.getSelectedItem().toString();
                String selectt = "SELECT Rate FROM Item where Item_Name = '" + it + "'";
                Cursor c = db.rawQuery(selectt, null);
                while (c.moveToNext()) {
                    String r = c.getString(0);
                    rt = Float.valueOf(r);

                    //   Toast.makeText(getActivity(), "rate_added: "+r, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getbill();
                Log.e("Date", date);
                Log.e("Time", time);
                if (bill_item.getSelectedItem() != null) {
                    cc();
                    billno_calc();
                    qt = qty.getText().toString();
                    float amt = rt * Integer.parseInt(qt);
                    product = bill_item.getSelectedItem().toString();
                    String s = "SELECT Item_Code,Tax_Price FROM Item where Item_Name = '" + product + "'";
                    Cursor cu = db.rawQuery(s, null);
                    while (cu.moveToNext()) {
                        item_code = cu.getInt(0);
                        tax_price = cu.getFloat(1);

                    }

//                 String[] billid = bill.split("-");
//                 String b = billid[1].trim();
                    ContentValues values = new ContentValues();
                    //   Toast.makeText(getActivity(), "product:"+billnumber, Toast.LENGTH_SHORT).show();
                    values.put("Bill_no", billnumber);
                    values.put("bdate", date);
                    values.put("pcode", item_code);
                    values.put("Product", product);
                    String rate = String.format("%.2f", rt);
                    values.put("Rate", rate);
//                     tax = rt * total_tax/100;
//                    values.put("Tax",tax);
//                    values.put("Total",total);
                    values.put("Created_date", date);
                    values.put("Created_time", time);
                    values.put("Enable", enable);

                    //rowIdExists(bill_item.getSelectedItem().toString());
                    //   Toast.makeText(getActivity(), "product:"+b, Toast.LENGTH_SHORT).show();
                    if (rowIdExists(product)) {
                        int var1 = 0;
                        String select = "SELECT Qty,Bill_no FROM Billing where Product = '" + product + "'";
                        Cursor c = db.rawQuery(select, null);
                        while (c.moveToNext()) {
                            var1 = c.getInt(0);
                            bl = c.getInt(1);
                         }
                        int val = Integer.parseInt(qt);
                        int value = var1 + val;
                        float amount = rt * value;
                        values.put("Qty", value);
                        amtt = String.format("%.2f", amount);
                        values.put("Amount", amtt);
                        tax = tax_price * value;
                     //   Toast.makeText(getActivity(), "tp:"+tax_price, Toast.LENGTH_SHORT).show();
                        f_t = String.format("%.2f", tax);
                        values.put("Tax", f_t);
                        total = amount + tax;
                        f_tot = String.format("%.2f", total);
                        values.put("Total", f_tot);
                        db.update("Billing", values, "Product ='" + product + "'", null);
                          mCallback.updatetextview(product);
                    } else {
                        values.put("Qty", qt);
                        amtt = String.format("%.2f", amt);
                        values.put("Amount", amtt);
                        int val = Integer.parseInt(qt);
                        tax = tax_price * val;
                        f_t = String.format("%.2f", tax);
                        values.put("Tax", f_t);
                        total = amt + tax;
                        f_tot = String.format("%.2f", total);
                        values.put("Total", f_tot);
                        db.insert("Billing", null, values);
                        mCallback.inserttextview();
                    }
                    count = 1;
                    qty.setText("" + count);
                    GetValues();
                } else {
                    Toast.makeText(getActivity(), "Some Fields Are Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void GetValues() {
        RequestQueue queue = Volley.newRequestQueue(getContext().getApplicationContext());
        Log.e("URL", JSON_URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("Json_Response", jsonObject.toString());
                    Toast.makeText(getContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext().getApplicationContext(), "That didn't work :(", Toast.LENGTH_SHORT).show();
            }
        })

        {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("billno", String.valueOf(billnumber));
                params.put("bdate", date);
                params.put("pcode", String.valueOf(item_code));
                params.put("product", product);
                params.put("rate", String.valueOf(rt));
                params.put("tax", f_t);
                params.put("qty", qt);
                params.put("amount", amtt);
                params.put("total", f_tot);
                params.put("created_date", date);
                params.put("created_time", time);
                params.put("enable", String.valueOf(enable));

                Log.e("Params", params.toString());
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    public boolean rowIdExists(String id) {
        String select = "select * from Billing where Product ='" + id + "'";
        Cursor cursor = db.rawQuery(select, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public List<String> getAllLabels() {
        List<String> labels = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT Category_Code,Name  FROM Category where Enable ='" + enable + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(1);//+" - "+cursor.getString(1);
                labels.add(var);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        // returning lables
        return labels;
    }

    public List<String> getItem() {
        List<String> labels = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT Item_Code,Item_Name,Rate FROM Item  where Category_Code = '" + bill_cat_code + "' And  Enable ='" + enable + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(1);//+" - "+cursor.getString(1);
                labels.add(var);
                rate = cursor.getFloat(2);
                //     Toast.makeText(getActivity(), "rate: "+rate, Toast.LENGTH_SHORT).show();
                //rate = Integer.parseInt(var1);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return labels;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (Bill_adding.TextClicked) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement TextClicked");
        }
    }

    public interface TextClicked {
        public void inserttextview();

        public void updatetextview(String product);

    }

    @Override
    public void onDetach() {
        mCallback = null; // => avoid leaking, thanks @Deepscorn
        super.onDetach();
    }

    //    void getbill()
//    {
//        String select = "SELECT * from Billing";
//        Cursor cursor = db.rawQuery(select,null);
//        if(cursor.moveToFirst())
//        {
//            do{
//                bill = cursor.getInt(1);
//
//            }while (cursor.moveToNext());
//            cursor.close();
//            Toast.makeText(getContext(),"billg:"+bill,Toast.LENGTH_SHORT).show();
//        }
//        if(cursor.getCount()==0)
//        {
//            billnumber = 101;
//           // BillList.billnum_editext.setText("101");
//        }
////        else{
////            billnumber = bill;
////        }
//
////        else {
////            if(bill>=100)
////            {
////                billnumber = bill;
////                Toast.makeText(getContext(),"bill:if:"+billnumber,Toast.LENGTH_SHORT).show();
////            }
////            else {
////                billnumber = 101;
////                Toast.makeText(getContext(),"bill:else:"+billnumber,Toast.LENGTH_SHORT).show();
////            }
////        }
////        if(cursor.getCount()>=10)
////        {
////            billnumber = billnumber + 1;
////        }
//        Toast.makeText(getContext(),"bill:"+cursor.getCount(),Toast.LENGTH_SHORT).show();
//    }
//    void bi()
//    {
//        String select = "Select * from Billing where Bill_no ='"+bill+"'";
//        Cursor cursor = db.rawQuery(select,null);
//        if(cursor.moveToFirst())
//        {
//            do{
//                int b = cursor.getInt(0);
//            }while (cursor.moveToNext());
//            cursor.close();
//        }
//        if(cursor.getCount()>=10)
//        {
//            billnumber = bill + 1;
//            Toast.makeText(getContext(),"calc:"+billnumber,Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            billnumber = bill;
//        }
//    }
    void cc() {
        String num = "select Bill_no from Billing";
        Cursor cursor = db.rawQuery(num, null);
        int count = cursor.getCount();
        //    Toast.makeText(getContext(),"cc :"+count,Toast.LENGTH_SHORT).show();
        if (count > 0) {
            if (cursor.moveToFirst()) {
                do {
                    bill = cursor.getInt(0);
                } while (cursor.moveToNext());
            }
        } else {
            bill = 101;
        }
    }


    void billno_calc() {
        // bill = 101;
        String select = "SELECT * from Billing where Bill_no='" + bill + "'";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                int b = cursor.getInt(0);

            } while (cursor.moveToNext());
            cursor.close();
        }
        if (cursor.getCount() >= 10) {
            billnumber = bill + 1;
            //      Toast.makeText(getContext(),"calc:"+billnumber,Toast.LENGTH_SHORT).show();

        } else {
            billnumber = bill;
        }
    }


//    void Billnum()
//    {
//        final View vi = LayoutInflater.from(getView().getContext()).inflate(R.layout.bill_list, null);
//        EditText et = vi.findViewById(R.id.billnum_editext);
//        String te = et.getText().toString();
////        String n = String.valueOf(R.id.billnum_editext);
////        int b = Integer.parseInt(n);
//        //  Toast.makeText(getContext(), " "+n, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getContext(), " "+te, Toast.LENGTH_SHORT).show();
//    }
}


