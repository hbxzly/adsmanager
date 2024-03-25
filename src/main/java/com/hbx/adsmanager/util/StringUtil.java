package com.hbx.adsmanager.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * 转化状态
     * @param status
     * @return
     */
    public static int convertStatus(String status){
        if (status.equals("良好")){
            return 1;
        }
        return 0;
    }

    /**
     * 判断是否卖出
     * @param isSell
     * @return
     */
    public static String isSellStr(String isSell){
        if (isSell !="" && isSell !=null &&isSell.equals("1")){
            return "卖出";
        }
        return "";
    }

    /**
     * 获取URL内信息
     * @param url
     * @return
     */
    public static Map<String, String> extractValuesFromURL(String url) {
        Map<String, String> resultMap = new HashMap<>();

        // 提取 tradeTid 参数值
        String tradeTid = extractValueFromURL(url, "tradeTid=");
        resultMap.put("tradeTid", tradeTid);

        // 提取 tradeOrderId 参数值
        String tradeOrderId = extractValueFromURL(url, "tradeOrderId=");
        resultMap.put("tradeOrderId", tradeOrderId);

        // 提取 payMethod 参数值
        String payMethod = extractValueFromURL(url, "payMethod=");
        resultMap.put("payMethod", payMethod);

        return resultMap;
    }

    /**
     * 获取URL内信息
     * @param url
     * @param parameter
     * @return
     */
    private static String extractValueFromURL(String url, String parameter) {
        int startIndex = url.indexOf(parameter);
        if (startIndex != -1) {
            startIndex += parameter.length();
            int endIndex = url.indexOf('&', startIndex);
            if (endIndex == -1) {
                endIndex = url.length();
            }
            return url.substring(startIndex, endIndex);
        }
        return null;
    }

    /**
     * 二维码字符串转化
     * @param input
     * @return
     */
    public static String extractImageBase64(String input) {
        String pattern = "data:image\\/\\w+;base64,([\\w\\/+=]+)";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(input);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    /**
     * 移除字符串中的非数字字符
     * @param cny
     * @return
     */
    public static String extractNumber(String cny){
        // 移除字符串中的非数字字符
        String numberString = cny.replaceAll("[^\\d.]", "");



        return numberString;
    }

}
