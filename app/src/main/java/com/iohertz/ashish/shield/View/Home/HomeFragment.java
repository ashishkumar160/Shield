package com.iohertz.ashish.shield.View.Home;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.iohertz.ashish.shield.Controller.DeviceAdapter;
import com.iohertz.ashish.shield.Controller.Networking;
import com.iohertz.ashish.shield.Model.Device;
import com.iohertz.ashish.shield.R;
import com.iohertz.ashish.shield.View.other.DividerItemDecoration;
import com.iohertz.ashish.shield.View.other.GridSpacingItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private DeviceAdapter adapter;
    private List<Device> deviceList;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_devices, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.device_recycler_view);

        deviceList = new ArrayList<>();
        adapter = new DeviceAdapter(getContext(), deviceList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        mSharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        prepareDevices();

        return rootView;
    }

    private void prepareDevices() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, mSharedPreferences.getString("IPAddress","http://127.0.0.1:8080/") + "devices", null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        JSONArray devices = response.getJSONArray("message");
                        for(int i=0 ; i<devices.length() ; i++) {
                            JSONObject device = devices.getJSONObject(i);
                            deviceList.add(new Device(device.getString("name"), device.getString("type")));
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
        Networking.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
    }
}
