package com.example.chuckchuck;

import java.util.ArrayList;

public class DateAndRecords {
    String date;
    ArrayList<Record> recordArrayList;

    public DateAndRecords() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Record> getRecordArrayList() {
        return recordArrayList;
    }

    public void setRecordArrayList(ArrayList<Record> recordArrayList) {
        this.recordArrayList = recordArrayList;
    }
}
