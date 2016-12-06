package com.example.wladek.pocketcard.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wladek on 10/21/16.
 */
public class CheckOutResponse {
    @SerializedName("checkedOut")
    private boolean checkedOut;
    @SerializedName("message")
    private String message;

    public boolean isCheckedOut() {
        return checkedOut;
    }

    public void setCheckedOut(boolean checkedOut) {
        this.checkedOut = checkedOut;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
