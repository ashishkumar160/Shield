package com.iohertz.ashish.shield.View.Systems;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.iohertz.ashish.shield.Controller.SystemsAdapter;
import com.iohertz.ashish.shield.Model.Device;
import com.iohertz.ashish.shield.Model.System;
import com.iohertz.ashish.shield.R;
import com.iohertz.ashish.shield.View.other.GridSpacingItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ManageSystemsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SystemsAdapter adapter;
    private List<System> systemList;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_systems);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        mSharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        TextView toolbartext = (TextView) findViewById(R.id.toolbar_text);
        toolbartext.setText(R.string.managesystems);

        recyclerView = (RecyclerView) findViewById(R.id.systems_recycler_view);

        systemList = new ArrayList<>();
        adapter = new SystemsAdapter(this, systemList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(this, 2, 10, true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        prepareSystems();

    }

    public void addSystem(View v) {
        startActivity(new Intent(this, AddSystemActivity.class));
    }

    private void prepareSystems() {
        final int[] images = new int[] {
                R.drawable.home_system,
                R.drawable.office_system
        };

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, mSharedPreferences.getString("IPAddress", "http://127.0.0.1:8080/") + "places", null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        JSONArray systems = response.getJSONArray("message");
                        for(int i=0 ; i<systems.length() ; i++) {
                            JSONObject system = systems.getJSONObject(i);
                            systemList.add(new System(system.getString("name"), system.getString("id"), images[0]));
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
