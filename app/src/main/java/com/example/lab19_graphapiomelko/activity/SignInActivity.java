package com.example.lab19_graphapiomelko.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab19_graphapiomelko.R;
import com.example.lab19_graphapiomelko.database.GraphDB;
import com.example.lab19_graphapiomelko.helper.Request;
import com.example.lab19_graphapiomelko.helper.StaticData;
import com.example.lab19_graphapiomelko.model.User;

import org.json.JSONException;
import org.json.JSONObject;


public class SignInActivity extends AppCompatActivity {

    EditText etLogin;
    EditText etPassword;
    Switch swtRemPas;
    String userName;
    String userSecret;
    String tempToken="";
    String tempCSTR;
    Activity actv;
    Context ctx;
    boolean validToken = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        etLogin = findViewById(R.id.etLoginSignIn);
        etPassword = findViewById(R.id.etPasswordSignIn);
        swtRemPas = findViewById(R.id.swtRemPasSignIn);
        actv = this;
        ctx = this;
        StaticData.ff = 0;
        StaticData.SetCodes();
        StaticData.DB = new GraphDB(ctx, "GraphsAPI.db", null, 1);
        tempToken = Usr
    }

    /**
     * Actions by click on login button
     *
     * @param v
     */
    public void onBtnLoginClick(View v) {
        userName = etLogin.getText().toString();
        userSecret = etPassword.getText().toString();
        if ((userName.length() > 30 || userName.length() < 3) || (userSecret.length() > 30) || userSecret.length() < 3) {
            ALDWindow.msgInfo(ctx,getResources().getString(R.string.txtInfo), getResources().getString(R.string.msgIncorrectLoginPassword));
            return;
        } else {
            OpenSession(userName, userSecret);
            Login();
        }
    }

    /**
     * Actions by click on set setting button
     *
     * @param v
     */
    public void onBtnSettingClick(View v) {
        AlertDialog.Builder window = new AlertDialog.Builder(ctx);
        window.setTitle("Adding text");
        window.setMessage("Enter text");
        final EditText etInput = new EditText(ctx);
        etInput.setText(User.getCSTR());
        window.setView(etInput);
        window.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                User.setCSTR("cstr");
            }
        });
        window.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                return;
            }
        });
        window.show();
    }

    /**
     * Method execute authorization
     */
    public void Login() {
        if(tempToken.length()>0 && validToken){
            User.setName(userName);
            User.setPassword(userSecret);
            User.setToken(tempToken);
            User.setSettings();
            Intent i = new Intent(ctx, MenuActivity.class);
            startActivityForResult(i, StaticData.LoginToMenuCode.getCode());
        }else{
            return;
        }

    }

    /**
     * Method implements getting token from server by sending request for opening session
     */
    public void OpenSession(String userName, String userSecret) {
        Log.e("test", "Preparing");
        Request openSession = new Request(actv) {
            @Override
            public void onFail(String errorMsg) {
                actv.runOnUiThread(() ->
                {
                    String er= getResources().getString(R.string.msgRequestFailed)+"\n"+errorMsg;
                    ALDWindow.msgInfo(actv,getResources().getString(R.string.txtError), er);
                });
            }
            @Override
            public void onSuccess(String res) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(res);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                //String token = obj.getString("token");
                Log.e("test", res.toString());//Call nullReferenceException for to check what required data is complete
                Log.e("test", "Success");
                validToken=true;
                //tempToken =

            }
        };
        try{
            openSession.isSuccessful=true;
            openSession.send(actv, "PUT", "/session/open?name=" + userName + "&secret=" + userSecret);
        }catch (Exception ex){
            ex.printStackTrace();
            return;
        }
        if (openSession.isSuccessful){
            try {
                openSession.requestSender.join();
                Log.e("test", "Joined");

            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            try {
                openSession.onSuccess(openSession.output);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }else{
            return;
        }
    }
}