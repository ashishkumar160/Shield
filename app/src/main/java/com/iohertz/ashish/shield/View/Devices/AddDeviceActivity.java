package com.iohertz.ashish.shield.View.Devices;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
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
import com.iohertz.ashish.shield.View.LoginSignup.LoginSignupActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AddDeviceActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private EditText systemName;
    private boolean primary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        TextView toolbar_text = (TextView) findViewById(R.id.toolbar_text);
        toolbar_text.setText(R.string.add_device);
        mSharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        systemName = (EditText) findViewById(R.id.text_new_system_name);
        primary = mSharedPreferences.getBoolean("is_primary",true);
        if (primary) {
            systemName.setVisibility(View.GONE);
        }
    }

    public void ActionAddDevice(View view) {
        EditText deviceName = (EditText) findViewById(R.id.text_new_device_name);
        EditText deviceID = (EditText) findViewById(R.id.text_new_device_id);
        String url;
        JSONObject jsonObject = new JSONObject();
        if (primary) {
            url = "devices";
            try {
                jsonObject.put("device_name", deviceName.getText().toString());
                jsonObject.put("device_id", Long.parseLong(deviceID.getText().toString()));
            }catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            url = "auth/device";
            try {
                jsonObject.put("device_name", deviceName.getText().toString());
                jsonObject.put("device_id", Long.parseLong(deviceID.getText().toString()));
                jsonObject.put("place_name", systemName.getText().toString());
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, mSharedPreferences.getString("IPAddress","http://127.0.0.1:8080/") + url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        Networking.getInstance(getApplicationContext()).sendLoginRequest();
                        finish();
                    }
                } catch (JSONException e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("TAG", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> mHeaders = new android.support.v4.util.ArrayMap<String, String>();
                mHeaders.put("Authorization", mSharedPreferences.getString("Token","Token"));
                return mHeaders;
            }
        };
        Networking.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}
