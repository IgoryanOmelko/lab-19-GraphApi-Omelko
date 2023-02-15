package com.example.lab19_graphapiomelko.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import com.example.lab19_graphapiomelko.R;
import com.example.lab19_graphapiomelko.helper.StaticData;


public class MenuActivity extends AppCompatActivity {
    TextView tvUserName;
    TextView tvCurrentToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        tvUserName = findViewById(R.id.tvUserName);
        tvCurrentToken=findViewById(R.id.tvCurrentToken);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null) {

        } else {

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void Initialize(){
        tvUserName.setText("");
        tvCurrentToken.setText("");
    }

}
