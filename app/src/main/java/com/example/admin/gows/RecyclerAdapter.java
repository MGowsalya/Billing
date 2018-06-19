package com.example.admin.gows;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.facebook.FacebookSdk.getApplicationContext;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
 //   private static final int TYPE_HEAD = 0;
 //   private static final int TYPE_lIST = 1;
    String date,ggg,product_name;
    Float tax_price;
    SQLiteDatabase db;
    ArrayList<ListProvider> arrayList = new ArrayList<ListProvider>();
    GridView gdv;

    public RecyclerAdapter(ArrayList<ListProvider> arrayList) {

        this.arrayList = arrayList;
        date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());


    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerViewHolder recyclerViewHolder;
//        if (viewType == TYPE_HEAD) {
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_header, parent, false);
//            recyclerViewHolder = new RecyclerViewHolder(view, viewType);
//            return recyclerViewHolder;
       // } else if (viewType == TYPE_lIST) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            recyclerViewHolder = new RecyclerViewHolder(view, viewType);
            return recyclerViewHolder;
//        }
//        return null;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        final ListProvider listProvider;
//        if (holder.view_type == TYPE_HEAD) {
//            holder.bill_date.setText(date);
      //  } else if (holder.view_type == TYPE_lIST) {
            listProvider = arrayList.get(position );
            holder.item_sno.setText(listProvider.getSno());
            holder.item_product.setText(listProvider.getProduct());
            holder.item_rate.setText(listProvider.getRate().toString());

        holder.item_qty.setPaintFlags(holder.item_qty.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            holder.item_qty.setText(listProvider.getQty());
            holder.item_amt.setText(listProvider.getAmt().toString());
//            BillList.recycler.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String pos = String.valueOf(BillList.recycler.getAdapter().getItemId(1));
//                    Toast.makeText(getApplicationContext(),"position "+pos,Toast.LENGTH_SHORT).show();
//                }
//            });


            holder.item_qty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    db = getApplicationContext().openOrCreateDatabase("Master.db", Context.MODE_PRIVATE ,null);
                   // db.execSQL("create table if not exists Billtype (Bill_code integer primary key autoincrement,Product varchar,Rate float ,Qty int,Amount float,Created_date date,Created_time time)");

                    db.execSQL("create table if not exists Billing(ID integer primary key autoincrement,Bill_no  bigint(11),bdate date,pcode int," +
                            "Product Varchar,Rate float,Tax int,Qty int,Amount float,Total float,Created_date Date,Created_time time,Enable int)");

                    final String value = holder.item_qty.getText().toString();
                //    Toast.makeText(getApplicationContext(),"calc:"+holder.item_qty.getText().toString(), Toast.LENGTH_SHORT).show();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.create();
                    final View vi = LayoutInflater.from(v.getContext()).inflate(R.layout.quan_grid, null);
                    builder.setView(vi);
                     gdv = vi.findViewById(R.id.text_one);
                    String[] arr = {"1","2","3","4","5","6","7","8","9","10"};
                    ArrayAdapter arr_adapt = new ArrayAdapter(getApplicationContext(),R.layout.quan_text,R.id.quan_grid_textview,arr);
                    gdv.setAdapter(arr_adapt);
                    final AlertDialog alrety = builder.show();
                    gdv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            builder.setCancelable(true);
                             ggg = gdv.getItemAtPosition(i).toString();
                         //   Toast.makeText(getApplicationContext(),"view:"+ggg, Toast.LENGTH_SHORT).show();
                            if(value.equalsIgnoreCase(ggg))
                            {
                               // Toast.makeText(getApplicationContext(),"sss",Toast.LENGTH_SHORT).show();
                            }
                            else {

                                holder.item_qty.setText(ggg);
                                int n = Integer.parseInt(ggg);
                                String product_amt = holder.item_amt.getText().toString();
                                Float pro_amt = Float.valueOf(product_amt);
                                float amt = listProvider.getRate()*n;
                                holder.item_amt.setText(""+amt);

//                                product_name = holder.item_product.getText().toString();
//                                getTaxPrice();
//                                Float tax_calc = n * tax_price;

                                // String selectQuery = "SELECT * FROM Billing where Qty ='"+value+"'";
                                String selectQuery = "SELECT ID,Product,Rate,Qty,Amount from Billing where Qty ='"+value+"'";
                                Cursor cursor = db.rawQuery(selectQuery, null);
                                if (cursor.moveToNext()) {
                                    do {
                                        String serial_num = cursor.getString(0);
                                        String product = cursor.getString(1);
                                        String rate = cursor.getString(2);
                                        String quantity = cursor.getString(3);
                                        String amount = cursor.getString(4);
                                        //  Toast.makeText(getApplicationContext(),"updatedd: "+product,Toast.LENGTH_SHORT).show();
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("Product",product);
                                        contentValues.put("Qty",ggg);
                                        contentValues.put("Rate",rate);
                                        contentValues.put("Amount",amt);
                                        db.update("Billing", contentValues, "Product='" + product + "'", null);

                                    } while (cursor.moveToNext());
                                }

                                if(amt>pro_amt)
                                {
                                    float f = amt - pro_amt;
                                    String total = BillList.total_value.getText().toString();
                                    Float tot = Float.valueOf(total);
                                    Float calc = tot+f;
                                 //   BillList.total_price.setText(String.valueOf(calc));
                                    BillList.total_value.setText(String.valueOf(calc));

                                    String get_grant = BillList.grand_total.getText().toString();
                                    Float g = Float.valueOf(get_grant);
                                    Float g_calc = g + f;
                                    BillList.grand_total.setText(String.valueOf(g_calc));

//                                    String selectQuery = "SELECT * FROM Billtype where Qty ='"+value+"'";
//                                    Cursor cursor = db.rawQuery(selectQuery, null);
//                                    if (cursor.moveToNext()) {
//                                        do {
//                                            String serial_num = cursor.getString(0);
//                                            String product = cursor.getString(1);
//                                            String rate = cursor.getString(2);
//                                            String quantity = cursor.getString(3);
//                                            String amount = cursor.getString(4);
//                                          //  Toast.makeText(getApplicationContext(),"updatedd: "+product,Toast.LENGTH_SHORT).show();
//                                            ContentValues contentValues = new ContentValues();
//                                            contentValues.put("Product",product);
//                                            contentValues.put("Qty",ggg);
//                                            contentValues.put("Rate",rate);
//                                            contentValues.put("Amount",amt);
//                                            db.update("Billtype", contentValues, "Qty='" + value + "'", null);
//
//                                        } while (cursor.moveToNext());
//                                    }


//                                    ContentValues contentValues = new ContentValues();
//                                    contentValues.put("Product","gowsi");
//                                    db.update("Billtype", contentValues, "Qty='" + value + "'", null);
                                   // Toast.makeText(getApplicationContext(),"updatedd: "+value,Toast.LENGTH_SHORT).show();
                                  //  db = (v.getContext()).openOrCreateDatabase("Master.db", Context.MODE_PRIVATE ,null);
                                  ///  db.update("")
                                    //Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                                }
                                else if(amt<pro_amt)
                                {
                                    float f = pro_amt - amt;
                                    String total = BillList.total_value.getText().toString();
                                    Float tot = Float.valueOf(total);
                                    Float calc = tot - f;
                                //    BillList.total_price.setText(String.valueOf(calc));
                                    BillList.total_value.setText(String.valueOf(calc));

                                    String get_grant = BillList.grand_total.getText().toString();
                                    Float g = Float.valueOf(get_grant);
                                    Float g_calc = g - f;
                                    BillList.grand_total.setText(String.valueOf(g_calc));
                                }
                              }
                              alrety.dismiss();
                        }
                    });


//                    vi.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Toast.makeText(getApplicationContext(),"view:"+view,Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    final TextView tx = vi.findViewById(R.id.one);
//                    tx.setOnClickListener(new View.OnClickListener() {
//                        @SuppressLint("ResourceAsColor")
//                        @Override
//                        public void onClick(View v) {
//                           float amt = listProvider.getRate()*1;
//                           holder.item_amt.setText(""+amt);
//                          String total = BillList.total_price.getText().toString();
//                          Float tot = Float.valueOf(total);
//                          //+Float.valueOf(amt);
//                            String text = tx.getText().toString();
//                            Toast.makeText(getApplicationContext(),"tx:"+tx.getText().toString(),Toast.LENGTH_SHORT).show();
//
//                          if(text.equalsIgnoreCase(value)){
//                              holder.item_qty.setText("1");
//                              Toast.makeText(getApplicationContext(),"sss",Toast.LENGTH_SHORT).show();
//                            }
//                            else
//                          {
//                              Float calc = tot+amt;
//                              BillList.total_price.setText(String.valueOf(calc));
//                              Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
//                          }
////                          ContentValues cv = new ContentValues();
//                          //  cv.
////                            db.update("Billtype",null,null);
//                        }
//                    });
//                    vi.findViewById(R.id.two).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            holder.item_qty.setText("2");
//                            float amt = listProvider.getRate()*2;
//                            holder.item_amt.setText(""+amt);
//
//                        }
//                    });
//                    vi.findViewById(R.id.three).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            holder.item_qty.setText("3");
//                            float amt = listProvider.getRate()*3;
//                            holder.item_amt.setText(""+amt);
//
//                        }
//                    });
//                    vi.findViewById(R.id.four).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            holder.item_qty.setText("4");
//                            float amt = listProvider.getRate()*4;
//                            holder.item_amt.setText(""+amt);
//
//                        }
//                    });
//                    vi.findViewById(R.id.five).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            holder.item_qty.setText("5");
//                            float amt = listProvider.getRate()*5;
//                            holder.item_amt.setText(""+amt);
//
//                        }
//                    });
//                    vi.findViewById(R.id.six).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            holder.item_qty.setText("6");
//                            float amt = listProvider.getRate()*6;
//                            holder.item_amt.setText(""+amt);
//
//                        }
//                    });
//                    vi.findViewById(R.id.seven).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            holder.item_qty.setText("7");
//                            float amt = listProvider.getRate()*7;
//                            holder.item_amt.setText(""+amt);
//
//                        }
//                    });
//                    vi.findViewById(R.id.eight).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            holder.item_qty.setText("8");
//                            float amt = listProvider.getRate()*8;
//                            holder.item_amt.setText(""+amt);
//
//                        }
//                    });
//                    vi.findViewById(R.id.nine).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            holder.item_qty.setText("9");
//                            float amt = listProvider.getRate()*9;
//                            holder.item_amt.setText(""+amt);
//
//                        }
//                    });
//                    vi.findViewById(R.id.ten).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            holder.item_qty.setText("10");
//                            float amt = listProvider.getRate()*10;
//                            holder.item_amt.setText(""+amt);
//                        }
//                    });


                }
            });

          //   }
        }
//        void getTaxPrice()
//        {
//            String select = "SELECT Tax_Price from Item where Item_Name = '"+product_name+"'";
//            Cursor cursor = db.rawQuery(select,null);
//            if(cursor.moveToFirst())
//            {
//                 tax_price = cursor.getFloat(0);
//                Toast.makeText(getApplicationContext(), "tax:"+tax_price, Toast.LENGTH_SHORT).show();
////
////                String t = tax_value.getText().toString();
////                Float ta = Float.valueOf(t);
////                Float calc = ta - tax;
////                tax_value.setText(String.valueOf(calc));
//
//            }
//
//        }

        @Override
        public int getItemCount () {
            return arrayList.size() ;
        }

//        @Override
//        public int getItemViewType ( int position){
//            if (position == 0)
//                return TYPE_HEAD;
//            return TYPE_lIST;
//
//        }



        public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
            int view_type;
            TextView billno, bill_date, sno, product, rate, qty, amt;

         public TextView item_sno, item_product, item_rate, item_qty, item_amt,item_total;
           public RelativeLayout viewBackground;
           public LinearLayout viewForeground;

            @SuppressLint("WrongViewCast")
            public RecyclerViewHolder(View itemView, int viewType) {
                super(itemView);
////                if (viewType == TYPE_HEAD) {
////                    billno = itemView.findViewById(R.id.bill_no);
////                    bill_date = itemView.findViewById(R.id.bill_date);
////                    view_type = 0;
//                } else if (viewType == TYPE_lIST) {
                    item_sno = itemView.findViewById(R.id.it_sno);
                    item_product = itemView.findViewById(R.id.it_product);

                    item_rate = itemView.findViewById(R.id.it_rate);
                    item_qty = itemView.findViewById(R.id.it_qty);
                    item_amt = itemView.findViewById(R.id.it_amount);
                 //   item_total = itemView.findViewById(R.id.total_price);
                    viewBackground = itemView.findViewById(R.id.view_background);
                    viewForeground = itemView.findViewById(R.id.view_foreground);

                   // view_type = 1;
               // }

            }


        }
    public void removeItem(int position) {
 if(arrayList.size()!=0) {
    // arrayList.remove(position);
//     System.out.println(arrayList.indexOf(position));
//     System.out.println(arrayList.size());
     Log.i("Check",""+position);
     arrayList.remove(position);
     notifyItemRemoved(position);
 }
    }



}


