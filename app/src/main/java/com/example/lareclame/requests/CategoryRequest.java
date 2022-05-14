package com.example.lareclame.requests;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CategoryRequest extends StringRequest {
    private static final String CategoryRequestUrl = "http://" + Config.host + ":" + Config.port + "/";
    private final Map<String, String> params;

    public CategoryRequest(Response.Listener<String> listener, Response.ErrorListener err) {
        super(Method.POST, CategoryRequestUrl, listener, err);
        params = new HashMap<>();
    }

    @Nullable
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
