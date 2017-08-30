package com.iohertz.ashish.shield.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.iohertz.ashish.shield.Model.System;
import com.iohertz.ashish.shield.R;
import com.iohertz.ashish.shield.View.Home.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by ashish on 6/3/17.
 */

public class SystemsAdapter extends RecyclerView.Adapter<SystemsAdapter.ViewHolder> {

    private Context mContext;
    private List<System> systemList;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        public ImageView thumbnail;
        public TextView systemName;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            systemName = (TextView) view.findViewById(R.id.system_name);
            thumbnail = (ImageView)  view.findViewById(R.id.systems_thumbnail);
        }

        @Override
        public boolean onLongClick(View v) {
            Animation shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);

            v.startAnimation(shake);
            return true;
        }

        @Override
        public void onClick(View v) {
            final System system = systemList.get(getPosition());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST, mSharedPreferences.getString("IPAddress", "http://127.0.0.1:8080/") + "places/switch/" + system.getId(), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getBoolean("success")) {
                            Log.v("TAG",response.toString());
                            mEditor.putString("Token",response.getString("token"));
                            mEditor.commit();
                            Intent intent = new Intent(mContext, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);
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
            Networking.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
        }
    }

    public SystemsAdapter(Context mContext, List<System> systemList) {
        this.mContext = mContext;
        this.systemList = systemList;
        mSharedPreferences = mContext.getSharedPreferences(mContext.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.system_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        System system = systemList.get(position);
        holder.systemName.setText(system.getName());

        Glide.with(mContext).load(system.getThumbnail()).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return systemList.size();
    }
}
