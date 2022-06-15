package com.example.lareclame.requests;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddReviewRequest extends StringRequest {
    private static final String CategoryRequestUrl = "http://" + Config.host + ":" + Config.port + "/api/add/review";
    private final Map<String, String> params;

    public AddReviewRequest(int item_id, int user_id, String title, String description, int rating, Response.Listener<String> listener, Response.ErrorListener err) {
        super(Request.Method.POST, CategoryRequestUrl, listener, err);
        params = new HashMap<>();
        params.put("user_id", user_id+"");
        params.put("item_id", item_id+"");
        params.put("title", title);
        params.put("description", description);
        params.put("rating", rating+"");
    }

    @Nullable
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
