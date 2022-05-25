package com.example.lareclame.requests;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;


public class ImageRequest extends StringRequest{
    private static final String LoginRequestUrl="http://" + Config.host + ":" + Config.port + "/api/get-image";
    private final Map <String, String> params;

    public ImageRequest(String table, String filename, Response.Listener<String> listener, Response.ErrorListener err) {
        super(Method.POST, LoginRequestUrl, listener, err);
        params = new HashMap<>();
        params.put("table", table);
        params.put("filename", filename);
    }

    public ImageRequest(String table, JSONArray filename, Response.Listener<String> listener, Response.ErrorListener err) {
        super(Method.POST, LoginRequestUrl, listener, err);
        params = new HashMap<>();
        params.put("table", table);
        params.put("filename", filename.toString());
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}