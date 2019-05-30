package com.wgu.zbentz2.semestertracker.utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UserInterfaceUtils {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    public static Date parseDateString(String dateString) {
        // Just for handling this try/catch in a central place.

        Date date = new Date();

        try {

            date = dateFormat.parse(dateString);

        } catch (ParseException e) {

            e.printStackTrace();

        }

        return date;

    }

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

    public static void toastUser(Context context, String message) {

        // Don't show toast if the message is null.
        if (message != null) {

            Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT)
            .show();

        }

    }

    public static void alertUser(Context context, String message, DialogInterface.OnClickListener dialogListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(message)
            .setPositiveButton(
                context.getString(android.R.string.yes),
                dialogListener
            )
            .setNegativeButton(
                context.getString(android.R.string.no),
                dialogListener
            )
        .show();

    }

    public static void snackbarUser(View view, String message) {

        Snackbar.make(
            view,
            message,
            Snackbar.LENGTH_LONG)
        .show();

    }

    public static int dpToPixel(int dp) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());

    }

}
