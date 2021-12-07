package org.techtown.archivebook;

import java.util.ArrayList;

public class SetDate {

    // year 스피너 item 생성 메소드
    static public ArrayList<Integer> setSpinnerItemYear (ArrayList<Integer> list, int yearNow){
        for (int i = yearNow; i > 2014; i--){
            list.add(i);
        }
        return list;
    }

    // month 스피너 item 생성 메소드
    static public ArrayList<Integer> setSpinnerItemMonth (ArrayList<Integer> list){
        for (int i = 1; i < 13; i++){
            list.add(i);
        }
        return list;
    }

    // day 스피너 item 생성 메소드
    static public ArrayList<Integer> setSpinnerItemDay (ArrayList<Integer> list){
        for (int i = 1; i < 32; i++){
            list.add(i);
        }
        return list;
    }
}