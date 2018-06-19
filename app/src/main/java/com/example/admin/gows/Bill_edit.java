package com.example.admin.gows;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.GridView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class  Bill_edit extends Fragment {
    SQLiteDatabase db;

    String selectedItem,bill,sno,qty,quantity,var1,data;
    String[] b,q;
    GridView gridView;
    int quan=1;
  //  Float rate=10.0f;
    Float rate;
    int ta =11;
    Float amt;
    String item_code;
    int enable=1,count,billnumber=101,total=150;
    ContentValues cv;
  public static ArrayList<String> mStringList = new ArrayList<String>();
  public static ArrayAdapter aa;
    List<String> type;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {


            view = inflater.inflate(R.layout.bill_edit, container, false);
            gridView = (GridView) view.findViewById(R.id.grid);
            db = getActivity().openOrCreateDatabase("Master.db", Context.MODE_PRIVATE, null);

        db.execSQL("create table if not exists Billing(ID bigint(20),Bill_no  bigint(11),bdate date,pcode int," +
                "Product Varchar,Rate float,Tax int,Qty int,Amount float,Total float,Created_date Date,Created_time time,Enable int)");
        db.execSQL("create table if not exists Item (Item_Code integer primary key autoincrement ,Item_Name text ," +
                "Category_Code int,Item_Type varchar,Tax1 varchar,Tax2 varchar,Tax3 varchar,Tax4 varchar,Rate float," +
                "HSNcode varchar(50),Total_Price float,Tax_Price float,Created_date Date,Created_time time,Enable int,Favour int);");
        mStringList.clear();

        final String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        final String time =  mdformat.format(calendar.getTime());

        final int images[] = {R.drawable.apple,R.drawable.grapes,R.drawable.strawberry,R.drawable.orange};

        final HashMap hm = new HashMap();
        hm.put("apple",images[0]);
        hm.put("grapes",images[1]);
        hm.put("strawberry",images[2]);
        hm.put("orange",images[3]);

        final String selectQuery = "SELECT * FROM Item where Favour=1 And  Enable ='"+enable+"'" ;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToNext()) {
                do {
                    // data = cursor.getString(0) + " - " + cursor.getString(1);
                     String d = cursor.getString(1);
                     if(mStringList.size()<12)
                     {
                         mStringList.add(d);
                     }
                     else
                         {
                       //  Toast.makeText(getContext(), " fav greater", Toast.LENGTH_SHORT).show();
                     }
                } while (cursor.moveToNext());
                cursor.close();
                 count =cursor.getCount();
            }
        aa = new ArrayAdapter(getActivity(), R.layout.grid1,R.id.gridvi,mStringList);
            gridView.setAdapter(aa);


//            if(count<=5)
//            {
//                aa = new ArrayAdapter(getActivity(), R.layout.grid1,R.id.gridvi,mStringList);
//                gridView.setAdapter(aa);
//            }
//            else {
//                Toast.makeText(getContext(), " fav greater", Toast.LENGTH_SHORT).show();
//            }


//        if(mStringList.size()==5)
//        {
//            mStringList.clear();
////            int first_position = gridView.getFirstVisiblePosition();
////            int last_position = gridView.getLastVisiblePosition();
////            mStringList.remove(0);
////            aa.notifyDataSetChanged();
////            for(int i = first_position;i<=last_position;i++)
////            {
//               // mStringList.add(i,"kargowssss");
////                gridView.removeViewAt(first_position);
//          //  }
////           mStringList.add(g+2,"gowsi");
////            mStringList.add(g+3,"gowsi");
////            aa = new ArrayAdapter(getActivity(), R.layout.grid1,R.id.gridvi,mStringList);
////            gridView.setAdapter(aa);
//        //    mStringList.setVisibility(View.INVISIBLE);
//         //   Toast.makeText(getContext(),"size: "+g,Toast.LENGTH_SHORT).show();
//           }



//        int size = mStringList.size();
//        Toast.makeText(getContext(),"size: "+size,Toast.LENGTH_SHORT).show();
//           if(size<=3){
//               Toast.makeText(getContext(),"size: "+size,Toast.LENGTH_SHORT).show();
//              }

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                 //   view.setBackgroundResource(images[0]);
//                    if(mStringList.size()>12)
//                    {
//                       String remove = mStringList.get(i);
//                       mStringList.remove(remove);
//                       // gridView.removeViewAt(i);
//                        aa.notifyDataSetChanged();
//                        ContentValues contentValues = new ContentValues();
//                        contentValues.put("Favour","0");
//                        db.update("Item", contentValues, "Item_Name='" + remove  + "'", null);
//                        Toast.makeText(getContext(),"removed"+remove,Toast.LENGTH_SHORT).show();
////                        String selectQuery = "SELECT * FROM Item where Favour=1 And  Enable ='"+enable+"'" ;
////                        Cursor cursor = db.rawQuery(selectQuery, null);
////                        if (cursor.moveToNext()) {
////                            do {
////
////                                // data = cursor.getString(0) + " - " + cursor.getString(1);
////                                String d = cursor.getString(1);
////                                if(mStringList.size()<12)
////                                {
////                                    mStringList.add(d);
////                                }
////                                else {
////                                    //  Toast.makeText(getContext(), " fav greater", Toast.LENGTH_SHORT).show();
////                                }
////                            } while (cursor.moveToNext());
////                            cursor.close();
////                            count =cursor.getCount();
////                        }
////                        aa = new ArrayAdapter(getActivity(), R.layout.grid1,R.id.gridvi,mStringList);
////                        gridView.setAdapter(aa);
//                    }
//                    else {
                  //  mStringList.remove(i);
//                    aa.notifyDataSetChanged();
                    selectedItem = adapterView.getItemAtPosition(i).toString();

//                    if(hm.containsKey(selectQuery)){
//                        gridView.setBackgroundResource(images[1]);
//                    }
                    getRate();
                    if (getProduct()) {
                        getDetails();
                    } else {
                        String select = "SELECT Item_Code FROM Item where Item_Name ='" + selectedItem + "'";
                        Cursor curs = db.rawQuery(select, null);
                        if (curs.moveToNext()) {
                            do {
                                 item_code = curs.getString(0);
                            } while (curs.moveToNext());
                            curs.close();
                        }
                        amt = rate * quan;
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("Bill_no",billnumber);
                        contentValues.put("bdate",date);
                        contentValues.put("pcode",item_code);
                        contentValues.put("Product", selectedItem);
                        contentValues.put("Rate", rate);
                        contentValues.put("Qty", quan);
                        contentValues.put("Amount", amt);
                        contentValues.put("Tax",ta);
                        contentValues.put("Total",total);
                        contentValues.put("Created_date", date);
                        contentValues.put("Created_time", time);
                        contentValues.put("Enable", enable);

                        db.insert("Billing", null, contentValues);

                      //  String selectQuery = "SELECT * FROM Billing where Product ='" + selectedItem + "'";
                        String selectQuery = "SELECT ID,Product,Rate,Qty,Amount from Billing where Product ='" + selectedItem + "'";
                        Cursor cursor = db.rawQuery(selectQuery, null);
                        if (cursor.moveToNext()) {
                            do {
                                String serial_num = cursor.getString(0);
                                String product = cursor.getString(1);
                                String rate = cursor.getString(2);
                                String quantity = cursor.getString(3);
                                String amount = cursor.getString(4);

                                Bill.sno.setText(serial_num);
//                                String[] var1 = product.split("-");
//                                String pro = var1[1].trim();
                                Bill.product.setText(product);
                                Bill.rate.setText(rate);
                                Bill.qty.setText(quantity);
                                Bill.amt.setText(amount);
                                new Handler().postDelayed(new Runnable() {

                                    public void run() {
                                        Bill.sno.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
                                        Bill.product.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
                                        Bill.rate.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
                                        Bill.qty.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
                                        Bill.amt.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));

                                    }
                                }, 4000);

                                Bill.sno.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
                                Bill.product.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
                                Bill.rate.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
                                Bill.qty.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
                                Bill.amt.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));

                            } while (cursor.moveToNext());
                            cursor.close();
                        }
                   // }
//                      //  Toast.makeText(getContext(), "inserted", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return view;
    }
    public void getRate()
    {
//        String[] it_name = selectedItem.split("-");
//        String it =it_name[1].trim();
        String select = "SELECT Rate FROM Item where Item_Name ='"+selectedItem+"'";
        Cursor cur = db.rawQuery(select, null);
        if (cur.moveToNext()) {
            do {
                String data = cur.getString(0);
                rate = Float.valueOf(data);
            } while (cur.moveToNext());
            cur.close();
        }
    }

    public boolean getProduct() {
        String select = "select Product from Billing where Product ='"+selectedItem+"'";
        Cursor cursor = db.rawQuery(select, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public void getDetails() {
        getRate();

            String Query = "SELECT ID,Qty FROM Billing where Product = '" + selectedItem + "'";// where Bill_code ='"+b[0].trim()+"'";
            final Cursor c = db.rawQuery(Query, null);

            if (c.moveToFirst()) {
                do {
                    bill = c.getString(0);
                    b = bill.split("-");
                    sno = b[0].trim();
                    qty = c.getString(1);
                    q = qty.split("-");
                    quantity = q[0].trim();
                    //     Toast.makeText(getActivity(), "q:" +qty, Toast.LENGTH_SHORT).show();
                    quan = Integer.parseInt(qty);
                    amt = rate*(quan+1);
                        cv = new ContentValues();
//                    cv.put("Product", selectedItem);
//                    cv.put("Rate", rate);
                        cv.put("Qty", String.valueOf(quan + 1));
                        cv.put("Amount",amt);

                        db.update("Billing", cv, "Product='" + selectedItem + "'", null);
                    String selectQuery = "SELECT ID,Product,Rate,Qty,Amount from Billing where Product ='" + selectedItem + "'";
                    Cursor cursor = db.rawQuery(selectQuery, null);
                    if (cursor.moveToNext()) {
                        do {
                            String serial_num = cursor.getString(0);
                            String product = cursor.getString(1);
                            String rate = cursor.getString(2);
                            String quantity = cursor.getString(3);
                            String amount = cursor.getString(4);

                            Bill.sno.setText(serial_num);
//                            String[] var1 = product.split("-");
//                            String pro = var1[1].trim();
//                            Bill.product.setText(pro);
                            Bill.product.setText(product);
                            Bill.rate.setText(rate);
                            Bill.qty.setText(quantity);
                            Bill.amt.setText(amount);

                            new Handler().postDelayed(new Runnable() {

                                public void run() {
                                    Bill.sno.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorBlack));
                                    Bill.product.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorBlack));
                                    Bill.rate.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorBlack));
                                    Bill.qty.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorBlack));
                                    Bill.amt.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorBlack));
                                }
                            }, 4000);

                            Bill.sno.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorGreen));
                            Bill.product.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorGreen));
                            Bill.rate.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorGreen));
                            Bill.qty.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorGreen));
                            Bill.amt.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorGreen));
                        } while (cursor.moveToNext());
                        cursor.close();
                    }

                      //  Toast.makeText(getActivity(), "updated", Toast.LENGTH_SHORT).show();

                } while (c.moveToNext());
                c.close();

            }
    }

}
