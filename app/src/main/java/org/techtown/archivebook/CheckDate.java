package org.techtown.archivebook;

public class CheckDate {

    // 날짜 유효성 체크 메서드
    public static boolean checkDateVali(int y, int m, int d) {
        int[] arrMaxDay = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        // 윤년의 경우
        if (((y%4 == 0) && (y%100 != 0)) || (y%400 == 0)) {
            arrMaxDay[1] = 29;
        }
        else {
            arrMaxDay[1] = 28;
        }

        // 입력받은 일이 해당 월의 일의 수보다 큰 경우 false 반환
        if (d > arrMaxDay[m-1]) {
            return false;
        }
        else {
            return true;
        }
    }

    // 날짜 시작, 종료 순서 체크 메서드
    public static boolean checkDateOrder(int ys, int ms, int ds, int yf, int mf, int df) {
        // 시작일 년도가 종료일 년도보다 큰 경우 fasle 반환
        if (ys > yf) {
            return false;
        }
        else {
            // 시작일 월일이 종료일 월일보다 클 경우 false 반환
            if ((ms*100 + ds) > (mf*100 + df)) {
                return false;
            }
        }

        return true;
    }
}
