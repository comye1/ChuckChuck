package com.example.chuckchuck;

import java.util.ArrayList;
import java.util.List;

public class TimeTable {

    public static List<String> getSubjectList() {
        return subjectList;
    }

    private static List<String> subjectList;
    private static List<String> dayList; //요일 배열 저장
    private static List<String> keyList; //key 배열 저장

    public void declare() {
        subjectList = new ArrayList<>();
        dayList = new ArrayList<>();
        keyList = new ArrayList<>();
    }

    public void clear(){
        subjectList.clear();
        dayList.clear();
        keyList.clear();
    }

    public void add(SubjectInfo subjectInfo, String key){
        subjectList.add(subjectInfo.getSubject());
        dayList.add(subjectInfo.getDays());
        keyList.add(key);
    }

    public void set(SubjectInfo subjectInfo, int position){
        subjectList.set(position, subjectInfo.getSubject());
        dayList.set(position, subjectInfo.getDays());
    }

    public void remove(int position){
        subjectList.remove(position);
        dayList.remove(position);
        keyList.remove(position);
    }
}

