package cn.xct.qrLogin.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

/**
 * @ClassName StrUtils
 * @Author HWG
 * @Time 2019/4/17 21:38
 */

public class StrUtils {
    public static String randomNum(boolean numberFlag, int length) {
        String retStr = "";
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWSYZ";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);
        return retStr;
    }

    public static String timeStamp() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return df.format(new Date());
    }

    /* //时间戳转换日期 */

    public static String stampToTime(String stamp) {
        String sd = "";
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sd = sdf.format(new Date(Long.parseLong(stamp))); // 时间戳转换日期
        return sd;
    }


    /* //日期转换为时间戳 */
    public long timeToStamp(String timers) {
        Date d = new Date();
        long timeStemp = 0;
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            d = sf.parse(timers);// 日期转换为时间戳
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        timeStemp = d.getTime();
        return timeStemp;
    }

    /**
     * 方法描述：自定义判断是否为空
     *
     * @param str the str
     * @return the boolean
     */
    public static Boolean isBlank(String str) {
        if (str != null)
            str = str.replaceAll("\r\n|\n\r|\n|\r|\f|\t", "");
        if (str == null)
            return true;
        else if (str.equals(""))
            return true;
        else if (str.equals("null"))
            return true;
        else if (str.equals("NULL"))
            return true;
        else if (str.equals("(null)"))
            return true;
        else if (str.equals("(NULL)"))
            return true;
        else if (str.trim().length() == 0)
            return true;
        return false;
    }

    /**
     * 方法描述：判断obj是否为空
     *
     * @param obj the 判断的对象
     * @return the boolean
     */
    public static Boolean isBlank(Object obj) {
        if (obj != null) {
            return isBlank(String.valueOf(obj));
        }
        return true;
    }
}


