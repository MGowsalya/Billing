package com.example.admin.gows;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Charts extends Fragment{
    SQLiteDatabase db;
    Button fromdate_button,todate_button;
    TextView t1,t2;
    Button ok;
    BarChart barChart;
    PieChart pieChart;
    ArrayList namelist  = new ArrayList();
    ArrayList<Float> pricelist  = new ArrayList<Float>();
    ArrayList datelist  = new ArrayList();
    String standard_date="";
    HashMap<String, String> hashMap = new HashMap<>();
    String piechart_date;
 //   ShimmerFrameLayout shimmerFrameLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.charts, container, false);
        getActivity().setTitle("Charts");
        db = getActivity().openOrCreateDatabase("Master.db", Context.MODE_PRIVATE, null);
        db.execSQL("create table if not exists Item (Item_Code integer primary key autoincrement ,Item_Name text ," +
                "Category_Code int,Item_Type varchar,Tax1 varchar,Tax2 varchar,Tax3 varchar,Tax4 varchar,Rate float," +
                "HSNcode varchar(50),Total_Price float,Tax_Price float,Created_date Date,Created_time time,Enable int,Favour int,Tax_Percent float);");

        fromdate_button = v.findViewById(R.id.fromdate_button);
        todate_button = v.findViewById(R.id.todate_button);
        ok = v.findViewById(R.id.ok_button);
        barChart = v.findViewById(R.id.bar);
        pieChart = v.findViewById(R.id.pie_id);
        t1 = v.findViewById(R.id.text_id);
        t2 = v.findViewById(R.id.text1_id);
//         shimmerFrameLayout =
//                (ShimmerFrameLayout) v.findViewById(R.id.shimmer_view_container);

         fromdate_button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 showFromDatePicker();
             }
         });

        todate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToDatePicker();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             //   shimmerFrameLayout.startShimmerAnimation();
                namelist.clear();
                pricelist.clear();
                datelist.clear();
                t1.setVisibility(View.INVISIBLE);
                t2.setVisibility(View.VISIBLE);
                trial();
            //    Toast.makeText(getContext(),"date : "+fromdate_button.getText(),Toast.LENGTH_SHORT).show();
             //   data();
                drawBarchart();
               // drawPieChart();
            }
        });


    return v;
    }
//    void data()
//    {
//      String from = fromdate_button.getText().toString();
//      String to = todate_button.getText().toString();
//      //  String select = "SELECT Item_Name,Total_Price from Item where Created_date='"+fromdate_button.getText()+"'";
//        String select = "SELECT Item_Name,Total_Price,Created_date from Item where  Created_date  BETWEEN '" + from + "' AND '" + to + "'";
//        Cursor cc = db.rawQuery(select, null);
//        if (cc.moveToFirst())
//        {
//            do
//            {
//                String n  = cc.getString(0);
//                Float tp = cc.getFloat(1);
//                String date = cc.getString(2);
//                namelist.add(n);
//                pricelist.add(tp);
//                datelist.add(date);
////                Toast.makeText(getContext(),"name : "+date,Toast.LENGTH_SHORT).show();
////                Toast.makeText(getContext(),"name : "+n,Toast.LENGTH_SHORT).show();
////                Toast.makeText(getContext(),"name : "+tp,Toast.LENGTH_SHORT).show();
//            } while (cc.moveToNext());
//            //  Log.d("gg", "values: " + arrayList.toString());
//            cc.close();
//        }
//    }

    private void showFromDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }
    private void showToDatePicker() {
        ToDatePicker date = new ToDatePicker();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.set(todate);
        date.show(getFragmentManager(), "Date Picker");
    }
    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth)
        {
            int m = monthOfYear+1;
            if(dayOfMonth<10)
            {
                standard_date= standard_date+(String.valueOf("0"+dayOfMonth) + "-");
            }
            else
            {
                standard_date= standard_date+(String.valueOf(dayOfMonth) + "-");
            }
            if(m<10)
            {
                standard_date= standard_date+(String.valueOf("0"+m) + "-");
            }
            else
            {
                standard_date= standard_date+(String.valueOf(m) + "-");
            }

            standard_date= standard_date+ String.valueOf(year);

            fromdate_button.setText(standard_date);
            standard_date="";
        }
    };
    DatePickerDialog.OnDateSetListener todate = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            int m = monthOfYear+1;
            if(dayOfMonth<10)
            {
                standard_date= standard_date+(String.valueOf("0"+dayOfMonth) + "-");
            }
            else
            {
                standard_date= standard_date+(String.valueOf(dayOfMonth) + "-");
            }
            if(m<10)
            {
                standard_date= standard_date+(String.valueOf("0"+m) + "-");
            }
            else
            {
                standard_date= standard_date+(String.valueOf(m) + "-");
            }
            standard_date= standard_date+ String.valueOf(year);
            todate_button.setText(standard_date);
            standard_date="";
        }
    };

    void drawBarchart()
    {
        ArrayList<BarEntry> BarEntry = new ArrayList<>();
        BarDataSet dataSet = new BarDataSet(BarEntry, "Sales");
        for(int i=0;i<pricelist.size();i++)
        {
            BarEntry.add(new BarEntry(Float.valueOf(String.valueOf(pricelist.get(i))), i));
          //  datelist.add(datelist.get(i));
        }
        BarData data = new BarData(datelist,dataSet);
        data.setValueTextSize(10f);
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
     //   data.setBarWidth(0.8f);
        barChart.setData(data);
        barChart.setVisibleXRangeMaximum(6);
        barChart.animateXY(2000,2000);
        barChart.setDrawBorders(true);
        barChart.setPinchZoom(false);
        barChart.setBorderColor(R.color.colorBlack);
        final XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
       // xAxis.setLabelRotationAngle(10);

//        Legend l = barChart.getLegend();
//        l.setEnabled(true);
//        l.setForm(Legend.LegendForm.SQUARE);
//        l.setTextSize(10f);
//        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);


//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        l.setOrientation(Legend.LegendOrientation.VERTICAL);
//        l.setDrawInside(false);
//        l.setTypeface(mTfLight);

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
               // shimmerFrameLayout.clearAnimation();
                t1.setVisibility(View.VISIBLE);


//                barChart.setDrawBarShadow(true);
//                barChart.invalidate();
                //shimmerFrameLayout.stopShimmerAnimation();
                Float value = Float.valueOf(e.getVal());
                String val = String.valueOf(value);
             //   Toast.makeText(getContext(), "key: "+value, Toast.LENGTH_SHORT).show();

//                SELECT NAME, SUM(SALARY) FROM Employee
//                GROUP BY NAME
//                HAVING SUM(SALARY)>3000;

                //Float agk = 690f;
             //   String select = "SELECT Created_date from Item "


                XAxis xAxis = barChart.getXAxis();
                String x =  xAxis.getValues().get(e.getXIndex());
                String select = "SELECT Created_date,sum(Total_Price) FROM Item  where Created_date ='"+x+"' GROUP BY Created_date HAVING sum(Total_Price)="+val ;

                // String select = "SELECT Created_date,sum(Total_Price) FROM Item GROUP BY Created_date HAVING sum(Total_Price)= '"+value+"'";
         //     String select = "SELECT Created_date,sum(Total_Price) FROM Item GROUP BY Created_date HAVING sum(Total_Price)="+val;

                //  String select = "SELECT Item_Name,sum(Total_Price) FROM Item GROUP BY Created_date HAVING sum(Total_Price)=690";
                Cursor cursor = db.rawQuery(select,null);
                if(cursor.moveToFirst()){
                    do{
                        piechart_date = cursor.getString(0);
                        Float sum = cursor.getFloat(1);
                      //  Toast.makeText(getContext(), "succeded!!"+sum, Toast.LENGTH_SHORT).show();
                    }while (cursor.moveToNext());
                }
                ArrayList piechart_name = new ArrayList();
                ArrayList piechart_total_price = new ArrayList();
                String selectquery = "SELECT Item_Name,Total_Price from Item  where" + " Created_date='"+piechart_date+"'";
                Cursor cursor1 = db.rawQuery(selectquery,null);
                if(cursor1.moveToFirst()){
                    do{
                        String name = cursor1.getString(0);
                        String price = cursor1.getString(1);
                     //   Toast.makeText(getContext(), "succeded!!gm", Toast.LENGTH_SHORT).show();
                        piechart_name.add(name);
                        piechart_total_price.add(price);
                    }while (cursor1.moveToNext());
                }
                ArrayList<Entry> PieEntry = new ArrayList<>();
                PieDataSet dataSet = new PieDataSet(PieEntry, "Products");
                for(int i=0;i<piechart_total_price.size();i++)
                {
                    PieEntry.add(new Entry(Float.valueOf(String.valueOf(piechart_total_price.get(i))), i));
                    piechart_name.add(piechart_name.get(i));
                }
                PieData data = new PieData(piechart_name,dataSet);
                data.setValueTextSize(8f);
                dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
                pieChart.setData(data);
                pieChart.setCenterText(piechart_date);
                pieChart.setCenterTextSize(15);
                pieChart.setHoleColor(Color.rgb(210, 223, 224));
//                Paint p2 = pieChart.getPaint(Chart.PAINT_CENTER_TEXT);
//                p2.setColor(getResources().getColor(R.color.green));
                pieChart.animateXY(2000,2000);
            }

            @Override
            public void onNothingSelected() {

            }
        });

//        barChart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("view:","view: "+view.toString());
//              //  Toast.makeText(getContext(), "view: "+view.toString(), Toast.LENGTH_SHORT).show();
//              //  String key = xAxis.getValues().toString();
//              //  String[] k = key.split("");
//            }
//        });
    }
//    void drawPieChart()
//    {
//        ArrayList<Entry> PieEntry = new ArrayList<>();
//        PieDataSet dataSet = new PieDataSet(PieEntry, "Products");
//         for(int i=0;i<pricelist.size();i++)
//         {
//            PieEntry.add(new Entry(Float.valueOf(String.valueOf(pricelist.get(i))), i));
//            datelist.add(datelist.get(i));
//         }
//        PieData data = new PieData(datelist,dataSet);
//        data.setValueTextSize(8f);
//        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
//        pieChart.setData(data);
//        pieChart.animateXY(2000,2000);
//    }
    void trial()
    {
        String from = fromdate_button.getText().toString();
        String to = todate_button.getText().toString();
      //  String select = "SELECT Item_Name,sum(Total_Price),Created_date from Item GROUP BY Created_date";//='"+from+"'";
        String select = "SELECT Item_Name,sum(Total_Price),Created_date from Item  where" +
                " Created_date BETWEEN '" + from + "' AND '" + to + "' GROUP BY Created_date";//='"+from+"'";
        Cursor cc = db.rawQuery(select, null);
        if (cc.moveToFirst())
        {
            do
            {
                String n  = cc.getString(0);
                Float tp = cc.getFloat(1);
                String date = cc.getString(2);
                namelist.add(n);
                pricelist.add(tp);
                datelist.add(date);
//                Log.d("date:","d: "+name);
//                Log.d("name:","d: "+price);
//                Log.d("price:","d: "+datel);
//                Toast.makeText(getContext(),"gd : "+date,Toast.LENGTH_SHORT).show();
//                Toast.makeText(getContext(),"gn : "+n,Toast.LENGTH_SHORT).show();
//                Toast.makeText(getContext(),"gp : "+tp,Toast.LENGTH_SHORT).show();
            } while (cc.moveToNext());
            //  Log.d("gg", "values: " + arrayList.toString());
            cc.close();
        }
    }
}
