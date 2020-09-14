package com.evsu.event.util;

import android.app.AlertDialog;
import android.widget.EditText;

public interface InputDialogInterface {

    interface OnSetListener<T> {

        void onSet(AlertDialog dialog, EditText editText, T value);
    }

}
