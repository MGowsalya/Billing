
package com.example.admin.gows;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.facebook.FacebookSdk.getApplicationContext;
import static java.util.Collections.min;

public class BillList extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    public static RecyclerView recycler;
    RecyclerAdapter madapter;
    RecyclerView.Adapter recyclerAdapter;
    RecyclerView.LayoutManager recyclerLayoutManager;
    ArrayList<ListProvider> arrayList = new ArrayList<ListProvider>();
    //String sno,product,rate,qty,amt;
    List<String> s_no = new ArrayList<String>();
    List<String> pro_duct = new ArrayList<String>();
    List<Float> ra_te = new ArrayList<Float>();
    List<String> q_ty = new ArrayList<String>();
    List<Float> am_t = new ArrayList<Float>();
    SQLiteDatabase db;
    Button print;
    public float total_bill;
    int total_tax,num;
    public static TextView total_value,tax_value,grand_total;
    TextView Percent;
    Button bill_save_button;
    public static EditText billnum_editext;
    Float aa = 0.0f;
    int bill,quan;

    String product,q;

    List prod,qnty,tax_price;
    //  SecondActivity sec = new SecondActivity();

    private static final boolean D = true;

    private static final String TAG = "BluetoothChat";

    private String mConnectedDeviceName = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bill_list, container, false);
        db = getContext().openOrCreateDatabase("Master.db", Context.MODE_PRIVATE, null);


        db.execSQL("create table if not exists Billing(ID integer primary key autoincrement,Bill_no  bigint(11),bdate date,pcode int," +
                "Product Varchar,Rate float,Tax int,Qty int,Amount float,Total float,Created_date Date,Created_time time,Enable int)");
      recycler = view.findViewById(R.id.recycle);
        total_value = view.findViewById(R.id.total_value);
        tax_value = view.findViewById(R.id.tax_value);
        grand_total = view.findViewById(R.id.Grand_total);
  //      bill_save_button = view.findViewById(R.id.bill_save_button);
        billnum_editext = view.findViewById(R.id.billnum_editext);
       // Percent = view.findViewById(R.id.percent_value);
        print = view.findViewById(R.id.print);
        //total_price = view.findViewById(R.id.t_price);
        recyclerLayoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(recyclerLayoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        //recycler.setAdapter(madapter);
        recycler.setHasFixedSize(true);
        recycler.setHasFixedSize(true);
        billnum_editext.setText("101");


//        getpercent();
        getbill();
        getData();
        //getTaxes();
        // Toast.makeText(getContext(),"tax: "+total_tax,Toast.LENGTH_SHORT).show();
        //  tax_value.setText(String.valueOf(total_tax));
        getcode();
        toalcalc();

        String sub_total = total_value.getText().toString();
        String tax = tax_value.getText().toString();
        if(sub_total.isEmpty() && tax.isEmpty())
        {
            sub_total = "0.00";
            tax = "0.00";
        }
        Float s = Float.valueOf(sub_total);
        Float t = Float.valueOf(tax);
        Float final_value = s + t ;
        // float final_value = Integer.parseInt(sub_total+ tax);
        grand_total.setText(String.valueOf(final_value));
//
        int count = 0;
        for (String SNO : s_no) {
            ListProvider listProvider = new ListProvider(SNO, pro_duct.get(count), ra_te.get(count), q_ty.get(count), am_t.get(count));
            arrayList.add(listProvider);
            count++;
        }
        madapter = new RecyclerAdapter(arrayList);
        recycler.setAdapter(madapter);
        // setupChat();
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycler);

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv  = new ContentValues();
                cv.put("Total",total_value.getText().toString());
                db.insert("Billing",null,cv);
                int ch = SecondActivity.mChatService.getState();
                StringBuilder Printxt = new StringBuilder();
                String prnName;
                String prnNos;
                String prnAmt;
                String prnQty;
                String prn;
                float tot2 = 0.00f;

                String selec = "Select Company_name,Address,State,Postal_code From  Company ";
                Cursor cu = db.rawQuery(selec, null);

                if (cu.moveToFirst()) {
                    String pricomp = String.format("", cu.getString(0));
                    String prinaddr = String.format("", cu.getString(1));
                    String prinstate = String.format("", cu.getString(2));
                    String postal = String.format("", cu.getString(3));
                }
                cu.close();
                String select1 = "SELECT * FROM  Billing";
                Cursor c = db.rawQuery(select1, null);
                Printxt.append("--------------------------------------" + "\n");
                Printxt.append("SNo  Product       Rate    Qty   Amount" + "\n");
                Printxt.append("--------------------------------------" + "\n");
                if (c.moveToNext()) {
                    do {
                        prnName = String.format("%-4s", c.getString(0));
                        //+"-"+c.getString(10).toString());
                        String va = c.getString(1);
                        // String[] var1 = va.split("-");
                        prn = String.format("%-14s", va);
                        prnNos = String.format("%5s", c.getString(2));
                        prnQty = String.format("%5s", c.getString(3));
                        prnAmt = String.format("%11s", c.getString(4));
                        float ta = c.getFloat(4);
                        tot2 = tot2 + ta;
                        Printxt.append(prnName).append(prn).append(prnNos).append(prnQty).append(prnAmt).append("\n");
                    } while (c.moveToNext());

                    String total = String.format("%13s", Float.toString(tot2)); //= Float.toString(tot2);
                    Printxt.append("--------------------------------------" + "\n");
                    Printxt.append("                    Total " + total + "\n");
//                    Printxt.append("--------------------------------------" + "\n");
//                    Printxt.append("         Thank you ! Visit Again    " + "\n");
//                    Printxt.append("**************************************" + "\n" + "\n" + "\n" + "\n");
                }
                c.close();

//          Printxt.append("--------------------------------------" + "\n");
//           Printxt = Printxt + "Total            " + String.format("%12s", ) + "\n";
                if (ch != BluetoothChatService.STATE_CONNECTED) {
                    Toast.makeText(getApplicationContext(), "Connection loss..Try Again...", Toast.LENGTH_SHORT).show();
                    // billlist.toast();
                    return;
                } else {
                    String message = Printxt.toString();
                    if (message.length() > 0) {
                        // Get the message bytes and tell the BluetoothChatService to write
                        byte[] send = message.getBytes();
                        SecondActivity.mChatService.write(send);
                    }
                }
                //sec.printed(db,ch);
            }

        });
//        bill_save_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                arrayList.clear();
//                // recycler.setAdapter(null);
//                madapter.notifyDataSetChanged();
//
//                total_value.setText("0.00");
//                tax_value.setText("0.00");
//                grand_total.setText("0.00");
//                String Select = "SELECT Bill_no from Billing";
//                Cursor cu = db.rawQuery(Select,null);
//                while (cu.moveToNext())
//                {
//                    num = cu.getInt(0);
//                    Toast.makeText(getContext(),"saved:" +num, Toast.LENGTH_SHORT).show();
//                }
//                Bill_adding.billnumber = num + 1;
//                billnum_editext.setText(String.valueOf(Bill_adding.billnumber));
//
//
////                billnum_editext.setText("102");
////                String select = "SELECT ID FROM Billing";// where Product = '"+bill+"'";
////                Cursor c= db.rawQuery(select,null);
////                while(c.moveToNext()) {
////                    num  = c.getInt(0);
////                }
////                ContentValues cv = new ContentValues();
////                cv.put("Bill_no",billnum_editext.getText().toString());
////            //    db.insert("Billing",null,cv);
////                db.update("Billing", cv, "ID ='" + num + "'", null);
////                Toast.makeText(getContext(),"saved:" +num,Toast.LENGTH_SHORT).show();
//
//            }
//        });

////                float tot2=0.00f ;
////                String select = "Select * From Billtype";
////                Cursor c = db.rawQuery(select, null);
//              //  StringBuilder Printxt= new StringBuilder();
////                String prnName ;
////                Float prnNos,prnAmt ;
////                String prnQty ;
////                String prn ;
////
//////                Printxt = Printxt + "Bill No : "+Termid.toString()+"/"+c.getString(11)+"\n";
//////                Printxt = Printxt + "Date    : "+c.getString(12)+" / ";
//////                //+c.getString(5).substring(0,5)+"\n";
////              //  Printxt = Printxt + kb4+""+kb3+" "+kb2+"\n";
////                Printxt.append("--------------------------------------" + "\n");
////                Printxt.append("SNo  Product       Rate    Qty   Amount" + "\n");
////                Printxt.append("--------------------------------------" + "\n");
////                if (c.moveToFirst()) {
////                    do {
////                        prnName = String.format("%-4s", c.getString(0));
////                        //+"-"+c.getString(10).toString());
////                        String va = c.getString(1);
////                        String[] var1 = va.split("-");
////                        prn = String.format("%-10s", var1[1]);
////                        prnNos = Float.valueOf(String.format("%8s", c.getFloat(2)));
////                        prnQty = String.format("%7s", c.getString(3));
////                        prnAmt = Float.valueOf(String.format("%9s", c.getFloat(4)));
////                        float ta = c.getFloat(4);
////                        tot2 = tot2 + ta;
////                        Printxt.append(prnName).append(prn).append(prnNos).append(prnQty).append(prnAmt).append("\n");
////                    } while (c.moveToNext());
////
////
////                    String total = String.format("%12s", Float.toString(tot2)); //= Float.toString(tot2);
////                    Printxt.append("--------------------------------------" + "\n");
////                    Printxt = Printxt + "Total            " + String.format("%12s", ) + "\n";
////                    Printxt.append("                   Total  " +total + "\n");
////                    // Printxt = Printxt + "Total             "+String.format("%12s", )+"\n";
////                    Printxt.append("--------------------------------------" + "\n");
////                    Printxt.append("         Thank you ! Visit Again    " + "\n");
////                    Printxt.append("**************************************" + "\n" + "\n" + "\n" + "\n");
//                  // setupChat();
//                   // sendMessage(Printxt.toString());
////
////                }
////                c.close();
//            }
//        });
        return view;

    }

//    private void sendMessage(String message) {
//        // Check that we're actually connected before trying anything
//        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
//            Toast.makeText(getContext(), R.string.not_connected, Toast.LENGTH_SHORT).show();
//            return;
//        }
//        else{
//           // Toast.makeText(getContext(), "Connected", Toast.LENGTH_SHORT).show();
//        }
//        if (message.length() > 0) {
//            // Get the message bytes and tell the BluetoothChatService to write
//            byte[] send = message.getBytes();
//            mChatService.write(send);
//        }
//    }

    void getcode() {
        String select = "Select ID,Product,Rate,Qty,Amount,Tax From Billing";
        Cursor c = db.rawQuery(select, null);
        if (c.moveToFirst()) {
            do {
                String var = c.getString(0);
                String va = c.getString(1);
                float var2 = c.getFloat(2);
                String var3 = c.getString(3);
                float var4 = c.getFloat(4);
                String t = c.getString(5);
                s_no.add(var);
//                String[] var1 = va.split("-");
//                pro_duct.add(var1[1]);
                pro_duct.add(va);
                ra_te.add(var2);
                q_ty.add(var3);
                am_t.add(var4);
                total_bill = 0 + var4;
                //total_price.setText("" + total_bill);

                // tax_value.setText(String.valueOf(total_tax));
                total_value.setText(String.valueOf(total_bill));
                String sub = total_value.getText().toString();
                Float s = Float.valueOf(sub);
                // int s = Integer.parseInt(sub);
//                Float calc = Float.valueOf(s * total_tax/100);
//                tax_value.setText(String.valueOf(calc));
            } while (c.moveToNext());
            c.close();

        }
    }
    void getbill()
    {
        String select = "SELECT Bill_no from Billing";
        Cursor cursor = db.rawQuery(select,null);
        if(cursor.moveToFirst())
        {
            do{
                bill = cursor.getInt(0);
            }while (cursor.moveToNext());
            cursor.close();
        }
        if(cursor.getCount()==0)
        {
            billnum_editext.setText("101");
        }
        else {
            billnum_editext.setText(String.valueOf(bill));
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void getData()
    {
         prod = new ArrayList();
         qnty = new ArrayList();
        String select = "SELECT Product,Qty from Billing";
        Cursor cc = db.rawQuery(select,null);
        if(cc.moveToFirst())
        {
            do{
                String pro = cc.getString(0);
                 q = cc.getString(1);
                prod.add(pro);
                qnty.add(q);

            }while (cc.moveToNext());
            cc.close();
//            Toast.makeText(getApplicationContext(),"taxx : "+prod, Toast.LENGTH_SHORT).show();
//            Toast.makeText(getApplicationContext(),"qty : "+qnty, Toast.LENGTH_SHORT).show();
            check();

        }}
        @RequiresApi(api = Build.VERSION_CODES.N)
        void check()
        {
            ArrayList resultlist = new ArrayList();
            Float dummy = 0.0f;
            tax_price = new ArrayList();
            for(int n=0 ; n < prod.size(); n++) {
                String selects = "SELECT Tax_Price from Item where Item_Name = '" + prod.get(n) + "'";
                Cursor cursor = db.rawQuery(selects, null);
                if (cursor.moveToFirst()) {
                    do {
                        Float price = cursor.getFloat(0);

                      //  for (int j = 0; j < 2; j++) {
                            Object gk = qnty.get(n);
                       //     Toast.makeText(getApplicationContext(), "gk : " + gk, Toast.LENGTH_SHORT).show();

                            //   tax_price.add(price);

//                        Float qt = Float.valueOf(q);
//                        Toast.makeText(getApplicationContext(), "price : " + qt, Toast.LENGTH_SHORT).show();
                            String kg = String.valueOf(gk);
                            int kk = Integer.parseInt(kg);
                            Float calc = kk * price;
                            dummy = dummy + calc;
                            String result = String.format("%.2f", dummy);
                            tax_value.setText(String.valueOf(result));
                            tax_price.add(price);
                        //    Toast.makeText(getApplicationContext(), "taxp : " + dummy, Toast.LENGTH_SHORT).show();

                     //   }

                    }
                        while (cursor.moveToNext()) ;
                        cursor.close();

                    //calculation();
                }
            }
    }
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    void calculation()
//    {
//        for (int i = 0; i < qnty.size(); i++) {
//
//            for (int j = 0; i < tax_price.size(); j++) {
//                try {
//                    plansval.add(qnty.get(i) * tax_price.get(j));
//                }
//                catch (IndexOutOfBoundsException e) {
//                    System.out.println("IndexOutOfBoundsException: " + e.getMessage());
//                }
//            }
//        }
//        int p = prod.size();
//        ArrayList cc = new ArrayList(p);
//        for(int i=0; i<= p;i++)
//        {
////            Object a = qnty.get(i);
////            Object b =  tax_price.get(i);
////            Float aa = Float.valueOf(a);
////            Float bb = Float.valueOf(b);
//
//            cc.set(i, qnty.get(i) * tax_price.get(i))
//            cc.set(i, a* b);
//
//            Toast.makeText(getApplicationContext(), "cc:"+cc, Toast.LENGTH_SHORT).show();
//             //cc.get(i) = qnty.get(i) * tax_price.get(i);
//
//        }
//    }

//    void getTaxes()
//    {
//        Float final_value  = 0.0f;
//        String select = "SELECT Tax from Billing";
//        Cursor cc = db.rawQuery(select,null);
//        if(cc.moveToFirst())
//        {
//            do{
//                int tax = cc.getInt(0);
//                Float tot = Float.valueOf(tax);
//                final_value = tot + final_value;
//                String result = String.format("%.2f", final_value);
//                //  t = final_value;
//                tax_value.setText(String.valueOf(result));
//                Toast.makeText(getApplicationContext(),"t: "+tax, Toast.LENGTH_SHORT).show();
//
//            }while (cc.moveToNext());
//            cc.close();
//        }
//    }
//    void getTax()
//    {
//       //  String name = "four";
//        String select = "SELECT Tax1,Tax2,Tax3,Tax4 from Item"; // where Item_Name = '"+name+"'";
//        Cursor c = db.rawQuery(select,null);
//        if(c.moveToFirst())
//        {
//            do{
//                String t1 = c.getString(0);
//                String t2 = c.getString(1);
//                String t3 = c.getString(2);
//                String t4 = c.getString(3);
//
//                String[] tt1 = t1.split("-");
//                String[] tt2 = t2.split("-");
//                String[] tt3 = t3.split("-");
//                String[] tt4 = t4.split("-");
//
//                String ttt1 = tt1[1].trim();
//                String ttt2 = tt2[1].trim();
//                String ttt3 = tt3[1].trim();
//                String ttt4 = tt4[1].trim();
//
//                int tax1 = Integer.parseInt(ttt1);
//                int tax2 = Integer.parseInt(ttt2);
//                int tax3 = Integer.parseInt(ttt3);
//                int tax4 = Integer.parseInt(ttt4);
//
//                total_tax = tax1 + tax2 + tax3 + tax4;
//
//
//                tax_value.setText(String.valueOf(total_tax));
//            }while (c.moveToNext());
//            c.close();
//        }
//    }

    public void toalcalc() {
        String s = "Select Amount From Billing";
        Cursor c = db.rawQuery(s, null);
        if (c.moveToFirst()) {
            do {
                Float v = c.getFloat(0);
                // String var = c.getString(0);
                //     Toast.makeText(getContext(), "text"+v, Toast.LENGTH_SHORT).show();
//                Float a = Float.valueOf(var);
                //  aa = Float.valueOf(aa + var);
                aa = aa + v;
                //total_price.setText(String.valueOf(aa));
                total_value.setText(String.valueOf(aa));

            } while (c.moveToNext());
            c.close();
        }
    }

    public void toast() {
        Toast.makeText(getContext(), "R.string.not_connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof RecyclerAdapter.RecyclerViewHolder) {
            // get the removed item name to display it in snack bar
            String sno = arrayList.get(viewHolder.getAdapterPosition()).getSno();

            product = arrayList.get(viewHolder.getAdapterPosition()).getProduct();

            taxCalc();
            //clearSpace();

            Float amut = arrayList.get(viewHolder.getAdapterPosition()).getAmt();


            //  Toast.makeText(getContext(),""+(viewHolder.getAdapterPosition()),Toast.LENGTH_SHORT).show();
            Log.i("Checking", "" + (viewHolder.getAdapterPosition()));
            // if(  viewHolder.getAdapterPosition() < arrayList.size()+1 ){
            madapter.removeItem(viewHolder.getAdapterPosition());

            String total = total_value.getText().toString();
            Float tot = Float.valueOf(total);
            Float totamt = tot - amut;
            String result = String.format("%.2f", totamt);
            //  int to = Integer.parseInt(String.valueOf(totamt));
            //total_price.setText(result);
            total_value.setText(result);

            String g = grand_total.getText().toString();
            Float gg = Float.valueOf(g);
            Float ggg = gg - amut;
            grand_total.setText(String.valueOf(ggg));

            // }

            //
            db.execSQL("DELETE FROM Billing  WHERE ID = '"+sno+"'");
            int get_sno = Integer.parseInt(sno);
            String select = "Select ID From Billing where ID > '"+get_sno+"'";
            Cursor c = db.rawQuery(select, null);
            if (c.moveToFirst()) {
                do {
                    int v = c.getInt(0);
                    if(v>get_sno)
                    {
                        ContentValues cv = new ContentValues();
                        cv.put("ID",v-1);
                        db.update("Billing", cv, "ID ='" + v + "'", null);
                        // db.update("Billing",cv,)
                    }

                } while (c.moveToNext());
                c.close();

            }

         //   Toast.makeText(getContext(), "deleted:"+sno, Toast.LENGTH_SHORT).show();

        }
    }
    void taxCalc()
    {
        String selection = "SELECT Qty from Billing where Product = '"+product+"'";
        Cursor curs = db.rawQuery(selection,null);
        if(curs.moveToFirst()) {
             quan = curs.getInt(0);
        }
        String select = "SELECT Tax_Price from Item where Item_Name = '"+product+"'";
        Cursor cursor = db.rawQuery(select,null);
        if(cursor.moveToFirst())
        {
            Float tax = cursor.getFloat(0);
            Float tax_c = tax * quan;
        //    Toast.makeText(getContext(), "tax:"+tax_c, Toast.LENGTH_SHORT).show();

            String t = tax_value.getText().toString();
            Float ta = Float.valueOf(t);
            Float calc = ta - tax_c;
            String result = String.format("%.2f", calc);
            tax_value.setText(String.valueOf(result));

            String grant = grand_total.getText().toString();
            Float grt = Float.valueOf(grant);
            Float cl = grt - tax_c;
            String result1 = String.format("%.2f", cl);
            grand_total.setText(String.valueOf(result1));

        }
    }
    void clearSpace()
    {
        String selection = "SELECT * from Billing";// where Product = '"+product+"'";
        Cursor curs = db.rawQuery(selection,null);
        int count = curs.getCount();
        Toast.makeText(getContext(), "gone count:"+count, Toast.LENGTH_SHORT).show();
        if(count<=1)
        {
            //            madapter.notifyDataSetChanged();
            recycler.setVisibility(View.INVISIBLE);
            Toast.makeText(getContext(), "gone:", Toast.LENGTH_SHORT).show();
        }
       }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.bill_frag, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.edit_item:
                //                arrayList.clear();
//                // recycler.setAdapter(null);
//                madapter.notifyDataSetChanged();
//                Toast.makeText(getContext(), "cleared", Toast.LENGTH_SHORT).show();
                final AlertDialog.Builder adb = new AlertDialog.Builder(getContext());//.create();
                adb.setMessage("delete..?");
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        arrayList.clear();
                        // recycler.setAdapter(null);
                        madapter.notifyDataSetChanged();
                        //total_price.setText("0.00");
                        total_value.setText("0.00");
                        tax_value.setText("0.00");
                        grand_total.setText("0.00");
                    //    Percent.setText("0.00");
                        Toast.makeText(getContext(), "cleared", Toast.LENGTH_SHORT).show();
                        db.delete("Billing", null, null);
                        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = 'Billing'");// '" + Billing + "'");

                    }
                });
                adb.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                adb.show();
//                db.delete("Billing", null, null);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}