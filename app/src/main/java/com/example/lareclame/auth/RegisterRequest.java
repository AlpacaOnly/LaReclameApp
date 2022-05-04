package com.example.lareclame.auth;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private static final String RegisterRequestUrl="http://134.209.109.73:5001/auth/add/user";
    private final Map <String, String> params;

    public RegisterRequest (String username, String password, String email, Response.Listener <String> listener, Response.ErrorListener err) {
        super(Method.POST, RegisterRequestUrl, listener, err);
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("email", email);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
