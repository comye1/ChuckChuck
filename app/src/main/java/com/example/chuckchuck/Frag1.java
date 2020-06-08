package com.example.chuckchuck;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.wefika.flowlayout.FlowLayout;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Frag1 extends Fragment {
    private View view;
    private View keywordView, record1;
    private TextView tv_date;
    private LinearLayout linearScroll;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag1, container, false);
        tv_date = view.findViewById(R.id.tv_date);
        tv_date.setText(getDate());
        linearScroll = view.findViewById(R.id.linear_scroll);

        //todo 메소드 만들기 (요일 해당 과목 찾아서 아래 뷰 만들고 추가하기 & 클릭리스너
        //todo firebase에 초기화 (keyword: 키워드)
        record1 = inflater.inflate(R.layout.frag_todayrecord, container, false);
        FlowLayout flowLayout = (FlowLayout)record1.findViewById(R.id.flowLayout);
        TextView textView = KeywordTextView();
        textView.setText("sample keyword");
        flowLayout.addView(textView);
        linearScroll.addView(record1);
        /////
        return view;
    }

    String getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M월 d일 ");
        String day = simpleDateFormat.format(new Date());
        switch(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)){
            case 1:
                day += "일요일";
                break;
            case 2:
                day += "월요일";
                break;
            case 3:
                day += "화요일";
                break;
            case 4:
                day += "수요일";
                break;
            case 5:
                day += "목요일";
                break;
            case 6:
                day += "금요일";
                break;
            case 7:
                day += "토요일";
                break;

        }
        return day;
    }

    private TextView KeywordTextView(){
        TextView textView = new TextView(getContext());
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(5,5,5,5);
        textView.setLayoutParams(params);
        textView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.round_border));
        return textView;
    }
}

class KeywordTextView{
//<TextView xmlns:android="http://schemas.android.com/apk/res/android"
//        android:layout_width="wrap_content" android:layout_height="wrap_content"
//        android:layout_margin="5dp" android:background="@drawable/round_border"
//        android:padding="5dp"
//        android:text="sample"/>
    private LinearLayout.LayoutParams params;
    private TextView textView;

    KeywordTextView(){

    }
}


