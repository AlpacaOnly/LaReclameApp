package com.example.lareclame.items;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ItemRequest extends StringRequest {
    private static final String LoginRequestUrl="http://134.209.109.73:5001/items/add/item";
    private final Map<String, String> params;

    public ItemRequest(String username, String title, String body, Response.Listener<String> listener, Response.ErrorListener err) {
        super(Method.POST, LoginRequestUrl, listener, err);
        params=new HashMap<>();
        params.put("username", username);
        params.put("title", title);
        params.put("body", body);
    }

    @Nullable
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
