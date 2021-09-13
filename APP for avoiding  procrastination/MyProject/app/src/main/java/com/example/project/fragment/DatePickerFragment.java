package com.example.project.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.time.Month;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    private  DatePickerDialog.OnDateSetListener onDateSetListener;
    private int YEAR, MONTH, DAY_OF_MONTH;

    public DatePickerFragment(DatePickerDialog.OnDateSetListener onDateSetListener, int YEAR, int MONTH, int DAY_OF_MONTH) {
        this.onDateSetListener = onDateSetListener;
        this.YEAR = YEAR;
        this.MONTH = MONTH;
        this.DAY_OF_MONTH = DAY_OF_MONTH;
    }

    public DatePickerFragment(DatePickerDialog.OnDateSetListener onDateSetListener) {
        this.onDateSetListener = onDateSetListener;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        if (!(YEAR != 0 && MONTH != 0 && DAY_OF_MONTH != 0)) {
            final Calendar c = Calendar.getInstance();
            YEAR = c.get(Calendar.YEAR);
            MONTH = c.get(Calendar.MONTH);
            DAY_OF_MONTH = c.get(Calendar.DAY_OF_MONTH);
        }
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), onDateSetListener, YEAR, MONTH, DAY_OF_MONTH);
    }

//    public void onDateSet(DatePicker view, int year, int month, int day) {
//        // Do something with the date chosen by the user
//    }

}
