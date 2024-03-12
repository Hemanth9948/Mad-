package com.vvit.hemanth2;

public class StudentItem {
    private int roll;
    private String name;

    public StudentItem(  long sid,int roll,String name) {
        this.roll = roll;
        this.name = name;
        this.sid = sid;
        status=" ";
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    private long sid;


    private String status;


    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
