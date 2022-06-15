package com.example.lareclame.requests;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdateUserRequest extends StringRequest {
    private static final String url="http://" + Config.host + ":" + Config.port + "/api/update-user-info";
    private final Map<String, String> params;

    public UpdateUserRequest(int user_id, String username, String telegram, String password, String bio, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(Method.POST, url, listener, errorListener);
        params = new HashMap<>();
        params.put("user_id", user_id+"");
        params.put("username", username);
        params.put("telegram", telegram);
        params.put("password", password);
        params.put("bio", bio);
    }


    public Map<String, String> getParams() {
        return params;
    }
}
