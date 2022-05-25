package com.example.lareclame.requests;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class GetItemRequest extends StringRequest{
    private static final String LoginRequestUrl="http://" + Config.host + ":" + Config.port + "/api/get/item";
    private final Map <String, String> params;

    public GetItemRequest(int item_id, Response.Listener<String> listener, Response.ErrorListener err) {
        super(Method.POST, LoginRequestUrl, listener, err);
        params = new HashMap<>();
        params.put("item_id", item_id + "");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
