package com.example.lab19_graphapiomelko.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.lab19_graphapiomelko.R;

public final class ALDWindow {
    public static void msgInfo(Context ctx,String Title, String message){
        LayoutInflater myLayout = LayoutInflater.from(ctx);
        View dialogView = myLayout.inflate(R.layout.msg_window, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setView(dialogView);
        AlertDialog ald = builder.create();
        ald.show();
        Button btnPos = ald.findViewById(R.id.btnPosMsgWindow);
        TextView tvTitle = ald.findViewById(R.id.tvTitleMsgWindow);
        TextView tvMsg = ald.findViewById(R.id.tvMsgMsgWindow);
        tvTitle.setText(Title);
        tvMsg.setText(message);
        btnPos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ald.dismiss();
            }
        });
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                ald.cancel();
//            }
//        });
        ald.show();
    }

}
