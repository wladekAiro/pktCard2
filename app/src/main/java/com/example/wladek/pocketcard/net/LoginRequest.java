package com.example.wladek.pocketcard.net;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.wladek.pocketcard.util.StringUtils;

import org.json.JSONObject;

/**
 * Created by wladek on 9/2/16.
 */
public class LoginRequest extends JsonObjectRequest {

    private static final String REGISTER_REQUEST_URL = StringUtils.SERVER_URL + "/school_login";

    public LoginRequest(JSONObject jsonRequest, Response.Listener<JSONObject> listener) {
        super(REGISTER_REQUEST_URL, jsonRequest, listener, null);
    }
}