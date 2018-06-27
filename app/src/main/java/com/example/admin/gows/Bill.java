package com.example.admin.gows;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by ADMIN on 12/12/2017.
 */

public class Bill extends Fragment {
    private SectionsPagerAdapter mSectionsPagerAdapter;


    private ViewPager mViewPager;
    SQLiteDatabase db;
   public static TextView sno,product,rate,qty,amt;
    Button list;
    LinearLayout layoutColor;
    View view1;

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

         view1 = inflater.inflate(R.layout.bill,container,false);
        db = getActivity().openOrCreateDatabase("Master.db", Context.MODE_PRIVATE ,null);

        db.execSQL("create table if not exists Billing(ID integer primary key autoincrement,Bill_no  bigint(11),bdate date,pcode int," +
                "Product Varchar,Rate float,Tax int,Qty int,Amount float,Total float,Created_date Date,Created_time time,Enable int)");
        db.execSQL("create table if not exists Category (Category_Code Integer primary key autoincrement ,Name Varchar,Created_date Date,Created_time Time,Enable int)");

        sno = view1.findViewById(R.id.sno);
        product = view1.findViewById(R.id.Product);
        rate = view1.findViewById(R.id.rate);
         qty = view1.findViewById(R.id.qty);
         amt = view1.findViewById(R.id.Amount);
           list = view1.findViewById(R.id.list);
           layoutColor = view1.findViewById(R.id.layout_color);
       /* recycler = view.findViewById(R.id.recycle);
        recyclerLayoutManager= new LinearLayoutManager(getContext());
        recycler.setLayoutManager(recyclerLayoutManager);
        recycler.setHasFixedSize(true);*/
        getcode();
       /*getProduct();
        getRate();
        getQty();
        getAmt();*/
       /* int count = 0;
         for (String SNO :s_no ) {
             ListProvider listProvider = new ListProvider(SNO, pro_duct.get(count), ra_te.get(count),q_ty.get(count),am_t.get(count));
             arrayList.add(listProvider);
             count++;
         }*/



       /* recyclerAdapter = new RecyclerAdapter(arrayList);
        recycler.setAdapter(recyclerAdapter);*/


        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager)view1. findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout)view1.findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Fragment fragment = new BillList();
               FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.addToBackStack(null);
                //ft.hide(frag);
                ft.commit();
//                Intent in = new Intent(getContext(),BillList.class);
            //    startActivity(in);

            }
        });


        return view1;
    }
public void getcode( ) {
    String select = "Select ID,Product,Rate,Qty,Amount From Billing";
     Cursor c = db.rawQuery(select, null);
    if (c.moveToFirst()) {
        do {
            String var = c.getString(0);
            String var1 = c.getString(1);
           Float var2 = c.getFloat(2);
            String var3 = c.getString(3);
            Float var4 = c.getFloat(4);
            sno.setText(var);
            String pro = var1;
           // String[] pro_duct = pro.split("-");
            product.setText(pro);
          //  Toast.makeText(getContext(),var1,Toast.LENGTH_SHORT).show();
            rate.setText(var2.toString());
            qty.setText(var3);
            amt.setText(var4.toString());
        } while (c.moveToNext());
        c.close();
    }
}

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Bill_adding ca = new Bill_adding();
                  //  Bill_add ca = new Bill_add();

                    return ca;

                case 1:
                  //  Bill_adding ce = new Bill_adding();
                    Bill_edit ce = new Bill_edit();
                    return ce;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "ADD";
                case 1:
                    return "EDIT";
            }
            return null;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Bill");

    }

    @SuppressLint("ResourceAsColor")
    public  void getUpdatecode(String code) {
        String select = "Select ID,Product,Rate,Qty,Amount From Billing Where Product ='" + code + "'";
        Cursor c = db.rawQuery(select, null);
        if (c.moveToFirst()) {
            do {
                String var = c.getString(0);
                String var1 = c.getString(1);
                Float var2 = c.getFloat(2);
                String var3 = c.getString(3);
                Float var4 = c.getFloat(4);
                sno.setText(var);
                String pro = var1;
//                String[] pro_duct = pro.split("-");
                product.setText(pro);
                rate.setText(var2.toString());
                qty.setText(var3);
                amt.setText(var4.toString());
              /*  layoutColor.setBackgroundColor(Color.BLUE);
                new CountDownTimer(5000, 50) {

                    @Override
                    public void onTick(long arg0) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onFinish() {
                        layoutColor.setBackgroundColor(Color.WHITE);
                    }
                }.start();*/
                new Handler().postDelayed(new Runnable() {

                    public void run() {
                        sno.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorBlack));
                        product.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorBlack));
                rate.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorBlack));
                qty.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorBlack));
                amt.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorBlack));

                    }
                }, 4000);

                sno.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorGreen));
                product.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorGreen));
                rate.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorGreen));
                qty.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorGreen));
                amt.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorGreen));

            } while (c.moveToNext());
            c.close();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
