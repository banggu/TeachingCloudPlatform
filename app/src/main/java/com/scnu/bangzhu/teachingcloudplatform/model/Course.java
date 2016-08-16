package com.scnu.bangzhu.teachingcloudplatform.model;

/**
 * Created by bangzhu on 2016/8/5.
 */
public class Course {
    private int courseID;
    private String courseName;
    private String courseTeachter;
    private String courseLocation;
    private int courseWeekday;
    private String beginTime;
    private String endTime;
    private int startWeek;
    private int endWeek;
    private double locationLongitude;
    private double locationLatitude;
    private int courseCredit;
    private String className;

    public Course() {
    }

    public Course(int courseID, String courseName, String courseTeachter, String courseLocation, int courseWeekday, String beginTime, String endTime, int startWeek, int endWeek, double locationLongitude, double locationLatitude, int courseCredit, String className) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.courseTeachter = courseTeachter;
        this.courseLocation = courseLocation;
        this.courseWeekday = courseWeekday;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.locationLongitude = locationLongitude;
        this.locationLatitude = locationLatitude;
        this.courseCredit = courseCredit;
        this.className = className;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseTeachter() {
        return courseTeachter;
    }

    public void setCourseTeachter(String courseTeachter) {
        this.courseTeachter = courseTeachter;
    }

    public String getCourseLocation() {
        return courseLocation;
    }

    public void setCourseLocation(String courseLocation) {
        this.courseLocation = courseLocation;
    }

    public int getCourseWeekday() {
        return courseWeekday;
    }

    public void setCourseWeekday(int courseWeekday) {
        this.courseWeekday = courseWeekday;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    public double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public int getCourseCredit() {
        return courseCredit;
    }

    public void setCourseCredit(int courseCredit) {
        this.courseCredit = courseCredit;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
