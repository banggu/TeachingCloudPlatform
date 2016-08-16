package com.scnu.bangzhu.teachingcloudplatform.model;

/**
 * Created by bangzhu on 2016/6/8.
 */
public class StudentSign {
    private int id;
    private String name;
    private int isSigned;
    private String signTime;

    public StudentSign() {
    }

    public StudentSign(int id, String name, int isSigned, String signTime) {
        this.id = id;
        this.name = name;
        this.isSigned = isSigned;
        this.signTime = signTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsSigned() {
        return isSigned;
    }

    public void setIsSigned(int isSigned) {
        this.isSigned = isSigned;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }
}
