package com.evsu.event.ui;

import android.os.Bundle;

import com.evsu.event.App;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.HttpException;

public abstract class BaseActivity<VM extends BaseViewModel> extends AppCompatActivity {

    protected VM vm;
    protected ViewUtil util;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vm = createViewModel();
        util = new ViewUtil(this);
    }

    protected abstract VM createViewModel();

    protected ResponseCallback<Void> mResponseCallback = new ResponseCallback<Void>() {
        @Override
        public void onServerError(Throwable e) {
            util.serverErrorDialog(null);
        }

        @Override
        public void onServerUnavailable(Throwable e) {
            util.serverUnavailableDialog(null);
        }

        @Override
        public void onFailed(Throwable e) {
            util.failedDialog("", null);
        }

        @Override
        public void onError(Throwable e) {
            if (App.instance().getDisplayErrorDialog()) {
                String msg = "";

                if (e instanceof HttpException) {
                    msg = "Status code: "+ String.valueOf(((HttpException)e).code()) + "\n\n";
                }

                msg += App.instance().getLoggedResponseBody();
                util.messageDialog("Http Response", msg, null);
            }
        }
    };
}