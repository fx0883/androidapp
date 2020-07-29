package com.ChildHealthDiet.app2.context;

import com.ChildHealthDiet.app2.utils.AgeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Kidinfo {
    private String nickName = "";
    private String birthdate = "";

    public Kidinfo(String strNickName,String strBirthdate){
        this.nickName = strNickName;
        this.birthdate = strBirthdate;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getAge() throws ParseException {
        String strMonthAge = "";
        try{
            if(!birthdate.equals("")){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                strMonthAge = AgeUtils.getAge(sdf.parse(this.birthdate));
            }
        }
        catch (Exception ex){

        }
        return strMonthAge;
    }

    public int getMonthAge() throws ParseException {
        int monthAge = 0;
        try{
            if(!birthdate.equals("")){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                monthAge = AgeUtils.getMonthAge(sdf.parse(this.birthdate));
            }
        }
        catch (Exception ex){

        }
        return monthAge;
    }
}
