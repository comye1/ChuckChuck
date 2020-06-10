package com.example.chuckchuck;

public class Record extends Content{
    private String subPath;

    public String getSubPath() {
        return subPath;
    }

    public void setSubPath(String subPath) {
        this.subPath = subPath;
    }

    public Record(String keyword, String content, String subPath) {
        super(keyword, content);
        this.subPath = subPath;
    }
}
