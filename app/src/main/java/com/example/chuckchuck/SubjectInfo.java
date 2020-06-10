package com.example.chuckchuck;

public class SubjectInfo{
    private String subject;
    private String days;

    public SubjectInfo(){

    }

    public SubjectInfo(String subject, String days) {
        this.subject = subject;
        this.days = days;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }


}
