package com.example.lab19_graphapiomelko.helper;


import static android.provider.Settings.System.getString;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.graphics.drawable.IconCompat;

import com.example.lab19_graphapiomelko.R;
import com.example.lab19_graphapiomelko.activity.ALDWindow;
import com.example.lab19_graphapiomelko.model.User;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class Request {
    public static String base = User.getCSTR();
    public Thread requestSender;
    public String output;
    public Activity actv;
    public boolean isSuccessful;
    //public static String token;
    public Request(Activity a){
        Log.e("test","Creating request");
        this.actv=a;
    }
    /**
     * Method execute actions when request succeed
     * For using method, it need to override and add in it own actions
     * @param result result of request execution
     */
    public void onSuccess(String result) {

    }

    /**
     * Method executes action when request failed
     * For using method, it need to override and add in it own actions
     */
    public void onFail(String errorMsg) {
    }
    public void send(Activity actv, String method, String body) {
        Runnable requestTask = new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(base + body);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod(method);
                    InputStream is = con.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[512];
                    String str = "";
                    Log.e("test","Try out to send");
                    isSuccessful=true;
                    while (true) {
                        int length = bis.read(buffer);
                        if(length < 0)break;
                        str += new String(buffer, 0, length);
                        Log.e("test","Getting data");
                    }
//                    for (int i=0;i<5;i++) {
//                        try {
//                            Thread.sleep(1000);
//                            //Log.e("test", "left " + (5-i) + " seconds");
//                            StaticData.ff++;
//                            Log.e("test", "left " + StaticData.ff + " seconds");
//                        } catch (InterruptedException ex) {
//                            ex.printStackTrace();
//                        }
//                    }

                    con.disconnect();
                    final String res = str;
                    output = res;
//                    ctx.runOnUiThread(() -> {
//                        try{onSuccess(res);} catch (Exception e){}
//                    });

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    onFail(e.toString());

                    isSuccessful=false;
                    Log.e("test","Fail");
                    //onFail();
                    //requestSender.interrupt();
                    //requestSender=null;
                    output=null;
                }
            }
        };
        requestSender = new Thread(requestTask);
        requestSender.start();
    }
}