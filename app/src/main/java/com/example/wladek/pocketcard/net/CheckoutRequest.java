package com.example.wladek.pocketcard.net;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.wladek.pocketcard.util.StringUtils;

import org.json.JSONArray;

/**
 * Created by wladek on 9/2/16.
 */
public class CheckoutRequest extends JsonArrayRequest {

    private static final String REGISTER_REQUEST_URL = StringUtils.SERVER_URL + "/student_checkout";

    public CheckoutRequest(JSONArray jsonArray, Response.Listener<JSONArray> listener) {
        super(Method.POST ,REGISTER_REQUEST_URL, jsonArray, listener, null);
    }

//    public CheckoutRequest(JSONObject jsonRequest, Response.Listener<JSONObject> listener) {
//        super(REGISTER_REQUEST_URL, jsonRequest, listener, null);
//    }
}