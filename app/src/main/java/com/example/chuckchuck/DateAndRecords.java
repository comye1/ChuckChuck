package com.example.chuckchuck;

import java.util.ArrayList;

public class DateAndRecords {
    String date;
    ArrayList<Record> recordArrayList;

    public DateAndRecords() {
        recordArrayList = new ArrayList<>();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void putRecord(Record record){
        recordArrayList.add(record);
    }

    public ArrayList<Record> getRecordArrayList() {
        return recordArrayList;
    }

    public void setRecordArrayList(ArrayList<Record> recordArrayList) {
        this.recordArrayList = recordArrayList;
    }
}
