package com.example.admin.gows;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class Dashboard extends Fragment {

    SQLiteDatabase db;
    String[] n, p;
    ArrayList arrayList  = new ArrayList();
    ArrayList pricelist  = new ArrayList();
    Float[] pp = new Float[pricelist.size()];
    String item_name;

    BarChart barChart;
    PieChart pieChart;
    LineChart lineChart;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.piechart, container, false);
        getActivity().setTitle("DashBoard");

        db = getActivity().openOrCreateDatabase("Master.db", Context.MODE_PRIVATE, null);

        barChart = v.findViewById(R.id.BarChart);
        pieChart = v.findViewById(R.id.pie);
        lineChart = v.findViewById(R.id.linechart);

      //  getDetails();
     //   getTotalprice();
//        drawBarchart();
//        drawPieChart();
      //  drawLinechart();
        //getTotalprice();

        return v;
    }

//    void getDetails()
//    {
//        String select = "SELECT Item_Name from Item";
//        Cursor cc = db.rawQuery(select, null);
//        if (cc.moveToFirst())
//        {
//            do
//            {
//                item_name = cc.getString(0);
//                arrayList.add(item_name);
//                //   Toast.makeText(getContext(),"name : "+list.toString(),Toast.LENGTH_SHORT).show();
//            } while (cc.moveToNext());
//            Log.d("gg", "values: " + arrayList.toString());
//            cc.close();
//        }
//    }
//
//    void getTotalprice()
//    {
//        Float price = 0.0f;
//        int n =0;
//        String select = "SELECT Total_Price from Item";// where Item_Name = '"+item_name+"'";
//        Cursor cc = db.rawQuery(select, null);
//        if (cc.moveToFirst())
//        {
//            do
//            {
//                price = cc.getFloat(0);
//                pricelist.add(price);
//
////                    pp[n] = price;
////
////                    n++;
////                    drawBarchart();
//
//            } while (cc.moveToNext());
//
//            Log.d("gkk", "price: " + pricelist.toString());
//            cc.close();
//        }
//
//    }
//    void drawBarchart(){
//        ArrayList<BarEntry> BarEntry = new ArrayList<>();
//        BarDataSet dataSet = new BarDataSet(BarEntry, "Products");
//        //    dataSet.setBarSpacePercent(50);
//
//
//        for(int i=0;i<pricelist.size();i++){
//            BarEntry.add(new BarEntry(Float.valueOf(String.valueOf(pricelist.get(i))), i));
//            arrayList.add(arrayList.get(i));
//        }
//        BarData data = new BarData(arrayList,dataSet);
//        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
//        // barChart.setBackgroundColor(rgb(68, 112, 85));
//        barChart.setData(data);
//        barChart.setVisibleXRangeMaximum(4);
//     //   barChart.setDescription("Rate of Products");
//        //  barChart.animateX(2000);
//        barChart.animateXY(2000,2000);
//    }
//    void drawPieChart()
//    {
//        ArrayList<Entry> PieEntry = new ArrayList<>();
//        PieDataSet dataSet = new PieDataSet(PieEntry, "Products");
//        //    dataSet.setBarSpacePercent(50);
//
//        for(int i=0;i<pricelist.size();i++){
//            PieEntry.add(new Entry(Float.valueOf(String.valueOf(pricelist.get(i))), i));
//            arrayList.add(arrayList.get(i));
//        }
//
//        PieData data = new PieData(arrayList,dataSet);
//        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
//        // barChart.setBackgroundColor(rgb(68, 112, 85));
//        pieChart.setData(data);
//      //  pieChart.setDescription("Rate of Products");
//        //  barChart.animateX(2000);
//        pieChart.animateXY(2000,2000);
//
//    }

//    void drawLinechart()
//    {
//        lineChart.setDragEnabled(true);
//        lineChart.setScaleEnabled(true);
//
//        //   String[] g = {one,two,}
//        int a[] = {10,20,30,40,50,60};
//        ArrayList<Entry> BarEntry = new ArrayList<>();
//        BarEntry.add(new BarEntry(20f, a[0]));
//        BarEntry.add(new BarEntry(40f, a[1]));
//        BarEntry.add(new BarEntry(50f, a[2]));
//        BarEntry.add(new BarEntry(60f, a[3]));
//        BarEntry.add(new BarEntry(70f, a[4]));
//        BarEntry.add(new BarEntry(80f, a[5]));
//
//        LineDataSet set1 = new LineDataSet(BarEntry,"set1");
//        set1.setFillAlpha(110);
//        set1.setLineWidth(3);
//        set1.setColor(R.color.colorRed);
//        set1.setDrawFilled(true);
//
//        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//        dataSets.add(set1);
//
//        LineData data = new LineData(dataSets);
//        //    data.addDataSet(dataSets);
//        lineChart.setData(data);
//    }

}
