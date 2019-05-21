package com.wgu.zbentz2.semestertracker.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UserInterfaceUtils {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    public static void setupCalendar(final EditText field, final Calendar calendar, final Context context) {

        // Set the initial value.
        field.setText(dateFormat.format(calendar.getTime()));

        final DatePickerDialog.OnDateSetListener fieldDate = new DatePickerDialog.OnDateSetListener() {
            @Override public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                // Update the field value on change.
                field.setText(dateFormat.format(calendar.getTime()));
            }
        };

        field.setOnClickListener(
            new View.OnClickListener() {
                @Override public void onClick(View v) {
                    new DatePickerDialog(
                        context,
                        fieldDate,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show();
                }
            }
        );
    }

}
