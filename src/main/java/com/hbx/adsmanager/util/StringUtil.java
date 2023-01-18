package com.hbx.adsmanager.util;

public class StringUtil {

    /**
     * 清除首尾、中间多余空格
     * @param str
     * @return
     */
    public static String cleanBlank(String str){
        String trimStr = str.trim();
        return trimStr.replaceAll(" +"," ");
    }

    /**
     * 按空格将字符串分割成数组
     * @param str
     * @return
     */
    public static String[] strToArray(String str){
        return str.trim().split("\\s+");
    }

    public static int convertStatus(String status){
        if (status.equals("良好")){
            return 1;
        }
        return 0;
    }

    public static String isSellStr(String isSell){
        if (isSell !="" && isSell !=null &&isSell.equals("1")){
            return "卖出";
        }
        return "";
    }

}
