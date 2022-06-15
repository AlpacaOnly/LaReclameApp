package com.example.lareclame.requests;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class UploadImageRequest extends StringRequest{
    private static final String UploadImageURL="http://" + Config.host + ":" + Config.port + "/api/update/profile-picture";
    private final Map <String, String> params;

    public UploadImageRequest(int user_id, String encodedImage, Response.Listener<String> listener, Response.ErrorListener err) {
        super(Method.POST, UploadImageURL, listener, err);
        params = new HashMap<>();
        try {
            URLEncoder.encode(encodedImage, StandardCharsets.UTF_8.toString());
            params.put("user_id", user_id+"");
            params.put("encodedImage", encodedImage);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
