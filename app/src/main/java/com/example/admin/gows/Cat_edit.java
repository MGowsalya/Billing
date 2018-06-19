package com.example.admin.gows;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 04-12-2017.
 */

public class Cat_edit extends Fragment {

   MaterialBetterSpinner category_select;
    EditText category_edit_name;
    Button category_edit_save,category_edit_enable;
    String[] cate_code ;
    SQLiteDatabase db;
    String enable,cate;
    String type1;
    String na;
    TextView status;
  //  ImageView category_edit_imageview;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.category_edit, container, false);
        db = getActivity().openOrCreateDatabase("Master.db",android.content.Context.MODE_PRIVATE ,null);
        db.execSQL("create table if not exists Category (Category_Code Integer primary key autoincrement ,Name Varchar," +
                "Created_date Date,Created_time Time,Enable int)");
        category_select = (MaterialBetterSpinner) rootView.findViewById(R.id.category_edit_select);
        category_edit_name = rootView.findViewById(R.id.category_edit_name);
        category_edit_save = rootView.findViewById(R.id.category_edit_save);
        category_edit_enable= rootView.findViewById(R.id.categoruy_edit_enable);
        status = rootView.findViewById(R.id.enable_category_status);


        List<String> type = getCategory();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_items, type);
        dataAdapter.setDropDownViewResource(R.layout.spinner_items);
        category_select.setAdapter(dataAdapter);
        category_select.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 cate =adapterView.getItemAtPosition(i).toString();
                cate_code = cate.split("-");
                type1 = getStatus();
                if(type1.equals("1")){
                    category_edit_enable.setText("Disable");
                    status.setText("Enabled");
                    enable = "1";
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            status.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorBlack));
                        }
                    },3000);
                    status.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorGreen));

                }
                else{
                    category_edit_enable.setText("Enable");
                    status.setText("Disabled");
                    enable = "0";
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            status.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorBlack));
                        }
                    },3000);
                    status.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorRed));
                }
                getname();
            }
        });
        category_edit_enable.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(enable.equals("1")){
                    status.setText("Enabled");
                    category_edit_enable.setText("Disable");
                    final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setMessage("Your category Id is Disabled");
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            category_edit_enable.setText("Enable");
                            status.setText("Disabled");
                            enable = "0";
                            alertDialog.dismiss();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    status.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorBlack));
                                }
                            },3000);
                            status.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorRed));
                        }
                    });
                    alertDialog.show();
                }
                else{
                    final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setMessage("Your Item Id is Enabled");
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            category_edit_enable.setText("Disable");
                            status.setText("Enabled");
                            enable = "1";
                            alertDialog.dismiss();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    status.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorBlack));
                                }
                            },3000);
                            status.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorGreen));
                        }
                    });
                    alertDialog.show();
                }
            }
        });

        category_edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String na = category_edit_name.getText().toString().trim();
                if(!na .isEmpty()) {
                    ContentValues cv = new ContentValues();
                    cv.put("Name", category_edit_name.getText().toString().trim());
                    cv.put("Enable", enable);
                    if (rowIdExists(cate_code[0])) {
                        db.update("Category", cv, "Name ='" + cate + "'", null);
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setMessage("Your category" + "" + cate + " has been Updated");
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                category_select.setText(category_edit_name.getText().toString().trim());
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.show();
                        List<String> type = getCategory();
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, type);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        category_select.setAdapter(dataAdapter);
                     //   getImage();
                    }
                    else {
                        Toast.makeText(getActivity(), "Company Name Already Used", Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                        Toast.makeText(getActivity(), "Empty Fields Are Not Allowed", Toast.LENGTH_SHORT).show();

                    }

            }
        });

        return rootView;
    }
    public List<String> getCategory(){
        List<String> labels = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT Category_Code,Name FROM Category" ;
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(1);//+" - "+cursor.getString(1);
                labels.add(var);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return labels;
    }
    public String getStatus(){
        // Select All Query
        String var = null;
        String selectQuery = "SELECT   Enable FROM  Category Where Name ='"+cate+"'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                var = cursor.getString(0);
               // Toast.makeText(getContext(),var,Toast.LENGTH_SHORT).show();
            } while (cursor.moveToNext());
        }
        cursor.close();
        return var;
    }
    public  void getname(){
        String selectQuery = "SELECT Name  FROM  Category Where Name ='"+cate+"'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()) {
            do {

                String cat = cursor.getString(0);
              //  Toast.makeText(getContext(),cat,Toast.LENGTH_SHORT).show();
                category_edit_name.setText(cat);
                category_edit_name.setSelectAllOnFocus(true);
                category_edit_name.selectAll();
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
//    public void getImage()
//    {
//        String selectQuery = "SELECT Images FROM  Category Where Name ='"+cate+"'";
//        Cursor cursor = db.rawQuery(selectQuery,null);
//        if (cursor.moveToFirst()) {
//            do {
//                byte[] cat = cursor.getBlob(0);
//
//                ByteArrayInputStream inputStream = new ByteArrayInputStream(cat);
//                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
////                ByteBuffer wrapped = ByteBuffer.wrap(cat); // big-endian by default
////                short num = wrapped.getShort(); // 1
//
//
//                Toast.makeText(getActivity(), "blob", Toast.LENGTH_SHORT).show();
//                category_edit_imageview.setImageBitmap(bitmap);
//             } while (cursor.moveToNext());
//        }
//        cursor.close();
//    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Category");
    }
    public boolean rowIdExists(String name) {
        String select = "select Name from Category Where Name != '"+cate+"'"  ;
        Cursor cursor = db.rawQuery(select, null);
        ArrayList<String> labels = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(0);
                labels.add(var);
            } while (cursor.moveToNext());
        }
        cursor.close();
       /* for (int i = 0; i < labels.size(); i++) {
            if (labels.get(i).equalsIgnoreCase( category_edit_name.getText().toString())) {
                return false;
            }
            else{
                return true;
            }
        }
        return true;*/
        boolean allMatch = true;
        for (String string : labels) {
            if (string.equalsIgnoreCase( category_edit_name.getText().toString().trim())) {
                allMatch = false;
                break;
            }
        }
        return allMatch;

    }
}
