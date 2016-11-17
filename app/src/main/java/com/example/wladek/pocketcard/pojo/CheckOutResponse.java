package com.example.wladek.pocketcard.pojo;

/**
 * Created by wladek on 10/21/16.
 */
public class CheckOutResponse {
    private boolean checkedOut;
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
