package com.ChildHealthDiet.app2.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by qing on 2017/3/28.
 */
public class AgeUtils {
    // 根据年月日计算年龄,birthTimeString:"1994-11-14"
    public static int getAgeFromBirthTime(String birthTimeString) {
        // 先截取到字符串中的年、月、日
        String strs[] = birthTimeString.trim().split("-");
        int selectYear = Integer.parseInt(strs[0]);
        int selectMonth = Integer.parseInt(strs[1]);
        int selectDay = Integer.parseInt(strs[2]);
        // 得到当前时间的年、月、日
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayNow = cal.get(Calendar.DATE);

        // 用当前年月日减去生日年月日
        int yearMinus = yearNow - selectYear;
        int monthMinus = monthNow - selectMonth;
        int dayMinus = dayNow - selectDay;

        int age = yearMinus;// 先大致赋值
        if (yearMinus < 0) {// 选了未来的年份
            age = 0;
        } else if (yearMinus == 0) {// 同年的，要么为1，要么为0
            if (monthMinus < 0) {// 选了未来的月份
                age = 0;
            } else if (monthMinus == 0) {// 同月份的
                if (dayMinus < 0) {// 选了未来的日期
                    age = 0;
                } else if (dayMinus >= 0) {
                    age = 1;
                }
            } else if (monthMinus > 0) {
                age = 1;
            }
        } else if (yearMinus > 0) {
            if (monthMinus < 0) {// 当前月>生日月
            } else if (monthMinus == 0) {// 同月份的，再根据日期计算年龄
                if (dayMinus < 0) {
                } else if (dayMinus >= 0) {
                    age = age + 1;
                }
            } else if (monthMinus > 0) {
                age = age + 1;
            }
        }
        return age;
    }

//    // 根据时间戳计算年龄
//    public static int getAgeFromBirthTime(long birthTimeLong) {
//        Date date = new Date(birthTimeLong * 1000l);
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        String birthTimeString = format.format(date);
//        return getAgeFromBirthTime(birthTimeString);
//    }

    public static String getAgeFromBirthTime(Date birthDay) {
        Calendar cal = Calendar.getInstance();
        // 得到当前时间的年、月、日
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH)+1;
        int dayNow = cal.get(Calendar.DATE);
        // 先截取到传入中的年、月、日
        cal.setTime(birthDay);
        int selectYear = cal.get(Calendar.YEAR);
        int selectMonth = cal.get(Calendar.MONTH) + 1;
        int selectDay = cal.get(Calendar.DAY_OF_MONTH);
        // 用当前年月日减去生日年月日
        int yearMinus = yearNow - selectYear;
        int monthMinus = monthNow - selectMonth;
        int dayMinus = dayNow - selectDay;
        String  ageToMonth= "1月";

        if (yearMinus > 0){
            ageToMonth = String.valueOf(yearMinus)+"岁";
        } else if (monthMinus > 0) {
            ageToMonth = String.valueOf(monthMinus)+"月";
        }
        return ageToMonth;
    }


    public static int getMonthAge(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int selectYear = cal.get(Calendar.YEAR);
        int selectMonth = cal.get(Calendar.MONTH) + 1;
        int selectDay = cal.get(Calendar.DAY_OF_MONTH);
        Calendar birthday = new GregorianCalendar(selectYear, selectMonth,selectDay);//2010年10月12日，month从0开始
        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_MONTH) - birthday.get(Calendar.DAY_OF_MONTH);
        int month = now.get(Calendar.MONTH)+1 - birthday.get(Calendar.MONTH);
        int year = now.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
        //按照减法原理，先day相减，不够向month借；然后month相减，不够向year借；最后year相减。
        if(day<0){
            month -= 1;
            now.add(Calendar.MONTH, -1);//得到上一个月，用来得到上个月的天数。
            day = day + now.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        if(month<0){
            month = (month+12)%12;
            year--;
        }
        if(year>0){
            month = month+year*12;
        }
        return (month);
    }

    public static String getAge(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int selectYear = cal.get(Calendar.YEAR);
        int selectMonth = cal.get(Calendar.MONTH) + 1;
        int selectDay = cal.get(Calendar.DAY_OF_MONTH);
        Calendar birthday = new GregorianCalendar(selectYear, selectMonth,selectDay);//2010年10月12日，month从0开始
        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_MONTH) - birthday.get(Calendar.DAY_OF_MONTH);
        int month = now.get(Calendar.MONTH)+1 - birthday.get(Calendar.MONTH);
        int year = now.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
        //按照减法原理，先day相减，不够向month借；然后month相减，不够向year借；最后year相减。
        if(day<0){
            month -= 1;
            now.add(Calendar.MONTH, -1);//得到上一个月，用来得到上个月的天数。
            day = day + now.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        if(month<0){
            month = (month+12)%12;
            year--;
        }

        return ((year > 0? (year+"岁") : "" )+(month>0?(month+"月"):"零")+day+"天");
    }

}


