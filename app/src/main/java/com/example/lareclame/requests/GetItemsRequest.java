package com.example.lareclame.requests;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;


public class GetItemsRequest extends StringRequest{
    private static final String LoginRequestUrl="http://" + Config.host + ":" + Config.port + "/api/items";
    private final Map <String, String> params;

    public GetItemsRequest(Response.Listener<String> listener, Response.ErrorListener err, String search) {
        super(Method.POST, LoginRequestUrl, listener, err);
        params = new HashMap<>();
        params.put("search", search);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
