package com.example.lareclame.requests;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ReviewRequest extends StringRequest {
    private static final String ReviewRequestUrl = "http://" + Config.host + ":" + Config.port + "/api/";
    private final Map<String, String> params;

    public ReviewRequest(int item_id, Response.Listener<String> listener, Response.ErrorListener err) {
        super(Method.POST, ReviewRequestUrl + item_id + "/reviews", listener, err);
        params = new HashMap<>();
    }

    @Nullable
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}