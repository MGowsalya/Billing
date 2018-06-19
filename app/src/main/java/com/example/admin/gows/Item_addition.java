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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Item_addition extends Fragment {
    SQLiteDatabase db;
    MaterialBetterSpinner item_category_name, item_type;
    EditText item_name,item_hsc, item_rate;
    CheckBox item_favour;
    Button item_save;
    String[] it_type;
    String item, fav = "0", category, taxes, enable = "1", category_code;
    int p1 = 0;
    int count = 0;
    Float total_tax;
    ArrayList<String> mStringList = new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    List<String> lable ;

    int Fav_count;
    ArrayList arrayList = new ArrayList();
    String[] months;
  //  GridView gridView;
 //   LinearLayout item_add_grid_layout;

    String gridview_item,t1,t2,t3,t4;
    public static Float ra,ta1,kk1,pa1,pc,p6,p7,p8,tpp,tpp1,tpp2;

  //  TextView mItemSelected;
    Button ok_button;
    String[] listItems ;//= {"gst","cgst","igst","others"};
  //  String[] kk = {"arun","shankar","karthi","keyan","manda","panda","jhandhu","java","visakirumi"};
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    ArrayList<String> tax_list = new ArrayList<String>();
    ArrayAdapter gridadapter;
    List<String> name;
    String percent;
    Float f = 0.0f,present;
    AlertDialog.Builder mBuilder;
    String gg,item1;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_addition, container, false);
        db = getActivity().openOrCreateDatabase("Master.db", Context.MODE_PRIVATE, null);
        db.execSQL("create table if not exists Item (Item_Code integer primary key autoincrement ,Item_Name text ," +
                "Category_Code int,Item_Type varchar,Tax1 varchar,Tax2 varchar,Tax3 varchar,Tax4 varchar,Rate float," +
                "HSNcode varchar(50),Total_Price float,Tax_Price float,Created_date Date,Created_time time,Enable int,Favour int,Tax_Percent float);");
        db.execSQL("create table if not exists Category (Category_Code Integer primary key autoincrement ,Name Varchar,Created_date Date,Created_time Time,Enable int)");
        db.execSQL("create table if not exists Taxes(Name text,Percentage varchar)");

        // Date & Time
        @SuppressLint("SimpleDateFormat") final String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        final String time = mdformat.format(calendar.getTime());

//        final ScrollView scrollview = ((ScrollView) view.findViewById(R.id.scrollview));
//        scrollview.post(new Runnable() {
//            @Override
//            public void run() {
//                scrollview.fullScroll(ScrollView.FOCUSABLE_AUTO);
//            }
//        });


        ok_button = view.findViewById(R.id.tax_add_ok_button_id);
     //   gridView = view.findViewById(R.id.gridview);
        getTax_List();
        listItems = new String[tax_list.size()];
        for (int kg = 0; kg < tax_list.size(); kg++) {
            listItems[kg] = String.valueOf(tax_list.get(kg));
        }
        checkedItems = new boolean[listItems.length];
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                mBuilder = new AlertDialog.Builder(getContext());
                mBuilder.setTitle("taxes");
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                            if (isChecked) {
                                // If user select a item then add it in selected items
                                mUserItems.add(position);
                                 name = Arrays.asList(listItems[position]);
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
                                // if the item is already selected then remove it

                                name = Arrays.asList(listItems[position]);
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
                                item1 = item1 + ", ";
                            }

                        }
                        if(item1.isEmpty()){
                            ok_button.setText("select tax");
                        }
                        else {
                            ok_button.setText(item1);
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
                                item1 = item1 + ", ";
                            }
                        }
                        if(item1.isEmpty()){
                            ok_button.setText("select tax");
                        }
                        else {
                            ok_button.setText(item1);
                        }
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();

            }
        });


        //MaterialBetterSpinner
        item_category_name = (MaterialBetterSpinner) view.findViewById(R.id.cate_spinner);
        item_type = (MaterialBetterSpinner) view.findViewById(R.id.item_spinner);
        //Edittext
        item_name = view.findViewById(R.id.item_name);
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

        item_rate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

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

        ItemActivity.total_price.setText("0.00");
        ItemActivity.price.setText("0.00");

        item_hsc = view.findViewById(R.id.item_hsc);
        //Checkbox
        item_favour = view.findViewById(R.id.item_favourite);
        //Button
        item_save = view.findViewById(R.id.item_save);
        item_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                 checking();
                taxPercent();
                item_favour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        if (item_favour.isChecked()) {
//                            fav="1";
//                        } else {
//                            fav="0";
//                        }
                    }
                });
                if(!item_name.getText().toString().trim().isEmpty()&&!item_category_name.getText().toString().isEmpty() &&
                        !item_type.getText().toString().isEmpty()&&!item_rate.getText().toString().trim().isEmpty() &&
                        !item_hsc.getText().toString().trim().isEmpty() ){
                    if (item_favour.isChecked()) {
                        fav="1";
                    } else {
                        fav="0";
                    }

                    String selectQuery1 = "SELECT Category_Code  FROM Category Where  Name='"+category+"'";
                    Cursor cursor1 = db.rawQuery(selectQuery1, null);
                    if (cursor1.moveToFirst()) {
                        do {
                            //   category_code = cursor.getString(0);
                            category_code= cursor1.getString(0);//+" - "+cursor.getString(1);
                        } while (cursor1.moveToNext());

                    }
                    cursor1.close();

                    final ContentValues values = new ContentValues();
                    values.put("Item_Name",item_name.getText().toString().trim());
                    values.put("Category_Code",category_code);
                    values.put("Item_Type",item);
                    values.put("Tax1",item1);
                    values.put("Tax2",t2);
//                    if(t3==null)
//                    {
//                        t3 = "0-0";
//                    }
                    values.put("Tax3",t3);
                    values.put("Tax4",t4);
                    values.put("Rate",item_rate.getText().toString());
                    values.put("Hsncode",item_hsc.getText().toString());
                    values.put("Total_Price",item_rate.getText().toString());
                    values.put("Tax_Price",ItemActivity.total_price.getText().toString());
                    //  values.put("Tax_Percent",total_tax);
//                    if(mStringList.size()==0)
//                    {
//                        values.put("Total_Price",item_rate.getText().toString());
//                        values.put("Tax_Price",0.00);
//                    }
//                    else
//                    {
//                        String price = ItemActivity.price.getText().toString();
//                        Float float_price = Float.valueOf(price);
//                        String f_price = String.format("%.2f", float_price);
//                        String tax = ItemActivity.total_price.getText().toString();
//                        Float float_tax = Float.valueOf(tax);
//                        String f_tax = String.format("%.2f", float_tax);
//                        values.put("Total_Price",f_price);
//                        values.put("Tax_Price",f_tax);
//                    }
                    values.put("Created_date",date);
                    values.put("Created_time",time);
                    values.put("Enable",enable);
                    values.put("Favour",fav);
                    values.put("Tax_Percent",total_tax );
                    if(rowIdExists( item_name.getText().toString().trim())) {
                        getFav();
                        if((Fav_count>= 12) && (item_favour.isChecked()))
                        {
                            final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                            alertDialog.setMessage("Many Favourites..you should remove..previously added");
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    fav ="0";
                                    values.put("Favour",fav);
                                    db.insert("Item", null, values);
                                    item_name.getText().clear();
                                    item_rate.getText().clear();
                                    item_rate.getText().clear();
                                    item_hsc.getText().clear();
                                    item_category_name.getText().clear();
                                    item_type.getText().clear();
//                                    if(!mStringList.isEmpty())
//                                    {
//                                        arrayAdapter.clear();
//                                    }
                                    ItemActivity.total_price.setText("0.00");
                                    ItemActivity.price.setText("0.00");

                                    item_favour.setChecked(false);
                                    alertDialog.dismiss();
                                }
                            });
                            alertDialog.show();
                        }
                        else {

                            db.insert("Item", null, values);
                            item_name.getText().clear();
                            item_rate.getText().clear();
                            item_rate.getText().clear();
                            item_hsc.getText().clear();
                            item_category_name.getText().clear();
                            item_type.getText().clear();
                            ItemActivity.total_price.setText("0.00");
                            ItemActivity.price.setText("0.00");

                            item_favour.setChecked(false);
                            String nn = item_name.getText().toString();
                            final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                            alertDialog.setMessage("Item" + " " +nn+ " has been added");
                            alertDialog.setMessage("Item "+" "+ item_name.getText()+" has been added");
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alertDialog.dismiss();
                                }
                            });
                            alertDialog.show();
                        }}
                    else {
                        Toast.makeText(getContext(), " item name Already Used", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(),"Empty Fields Are Not Allowed",Toast.LENGTH_SHORT).show();
                    if ( item_name.getText().toString().trim().isEmpty()) {
                        item_name.requestFocus();
                    }
                    else if ( item_category_name.getText().toString().trim().isEmpty()) {
                        item_category_name.requestFocus();
                    }
                    else if ( item_type.getText().toString().trim().isEmpty()) {
                        item_type.requestFocus();
                    }
                    else if ( item_rate.getText().toString().trim().isEmpty()) {
                        item_rate.requestFocus();
                    }
                    else if (item_hsc.getText().toString().trim().isEmpty()) {
                        item_hsc.requestFocus();
                    }
                }
            }
        });

        List<String> lables = getAllLabels();
        ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_items, lables);
        catAdapter.setDropDownViewResource(R.layout.spinner_items);
        item_category_name.setAdapter(catAdapter);
        item_category_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                category = adapterView.getItemAtPosition(i).toString();
                //  category_spilt = category.split("-");
                //ct = category_spilt[0].trim();
            }
        });

        it_type = getResources().getStringArray(R.array.item_type);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_items, it_type);
        item_type.setAdapter(adapter);
        item_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item = adapterView.getItemAtPosition(i).toString();
            }
        });

        return view;
    }
    public void getTax_List(){
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

        String[] names = new String[name.size()];
        names = name.toArray(names);
        //String trial = Arrays.toString(name);
//        Log.e("tax_name","calculation:"+ Arrays.toString(name));
        Log.e("tax_name","calculation:"+Arrays.toString(names));
      //  Toast.makeText(getContext(), "name:"+name.toString(), Toast.LENGTH_SHORT).show();
        for(int p=0 ; p < name.size(); p++){
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
            if(c.moveToFirst()){
                do{
                    percent = c.getString(0);
                     present = Float.valueOf(percent);
                     f = f - present;
                   // Toast.makeText(getContext(), "percent: "+f, Toast.LENGTH_SHORT).show();
                }while (c.moveToNext());
            }

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
    public void getFav()
    {
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
    void taxPercent()
    {
        String tax_one = t1;
        String tax_two = t2;
        String tax_three = t3;
        String tax_four = t4;
        if(tax_one==null)
        {
            tax_one = "0-0";
            tax_two = "0-0";
            tax_three = "0-0";
            tax_four = "0-0";
        }
        if(tax_two==null)
        {
            tax_two = "0-0";
            tax_three = "0-0";
            tax_four = "0-0";
        }
        if(tax_three==null)
        {
            tax_three = "0-0";
            tax_four = "0-0";
        }
        if(tax_four==null)
        {   tax_four = "0-0";
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

