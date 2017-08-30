package com.iohertz.ashish.shield.View.Home;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.iohertz.ashish.shield.Controller.Networking;
import com.iohertz.ashish.shield.Controller.UsersAdapter;
import com.iohertz.ashish.shield.Model.Device;
import com.iohertz.ashish.shield.Model.User;
import com.iohertz.ashish.shield.R;
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
public class UserFragment extends Fragment {

    private RecyclerView recyclerView;
    private UsersAdapter adapter;
    private List<User> userList;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_user, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.user_recycler_view);
        mSharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        userList = new ArrayList<>();
        adapter = new UsersAdapter(getContext(), userList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(getContext(), 2, 10, true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareUsers();

        return rootView;
    }

    private void prepareUsers() {
        final int[] images = new int[] {
                R.drawable.face1,
                R.drawable.face2,
                R.drawable.face3
        };

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, mSharedPreferences.getString("IPAddress","http://127.0.0.1:8080/") + "users", null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        JSONArray users = response.getJSONArray("message");
                        for(int i=0 ; i<users.length() ; i++) {
                            JSONObject user = users.getJSONObject(i);
                            userList.add(new User(user.getString("name"),images[0]));
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
