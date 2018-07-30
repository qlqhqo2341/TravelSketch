package com.livenoproblem.travelsketch.Common;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    public static String API_KEY = "84d137903fc99381f03bba429c6537f6";
    public static String API_LINK = "http://api.openweathermap.org/data/2.5/weather";

    public static String apiRequest(String lat,String lng) {
        StringBuilder sb = new StringBuilder(API_LINK);
        sb.append(String.format("?lat=%s&lon=%s&APPID=%s&units=metric",lat,lng,API_KEY));
        return sb.toString();

    }

    //포맷설정 시간 : 분
    public static String unixTimeStampToDateTime(double unixTimeStamp) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date ();
        date.setTime((long)unixTimeStamp*1000);
        return dateFormat.format(date);
    }

    //이미지 링크
    public static String getImage (String icon) {
        return String.format("http://openweathermap.org/img/w/%s.png",icon);

    }

    //업데이트 날짜
    public static String getDateNow() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy 년 M 월 dd 일 HH:mm");
        Date date = new Date();
        return dateFormat.format(date);

    }

}
