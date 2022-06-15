package com.example.lareclame.requests;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private static final String RegisterRequestUrl="http://" + Config.host + ":" + Config.port + "/api/auth/register";
    private final Map <String, String> params;

    public RegisterRequest (String username, String password, String barcode, Response.Listener <String> listener, Response.ErrorListener err) {
        super(Method.POST, RegisterRequestUrl, listener, err);
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("barcode", barcode);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
