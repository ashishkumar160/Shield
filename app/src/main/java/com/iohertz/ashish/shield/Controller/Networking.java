package com.iohertz.ashish.shield.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.iohertz.ashish.shield.R;
import com.iohertz.ashish.shield.View.Home.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by ashish on 27/3/17.
 */

public class Networking {
    private static Networking mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private Networking(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
        mSharedPreferences = mCtx.getSharedPreferences(mCtx.getString(R.string.preference_file_key), mCtx.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {

                    private final LruCache<String, Bitmap>
                        cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });

    }

    public static synchronized Networking getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Networking(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public void sendLoginRequest() {
        JSONObject request = new JSONObject();
        try {
            request.put("mobile",Long.parseLong(mSharedPreferences.getString("mobile","mobile")));
            request.put("password", mSharedPreferences.getString("password","password"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, mSharedPreferences.getString("IPAddress","http://127.0.0.1:8080/") + "auth/login", request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.v("TAG",response.toString());
                    if (response.has("token")) {
                        mEditor.putString("Token",response.getString("token"));
                        mEditor.commit();
                        if (response.getBoolean("success")) {
                            mEditor.putBoolean("is_primary", true);
                            mEditor.commit();
                            Intent intent = new Intent(mCtx, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            mCtx.startActivity(intent);
                        } else {
                            sendPermissionRequest();
                        }
                    } else {
                        Log.v("TAG", response.getString("message"));
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        addToRequestQueue(jsonObjectRequest);
    }

    private void sendPermissionRequest() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, mSharedPreferences.getString("IPAddress","http://127.0.0.1:8080/") + "auth/permissions", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        sendLoginRequest();
                    } else {
                        mEditor.putBoolean("is_primary", false);
                        mEditor.commit();
                        Intent intent = new Intent(mCtx, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mCtx.startActivity(intent);
                    }
                } catch (JSONException e) {
                    Log.v("TAG", e.toString());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> mHeaders = new ArrayMap<String,String>();
                mHeaders.put("Authorization", mSharedPreferences.getString("Token","Token"));
                return mHeaders;
            }
        };
        addToRequestQueue(jsonObjectRequest);
    }


    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

}
