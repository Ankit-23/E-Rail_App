package com.example.user.e_rail.Utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by user on 5/1/2017.
 */
public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    EditText txtDate;
    public DateDialog(View view){
        txtDate=(EditText)view;
    }



    public Dialog onCreateDialog(Bundle savedInstance){
        Calendar calendar=Calendar.getInstance();
       int  day=calendar.get(Calendar.DAY_OF_MONTH);
       int month=calendar.get(Calendar.MONTH);
       int  year=calendar.get(Calendar.YEAR);
        return new DatePickerDialog(getActivity(),this,year,month,day);

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String monthString="";
        if(month<10)
        {
         monthString="0"+(month+1);
        }
        String date=dayOfMonth+"-"+monthString+"-"+year;
        txtDate.setText(date);
    }
}
