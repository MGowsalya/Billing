package com.example.admin.gows;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ADMIN on 1/20/2018.
 */

public class Item_edit extends Fragment{
    SQLiteDatabase db;
    MaterialBetterSpinner item_category,item_type_edit;
    MaterialBetterSpinner tax_edit_spinner,item_select;
    EditText item_name,item_hsc,item_rate;
    TextView enable_status,taxes1,taxes2,taxes3,taxes4,total_price,price;
    CheckBox item_edit_favour;
    String[] cat,it_type,tt;
    String itemtype,catspin,catg,taxes,enable,favour,t1,t2,t3,t4,tax1,tax2,tax3,tax4,
            gridview_item,name1,name2,name3,name4,name,c_code,category_code_save;
    int tax_1=0,tax_2=0,tax_3=0,tax_4=0;
    Button item_edit_save,item_edit_enable,clear1,clear2,clear3,clear4;
    List<String> lables,tax;
    float total_pri;
    GridView gridView;
    ArrayList arr_list;
    //  ArrayList<String> mStringList = new ArrayList<String>();
    ArrayAdapter arrayAdapter,ad;
    int pp1=0;
    Float tpp,tpp1,tpp2,kk2,kk3,kk4,quan;
    Float ra=0.0f,ta1,pa1,pc,kk1,p6,p7,p8;
    List<String> list,type;
    TextWatcher textWatcher;
    String item_code,ll;
    int Fav_count;


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
        View view = inflater.inflate(R.layout.it_ed, container, false);
        db = getActivity().openOrCreateDatabase("Master.db", MODE_PRIVATE ,null);

        db.execSQL("create table if not exists Item (Item_Code integer primary key autoincrement ,Item_Name text ," +
                "Category_Code int,Item_Type varchar,Tax1 varchar,Tax2 varchar,Tax3 varchar,Tax4 varchar,Rate float," +
                "HSNcode varchar(50),Total_Price float,Tax_Price float,Created_date Date,Created_time time,Enable int,Favour int,Tax_Percent float);");


//        db.execSQL("create table if not exists Item (Item_Code integer primary key autoincrement ,Item_Name text ," +
//                "Category_Code int,Item_Type varchar,Tax1 varchar,Tax2 varchar,Tax3 varchar,Tax4 varchar,Rate float," +
//                "HSNcode varchar(50),Total_Price float,Tax_Price float,Created_date Date,Created_time time,Enable int,Favour int);");
        db.execSQL("create table if not exists Category (Category_Code Integer primary key autoincrement ,Name Varchar,Created_date Date,Created_time Time,Enable int)");
        //db.execSQL("create table if not exists Billtype (Bill_code integer primary key autoincrement,Product varchar,Rate float ,Qty int,Amount float,Total float,Created_date date,Created_time time,Enable int)");
        db.execSQL("create table if not exists Taxes(Name text,Percentage varchar)");

        // Date & Time
        @SuppressLint("SimpleDateFormat") final String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        final String time = mdformat.format(calendar.getTime());

        final ScrollView scrollview = ((ScrollView)view.findViewById(R.id.scrollview));
        scrollview.post(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUSABLE_AUTO);
            }
        });
        //Spinners
        item_select=view.findViewById(R.id.item_select);
        item_category=view.findViewById(R.id.item_category_edit_spinner);
        item_type_edit=view.findViewById(R.id.itemetype_edit_spinner);
        tax_edit_spinner= (MaterialBetterSpinner)view.findViewById(R.id.tax_edit_spinner);
        //Edittext
        item_name = view.findViewById(R.id.item_edit_name);
        enable_status = view.findViewById(R.id.enable_status);
        item_rate = view.findViewById(R.id.item_edit_rate);
        item_rate.setFilters(new InputFilter[] {
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 6, afterDecimal = 2;
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = item_rate.getText() + source.toString();

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
        ItemActivity.total_price.setText("0.00");
        ItemActivity.price.setText("0.00");

        //  item_decimal = view.findViewById(R.id.item_edit_decimal);
        item_hsc = view.findViewById(R.id.item_edit_hsc);
        //checkbox
        item_edit_favour = view.findViewById(R.id.item_edit_favourite);
        //Button
        item_edit_save=view.findViewById(R.id.item_edit_save);
        item_edit_enable = view.findViewById(R.id.item_edit_enable);
        gridView = view.findViewById(R.id.griditem);

        //Disabling scroll for gridview...
//        gridView.setOnTouchListener(new View.OnTouchListener(){
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return event.getAction() == MotionEvent.ACTION_MOVE;
//            }
//
//        });


        it_type= getResources().getStringArray(R.array.item_type);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_items, it_type);
        item_type_edit.setAdapter(adapter);
        item_type_edit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                itemtype = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        lables = getAllLabels();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_items,lables);
        dataAdapter.setDropDownViewResource(R.layout.spinner_items);
        item_category.setAdapter(dataAdapter);
        tax = getTax();
        ArrayAdapter<String> taxadapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_items,tax);
        taxadapter.setDropDownViewResource(R.layout.spinner_items);
        tax_edit_spinner.setAdapter(taxadapter);

        item_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                catspin = adapterView.getItemAtPosition(i).toString();
                //   Toast.makeText(getActivity(), "c"+catspin, Toast.LENGTH_SHORT).show();

            }
        });
//        item_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                catspin = adapterView.getItemAtPosition(i).toString();
//                Toast.makeText(getActivity(), "c"+catspin, Toast.LENGTH_SHORT).show();
//                String selectQuery1 = "SELECT Category_Code  FROM Category where  Name='"+catspin+"'";
//                Cursor cursor1 = db.rawQuery(selectQuery1, null);
//                if (cursor1.moveToFirst()) {
//                    do {
//                        //   category_code = cursor.getString(0);
//                        c_code= cursor1.getString(0);//+" - "+cursor.getString(1);
//
//                        Toast.makeText(getActivity(), "cc"+c_code, Toast.LENGTH_SHORT).show();
//                    } while (cursor1.moveToNext());
//
//                }
////
////                cat = catspin.split("-");
////                catg = cat[0].trim();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        type = getItem();
        ArrayAdapter<String> typeAdapter1 = new ArrayAdapter<String>(getActivity(),R.layout.spinner_items, type);
        typeAdapter1.setDropDownViewResource(R.layout.spinner_items);
        item_select.setAdapter(typeAdapter1);

        item_select.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tt = new String[]{};//, tax2,tax3,tax4};
                list = new ArrayList<String>(Arrays.asList(tt));
                tt = list.toArray(new String[0]);
                ad = new ArrayAdapter(getContext(),  R.layout.grid, R.id.gridview_text, list);
                gridView.setAdapter(ad);
                //   Toast.makeText(getActivity(), "gridview"+list.toString(), Toast.LENGTH_SHORT).show();
                String it_name = adapterView.getItemAtPosition(i).toString();
//                item_code = item.split("-");
//                String it_name = item_code[1].trim();
                //  item_select.setText(it_name);

                String selectedQuery1 = "SELECT Item_Code FROM Item where  Item_Name='"+it_name+"'";
                Cursor curs = db.rawQuery(selectedQuery1, null);
                if (curs.moveToFirst()) {
                    do {
                        //   category_code = cursor.getString(0);
                        item_code= curs.getString(0);//+" - "+cursor.getString(1);
                    } while (curs.moveToNext());

                }
                String selectQuery = "SELECT * FROM  Item Where Item_Code ='"+item_code+"'";
                Cursor cursor = db.rawQuery(selectQuery,null);
                while(cursor.moveToNext()) {
                    name = cursor.getString(1);
                    String cat_code = cursor.getString(2);
                    String ite = cursor.getString(3);
                    tax1 = cursor.getString(4);
                    tax2 = cursor.getString(5);
                    tax3 = cursor.getString(6);
                    tax4 = cursor.getString(7);
                    String selectQuery1 = "SELECT  Name FROM Category where  Category_Code='"+cat_code+"'";
                    Cursor cursor1 = db.rawQuery(selectQuery1, null);
                    if (cursor1.moveToFirst()) {
                        do {
                            //   category_code = cursor.getString(0);
                            c_code= cursor1.getString(0);//+" - "+cursor.getString(1);

                            //    Toast.makeText(getActivity(), "cc"+c_code, Toast.LENGTH_SHORT).show();
                        } while (cursor1.moveToNext());
                        cursor1.close();
                    }
                    item_category.setText(c_code);

                    //     Toast.makeText(getActivity(), "codeg"+cat_code, Toast.LENGTH_SHORT).show();
                    if(tax1==null)// && tax2==null && tax3==null && tax4==null)
                    {
                        tt = new String[]{};//, tax2,tax3,tax4};
                        list = new ArrayList<String>(Arrays.asList(tt));
                        tt = list.toArray(new String[0]);
                        ad = new ArrayAdapter(getContext(),  R.layout.grid, R.id.gridview_text, list);
                        gridView.setAdapter(ad);
                    }
                    else if(tax2==null && tax3==null && tax4==null)
                    {
                        tt = new String[]{tax1};//, tax2,tax3,tax4};
                        list = new ArrayList<String>(Arrays.asList(tt));
                        tt = list.toArray(new String[0]);
                        ad = new ArrayAdapter(getContext(),  R.layout.grid, R.id.gridview_text, list);
                        gridView.setAdapter(ad);
                    }
                    else if(tax3==null)// && tax4==null)
                    {
                        tt = new String[]{tax1,tax2};//, tax2,tax3,tax4};
                        list = new ArrayList<String>(Arrays.asList(tt));
                        tt = list.toArray(new String[0]);
                        ad = new ArrayAdapter(getContext(),  R.layout.grid, R.id.gridview_text, list);
                        gridView.setAdapter(ad);
                    }
                    else if(tax4==null)
                    {
                        tt = new String[]{tax1,tax2,tax3};//tax4};
                        list = new ArrayList<String>(Arrays.asList(tt));
                        tt = list.toArray(new String[0]);
                        ad = new ArrayAdapter(getContext(),  R.layout.grid, R.id.gridview_text, list);
                        gridView.setAdapter(ad);
                    }
                    else {
                        tt = new String[]{tax1,tax2,tax3,tax4};
                        list = new ArrayList<String>(Arrays.asList(tt));
                        tt = list.toArray(new String[0]);
                        ad = new ArrayAdapter(getContext(),  R.layout.grid, R.id.gridview_text, list);
                        gridView.setAdapter(ad);
                    }
                    //   Toast.makeText(getActivity(), "gridview"+list.toString(), Toast.LENGTH_SHORT).show();
                    item_type_edit.setText(ite);
                    //                    it_type = getResources().getStringArray(R.array.item_type);
//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_items,it_type);
//                    item_type_edit.setAdapter(adapter);
//
//                    for  (int j = 0; j < item_type_edit.getCount(); j++) {
//                        if (item_type_edit.getItemAtPosition(j).equals(ite)) {
//                            item_type_edit.setSelection(j);
//                            break;
//                        }
//                    }
//                    for (int k = 0; k < item_category.getCount(); k++) {
//                        String gk = (String) item_category.getItemAtPosition(k);
//                        String[] gg =gk.split("-");
//                        String ggg = gg[0].trim();
//                        if (ggg.equals(cat_code)) {
//                            item_category.setSelection(k);
//                            break;
//                        }
//                    }

//                    Float rate = cursor.getFloat(8);
                    String rate = cursor.getString(8);
                    String hsn = cursor.getString(9);
                    String total_price = cursor.getString(10);
//                    Float total = Float.valueOf(total_price);
                    String tot = String.format("%.2f",Float.valueOf(total_price));
                    //   String tot = String.format("%.2f", t_p);
                    String tax_price = cursor.getString(11);
                    String tot1 = String.format("%.2f",Float.valueOf(tax_price));
                    String ena = cursor.getString(14);
                    String fav = cursor.getString(15);
                    String per = cursor.getString(16);
                    Toast.makeText(getActivity(), "Percent: "+per, Toast.LENGTH_SHORT).show();

                    item_name.setText(name);
                    item_name.setSelectAllOnFocus(true);
                    item_name.selectAll();
                    //    Float rt = Float.valueOf(rate);
                    // String rr = String.format("%.2f",rt);
//                    item_rate.setText(String.valueOf(rate));
                    //  Toast.makeText(getActivity(), ""+rt, Toast.LENGTH_SHORT).show();
                    //  item_rate.setText(String.valueOf(rate));
                    item_rate.setText(rate);
                    item_rate.setSelectAllOnFocus(true);
                    item_rate.selectAll();
                    item_hsc.setText(hsn);
                    item_hsc.selectAll();
                    item_hsc.setSelectAllOnFocus(true);
                    ItemActivity.total_price.setText(tot1);
                    ItemActivity.price.setText(tot);
                    //list.clear();
                    if(ena.equals("1")){
                        enable_status.setText("Enabled");
                        item_edit_enable.setText("Disable");
                        enable = "1";
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                enable_status.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorBlack));
                            }
                        },3000);
                        enable_status.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorGreen));
                    }
                    else{
                        enable_status.setText("Disabled");
                        item_edit_enable.setText("Enable");
                        enable = "0";
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                enable_status.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorBlack));
                            }
                        },3000);
                        enable_status.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorRed));
                    }
                    if(fav.equals("1")){
                        favour = "1";
                        item_edit_favour.setChecked(true);
                    }
                    else {
                        favour = "0";
                        item_edit_favour.setChecked(false);
                    }
                }
                cursor.close();
                // Toast.makeText(getContext(),"list :"+list.size(),Toast.LENGTH_SHORT).show();
            }

        });
//        Toast.makeText(getContext(),"list :"+list.size(),Toast.LENGTH_SHORT).show();
//
//        item_rate.addTextChangedListener(new TextWatcher() {
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//            @Override
//            public void afterTextChanged(Editable editable) {
//                Float previous =0.00f;
//                String get = item_rate.getText().toString();
//                if(get.isEmpty()){
//                    ra = 0.00f;
//                    Float calc = ra * previous/100;
//                    String result = String.format("%.2f", calc);
//                    ItemActivity.total_price.setText(result);
//                    Float pri = ra + calc;
//                    String result1 = String.format("%.2f", pri);
//                    ItemActivity.price.setText(result1);
//                }
//                else {
//                    ra = Float.valueOf(item_rate.getText().toString());
//                     //int count = list.size();
//                for(int x =0; x<list.size(); x++) {
//                    String text = list.get(x+1);
//                    String[] tex = text.split("-");
//                    String t_text = tex[1].trim();
//                    Float t_rate = Float.valueOf(t_text);
//                    Float add = t_rate + previous;
//                    previous = add;
//                    Toast.makeText(getActivity(), "text :" + text, Toast.LENGTH_SHORT).show();
//                }
//                    Toast.makeText(getActivity(), "text :" + list.get(1), Toast.LENGTH_SHORT).show();
//                Float calc = ra * previous/100;
//                String result = String.format("%.2f", calc);
//                    ItemActivity.total_price.setText(result);
//                Float pri = ra + calc;
//                String result1 = String.format("%.2f", pri);
//                    ItemActivity.price.setText(result1);
//           //     Toast.makeText(getActivity(), ""+result, Toast.LENGTH_SHORT).show();
//            }
//            }
//        });

        item_rate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                ItemActivity.total_price.setVisibility(View.VISIBLE);
//                ItemActivity.price.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Float previous =0.0f;
                String get = item_rate.getText().toString();
                if(get.isEmpty()){
                    ra = 0.0f;
                    Float calc = ra * previous/100;
                    String result = String.format("%.2f", calc);
                    ItemActivity.total_price.setText(result);
                    Float pri = ra + calc;
                    String result1 = String.format("%.2f", pri);
                    ItemActivity.price.setText(result1);
                }
                else {
                    ra = Float.valueOf(item_rate.getText().toString());
                    int count = list.size();
                    for(int x =0;x<count;x++) {
                        String text = list.get(x);
                        String[] tex = text.split(" ");
                        String t_text = tex[2].trim();
                        Float t_rate = Float.valueOf(t_text);
                        Float add = t_rate + previous;
                        previous = add;
                        //   Toast.makeText(getActivity(), "" + add, Toast.LENGTH_SHORT).show();
                    }
                    Float calc = ra * previous/100;
                    String result = String.format("%.2f", calc);
                    ItemActivity.total_price.setText(result);
                    Float pri = ra + calc;
                    String result1 = String.format("%.2f", pri);
                    ItemActivity.price.setText(result1);
                    //     Toast.makeText(getActivity(), ""+result, Toast.LENGTH_SHORT).show();
                }
            }
        });
        tax_edit_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                taxes = adapterView.getItemAtPosition(i).toString();
                String[] tax_array = taxes.split(" ");
                String tt = tax_array[2].trim();
                //item_rate.addTextChangedListener(textWatcher);
                //  mStringList.clear();
//
//                list.clear();
                //    Toast.makeText(getActivity()," "+list.size(),Toast.LENGTH_SHORT).show();
                if (list.size() <= 3) {
                    //  List<String> types = getTaxes();
                    list.add(taxes);
                    // String[] types  ={"aa","gg","kk"};
                    //    arrayAdapter = new ArrayAdapter(getActivity(), R.layout.grid, R.id.gridview_text, list);
                    gridView.setAdapter(ad);
                    ad.notifyDataSetChanged();


//                    if (list.size() == 0) {
//                        int zero = 0;
//                        total_price.setText(String.valueOf(zero));
//                        int price_rate = Integer.parseInt(item_rate.getText().toString());
//                        price.setText(String.valueOf(price_rate));
//                    }
                    ra = Float.valueOf(item_rate.getText().toString());
                    if (list.size() == 1) {

                        t1 = tax_edit_spinner.getText().toString();
                        String[] tt1 = t1.split(" ");
                        String ttt1 = tt1[2].trim();
                        ta1 = Float.valueOf((ttt1));
                        pa1 = ra * ta1 / 100;
                        String text = ItemActivity.total_price.getText().toString();
                        Float k = Float.valueOf(text);
                        kk1 = k + pa1;
                        String result = String.format("%.2f", kk1);
                        ItemActivity.total_price.setText(String.valueOf(result));
//                        String pe = ItemActivity.price.getText().toString();
//                        Float pe1 = Float.valueOf(pe);
                        pc = ra + kk1;
                        //    Toast.makeText(getActivity(),"pr:"+pc,Toast.LENGTH_SHORT).show();
                        String result1 = String.format("%.2f", pc);
                        ItemActivity.price.setText(String.valueOf(result1));

//                        t1 = tax_edit_spinner.getText().toString();
//                        String[] tt1 = t1.split(" ");
//                        String ttt1 = tt1[2].trim();
//                        ta1 = Float.valueOf(ttt1);
//                        pa1 = ra * ta1 / 100;
//                        String text = total_price.getText().toString();
//                        Float k = Float.valueOf(text);
//                        kk1 = k + pa1;
//                        String result = String.format("%.2f", kk1);
//                        total_price.setText(String.valueOf(result));
//                        String pe = item_rate.getText().toString();
//                        Float pe1 = Float.valueOf(pe);
//                        pc = pe1 + kk1;
//                        String result1 = String.format("%.2f", pc);
//                        price.setText(String.valueOf(result1));
                    }
                    if (list.size() == 2) {
                        t2 = tax_edit_spinner.getText().toString();
                        String[] tt1 = t2.split(" ");
                        String ttt1 = tt1[2].trim();
                        ta1 = Float.valueOf(ttt1);
                        p6 = ra * ta1 / 100;
                        String text = ItemActivity.total_price.getText().toString();
                        Float k = Float.valueOf(text);
                        kk2 = k + p6;
                        String result = String.format("%.2f", kk2);
                        ItemActivity.total_price.setText(String.valueOf(result));
                        String pe = ItemActivity.price.getText().toString();
                        Float pe1 = Float.valueOf(pe);
                        tpp = pe1 + p6;
                        String result1 = String.format("%.2f", tpp);
                        ItemActivity.price.setText(String.valueOf(result1));
                    }
                    if (list.size() == 3) {
                        t3 = tax_edit_spinner.getText().toString();
                        String[] tt1 = t3.split(" ");
                        String ttt1 = tt1[2].trim();
                        ta1 =Float.valueOf(ttt1);
                        p7 = ra * ta1 / 100;
                        String text = ItemActivity.total_price.getText().toString();
                        Float k = Float.valueOf(text);
                        kk3 = k + p7;
                        //    pr2 = pr1 + p7;
                        String result = String.format("%.2f", kk3);
                        ItemActivity.total_price.setText(String.valueOf(result));
                        String pe = ItemActivity.price.getText().toString();
                        Float pe1 = Float.valueOf(pe);
                        tpp1 = pe1 + p7;
                        String result1 = String.format("%.2f", tpp1);
                        ItemActivity.price.setText(String.valueOf(result1));
                    }
                    if (list.size() == 4) {
                        t4 = tax_edit_spinner.getText().toString();
                        String[] tt1 = t4.split(" ");
                        String ttt1 = tt1[2].trim();
                        ta1 = Float.valueOf(ttt1);
                        p8 = ra * ta1 / 100;
                        String text = ItemActivity.total_price.getText().toString();
                        Float k = Float.valueOf(text);
                        //  pr3 = pr2 + p8;
                        kk4 = k + p8;
                        String result = String.format("%.2f", kk4);
                        ItemActivity.total_price.setText(String.valueOf(result));
                        String pe = ItemActivity.price.getText().toString();
                        Float pe1 =Float.valueOf(pe);
                        tpp2 = pe1 + p8;
                        String result1 = String.format("%.2f", tpp2);
                        ItemActivity.price.setText(String.valueOf(result1));
                    }
                }
            }
        });


        item_edit_enable.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(enable.equals("1")){
                    enable_status.setText("Enabled");
                    item_edit_enable.setText("Disable");
                    final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setMessage("Your category Id is Disabled");
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            item_edit_enable.setText("Enable");
                            enable_status.setText("Disabled");
                            enable = "0";
                            alertDialog.dismiss();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    enable_status.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorBlack));
                                }
                            },3000);
                            enable_status.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorRed));
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
                            item_edit_enable.setText("Disable");
                            enable_status.setText("Enabled");
                            enable = "1";
                            alertDialog.dismiss();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    enable_status.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorBlack));
                                }
                            },3000);
                            enable_status.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorGreen));
                        }
                    });
                    alertDialog.show();
                }
            }
        });
        item_edit_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!item_name.getText().toString().trim().isEmpty() && !item_category.getText().toString().trim().isEmpty()
                        && !item_type_edit.getText().toString().isEmpty() && !item_rate.getText().toString().trim().isEmpty()
                        ) {
                    if (item_edit_favour.isChecked()) {
                        favour = "1";
                    } else {
                        favour = "0";
                    }

                    String category_spin = item_category.getText().toString();
                    String selectQuery2 = "SELECT  Category_Code FROM Category where  Name='"+category_spin+"'";
                    Cursor cursor2 = db.rawQuery(selectQuery2, null);
                    if (cursor2.moveToFirst()) {
                        do {
                            category_code_save= cursor2.getString(0);//+" - "+cursor.getString(1);
                        } while (cursor2.moveToNext());

                    }
                    ContentValues values = new ContentValues();
                    values.put("Item_Name", item_name.getText().toString().trim());
                    values.put("Category_Code", category_code_save);
                    values.put("Item_Type", item_type_edit.getText().toString());
                    int adapt_count = gridView.getCount();
                    //    Toast.makeText(getActivity(), ""+category_code_save, Toast.LENGTH_SHORT).show();
                    if(adapt_count==4)
                    {
                        String n1 = gridView.getItemAtPosition(0).toString();
                        String n2 = gridView.getItemAtPosition(1).toString();
                        String n3 = gridView.getItemAtPosition(2).toString();
                        String n4 = gridView.getItemAtPosition(3).toString();
                        values.put("Tax1",gridView.getItemAtPosition(0).toString());
                        values.put("Tax2",gridView.getItemAtPosition(1).toString());
                        values.put("Tax3",gridView.getItemAtPosition(2).toString());
                        values.put("Tax4",n4);
//                        Toast.makeText(getActivity(), "tax1: "+gridView.getItemAtPosition(0).toString()+gridView.getItemAtPosition(1).toString()
//                                +gridView.getItemAtPosition(2).toString()+n4, Toast.LENGTH_SHORT).show();
                    }
                    if(adapt_count==3)
                    {
                        String n1 = gridView.getItemAtPosition(0).toString();
                        String n2 = gridView.getItemAtPosition(1).toString();
                        String n3 = gridView.getItemAtPosition(2).toString();
                        String n4 = null;
                        values.put("Tax1",n1);
                        values.put("Tax2",n2);
                        values.put("Tax3",n3);
                        values.put("Tax4",n4);
                        //      Toast.makeText(getActivity(), "tax1: "+n1+n2+n3+n4, Toast.LENGTH_SHORT).show();
                    }
                    if(adapt_count==2)
                    {
                        String n1 = gridView.getItemAtPosition(0).toString();
                        String n2 = gridView.getItemAtPosition(1).toString();
                        String n3 = null;
                        String n4 = null;
                        values.put("Tax1",n1);
                        values.put("Tax2",n2);
                        values.put("Tax3",n3);
                        values.put("Tax4",n4);
                        //  Toast.makeText(getActivity(), "tax1: "+n1+n2+n3+n4, Toast.LENGTH_SHORT).show();
                    }
                    if(adapt_count==1)
                    {
                        String n1 = gridView.getItemAtPosition(0).toString();
                        String n2 = null;
                        String n3 = null;
                        String n4 = null;
                        values.put("Tax1",n1);
                        values.put("Tax2",n2);
                        values.put("Tax3",n3);
                        values.put("Tax4",n4);
                        // Toast.makeText(getActivity(), "tax1: "+n1+n2+n3+n4, Toast.LENGTH_SHORT).show();
                    }
                    if(adapt_count==0)
                    {

                        String n1 =null;
                        String n2 =null;
                        String n3 =null;
                        String n4 =null;
                        values.put("Tax1",n1);
                        values.put("Tax2",n2);
                        values.put("Tax3",n3);
                        values.put("Tax4",n4);
                        //      Toast.makeText(getActivity(), "tax1: "+n1+n2+n3+n4, Toast.LENGTH_SHORT).show();
                    }
                    values.put("Rate", item_rate.getText().toString());
                    values.put("Hsncode", item_hsc.getText().toString());
                    values.put("Total_Price",ItemActivity.price.getText().toString());
                    values.put("Tax_Price",ItemActivity.total_price.getText().toString());
                    values.put("Created_date", date);
                    values.put("Created_time", time);
                    values.put("Enable", enable);
                    values.put("Favour", favour);
                    if (rowIdExists(item_code)) {
//                        getFav();
//                        if((Fav_count>12) && (item_edit_favour.isChecked()))
//                        {
//                            values.put("Favour", "0");
//                            db.update("Item", values, "Item_Code ='" + item_code + "'", null);
//                            Toast.makeText(getActivity(), "too..many favourites..remove previously added", Toast.LENGTH_SHORT).show();
//                        }
//                        else {
                        db.update("Item", values, "Item_Code ='" + item_code + "'", null);
                        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setMessage("Your item" + "" + item_code + " has been Updated");
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //item_select.setText(item_name.getText().toString().trim());
                                alertDialog.dismiss();
                                ItemActivity.price.setText("0.0");
                                ItemActivity.total_price.setText("0.0");
                            }
                        });
                        alertDialog.show();
                        List<String> type = getItem();
                        ArrayAdapter<String> typeAdapter1 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_items, type);
                        typeAdapter1.setDropDownViewResource(R.layout.spinner_items);
                        item_select.setAdapter(typeAdapter1);
                        //   Toast.makeText(getActivity(), "update product: "+name, Toast.LENGTH_SHORT).show();
                        if (getProduct()) {
                            //getDetails();
                            Float rate = Float.valueOf(ItemActivity.price.getText().toString());
                            getQuan();
                            Float amt = rate * (quan);
                            ContentValues con = new ContentValues();
                            con.put("Product", item_name.getText().toString().trim());
                            con.put("Rate", ItemActivity.price.getText().toString());
                            con.put("Amount", amt);
                            db.update("Billtype", con, "Product='" + name + "'", null);
//                              Toast.makeText(getActivity(), "q : "+quan, Toast.LENGTH_SHORT).show();
//                            Toast.makeText(getActivity(), "amt: "+amt, Toast.LENGTH_SHORT).show();
                            //  }

                        }
                        getFav();
                        if((Fav_count>12) && (item_edit_favour.isChecked()))
                        {
                            values.put("Favour", "0");
                            item_edit_favour.setChecked(false);
                            db.update("Item", values, "Item_Code ='" + item_code + "'", null);
                            Toast.makeText(getActivity(), "too..many favourites..remove previously added", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Item Name Already Used", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Empty Fields Are Not Allowed", Toast.LENGTH_SHORT).show();
                    if (item_name.getText().toString().trim().isEmpty()) {
                        item_name.requestFocus();
                    }
                    else if (item_rate.getText().toString().trim().isEmpty()) {
                        item_rate.requestFocus();
                    }

                    else if (item_hsc.getText().toString().trim().isEmpty()) {
                        item_hsc.requestFocus();
                    }
                    else if (catg != null) {
                        item_category.requestFocus();
                    } else if (item_type_edit.getText().toString().trim().isEmpty()) {
                        item_type_edit.requestFocus();
                    }
                }
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onItemClick(final AdapterView<?> adapterView, View viw, final int i, long l) {
                gridview_item = adapterView.getItemAtPosition(i).toString();
//                if(gridview_item.isEmpty() || gridview_item == null)
//                {
//                    Toast.makeText(getActivity()," "+gridview_item,Toast.LENGTH_SHORT).show();
//                }
                final AlertDialog.Builder adb = new AlertDialog.Builder(getContext());//.create();
                adb.setMessage("delete" + " " +gridview_item+ " ?");
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    //   String item_del = mStringList.get(i);
                    String[] t =gridview_item.split(" ");
                    String g = t[2].trim();

                    @Override
                    public void onClick(DialogInterface dialogInterface, int k) {
                        ra = Float.valueOf(item_rate.getText().toString());
//                        ItemActivity.price.setText("0.0");
//                        ItemActivity.total_price.setText("0.0");
//                        mStringList.remove(i);

//                        Toast.makeText(getActivity(), " " + g, Toast.LENGTH_SHORT).show();
//                        Log.e("null "," "+g);

//                        if(g==null)
//                        {
//
//                        }

//                        mStringList.remove(i);
//                        arrayAdapter.notifyDataSetChanged();
                        //  alertDialog.dismiss();
                        if(i==0) {
                            name1 = adapterView.getItemAtPosition(i).toString();
                            Float gg = Float.valueOf(g);
                            Float p_p = ra * gg / 100;
                            String t_p = ItemActivity.total_price.getText().toString();
                            Float tt_p = Float.valueOf(t_p);
                            Float pp3 = tt_p - p_p;
                            String result = String.format("%.2f", pp3);
                            ItemActivity.total_price.setText(String.valueOf(result));
                            String pri_p = ItemActivity.price.getText().toString();
                            Float prc = Float.valueOf(pri_p);
                            Float p_c = prc - p_p;
                            String result1 = String.format("%.2f", p_c);
                            ItemActivity.price.setText(String.valueOf(result1));
                            list.remove(i);
                            ad.notifyDataSetChanged();
//
//                            Float gg = Float.valueOf(g);
//                            Float p_p = ra * gg / 100;
//                            String t_p = total_price.getText().toString();
//                            Float tt_p = Float.valueOf(t_p);
//                            Float pp3 = tt_p - p_p;
//                            String result = String.format("%.2f", pp3);
//                            total_price.setText(String.valueOf(result));
//                            String pri_p = price.getText().toString();
//                            Float prc = Float.valueOf(pri_p);
//                            Float p_c = prc - p_p;
//                             //    Toast.makeText(getActivity(), " " + tt_p, Toast.LENGTH_SHORT).show();
//                            String result1 = String.format("%.2f", p_c);
//                            price.setText(String.valueOf(result1));
                        }
                        if(i==1) {
                            name2 = adapterView.getItemAtPosition(i).toString();
                            Float gg = Float.valueOf(g);
                            Float p_p = ra * gg / 100;
                            String t_p = ItemActivity.total_price.getText().toString();
                            Float tt_p = Float.valueOf(t_p);
                            Float pp3 = tt_p - p_p;
                            String result = String.format("%.2f", pp3);
                            ItemActivity.total_price.setText(String.valueOf(result));
                            String pri_p = ItemActivity.price.getText().toString();
                            Float prc = Float.valueOf(pri_p);
                            Float p_c = prc - p_p;
                            String result1 = String.format("%.2f", p_c);
                            ItemActivity.price.setText(String.valueOf(result1));
                            list.remove(i);
                            ad.notifyDataSetChanged();

//                            Float gg = Float.valueOf(g);
//                            Float p_p = ra * gg / 100;
//                            String t_p = total_price.getText().toString();
//                            Float tt_p = Float.valueOf(t_p);
//                            Float pp3 = tt_p - p_p;
//                            String result = String.format("%.2f", pp3);
//                            total_price.setText(String.valueOf(result));
//                            String pri_p = price.getText().toString();
//                            Float prc = Float.valueOf(pri_p);
//                            Float p_c = prc - p_p;
//                            //     Toast.makeText(getActivity(), " " + p_c, Toast.LENGTH_SHORT).show();
//                            String result1 = String.format("%.2f", p_c);
//                            price.setText(String.valueOf(result1));
                        }
                        if(i==2) {
                            name3 = adapterView.getItemAtPosition(i).toString();
                            Float gg = Float.valueOf(g);
                            Float p_p = ra * gg / 100;
                            String t_p = ItemActivity.total_price.getText().toString();
                            Float tt_p = Float.valueOf(t_p);
                            Float pp3 = tt_p - p_p;
                            String result = String.format("%.2f", pp3);
                            ItemActivity.total_price.setText(String.valueOf(result));
                            String pri_p = ItemActivity.price.getText().toString();
                            Float prc = Float.valueOf(pri_p);
                            Float p_c = prc - p_p;
                            String result1 = String.format("%.2f", p_c);
                            ItemActivity.price.setText(String.valueOf(result1));
                            list.remove(i);
                            ad.notifyDataSetChanged();
//                            Float gg = Float.valueOf(g);
//                            Float p_p = ra * gg / 100;
//                            String t_p = total_price.getText().toString();
//                            Float tt_p = Float.valueOf(t_p);
//                            Float pp3 = tt_p - p_p;
//                            String result = String.format("%.2f", pp3);
//                            total_price.setText(String.valueOf(result));
//                            String pri_p = price.getText().toString();
//                            Float prc = Float.valueOf(pri_p);
//                            Float p_c = prc - p_p;
//                            String result1 = String.format("%.2f", p_c);
//                            price.setText(String.valueOf(result1));
                        }
                        if(i==3) {
                            name4 = adapterView.getItemAtPosition(i).toString();
                            Float gg = Float.valueOf(g);
                            Float p_p = ra * gg / 100;
                            String t_p = ItemActivity.total_price.getText().toString();
                            Float tt_p = Float.valueOf(t_p);
                            Float pp3 = tt_p - p_p;
                            String result = String.format("%.2f", pp3);
                            ItemActivity.total_price.setText(String.valueOf(result));
                            String pri_p = ItemActivity.price.getText().toString();
                            Float prc = Float.valueOf(pri_p);
                            Float p_c = prc - p_p;
                            String result1 = String.format("%.2f", p_c);
                            ItemActivity.price.setText(String.valueOf(result1));
                            list.remove(i);
                            ad.notifyDataSetChanged();
//                            Float gg = Float.valueOf(g);
//                            Float p_p = ra * gg / 100;
//                            String t_p = total_price.getText().toString();
//                            Float tt_p = Float.valueOf(t_p);
//                            Float pp3 = tt_p - p_p;
//                            String result = String.format("%.2f", pp3);
//                            total_price.setText(String.valueOf(result));
//                            String pri_p = price.getText().toString();
//                            Float prc = Float.valueOf(pri_p);
//                            Float p_c = prc - p_p;
//                            String result1 = String.format("%.2f", p_c);
//                            //    Toast.makeText(getActivity(), " " + p_c, Toast.LENGTH_SHORT).show();
//                            price.setText(String.valueOf(result1));
                        }
                    }
                });
                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Toast.makeText(getActivity(), "negative" , Toast.LENGTH_SHORT).show();
                    }
                });
                adb.show();
            }
        });
//        ItemActivity.price.setText("0.0");
//        ItemActivity.total_price.setText("0.0");
        return view;
    }
    public List<String> getTaxes(){
        List<String> labels = new ArrayList<String>();
        String selectQuery = "SELECT Tax1,Tax2,Tax3,Tax4 FROM Item " ;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(0)
                        +" - "+cursor.getString(1)+" - "+cursor.getString(2)+" - "+cursor.getString(3);
                labels.add(var);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return labels;
    }
    public List<String> getItem(){
        List<String> labels = new ArrayList<String>();
        String selectQuery = "SELECT Item_Code,Item_Name FROM Item " ;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(1);//+" - "+cursor.getString(1);
                labels.add(var);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return labels;
    }
    public List<String> getAllLabels(){
        List<String> lab = new ArrayList<String>();
        String selectQuery = "SELECT Category_Code,Name  FROM Category where Enable=1" ;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                //   category_code = cursor.getString(0);
                String category_name = cursor.getString(1);//+" - "+cursor.getString(1);
                lab.add(category_name);
            } while (cursor.moveToNext());

        }
        cursor.close();
        return lab;
    }
    public List<String> getTax(){
        List<String> tax = new ArrayList<String>();
        String selectQuery = "SELECT *  FROM Taxes";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(0)+" - "+cursor.getString(1);
                tax.add(var);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tax;
    } @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Item");
    }
    public boolean rowIdExists(String name) {
        String select = "select Item_Name from Item Where Item_Code != '"+name+"'"  ;
        Cursor cursor = db.rawQuery(select, null);
        ArrayList<String> labels  = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(0);
                labels.add(var);
            } while (cursor.moveToNext());
        }
        cursor.close();
        boolean allMatch = true;
        for (String string : labels) {
            if (string.equalsIgnoreCase( item_name.getText().toString().trim())) {
                allMatch = false;
                break;
            }
        }
        return allMatch;
    }
    public void getQuan()
    {
//        String[] it_name = selectedItem.split("-");
//        String it =it_name[1].trim();
        String select = "SELECT Qty FROM Billtype where Product ='"+name+"'";
        Cursor cur = db.rawQuery(select, null);
        if (cur.moveToNext()) {
            do {
                String data = cur.getString(0);
                quan = Float.valueOf(data);
            } while (cur.moveToNext());
            cur.close();
        }
    }
    public void getFav()
    {
        String selectQuery = "SELECT Favour FROM Item where Favour =1";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(0);
            } while (cursor.moveToNext());
            Fav_count = cursor.getCount();
        }
    }
    public boolean getProduct() {
        String select = "select Product from Billtype where Product ='"+name+"'";
        Cursor cursor = db.rawQuery(select, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
//    public void getDetails() {
//        getRate();
//
//        String Query = "SELECT Bill_code,Qty FROM Billtype where Product = '" + name + "'";// where Bill_code ='"+b[0].trim()+"'";
//        final Cursor c = db.rawQuery(Query, null);
//
//        if (c.moveToFirst()) {
//            do {
//               String bill = c.getString(0);
//              String[]  b = bill.split("-");
//             String   sno = b[0].trim();
//                String qty = c.getString(1);
//                String[] q = qty.split("-");
//                String quantity = q[0].trim();
//                //     Toast.makeText(getActivity(), "q:" +qty, Toast.LENGTH_SHORT).show();
//              int  quan = Integer.parseInt(qty);
//               Float amt = rate*(quan+1);
//              ContentValues  content = new ContentValues();
//              content.put("Product",item_name.getText().toString().trim());
//                content.put("Qty",String.valueOf(quan + 1));
//                content.put("Amount",amt);
//
//                db.update("Billtype", content, "Product='" + name + "'", null);
//
//            } while (c.moveToNext());
//            c.close();
//
//        }
//    }


}



