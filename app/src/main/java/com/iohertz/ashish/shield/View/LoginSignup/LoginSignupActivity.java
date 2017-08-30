package com.iohertz.ashish.shield.View.LoginSignup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.iohertz.ashish.shield.Controller.Networking;
import com.iohertz.ashish.shield.R;
import com.iohertz.ashish.shield.View.Home.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class LoginSignupActivity extends AppCompatActivity {

    private FragmentTransaction fragmentTransaction;
    private Networking mNetworking;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.login_frame, new LoginSignUpFragment());
        fragmentTransaction.commitAllowingStateLoss();
        mNetworking = Networking.getInstance(this);
        mSharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public void loginAction(View view) {
        EditText mIPAdress = (EditText) findViewById(R.id.ip_address);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.login_frame, new LoginFragment());
        fragmentTransaction.commitAllowingStateLoss();
        mEditor.putString("IPAddress",mIPAdress.getText().toString());
        mEditor.commit();
    }

    public void signupAction(View view) {
        EditText mIPAdress = (EditText) findViewById(R.id.ip_address);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.login_frame, new SignUpFragment());
        fragmentTransaction.commitAllowingStateLoss();
        mEditor.putString("IPAddress",mIPAdress.getText().toString());
        mEditor.commit();
    }

    public void ActionLogging(View view) {
        EditText loginText = (EditText) findViewById(R.id.login_mobile);
        EditText passwordText = (EditText) findViewById(R.id.login_password);
        mEditor.putString("mobile",loginText.getText().toString());
        mEditor.putString("password",passwordText.getText().toString());
        mEditor.commit();
        mNetworking.sendLoginRequest();
    }

    public void ActionRegister(View view) {
        final EditText nameText = (EditText) findViewById(R.id.signup_name);
        final EditText mobileText = (EditText) findViewById(R.id.signup_mobile);
        final EditText passwordText = (EditText) findViewById(R.id.signup_password);
        EditText emailText = (EditText) findViewById(R.id.signup_email);
        mEditor.putString("name",nameText.getText().toString());
        mEditor.putString("mobile",mobileText.getText().toString());
        mEditor.putString("password",passwordText.getText().toString());
        mEditor.putString("email",emailText.getText().toString());
        mEditor.commit();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", nameText.getText().toString());
            jsonObject.put("mobile", Long.parseLong(mobileText.getText().toString()));
            jsonObject.put("password", passwordText.getText().toString());
            jsonObject.put("email", emailText.getText().toString());
        }catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, mSharedPreferences.getString("IPAddress","http://127.0.0.1:8080/") + "auth/signup", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        mNetworking.sendLoginRequest();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("TAG",error.toString());
            }
        });
        mNetworking.addToRequestQueue(jsonObjectRequest);
    }
}
