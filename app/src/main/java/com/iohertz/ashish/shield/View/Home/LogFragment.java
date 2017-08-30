package com.iohertz.ashish.shield.View.Home;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.iohertz.ashish.shield.Controller.LogAdapter;
import com.iohertz.ashish.shield.Controller.Networking;
import com.iohertz.ashish.shield.Model.Log;
import com.iohertz.ashish.shield.R;
import com.iohertz.ashish.shield.View.other.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogFragment extends Fragment {

    private List<Log> logList;
    private RecyclerView recyclerView;
    private LogAdapter mAdapter;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    public LogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_log, container, false);
        mSharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.log_recycler_view);
        logList = new ArrayList<>();
        mAdapter = new LogAdapter(getContext(), logList);
        recyclerView.setAdapter(mAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));


        prepareLogData();

        return rootView;
    }

    private void prepareLogData() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, mSharedPreferences.getString("IPAddress","http://127.0.0.1:8080/") + "activities", null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        JSONArray logs = response.getJSONArray("message");
                        for(int i=0 ; i<logs.length() ; i++) {
                            JSONObject log = logs.getJSONObject(i);
                            android.util.Log.v("TAG",log.toString());
                            logList.add(new Log(log.getString("message"), log.getString("createdAt"), R.drawable.ic_activity_locked));
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                android.util.Log.v("TAG", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> mHeaders = new android.support.v4.util.ArrayMap<String, String>();
                mHeaders.put("Authorization", mSharedPreferences.getString("Token","Token"));
                return mHeaders;
            }
        };
        Networking.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
    }

}
