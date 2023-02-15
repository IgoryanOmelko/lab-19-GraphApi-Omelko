package com.example.lab19_graphapiomelko.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.GregorianCalendar;

public class Session {
    public int id;
    public String token;
    public String timestamp;

    public Session(int id, String token, String timestamp) {
        this.id = id;
        this.token = token;
        this.timestamp = timestamp;
    }


    public Session(JSONObject obj) throws JSONException {
        id = obj.getInt("id");
        token = obj.getString("token");
        timestamp = obj.getString("timestamp");
    }

    //@RequiresApi(api = Build.VERSION_CODES.O)
    public String date(String s) {
        GregorianCalendar calendar = new GregorianCalendar(1, 0, 1);
        calendar.setTimeInMillis(calendar.getTimeInMillis() + Long.valueOf(s) * 1000);
        Date date = calendar.getTime();
        return date.toGMTString();
    }

    //@RequiresApi(api = Build.VERSION_CODES.O)
    public String toString() {
        return "(" + String.valueOf(id) + ")" + token + " " + date(timestamp);
    }
}
