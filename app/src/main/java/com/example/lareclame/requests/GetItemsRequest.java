package com.example.lareclame.requests;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class GetItemsRequest extends StringRequest{
    private static final String LoginRequestUrl="http://" + Config.host + ":" + Config.port + "/api/items";
    private final Map <String, String> params;

    public GetItemsRequest(Response.Listener<String> listener, Response.ErrorListener err, String search, int category_id, int user_id) {
        super(Method.POST, LoginRequestUrl, listener, err);
        params = new HashMap<>();
        if (!Objects.equals(search, "")) params.put("search", search);
        if (category_id != 0) params.put("filter_by", category_id + "");
        if (user_id != 0) params.put("user_id", user_id + "");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
