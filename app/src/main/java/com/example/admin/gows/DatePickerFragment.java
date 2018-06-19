package com.example.admin.gows;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by ADMIN on 12/30/2017.
 */

public class DatePickerFragment extends DialogFragment {
    DatePickerDialog.OnDateSetListener ondateSet;
    private int year, month, day;


    public DatePickerFragment() {}

    public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {

        ondateSet = ondate;
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
        DatePickerDialog dialog = new DatePickerDialog(getContext(), ondateSet, year, month, day);
   //     dialog.getDatePicker().setMinDate(new Date().getTime());
        return dialog;

        //return new DatePickerDialog(getActivity(), ondateSet, year, month, day);
    }
}

