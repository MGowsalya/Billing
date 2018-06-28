package com.example.admin.gows;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ADMIN on 1/20/2018.
 */

public class Item_editing extends Fragment{
    SQLiteDatabase db;
    MaterialBetterSpinner item_category,item_type_edit;
    MaterialBetterSpinner item_select;
    EditText item_name,item_hsc,item_rate;
    TextView enable_status,total_price,price;
    CheckBox item_edit_favour;
    String[] it_type,tt;
    String itemtype,catspin,catg,taxes,enable,favour,tax1,tax2,tax3,tax4,
            name,c_code,category_code_save;
    Button item_edit_save,item_edit_enable;
    List<String> lables,tax;
    Float quan;
    List<String> list,type;
    String item_code;
    int Fav_count;
    AlertDialog.Builder mBuilder;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    ArrayList<String> tax_list = new ArrayList<>();
    Button taxButton;
    List<String> list_name;
    String percent;
    String gg,item1;
    Float f = 0.0f,present;

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
        View view = inflater.inflate(R.layout.item_editing, container, false);
        db = getActivity().openOrCreateDatabase("Master.db", MODE_PRIVATE ,null);

        db.execSQL("create table if not exists Item (Item_Code integer primary key autoincrement ,Item_Name text ," +
                "Category_Code int,Item_Type varchar,Taxes varchar,Rate float," +"HSNcode varchar(50),Total_Price float,Tax_Price float,Created_date Date,Created_time time,Enable int,Favour int,Tax_Percent float);");

//        db.execSQL("create table if not exists Item (Item_Code integer primary key autoincrement ,Item_Name text ," +
//                "Category_Code int,Item_Type varchar,Tax1 varchar,Tax2 varchar,Tax3 varchar,Tax4 varchar,Rate float," +
//                "HSNcode varchar(50),Total_Price float,Tax_Price float,Created_date Date,Created_time time,Enable int,Favour int,Tax_Percent float);");
        db.execSQL("create table if not exists Category (Category_Code Integer primary key autoincrement ,Name Varchar,Created_date Date,Created_time Time,Enable int)");
        db.execSQL("create table if not exists Taxes(Name text,Percentage varchar)");

        // Date & Time
        @SuppressLint("SimpleDateFormat") final String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        final String time = mdformat.format(calendar.getTime());

        //Spinners
        item_select=view.findViewById(R.id.item_select);
        item_category=view.findViewById(R.id.item_category_edit_spinner);
        item_type_edit=view.findViewById(R.id.itemetype_edit_spinner);
        //ImageButton
        taxButton = view.findViewById(R.id.tax_spin_button);
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
        item_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                catspin = adapterView.getItemAtPosition(i).toString();
                //   Toast.makeText(getActivity(), "c"+catspin, Toast.LENGTH_SHORT).show();
            }
        });

        getTax_List();
        listItems = new String[tax_list.size()];
        for (int kg = 0; kg < tax_list.size(); kg++) {
            listItems[kg] = String.valueOf(tax_list.get(kg));
        }

        type = getItem();
        ArrayAdapter<String> typeAdapter1 = new ArrayAdapter<String>(getActivity(),R.layout.spinner_items, type);
        typeAdapter1.setDropDownViewResource(R.layout.spinner_items);
        item_select.setAdapter(typeAdapter1);

        item_select.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tt = new String[]{};//, tax2,tax3,tax4};
                list = new ArrayList<>(Arrays.asList(tt));
                tt = list.toArray(new String[0]);
                String it_name = adapterView.getItemAtPosition(i).toString();
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
                    taxButton.setText(tax1);
                    checkedItems = new boolean[listItems.length];
                    String text = taxButton.getText().toString();
                    String[] tt = text.split(",");

                    HashSet<String> set = new HashSet<>();

                    for (int c = 0; c < listItems.length; c++)
                    {
                        for (int j = 0; j < tt.length; j++)
                        {
                            //  Toast.makeText(getActivity(), "tt.."+tt[j], Toast.LENGTH_SHORT).show();
                            if(listItems[c].equalsIgnoreCase(tt[j]))
                            {
                                set.add(listItems[c]);
                                checkedItems[c] = true;
                                //  Toast.makeText(getActivity(), "satisfied", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    System.out.println("list element : "+(Arrays.asList(listItems)));
                    System.out.println("tt element : "+(Arrays.asList(tt)));
                    System.out.println("Common element : "+(set));



                    //    Toast.makeText(getActivity(), "t: "+tax1, Toast.LENGTH_SHORT).show();
                    //   retrieveTaxVaalues();
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
                    String rate = cursor.getString(5);
                    String hsn = cursor.getString(6);
                    String total_price = cursor.getString(7);
//                    Float total = Float.valueOf(total_price);
                    String tot = String.format("%.2f",Float.valueOf(total_price));
                    //   String tot = String.format("%.2f", t_p);
                    String tax_price = cursor.getString(8);
                    String tot1 = String.format("%.2f",Float.valueOf(tax_price));
                    String ena = cursor.getString(11);
                    String fav = cursor.getString(12);
                    String per = cursor.getString(13);
                    //    Toast.makeText(getActivity(), "Percent: "+tax_price, Toast.LENGTH_SHORT).show();

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
                        new Handler().postDelayed(  new Runnable() {
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
//        item_rate.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                Float previous =0.0f;
//                String get = item_rate.getText().toString();
//                if(get.isEmpty()){
//                    ra = 0.0f;
//                    Float calc = ra * previous/100;
//                    String result = String.format("%.2f", calc);
//                    ItemActivity.total_price.setText(result);
//                    Float pri = ra + calc;
//                    String result1 = String.format("%.2f", pri);
//                    ItemActivity.price.setText(result1);
//                }
//                else {
//                    ra = Float.valueOf(item_rate.getText().toString());
//                    int count = list.size();
//                    for(int x =0;x<count;x++) {
//                        String text = list.get(x);
//                        String[] tex = text.split(" ");
//                        String t_text = tex[2].trim();
//                        Float t_rate = Float.valueOf(t_text);
//                        Float add = t_rate + previous;
//                        previous = add;
//                        //   Toast.makeText(getActivity(), "" + add, Toast.LENGTH_SHORT).show();
//                    }
//                    Float calc = ra * previous/100;
//                    String result = String.format("%.2f", calc);
//                    ItemActivity.total_price.setText(result);
//                    Float pri = ra + calc;
//                    String result1 = String.format("%.2f", pri);
//                    ItemActivity.price.setText(result1);
//                    //     Toast.makeText(getActivity(), ""+result, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        taxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                String text = taxButton.getText().toString();
//                String[] tt = text.split(",");
//
//                HashSet<String> set = new HashSet<>();
//
//                for (int i = 0; i < listItems.length; i++)
//                {
//                    for (int j = 0; j < tt.length; j++)
//                    {
//                        if(listItems[i].equalsIgnoreCase(tt[j]))
//                        {
//                            set.add(listItems[i]);
//                            checkedItems[i] = true;
//                        }
//                    }
//                }
                // return common elements.
//                System.out.println("list element : "+(Arrays.asList(listItems)));
//                System.out.println("tt element : "+(Arrays.asList(tt)));
//                System.out.println("Common element : "+(set));
//                boolean compare = Arrays.equals(listItems,tt);
//
//                Toast.makeText(getContext(), "compare.."+compare, Toast.LENGTH_SHORT).show();
                int k1=Math.max(listItems.length,tt.length);
//                        Log.e("listItems","list: "+Arrays.toString(listItems));
//                        Log.e("listItems","check: "+Arrays.toString(checkedItems));
//                        if(!listItems.equals(t)){
//                            Log.e("listItems","list: "+Arrays.toString(listItems));
//                            Toast.makeText(getContext(), "success!!"+position, Toast.LENGTH_SHORT).show();
//                        }

//                for(int k=0;k<listItems.length;k++){
//                    if(listItems[k].equalsIgnoreCase(tt[0])){
//                        checkedItems[k] = true;
//                        Toast.makeText(getContext(), "succeeded..", Toast.LENGTH_SHORT).show();
//                        for(int kk=0;kk<listItems.length;kk++){
//                            if(listItems[kk].equalsIgnoreCase(tt[1])){
//                                checkedItems[kk] = true;
//                                Toast.makeText(getContext(), "succeeded1..", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                }
//                Toast.makeText(getContext(), "tt.."+tt[1].toString(), Toast.LENGTH_SHORT).show();
//                for(int kk=0;kk<listItems.length;kk++){
//                    if(listItems[kk].equals(tt[1])){
//                        checkedItems[kk] = true;
//                        Toast.makeText(getContext(), "succeeded1..", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                for(int i=0;i<5;i++){
//                    if(listItems[i].contains(tt[k])){
//                        checkedItems[i] = true;
//                        Log.e("listItems","check: "+Arrays.toString(checkedItems));
//                        Toast.makeText(getContext(), "contains.."+i, Toast.LENGTH_SHORT).show();
//                        ++k;
//                    }
//                }
                //
                //    Toast.makeText(getContext(), "text :"+tt.toString(), Toast.LENGTH_SHORT).show();
//                getTax_List();
//                listItems = new String[tax_list.size()];
//                for (int kg = 0; kg < tax_list.size(); kg++) {
//                    listItems[kg] = String.valueOf(tax_list.get(kg));
//                }
//                checkedItems = new boolean[listItems.length];

                //  checked();
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                mBuilder = new AlertDialog.Builder(getContext());
                mBuilder.setTitle("taxes");
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

//                        if(listItems[position].contains(t))
//                        {
//                            checkedItems[2] = true;
//                            Log.e("listItems","check: "+Arrays.toString(checkedItems));
//                            Toast.makeText(getContext(), "yes!!"+Arrays.toString(tt), Toast.LENGTH_SHORT).show();
//
//                        }

                        if (isChecked) {
                            //  checkedItems[position] = isChecked;
                            // If user select a item then add it in selected items
                            mUserItems.add(position);
                            list_name = Arrays.asList(listItems[position]);
                            calculation();
                            String r = item_rate.getText().toString();
                            if(r.isEmpty()){
                                r = "0.0";
                                ItemActivity.price.setText(String.valueOf(r));
                            }
                            Float rr = Float.valueOf(r);
                            Float calc = rr * f /100;
                            //      Toast.makeText(getContext(), "calc f: "+f, Toast.LENGTH_SHORT).show();
                            ItemActivity.total_price.setText(String.valueOf(calc));
                            String tp = ItemActivity.price.getText().toString();
                            Float total = Float.valueOf(tp);
                            Float c = rr* present /100;
                            Float calc1 = (total + c);
                            ItemActivity.price.setText(String.valueOf(calc1));
                        } else if (mUserItems.contains(position)) {
//                            isChecked = true;
//                            Toast.makeText(getContext(), "dialog: "+isChecked, Toast.LENGTH_SHORT).show();
//                            for(int i =0; i<mUserItems.size(); i++){
//                            }
                            // Toast.makeText(getContext(), "checked: "+isChecked, Toast.LENGTH_SHORT).show();
                            // if the item is already selected then remove it
                            list_name = Arrays.asList(listItems[position]);
                            gg = listItems[position].toString();
                            reverseCalc();
                            String r = item_rate.getText().toString();
                            if(r.isEmpty()){
                                r = "0.00";
                                ItemActivity.price.setText(String.valueOf(r));
                            }
                            Float rr = Float.valueOf(r);
                            Float calc = rr * f /100;
                            String decimal = String.format("%.2f", calc);
                            ItemActivity.total_price.setText(String.valueOf(decimal));

                            String tp = ItemActivity.price.getText().toString();
                            Float total = Float.valueOf(tp);
                            //       Toast.makeText(getContext(), "calc reverse: "+total, Toast.LENGTH_SHORT).show();
                            Float c = rr* present /100;
                            Float calc1 = total - c;
                            String decimal1 = String.format("%.2f", calc1);
                            ItemActivity.price.setText(String.valueOf(decimal1));

                            mUserItems.remove(Integer.valueOf(position));
                        }

                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        //   calculation();
                        item1 = "";
                        for(int i=0; i<mUserItems.size(); i++){
                            item1 = item1 + listItems[mUserItems.get(i)];
                            if(i != mUserItems.size() -1){
                                item1 = item1 + ",";
                            }

                        }
                        if(item1.isEmpty()){
                            taxButton.setText("select tax");
                        }
                        else {
                            taxButton.setText(item1);
                        }
                    }
                });
                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int ii) {
                        item1 = "";
                        for(int i=0; i<mUserItems.size(); i++){
                            item1 = item1 + listItems[mUserItems.get(i)];
                            if(i != mUserItems.size() -1){
                                item1 = item1 + ",";
                            }
                        }
                        if(item1.isEmpty()){
                            taxButton.setText("select tax");
                        }
                        else {
                            taxButton.setText(item1);
                        }
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

//        taxButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
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

                    values.put("Rate", item_rate.getText().toString());
                    values.put("Hsncode", item_hsc.getText().toString());
                    values.put("Total_Price",ItemActivity.price.getText().toString());
                    values.put("Tax_Price",ItemActivity.total_price.getText().toString());
                    values.put("Created_date", date);
                    values.put("Created_time", time);
                    values.put("Enable", enable);
                    values.put("Favour", favour);
                    String tx = taxButton.getText().toString();
                    values.put("Taxes",tx);
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
                        Toast.makeText(getContext(), "tax1: "+tax1, Toast.LENGTH_SHORT).show();
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
                            db.update("Billing", con, "Product='" + name + "'", null);
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
        String select = "SELECT Qty FROM Billing where Product ='"+name+"'";
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
    public void getTax_List(){
        tax_list.clear();
        String selectQuery = "SELECT * FROM Taxes";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(0);
                // listItems = var.split(" ");
                tax_list.add(var);
//                  Log.e("list","values:"+listItems.toString());
            } while (cursor.moveToNext());
            //Toast.makeText(getContext(), "var:"+tax_list.get(1), Toast.LENGTH_SHORT).show();
        }
    }
    private void calculation(){

        String[] names = new String[list_name.size()];
        names = list_name.toArray(names);
        //String trial = Arrays.toString(name);
//        Log.e("tax_name","calculation:"+ Arrays.toString(name));
        Log.e("tax_name","calculation:"+Arrays.toString(names));
        //  Toast.makeText(getContext(), "name:"+name.toString(), Toast.LENGTH_SHORT).show();
        for(int p=0 ; p < list_name.size(); p++){
            String selection = "select Percentage from Taxes where Name = '"+names[p]+"'";
            Cursor c = db.rawQuery(selection,null);
            if(c.moveToFirst()){
                do{
                    percent = c.getString(0);
                    present = Float.valueOf(percent);
                    Float ff = present + f;
                    f = ff;
                    //      Toast.makeText(getContext(), "percent: "+present, Toast.LENGTH_SHORT).show();
                }while (c.moveToNext());
            }}
    }
    private void reverseCalc(){
        String selection = "SELECT Percentage FROM Taxes Where  Name='"+gg+"'";
        Cursor c = db.rawQuery(selection,null);
        if(c.moveToFirst())
        {
            do{
                percent = c.getString(0);
                present = Float.valueOf(percent);
                f = f - present;
                // Toast.makeText(getContext(), "percent: "+f, Toast.LENGTH_SHORT).show();
            }while (c.moveToNext());
        }
    }
    private  void checked(){
        String text = taxButton.getText().toString();
        ArrayList arrayList = new ArrayList();
        arrayList.add(listItems);
        String strArray[] = text. split(",");
        for(int i=0; i < strArray. length; i++) {
            //   Toast.makeText(getContext(), "strarray: " + strArray[i] + " "+arrayList.toArray(listItems), Toast.LENGTH_SHORT).show();
            if(arrayList.contains("gst")){
                String gg = checkedItems.toString();
                Toast.makeText(getActivity(), "gg: "+gg, Toast.LENGTH_SHORT).show();
            }
//            if(listItems.equals(strArray[i])){
//                String gg = checkedItems.toString();
//                Toast.makeText(getActivity(), "gg: "+gg, Toast.LENGTH_SHORT).show();
//            }
        }}
    public boolean getProduct() {
        String select = "select Product from Billing where Product ='"+name+"'";
        Cursor cursor = db.rawQuery(select, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
}
