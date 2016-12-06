package com.example.wladek.pocketcard.net;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.wladek.pocketcard.util.StringUtils;

import org.json.JSONObject;

/**
 * Created by wladek on 9/2/16.
 */
public class CheckoutRequest extends JsonObjectRequest {

    private static final String REGISTER_REQUEST_URL = StringUtils.SERVER_URL + "/student_checkout";

    public CheckoutRequest(JSONObject jsonObject, Response.Listener<JSONObject> listener) {
        super(Method.POST ,REGISTER_REQUEST_URL, jsonObject, listener, null);
    }

//    public CheckoutRequest(JSONObject jsonRequest, Response.Listener<JSONObject> listener) {
//        super(REGISTER_REQUEST_URL, jsonRequest, listener, null);
//    }
}