package com.example.admin.gows;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by ADMIN on 1/20/2018.
 */

public class Item_add extends Fragment {
    SQLiteDatabase db;
    MaterialBetterSpinner item_category_name, item_type, tax_spin;
    EditText item_name, item_decimal, item_hsc, item_rate;
    //  ItemActivity it;
//   TextView total_price,price;
    CheckBox item_favour;
    Button item_save, clear2, clear3, clear4;
    String[] category_spilt, it_type;
    String item, fav = "0", category, taxes, tax1, tax2, tax3, tax4, enable = "1", pp, gridview_item, t1, t2, t3, t4, category_code;

    int tax_1 = 0, tax_2 = 0, tax_3 = 0, tax_4 = 0, ta2, ta3, ta4;
    float total_pri;
    // GridView gridView;
    int pa2;
    int p1 = 0;
    int count = 0;
    Float total_tax;
    //int pp1=0;
    Float pr1, pr2, pr3;
    //  int pc;//Integer.parseInt(item_rate.getText().toString());
    // View rootView;
    ArrayList<String> mStringList = new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    List<String> lable ;

    public static Float ra, ta1, kk1, kk2, kk3, kk4, pa1, pc, p6, p7, p8, tpp, tpp1, tpp2;
    int Fav_count;
    // SetTextView text_view;
    public static Float pp1, pp2, pp3, pp4;
    ArrayList arrayList = new ArrayList();
    String[] months;
    GridView mainListView;
    LinearLayout item_add_grid_layout;
//            = {
//            "gst - 2",
//            "cgst - 3",
//            "sgst - 1",
//            "igst - 4"
//    };


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.it_add, container, false);
        db = getActivity().openOrCreateDatabase("Master.db", Context.MODE_PRIVATE, null);
        db.execSQL("create table if not exists Item (Item_Code integer primary key autoincrement ,Item_Name text ," +
                "Category_Code int,Item_Type varchar,Tax1 varchar,Tax2 varchar,Tax3 varchar,Tax4 varchar,Rate float," +
                "HSNcode varchar(50),Total_Price float,Tax_Price float,Created_date Date,Created_time time,Enable int,Favour int,Tax_Percent float);");
        db.execSQL("create table if not exists Category (Category_Code Integer primary key autoincrement ,Name Varchar,Created_date Date,Created_time Time,Enable int)");
        db.execSQL("create table if not exists Taxes(Name text,Percentage varchar)");

        getTax_List();
        // Date & Time
        @SuppressLint("SimpleDateFormat") final String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        final String time = mdformat.format(calendar.getTime());

        final ScrollView scrollview = ((ScrollView) view.findViewById(R.id.scrollview));
        scrollview.post(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUSABLE_AUTO);
            }
        });

        item_add_grid_layout = view.findViewById(R.id.item_add_grid_layout);

        mainListView = view.findViewById(R.id.tax_listview);
      //  mainListView.setVisibility(View.GONE);
        lable = getTax_List();
        ArrayAdapter<String> taxAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, lable);
        //  final ArrayAdapter[] arrayAdapter = {new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, months)};
        mainListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mainListView.setAdapter(taxAdapter);

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String kag = mainListView.getItemAtPosition(i).toString();

                if (arrayList.contains(kag)) {
                    arrayList.remove(kag);
                    Toast.makeText(getApplicationContext(), "grid removed: " + arrayList.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    arrayList.add(kag);
                    Toast.makeText(getApplicationContext(), "grid added successfully: " + arrayList.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

//        mainListView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               }
//        });

        //MaterialBetterSpinner
        item_category_name = (MaterialBetterSpinner) view.findViewById(R.id.cate_spinner);
        item_type = (MaterialBetterSpinner) view.findViewById(R.id.item_spinner);
        //  tax_spin= (MaterialBetterSpinner)view.findViewById(R.id.tax_spinner);
        //Edittext
        item_name = view.findViewById(R.id.item_name);
        //    item_decimal = view.findViewById(R.id.item_decimal);
        item_rate = view.findViewById(R.id.item_rate);
        item_rate.setFilters(new InputFilter[]{
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 6, afterDecimal = 2;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = item_rate.getText() + source.toString();

                        if (temp.equals(".")) {
                            return "0.";
                        } else if (temp.toString().indexOf(".") == -1) {
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

        item_hsc = view.findViewById(R.id.item_hsc);
        //Checkbox
        item_favour = view.findViewById(R.id.item_favourite);
        //Button
        item_save = view.findViewById(R.id.item_save);
        //gridview
        //    gridView = view.findViewById(R.id.griditem);

        it_type = getResources().getStringArray(R.array.item_type);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_items, it_type);
        item_type.setAdapter(adapter);
        item_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item = adapterView.getItemAtPosition(i).toString();
            }
        });

        List<String> lables = getAllLabels();
        ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_items, lables);
        catAdapter.setDropDownViewResource(R.layout.spinner_items);
        item_category_name.setAdapter(catAdapter);

//        List<String> tax = getAlltaxes();
//        final ArrayAdapter<String> taxAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_items, tax);
//        catAdapter.setDropDownViewResource(R.layout.spinner_items);
//        tax_spin.setAdapter(taxAdapter);

        item_category_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                category = adapterView.getItemAtPosition(i).toString();
                //  category_spilt = category.split("-");
                //ct = category_spilt[0].trim();
            }
        });
        item_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                taxPercent();
                item_favour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
                if (!item_name.getText().toString().trim().isEmpty() && !item_category_name.getText().toString().isEmpty() &&
                        !item_type.getText().toString().isEmpty() && !item_rate.getText().toString().trim().isEmpty() &&
                        !item_hsc.getText().toString().trim().isEmpty()) {
                    if (item_favour.isChecked()) {
                        fav = "1";
                    } else {
                        fav = "0";
                    }

                    String selectQuery1 = "SELECT Category_Code  FROM Category where  Name='" + category + "'";
                    Cursor cursor1 = db.rawQuery(selectQuery1, null);
                    if (cursor1.moveToFirst()) {
                        do {
                            category_code = cursor1.getString(0);
                        } while (cursor1.moveToNext());
                    }
                    cursor1.close();

                    // tax list receive and split to add in the database.
                    int size = arrayList.size();
                    Toast.makeText(getContext(), " array size:"+size, Toast.LENGTH_SHORT).show();

                    if(size==0)
                    {
                        if (arrayList.get(0) == null) {
                            t1 = "0-0";
                            t2 = "0-0";
                            t3 = "0-0";
                            t4 = "0-0";
                        }
//                        if (tax_two == null) {
//                            tax_two = "0-0";
//                            tax_three = "0-0";
//                            tax_four = "0-0";
//                        }
//                        if (tax_three == null) {
//                            tax_three = "0-0";
//                            tax_four = "0-0";
//                        }
//                        if (tax_four == null) {
//                            tax_four = "0-0";
//                        }
                    }
                  //  Toast.makeText(getContext(), " array value:"+arrayList.get(2), Toast.LENGTH_SHORT).show();

//                    String tax1 = (String) arrayList.get(0);
//                    String tax2 = lable.get(1);
//                    String tax3 = lable.get(2);
//                    String tax4 = lable.get(3);


                    final ContentValues values = new ContentValues();
                    values.put("Item_Name", item_name.getText().toString().trim());
                    values.put("Category_Code", category_code);
                    values.put("Item_Type", item);
                    values.put("Tax1", t1);
                    values.put("Tax2",  t2);
                    values.put("Tax3",  t3);
                    values.put("Tax4", t4);
                    values.put("Rate", item_rate.getText().toString());
                    values.put("Hsncode", item_hsc.getText().toString());
                    if (mStringList.size() == 0) {
                        values.put("Total_Price", item_rate.getText().toString());
                        values.put("Tax_Price", 0.00);
                    } else {
                        String price = ItemActivity.price.getText().toString();
                        Float float_price = Float.valueOf(price);
                        String f_price = String.format("%.2f", float_price);
                        String tax = ItemActivity.total_price.getText().toString();
                        Float float_tax = Float.valueOf(tax);
                        String f_tax = String.format("%.2f", float_tax);
                        values.put("Total_Price", f_price);
                        values.put("Tax_Price", f_tax);
                    }
                    values.put("Created_date", date);
                    values.put("Created_time", time);
                    values.put("Enable", enable);
                    values.put("Favour", fav);
                    values.put("Tax_Percent", total_tax);
                    if (rowIdExists(item_name.getText().toString().trim())) {
                        getFav();
                        if ((Fav_count >= 12) && (item_favour.isChecked())) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                            alertDialog.setMessage("Many Favourites..you should remove..previously added");
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    fav = "0";
                                    values.put("Favour", fav);
                                    db.insert("Item", null, values);
                                    item_name.getText().clear();
                                    item_rate.getText().clear();
                                    item_rate.getText().clear();
                                    item_hsc.getText().clear();
                                    item_category_name.getText().clear();
                                    item_type.getText().clear();
                                    tax_spin.getText().clear();
                                    if (!mStringList.isEmpty()) {
                                        arrayAdapter.clear();
                                    }
                                    ItemActivity.total_price.setText("0.00");
                                    ItemActivity.price.setText("0.00");

                                    item_favour.setChecked(false);
                                    alertDialog.dismiss();
                                }
                            });
                            alertDialog.show();
                        } else {
                            db.insert("Item", null, values);
                            item_name.getText().clear();
                            item_rate.getText().clear();
                            item_rate.getText().clear();
                            item_hsc.getText().clear();
                            item_category_name.getText().clear();
                            item_type.getText().clear();
                           // tax_spin.getText().clear();
                            if (!mStringList.isEmpty()) {
                                arrayAdapter.clear();
                            }
                            ItemActivity.total_price.setText("0.00");
                            ItemActivity.price.setText("0.00");

                            item_favour.setChecked(false);
                            String nn = item_name.getText().toString();
                            final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                            alertDialog.setMessage("Item" + " " + nn + " has been added");
                            alertDialog.setMessage("Item " + " " + item_name.getText() + " has been added");
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alertDialog.dismiss();
                                }
                            });
                            alertDialog.show();
                        }
                    } else {
                        Toast.makeText(getContext(), " item name Already Used", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Empty Fields Are Not Allowed", Toast.LENGTH_SHORT).show();
                    if (item_name.getText().toString().trim().isEmpty()) {
                        item_name.requestFocus();
                    } else if (item_category_name.getText().toString().trim().isEmpty()) {
                        item_category_name.requestFocus();
                    } else if (item_type.getText().toString().trim().isEmpty()) {
                        item_type.requestFocus();
                    } else if (item_rate.getText().toString().trim().isEmpty()) {
                        item_rate.requestFocus();
                    } else if (item_hsc.getText().toString().trim().isEmpty()) {
                        item_hsc.requestFocus();
                    }

                }
            }
        });
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
                Float previous = 0.0f;
                String get = item_rate.getText().toString();
                if (get.isEmpty()) {
                    ra = 0.0f;
                    Float calc = ra * previous / 100;
                    String result = String.format("%.2f", calc);
                    ItemActivity.total_price.setText(result);
                    Float pri = ra + calc;
                    String result1 = String.format("%.2f", pri);
                    ItemActivity.price.setText(result1);
                } else {
                    ra = Float.valueOf(item_rate.getText().toString());
                    int count = mStringList.size();
                    for (int x = 0; x < count; x++) {
                        String text = mStringList.get(x);
                        String[] tex = text.split(" ");
                        String t_text = tex[2].trim();
                        Float t_rate = Float.valueOf(t_text);
                        Float add = t_rate + previous;
                        previous = add;
                    }
                    Float calc = ra * previous / 100;
                    String result = String.format("%.2f", calc);
                    ItemActivity.total_price.setText(result);
                    Float pri = ra + calc;
                    String result1 = String.format("%.2f", pri);
                    ItemActivity.price.setText(result1);
                    //     Toast.makeText(getActivity(), ""+result, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;

    }

    public List<String> getTax_List() {
        List<String> labels = new ArrayList<>();
        String selectQuery = "SELECT * FROM Taxes"; // where Enable=1" ;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(0) + " - " + cursor.getString(1);
                labels.add(var);
            } while (cursor.moveToNext());
        }
        int c = cursor.getCount();
    //    Toast.makeText(getContext(), "cursor count:" + c, Toast.LENGTH_SHORT).show();
        if (cursor.getCount()>0) {
         //   View v = View.inflate(R.layout.my_layout,null,false);
         //   final int v = R.layout.it_add;
//         item_add_grid_layout.setVisibility(View.VISIBLE);
            // mainListView.setVisibility(View.VISIBLE);
        }

        return labels;
//        final ArrayAdapter[] arrayAdapter = {new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, months)};
//        mainListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//        mainListView.setAdapter(arrayAdapter[0]);

    }

    public List<String> getAllLabels() {
        List<String> labels = new ArrayList<String>();
        String selectQuery = "SELECT Category_Code,Name  FROM Category where Enable=1";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(1);
                labels.add(var);
            } while (cursor.moveToNext());
        }
        return labels;
    }

    public List<String> getAlltaxes() {
        List<String> tax = new ArrayList<String>();
        String selectQuery = "SELECT * FROM Taxes";// where Enable=1" ;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(0) + " - " + cursor.getString(1);
                tax.add(var);
            } while (cursor.moveToNext());
        }
        return tax;
    }

    public void getFav() {
        String selectQuery = "SELECT Favour FROM Item  where Favour =1";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(0);
            } while (cursor.moveToNext());
            Fav_count = cursor.getCount();
        }
    }

    public boolean rowIdExists(String name) {
        String select = "select Item_Name from Item ";
        Cursor cursor = db.rawQuery(select, null);
        List<String> labels = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(0);
                labels.add(var);
            } while (cursor.moveToNext());
        }
        cursor.close();
        boolean allMatch = true;
        for (String string : labels) {
            if (string.equalsIgnoreCase(name)) {
                allMatch = false;
                break;
            }
        }
        return allMatch;
    }

    void taxPercent() {
        String tax_one = t1;
        String tax_two = t2;
        String tax_three = t3;
        String tax_four = t4;
        if (tax_one == null) {
            tax_one = "0-0";
            tax_two = "0-0";
            tax_three = "0-0";
            tax_four = "0-0";
        }
        if (tax_two == null) {
            tax_two = "0-0";
            tax_three = "0-0";
            tax_four = "0-0";
        }
        if (tax_three == null) {
            tax_three = "0-0";
            tax_four = "0-0";
        }
        if (tax_four == null) {
            tax_four = "0-0";
        }
        String[] tt1 = tax_one.split("-");
        String[] tt2 = tax_two.split("-");
        String[] tt3 = tax_three.split("-");
        String[] tt4 = tax_four.split("-");

        String ttt1 = tt1[1].trim();
        String ttt2 = tt2[1].trim();
        String ttt3 = tt3[1].trim();
        String ttt4 = tt4[1].trim();
        float tax1 = Float.valueOf(ttt1);
        float tax2 = Float.valueOf(ttt2);
        float tax3 = Float.valueOf(ttt3);
        float tax4 = Float.valueOf(ttt4);

        total_tax = Float.valueOf(tax1 + tax2 + tax3 + tax4);
        //  Toast.makeText(getContext(),"tax%: "+total_tax,Toast.LENGTH_SHORT).show();
    }
}
