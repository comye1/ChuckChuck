package com.example.chuckchuck;

public class Category {
    private String subjectName;
    private String subjectKey;

    public Category() {
    }

    public Category(String subjectName, String subjectKey) {
        this.subjectName = subjectName;
        this.subjectKey = subjectKey;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectKey() {
        return subjectKey;
    }

    public void setSubjectKey(String subjectKey) {
        this.subjectKey = subjectKey;
    }
}
