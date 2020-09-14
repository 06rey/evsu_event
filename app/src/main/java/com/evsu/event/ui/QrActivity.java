package com.evsu.event.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.evsu.event.App;
import com.evsu.event.R;
import com.evsu.event.model.User;
import com.google.gson.Gson;
import com.google.zxing.WriterException;

import java.util.HashMap;
import java.util.Map;

public class QrActivity extends AppCompatActivity {

    public final static String SEPARATOR = "//";

    private ViewUtil util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        util = new ViewUtil(this);

        User user = App.instance().getActiveUser();
        Map<String, String> userMap = new HashMap<>();
        userMap.put("profile_id", user.getProfile_id());
        userMap.put("user_id", user.getUser_id());
        userMap.put("full_name", user.getFullName());
        userMap.put("user_level", user.getUser_level());
        userMap.put("college", user.getCollege());

        if (user.getUser_level().toLowerCase().equals(User.STUDENT) || user.getUser_level().toLowerCase().equals(User.ORGANIZATION_PRESIDENT)) {
            userMap.put("course", user.getCourse());
            userMap.put("year_level", user.getYear_level());
        }

        String qrStr = new Gson().toJson(userMap);

        Log.d("ddd", "USER QR: "+ qrStr);

        ImageView qrImage = findViewById(R.id.qr_image);

        try {
            qrImage.setImageBitmap(util.generateQrImage(qrStr, 600, 600));
        } catch (WriterException e) {
            util.messageDialog("", "Error generating QR code.", null);
            e.printStackTrace();
        }
    }
}
