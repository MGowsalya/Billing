package com.example.admin.gows;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class Tax_edit extends Fragment {
    @Nullable

    List<String> na = new ArrayList<String>();
    List<String> pr = new ArrayList<String>();
    //    SQLiteDatabase db;
    Button add, delete;
    SQLiteDatabase db;
    String t_name, var;
    String[] var_n, var_p;
    MaterialBetterSpinner tax_name_spin;//tax_percent_spin;
    EditText percent;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tax_edit, container, false);
        db = getActivity().openOrCreateDatabase("Master.db", Context.MODE_PRIVATE, null);
        db.execSQL("create table if not exists Taxes(Name text,Percentage varchar)");
        add = view.findViewById(R.id.tax_save);
        delete = view.findViewById(R.id.delete);
        tax_name_spin = view.findViewById(R.id.name_spin);
        // tax_percent_spin =view.findViewById(R.id.percent_spin);
        percent = view.findViewById(R.id.tax_edit_percent);

        // jsondetails();
        List<String> lables = getDetails();
        ArrayAdapter<String> Adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_items, lables);
        Adapter.setDropDownViewResource(R.layout.spinner_items);
        tax_name_spin.setAdapter(Adapter);
        tax_name_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                t_name = adapterView.getItemAtPosition(i).toString();

                String selectQuery = "SELECT Percentage FROM Taxes where Name='" + t_name + "'";
                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do {
                        var = cursor.getString(0);
                        // per.add(var);
                    } while (cursor.moveToNext());
                }
                percent.setText(var);
                percent.setSelectAllOnFocus(true);
                percent.selectAll();

            }
        });
        percent.setFilters(new InputFilter[] {
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 3, afterDecimal = 2;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = percent.getText() + source.toString();

                        if (temp.equals(".")) {
                            return "0.";
                        }
                        else if (temp.toString().indexOf(".") == -1) {
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

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  ConvertBtnActionPerformed();
//                try {
//                    jsondetails();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                //    Log.d("tag", jsonObject.toString(2));
                String nam = tax_name_spin.getText().toString();
                String per = percent.getText().toString();
                if (!nam.isEmpty() && !per.isEmpty()) {
                    ContentValues cv = new ContentValues();
                    cv.put("Name", nam);
                    cv.put("Percentage", per);
                    Float pp = Float.valueOf(per);// percent.getText();
                    if(pp<= 100.0f) {
                        db.update("Taxes", cv, "Name ='" + nam + "'", null);
                        Toast.makeText(getContext(), "updated", Toast.LENGTH_SHORT).show();
                        percent.getText().clear();
                        tax_name_spin.getText().clear();
                    }
                    else {
                        Toast.makeText(getContext(), "Percentage should be between 1 to 100..", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Empty values not allowed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nam = tax_name_spin.getText().toString();
                String per = percent.getText().toString();
                String[] n = nam.split("-");
                String[] p = per.split("-");
                if (!nam.isEmpty() && !per.isEmpty()) {
//                    ContentValues cv = new ContentValues();
//                    cv.put("Name", nam);
//                    cv.put("Percentage", per);

                    db.execSQL("DELETE FROM Taxes  WHERE Name = '" + nam + "'");
                    Toast.makeText(getContext(), "deleted", Toast.LENGTH_SHORT).show();
                    List<String> lables = getDetails();
                    ArrayAdapter<String> Adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, lables);
                    Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    tax_name_spin.setAdapter(Adapter);
                    percent.getText().clear();
                    tax_name_spin.getText().clear();
                } else {
                    Toast.makeText(getContext(), "Empty values not allowed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    public List<String> getDetails() {
        List<String> labels = new ArrayList<String>();
        String selectQuery = "SELECT Name FROM Taxes";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(0);
                labels.add(var);
            } while (cursor.moveToNext());
        }
        return labels;
    }

    public List<String> getPercent() {
        List<String> per = new ArrayList<String>();
        String selectQuery = "SELECT Percentage FROM Taxes";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String var = cursor.getString(0);
                per.add(var);
            } while (cursor.moveToNext());
        }
        return per;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}


