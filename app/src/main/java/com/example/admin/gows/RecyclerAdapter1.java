package com.example.admin.gows;


import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class RecyclerAdapter1 extends RecyclerView.Adapter<RecyclerAdapter1.RecyclerViewHolder> {
    //   private static final int TYPE_HEAD = 0;
    //   private static final int TYPE_lIST = 1;
    String date,ggg;
    SQLiteDatabase db;
    ArrayList<ListProvider> arrayList = new ArrayList<ListProvider>();
    GridView gdv;

    public RecyclerAdapter1(ArrayList<ListProvider> arrayList) {
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
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        final ListProvider listProvider;
        listProvider = arrayList.get(position );
        holder.item_sno.setText(listProvider.getSno());
        holder.item_product.setText(listProvider.getProduct());
        holder.item_rate.setText(listProvider.getRate().toString());

      //  holder.item_qty.setPaintFlags(holder.item_qty.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.item_qty.setText(listProvider.getQty());
        holder.item_amt.setText(listProvider.getAmt().toString());
    }

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

            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
            }
    }
    public void removeItem(int position) {
        if(arrayList.size()!=0) {
            Log.i("Check",""+position);
            arrayList.remove(position);
            notifyItemRemoved(position);
        }
    }
}


