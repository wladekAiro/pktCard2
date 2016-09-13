package com.example.wladek.pocketcard.pojo;

import java.io.Serializable;

/**
 * Created by wladek on 8/13/16.
 */
public class SchoolDetails implements Serializable{
    private String schoolName;
    private String schoolCode;
    private Boolean loggedIn;
    private String logInResponse;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public Boolean getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getLogInResponse() {
        return logInResponse;
    }

    public void setLogInResponse(String logInResponse) {
        this.logInResponse = logInResponse;
    }
}
