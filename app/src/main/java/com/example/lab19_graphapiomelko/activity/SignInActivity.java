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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab19_graphapiomelko.R;
import com.example.lab19_graphapiomelko.database.GraphDB;
import com.example.lab19_graphapiomelko.helper.Request;
import com.example.lab19_graphapiomelko.helper.StaticData;
import com.example.lab19_graphapiomelko.model.Session;
import com.example.lab19_graphapiomelko.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class SignInActivity extends AppCompatActivity {

    EditText etLogin;
    EditText etPassword;
    Switch swtRemPas;
    String userName;
    String userSecret;
    String tempToken = "";
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
        if (StaticData.DB.GetSettingsID() < 0) {
            StaticData.DB.SetSettings(User.getCSTR());
            User.setSettings();
            return;
        } else {
            User.getSettings();
            if ((User.getName().length() == 0 || User.getName().equals("null"))||(User.getPassword().length() == 0||User.getPassword().equals("null"))) {
                //ALDWindow.msgInfo(ctx,getResources().getString(R.string.txtInfo),"Пользователь не найден");
                swtRemPas.setChecked(false);
                return;
            } else {
                etLogin.setText(User.getName());
                etPassword.setText(User.getPassword());
                swtRemPas.setChecked(true);
                if(User.getToken().length()>0&&!User.getToken().equals("null")){
                   tempToken = User.getToken();
                    ListSession(tempToken,StaticData.Sessions);
                    if(StaticData.Sessions.size()>0){
                        validToken = true;
                        userName = User.getName();
                        userSecret = User.getPassword();
                        Login();
                    }else {
                        ALDWindow.msgInfo(ctx,getResources().getString(R.string.txtInfo),getResources().getString(R.string.msgInvalidToken));
                    }
                }else{
                    ALDWindow.msgInfo(ctx,getResources().getString(R.string.txtInfo),getResources().getString(R.string.msgInvalidToken));
                }
            }
        }
    }

    /**
     * Method executes actions by call this activity when calling this activity from another activity
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        validToken=false;
        super.onActivityResult(requestCode, resultCode, data);
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
            ALDWindow.msgInfo(ctx, getResources().getString(R.string.txtInfo), getResources().getString(R.string.msgIncorrectLoginPassword));
            return;
        } else {
            OpenSession(userName, userSecret);
            if(validToken){
                Login();
            }else {
                return;
            }
        }
    }
    /**
     * Actions by click on register button
     *
     * @param v
     */
    public void onBtnRegisterClick(View v) {
        Intent i = new Intent(ctx, SignUpActivity.class);
        startActivityForResult(i, StaticData.LoginToRegisterCode.getCode());
    }

    /**
     * Actions by click on set setting button
     *
     * @param v
     */
    public void onBtnSettingClick(View v) {
        AlertDialog.Builder window = new AlertDialog.Builder(ctx);
        window.setTitle(getResources().getString(R.string.txtCSTR));
        window.setMessage(getResources().getString(R.string.txtValue));
        final EditText etInput = new EditText(ctx);
        etInput.setText(User.getCSTR());
        window.setView(etInput);
        window.setPositiveButton(getResources().getString(R.string.txtApply), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                User.setCSTR(etInput.getText().toString());
                StaticData.DB.SetSettingsCSTR(User.getCSTR());
            }
        });
        window.setNegativeButton(getResources().getString(R.string.txtCancel), new DialogInterface.OnClickListener() {
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
        if (swtRemPas.isChecked()){
            User.setName(userName);
            User.setPassword(userSecret);
            User.setToken(tempToken);
        }else{
            User.setName(null);
            User.setPassword(null);
            User.setToken(null);
        }
        User.setSettings();
        Intent i = new Intent(ctx, MenuActivity.class);
        startActivityForResult(i, StaticData.LoginToMenuCode.getCode());
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
                    String er = getResources().getString(R.string.msgRequestFailed) + "\n" + errorMsg;
                    ALDWindow.msgInfo(actv, getResources().getString(R.string.txtError), er);
                });
            }
            @Override
            public void onSuccess(String res) {
                try {
                    Log.e("test", res.toString());//Call nullReferenceException for to check what required data is complete
                    Log.e("test", "Success");
                    validToken = true;
                    JSONObject obj = new JSONObject(res);
                    tempToken = obj.getString("token");//getting token
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        try {
            openSession.isSuccessful = false;
            openSession.send(actv, "PUT", "/session/open?name=" + userName + "&secret=" + userSecret);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        try {
            openSession.requestSender.join();
            Log.e("test", "Joined");
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        if (openSession.isSuccessful) {
            try {
                openSession.onSuccess(openSession.output);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            validToken=false;
            return;
        }
    }

    public void ListSession(String token, ArrayList<Session> sessionList) {
        Log.e("test", "Preparing");
        Request listSession = new Request(actv) {
            @Override
            public void onFail(String errorMsg) {
                actv.runOnUiThread(() ->
                {
                    String message = getResources().getString(R.string.msgRequestFailed) + "\n" + errorMsg;
                    ALDWindow.msgInfo(actv, getResources().getString(R.string.txtError), message);
                });
            }

            @Override
            public void onSuccess(String res) {
                try {
                    JSONArray jsonArray = new JSONArray(res);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Session s = new Session(jsonObject.getInt("id"), jsonObject.getString("token"), jsonObject.getString("timestamp"));
                        sessionList.add(s);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                Log.e("test", res.toString());//Call nullReferenceException for to check what required data is complete
                Log.e("test", "Success");
            }
        };
        try {
            listSession.isSuccessful = false
            ;
            listSession.send(actv, "GET", "/session/list?token=" + token);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        try {
            listSession.requestSender.join();
            Log.e("test", "Joined");
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        if (listSession.isSuccessful) {

            try {
                listSession.onSuccess(listSession.output);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            validToken = false;
            return;
        }
    }
}