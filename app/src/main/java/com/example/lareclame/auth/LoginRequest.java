package com.example.lareclame.auth;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class LoginRequest extends StringRequest{
    private static final String LoginRequestUrl="http://2.57.186.206:5001/auth/login";
    private final Map <String, String> params;

    public LoginRequest(String username, String password, Response.Listener<String> listener, Response.ErrorListener err) {
        super(Method.POST, LoginRequestUrl, listener, err);
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
