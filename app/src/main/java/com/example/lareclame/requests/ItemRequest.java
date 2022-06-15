package com.example.lareclame.requests;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class ItemRequest extends StringRequest {
    private static final String LoginRequestUrl="http://" + Config.host + ":" + Config.port + "/api/add/item";
    private final Map<String, String> params;

    public ItemRequest(int user_id, int category_id, String title, String body, String price_type, int price, JSONArray pictures, Response.Listener<String> listener, Response.ErrorListener err) {
        super(Method.POST, LoginRequestUrl, listener, err);
        params=new HashMap<>();
        params.put("user_id", user_id + "");
        params.put("category_id", category_id + "");
        params.put("title", title);
        params.put("description", body);
        params.put("price_type", price_type);
        params.put("price", price+"");
        params.put("pictures", pictures.toString());
    }

    @Nullable
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
