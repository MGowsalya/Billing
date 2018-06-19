package com.example.admin.gows;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Database extends Activity {
    SQLiteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = getApplicationContext().openOrCreateDatabase("Master.db",Context.MODE_PRIVATE,null);

        //Company Table:
        db.execSQL("create table if not exists Company(Code Integer primary key autoincrement,Company_name varchar,Address text,State varchar," +
                "Postal_code varchar,GST_no varchar,Phone bigint,Mobile_no bigint,Created_date date,Created_time time," +
                "Bill_prefix varchar);");
        //Item Table:
        db.execSQL("create table if not exists Item(Item_Code integer primary key autoincrement,Item_Name text," +
                "Category_Code int,Item_Type varchar,Tax1 varchar,Tax2 varchar,Tax3 varchar,Tax4 varchar,Rate float," +
                "HSNcode varchar(50),Total_Price float,Tax_Price float,Created_date Date,Created_time time,Enable int,Favour int,Tax_Percent float);");

        //Category table:
        db.execSQL("create table if not exists Category(Category_Code Integer primary key autoincrement,Name varchar,Created_date Date,Created_time Time,Enable int)");

        //Taxes table:
        db.execSQL("create table if not exists Taxes(Name text,Percentage varchar)");

        //Billing table
        db.execSQL("create table if not exists Billing(ID integer primary key autoincrement,Bill_no bigint(11),bdate date,pcode int," +
                "Product varchar,Rate float,Tax int,Qty int,Amount float,Total float,Created_date Date,Created_time time,Enable int)");
    }
}
