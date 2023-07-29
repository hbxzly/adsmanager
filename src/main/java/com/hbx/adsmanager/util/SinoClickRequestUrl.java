package com.hbx.adsmanager.util;

public class SinoClickRequestUrl {

        /**钱包余额,账户总数*/
        public static final String GET_USER_ASSETS_POST = "https://rapi.sinoclick.com/user/getUserAssets";

        /**违规状况*/
        public static final String CLIENT_FINAL_LEVEL_POST = "https://rapi.sinoclick.com/client/getClientFinalLevel";

        /**账户详情*/
        public static final String OVERVIEW_POST = "https://rapi.sinoclick.com/adaccount/overview";

        /**账户列表*/
        public static final String GET_LIST_POST = "https://rapi.sinoclick.com/adaccount/getList";

        /**账户申请列表*/
        public static final String GET_FACEBOOK_PROCESS_LIST_POST = "https://rapi.sinoclick.com/openaccount/facebook/getFacebookProcessList";

        /**获取后台信息*/
        public static final String GET_UDESK_CLIENT_INFO_POST = "https://rapi.sinoclick.com/user/getUdeskClientInfo";

        /**获取账户清零到钱包的记录*/
        public static final String GET_RECHARGE_RECORD_LIST_POST = "https://rapi.sinoclick.com/merchants/trade/getRechargeRecordList";

        /**获取账户消耗金额*/
        public static final String GET_AD_ACCOUNT_INSIGHT_LIST_POST = "https://rapi.sinoclick.com/merchants/trade/getAdaccountInsightsList";

        /**获取充值记录*/
        public static  final String GET_TRADE_ORDER_LIST_POST = "https://rapi.sinoclick.com/merchants/trade/getTradeOrderList";

        /**获取充值细节*/
        public static  final String QUERY_TRADE_ORDER_POST = "https://rapi.sinoclick.com/merchants/payment/queryTradeOrder";

        /**绑定BM*/
        public static final String BIND_BM_POST = "https://rapi.sinoclick.com/adAccounts/operation/facebook/bindBm";

        /**解绑BM*/
        public static final String UNBIND_BM_POST = "https://rapi.sinoclick.com/adAccounts/operation/facebook/unBindBm";

        public static final String GET_ACCOUNT_BM_LIST_POST = "https://rapi.sinoclick.com/adAccounts/operation/facebook/getAccountBmList";

        /**检查订单*/
        public static final String QUERY_PAY_RESULT = "https://rapi.sinoclick.com/merchants/payment/queryPayResult";

}
