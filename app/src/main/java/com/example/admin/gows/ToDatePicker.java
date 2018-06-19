package com.example.admin.gows;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * Created by ADMIN on 1/12/2018.
 */

public class ToDatePicker extends DialogFragment {
   // DatePickerDialog.OnDateSetListener ondateSet;
      DatePickerDialog.OnDateSetListener todateSet;
    private int year, month, day;


    public ToDatePicker() {}

    public void set(DatePickerDialog.OnDateSetListener todate) {

        todateSet = todate;
    }

    @SuppressLint("NewApi")
    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        DatePickerDialog dialog = new DatePickerDialog(getContext(), todateSet, year, month, day);
        Calendar calender = Calendar.getInstance();
        calender.add(Calendar.DAY_OF_YEAR, 1);
             dialog.getDatePicker().setMaxDate(calender.getTimeInMillis());
        return dialog;
        //return new DatePickerDialog(getActivity(), ondateSet, year, month, day);
    }


}
