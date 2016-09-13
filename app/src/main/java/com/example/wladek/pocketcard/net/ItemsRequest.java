package com.example.wladek.pocketcard.net;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.wladek.pocketcard.util.StringUtils;

import org.json.JSONArray;

import java.util.Map;

/**
 * Created by wladek on 9/2/16.
 */
public class ItemsRequest extends JsonArrayRequest {

    private static final String ITEMS_REQUEST_URL = StringUtils.SERVER_URL + "/school_items";
    private Map<String , String> params;

    public ItemsRequest(String schoolCode , Response.Listener<JSONArray> listener) {
        super(ITEMS_REQUEST_URL+"/"+schoolCode , listener, null);
    }


//    public ItemsRequest(String name, String username, String password, Response.Listener<String> listener){
//        super(Method.POST , REGISTER_REQUEST_URL , listener , null);
//        params = new HashMap<>();
//        params.put("name" , name);
//        params.put("username" , username);
//        params.put("password" , password);
//    }
}