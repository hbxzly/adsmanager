package com.hbx.adsmanager.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbx.adsmanager.domain.AdAccount;
import com.hbx.adsmanager.domain.WalletRechargeRecord;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class JsonParseUtil {

    /**
     * 获取钱包余额
     * @param jsonData
     * @return
     */
    public static String getWalletBalance(String jsonData) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(jsonData);
            JsonNode walletNode = rootNode.get("result").get("wallet");
            return walletNode.get("balance").asText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // 在异常发生时返回null，或根据实际需求返回适当的默认值
    }

    /**
     * 格式化广告账户列表
     * @param jsonData
     * @return
     */
    public static List<AdAccount> parseAdAccountList(String jsonData) {
        List<AdAccount> adAccountList = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonData);
            JsonNode resultNode = rootNode.get("result");
            if (resultNode != null && resultNode.has("result")) {
                JsonNode accountNodeList = resultNode.get("result");
                for (JsonNode accountNode : accountNodeList) {
                    AdAccount adAccount = new AdAccount();
                    adAccount.setId(accountNode.get("adAccountId").asText());
                    adAccount.setAdAccountId(accountNode.get("adAccountId").asText().replace("act_",""));
                    adAccount.setAccountStatus(accountNode.get("accountStatus").asText());
                    adAccount.setTransferAndRestStatus(accountNode.get("transferAndRestStatus").asText());
                    adAccount.setAdAccountName(accountNode.get("adAccountName").asText());
                    adAccount.setBalance(accountNode.get("balance").asDouble());
                    adAccount.setRechargeStatus(accountNode.get("rechargeStatus").asText());
                    adAccount.setPauseActionStatus(accountNode.get("pauseActionStatus").asText());
                    adAccount.setBalanceWarningStatus(accountNode.get("balanceWarningStatus").asText());
                    adAccount.setDisableWarningStatus(accountNode.get("disableWarningStatus").asText());
                    adAccount.setUpdateTime(new Date());

                    adAccountList.add(adAccount);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return adAccountList;
    }

    /**
     * 格式化充值记录
     * @param jsonData
     * @return
     */
    public static List<WalletRechargeRecord> parseRechargeRecordList(String jsonData) {
        List<WalletRechargeRecord> rechargeRecordList = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonData);
            JsonNode resultNode = rootNode.get("result");

            if (resultNode != null && resultNode.has("result")) {
                JsonNode recordNodeList = resultNode.get("result");
                for (JsonNode recordNode : recordNodeList) {
                    WalletRechargeRecord rechargeRecord = new WalletRechargeRecord();
                    rechargeRecord.setChangeType(recordNode.get("changeType").asText());
                    rechargeRecord.setActionType(recordNode.get("actionType").asText());
                    rechargeRecord.setUsdAmount(Double.parseDouble(recordNode.get("changeAmount").get("usdAmount").asText()));

                    String createTimeString = recordNode.get("createTime").asText();
                    Date createTime = parseDate(createTimeString);
                    rechargeRecord.setCreateTime(createTime);

                    rechargeRecord.setChangeDesc(recordNode.get("changeDesc").asText());
                    rechargeRecord.setNickName(recordNode.get("nickName").asText());
                    rechargeRecord.setMobile(recordNode.get("mobile").asText());
                    rechargeRecord.setCompanyName(recordNode.get("companyName").asText());

                    rechargeRecordList.add(rechargeRecord);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rechargeRecordList;
    }

    /**
     * 格式化日期
     * @param dateString
     * @return
     */
    public static Date parseDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 格式化充值状态
     * @param jsonData
     * @return
     * @throws IOException
     */
    public static String paresRechargeStatus(String jsonData) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(jsonData);
        return jsonNode.get("result").get("status").asText();
    }


    public static Map<String, Object> parseJSON(String jsonData) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            parseJSONObject(jsonObject, "", resultMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    private static void parseJSONObject(JSONObject jsonObject, String prefix, Map<String, Object> resultMap) throws JSONException {
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.get(key);
            resultMap.put(prefix + key, value);

            if (value instanceof JSONObject) {
                parseJSONObject((JSONObject) value, prefix + key + ".", resultMap);
            } else if (value instanceof JSONArray) {
                parseJSONArray((JSONArray) value, prefix + key + ".", resultMap);
            }
        }
    }

    private static void parseJSONArray(JSONArray jsonArray, String prefix, Map<String, Object> resultMap) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);
            if (value instanceof JSONObject) {
                parseJSONObject((JSONObject) value, prefix, resultMap);
            } else if (value instanceof JSONArray) {
                parseJSONArray((JSONArray) value, prefix, resultMap);
            }
        }
    }

}
