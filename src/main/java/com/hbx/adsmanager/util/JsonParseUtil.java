package com.hbx.adsmanager.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbx.adsmanager.domain.AdAccount;
import com.hbx.adsmanager.domain.WalletRechargeRecord;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JsonParseUtil {

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
                    adAccount.setAccountStatus(accountNode.get("accountStatus").asText());
                    adAccount.setTransferAndRestStatus(accountNode.get("transferAndRestStatus").asText());
                    adAccount.setAdAccountName(accountNode.get("adAccountName").asText());
                    adAccount.setBalance(accountNode.get("balance").asDouble());
                    adAccount.setAdAccountId(accountNode.get("dateCenterAdAccountId").asText());
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


    public static Date parseDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String paresRechargeStatus(String jsonData) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(jsonData);
        return jsonNode.get("result").get("status").asText();
    }


}
