package com.evsu.event.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.evsu.event.R;
import com.evsu.event.util.InputDialogInterface;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.lang.reflect.Method;

import androidx.annotation.NonNull;

public class ViewUtil {

    private Context context;
    private AlertDialog loadingDialog;

    public ViewUtil(Context context) {
        this.context = context;
    }

    private ViewUtil() { }

    public static ViewUtil create() {
        return new ViewUtil();
    }

    // Message dialog helper
    public void messageDialog(String title, String message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", listener)
                .create()
                .show();
    }

    public void failedDialog(String title, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                .setTitle(title)
                .setMessage("Something went wrong there. Make sure you are connected to internet and try again.")
                .setCancelable(false)
                .setPositiveButton("OK", listener)
                .create()
                .show();
    }

    // Http errors

    public void serverErrorDialog(DialogInterface.OnClickListener listener) {
        messageDialog(
                "",
                "500 Internal Server Error Oh no! Something bad happened. Please come back later when we fixed that problem. Thanks.",
                listener
        );
    }

    public void serverUnavailableDialog(DialogInterface.OnClickListener listener) {
        messageDialog(
                "",
                "Service is not available at the moment. Try again later. Thanks.",
                listener
        );
    }

    // Confirm dialog
    public void confirmDialog(String message, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative) {
        new AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("YES", positive)
                .setNegativeButton("CANCEL", negative)
                .create()
                .show();
    }

    public void showLoadingDialog(String title) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        View loadingView = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);

        TextView txtLoading = loadingView.findViewById(R.id.txtLoadingText);

        if (title != null) {
            txtLoading.setText(title);
        }

        loadingDialog = alertBuilder.setView(loadingView)
                .setCancelable(false)
                .create();

        loadingDialog.show();
    }

    public void dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    public  void textInputDialog(@NonNull String title, @NonNull String oldVal, @NonNull InputDialogInterface.OnSetListener<String> listener) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_input, null);
        final EditText etInput = dialogView.findViewById(R.id.txt1);
        etInput.setText(oldVal);

        AlertDialog dialog = alertBuilder.setTitle(title)
                .setView(dialogView)
                .setNegativeButton("CANCEL",null)
                .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                    }
                })
                .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSet(dialog, etInput, etInput.getText().toString());
            }
        });
    }

    public Bitmap generateQrImage(String text, int width, int height) throws WriterException, NullPointerException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE,
                    width, height, null);
        } catch (IllegalArgumentException Illegalargumentexception) {
            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        int colorWhite = 0xFFFFFFFF;
        int colorBlack = 0xFF000000;

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? colorBlack : colorWhite;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, width, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    public void hideSpinnerDropdown(Spinner spinner) {
        try {
            Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
            method.setAccessible(true);
            method.invoke(spinner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setHighLightedText(TextView textView, String textToHighlight) {
        String textViewText = textView.getText().toString();
        int ofe = textViewText.indexOf(textToHighlight, 0);
        Spannable wordToSpan = new SpannableString(textView.getText());
        for (int ofs = 0; ofs < textViewText.length() && ofe != -1; ofs = ofe + 1) {
            ofe = textViewText.indexOf(textToHighlight, ofs);
            if (ofe == -1)
                break;
            else {
                // set color here
                wordToSpan.setSpan(new BackgroundColorSpan(textView.getResources().getColor(R.color.colorAccent)), ofe, ofe + textToHighlight.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(wordToSpan, TextView.BufferType.SPANNABLE);
            }
        }
    }
}
