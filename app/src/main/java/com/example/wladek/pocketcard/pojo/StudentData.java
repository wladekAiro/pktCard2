package com.example.wladek.pocketcard.pojo;

import java.io.Serializable;

/**
 * Created by wladek on 10/21/16.
 */
public class StudentData implements Serializable {

    private String studentName;
    private String studentNumber;
    private String cardNumber;


    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
