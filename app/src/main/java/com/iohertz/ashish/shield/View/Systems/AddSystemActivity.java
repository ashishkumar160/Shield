package com.iohertz.ashish.shield.View.Systems;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AddSystemActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_system);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        TextView textView = (TextView) findViewById(R.id.toolbar_text);
        textView.setText(R.string.add_system);
        mSharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public void ActionAddPlace(View view) {
        EditText placeName = (EditText) findViewById(R.id.text_new_system_name);
        EditText deviceName = (EditText) findViewById(R.id.text_new_device_name);
        EditText deviceId = (EditText) findViewById(R.id.text_new_device_id);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_name", deviceName.getText().toString());
            jsonObject.put("device_id", Long.parseLong(deviceId.getText().toString()));
            jsonObject.put("place_name", placeName.getText().toString());
        }catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, mSharedPreferences.getString("IPAddress", "http://127.0.0.1:8080/") + "places", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("TAG", response.toString());
                finish();
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
