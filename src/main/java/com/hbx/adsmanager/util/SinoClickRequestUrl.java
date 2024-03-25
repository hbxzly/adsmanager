package com.hbx.adsmanager.util;

public class SinoClickRequestUrl {

        /**钱包余额,账户总数*/
        public static final String GET_DEPARTMENT_ASSETS_POST = "https://sino-gateway.sinoclick.com/api/platform/user/getDepartmentAssets";

        /**违规状况*/
        public static final String CLIENT_FINAL_LEVEL_POST = "https://rapi.sinoclick.com/client/getClientFinalLevel";

        /**账户详情*/
        public static final String OVERVIEW_POST = "https://sino-gateway.sinoclick.com/api/platform/adaccount/index/overview";

        /**账户列表*/
        public static final String AD_ACCOUNT_LIST_POST = "https://sino-gateway.sinoclick.com/api/platform/adaccount/department/adAccountList";

        /**账户申请列表*/
        public static final String GET_FACEBOOK_PROCESS_LIST_POST = "https://rapi.sinoclick.com/openaccount/facebook/getFacebookProcessList";

        /**获取后台信息*/
        public static final String GET_INFO_POST = "https://sino-gateway.sinoclick.com/api/platform/user/detail/info";

        /**获取商务信息*/
        public static final String GET_SERVICE_STAFF_INFO_POST = "https://sino-gateway.sinoclick.com/api/platform/corporations/serviceStaffInfo";

        /**获取账户清零到钱包的记录*/
        public static final String GET_RECHARGE_RECORD_LIST_POST = "https://sino-gateway.sinoclick.com/api/platform/merchants/trade/getRechargeRecordList";

        /**获取账户消耗金额*/
        public static final String GET_AD_ACCOUNT_INSIGHT_LIST_POST = "https://rapi.sinoclick.com/merchants/trade/getAdaccountInsightsList";

        /**获取充值记录*/
        public static  final String GET_TRADE_ORDER_LIST_POST = "https://sino-gateway.sinoclick.com/api/platform/merchants/trade/getTradeOrderList";

        /**获取充值细节*/
        public static  final String QUERY_TRADE_ORDER_POST = "https://rapi.sinoclick.com/merchants/payment/queryTradeOrder";

        /**绑定BM*/
        public static final String BIND_BM_POST = "https://sino-gateway.sinoclick.com/api/platform/adAccounts/operation/authBm";

        /**解绑BM*/
        public static final String UNBIND_BM_POST = "https://rapi.sinoclick.com/adAccounts/operation/facebook/unBindBm";

        /**BM列表*/
        public static final String GET_ACCOUNT_BM_LIST_POST = "https://sino-gateway.sinoclick.com/api/platform/adAccounts/operation/getAuthList";

        /**检查订单*/
        public static final String QUERY_PAY_RESULT = "https://sino-gateway.sinoclick.com/api/platform/merchants/payment/queryPayResult";

}
