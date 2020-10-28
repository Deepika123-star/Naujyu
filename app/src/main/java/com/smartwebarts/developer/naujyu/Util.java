package com.smartwebarts.developer.naujyu;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.appcompat.app.AlertDialog;

import java.util.Calendar;

/**
 * Created by sunny shah on Apr, 2020
 */
final public class Util {
    public Util() {
    }

    public static boolean isValidPhoneNumber(CharSequence phoneNumber) {
        return !TextUtils.isEmpty(phoneNumber) && Patterns.PHONE.matcher(phoneNumber).matches();
    }

    public static String getPhoneNumberWithPlus(String phoneNumber, String phoneCode) {
        return "+".concat(phoneCode).concat(phoneNumber).trim();
    }

    public static boolean isValidEmail(CharSequence email) {
        return !(TextUtils.isEmpty(email)) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String convertDateToDobFormat(String time) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(time));
            stringBuilder.append(calendar.get(Calendar.DAY_OF_MONTH))
                    .append("/")
                    .append(calendar.get(Calendar.MONTH) + 1)
                    .append("/")
                    .append(calendar.get(Calendar.YEAR));
        } catch (NumberFormatException ex) {
        }

        return stringBuilder.toString();
    }

    // TODO -> Use google reverse api, to get address
    public static String convertLatLongToAddress(double latitude, double longitiude) {
        if (latitude == 0.0 || longitiude == 0.0) {
            return null;
        }
        return String.valueOf(latitude).concat("      ")
                .concat(String.valueOf(longitiude));
    }

    public boolean isNetworkAvialable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void showNetworkError(Context context) {

        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle(context.getString(R.string.network_error));
        dialog.setMessage(context.getString(R.string.internet_error_warning_message));
        dialog.setIcon(context.getDrawable(R.drawable.ic_network_check_black_24dp));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
