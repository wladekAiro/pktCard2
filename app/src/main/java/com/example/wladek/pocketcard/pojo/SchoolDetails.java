package com.example.wladek.pocketcard.pojo;

import java.io.Serializable;

/**
 * Created by wladek on 8/13/16.
 */
public class SchoolDetails implements Serializable{
    private String schoolName;
    private String schoolcode;
    private int loggedIn;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolcode() {
        return schoolcode;
    }

    public void setSchoolcode(String schoolcode) {
        this.schoolcode = schoolcode;
    }

    public int getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(int loggedIn) {
        this.loggedIn = loggedIn;
    }
}
