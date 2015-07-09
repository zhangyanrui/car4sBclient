package cn.car4s.app.bean;

import cn.car4s.app.AppConfig;
import cn.car4s.app.api.ApiService;
import cn.car4s.app.api.HttpCallback;
import cn.car4s.app.util.NetUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/4/22.
 */
public class TixianBean extends BaseBean {


    public void getTixianList(HttpCallback callback, int page) {
        Map map = new HashMap();
        map.put("action", "GetWithdrawalList");
        map.put("PageCode", "" + page);
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_TIXIAN, map, callback);
    }

//    static Type list_type = new TypeToken<List<TixianBean>>() {
//    }.getType();
//
//    public static List<TixianBean> getData(String json) {
//        List<TixianBean> list = null;
//        JSONObject jsonObject = null;
//        try {
//            jsonObject = new JSONObject(json);
//            JSONArray array = jsonObject.getJSONArray("Data");
//            list = new Gson().fromJson(array.toString(), list_type);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }

    public int WithdrawalID;
    public String ApplicationNo;
    public String ApplicationTime;
    public String qurenshijian;
    public String Status;
    public String WithdrawalPoint;
    public String WithdrawalAmount;
    public String TransferMode;
    public String AccountNumber;
    public String AccountName;
    public String AccountBankName;
    public String ContactName;
    public String ContactNumber;
    public String IDCardNo;
    public String Description;

//
//    "WithdrawalID": "1",
//            "ApplicationNo": "2015061400001",
//            "ApplicationTime": "2015-06-14 17:49:42",
//            "Status": "1",
//            "WithdrawalPoint": "0.00",
//            "WithdrawalAmount": "100.00",
//            "TransferMode": "支付宝",
//            "AccountNumber": "15921811111",
//            "AccountName": "",
//            "AccountBankName": "",
//            "ContactName": "张三",
//            "ContactNumber": "15911111111",
//            "IDCardNo": "",
//            "Description": "234rtrfddfsdsfsadfsas"
//action	AddWithdrawal	必填
//    Token	用户密钥	必填
//    WithdrawalPoint	本次提现积分	必填
//    TransferMode	转账方式	必填
//            (1:支付宝,2:银行卡)
//    AccountNumber	收款帐号	必填
//    AccountBankName	收款银行名称	必填
//    AccountName	收款人名称	必填
//    ContactName	联系人名称	必填
//    ContactNumber	联系人电话	必填
//    IDCardNo	身份证号
//    Description	其他说明


    public void applyTixian(HttpCallback callback, TixianBean bean) {
        Map map = new HashMap();
        map.put("action", "AddWithdrawal");
        map.put("WithdrawalPoint", "" + bean.WithdrawalPoint);
        map.put("TransferMode", "" + bean.TransferMode);
        map.put("AccountNumber", "" + bean.AccountNumber);
        map.put("AccountBankName", "" + bean.AccountBankName);
        map.put("AccountName", "" + bean.AccountName);
        map.put("ContactName", "" + bean.ContactName);
        map.put("ContactNumber", "" + bean.ContactNumber);
        map.put("IDCardNo", "" + bean.IDCardNo);
        map.put("Description", "" + bean.Description);
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_TIXIAN, map, callback);
    }


    //    action	GetWithdrawalDetail	必填
//    Token	用户密钥	必填
//    WithdrawalID	提现申请单ID	必填
    public void getTixianDetial(HttpCallback callback, int itxianId) {
        Map map = new HashMap();
        map.put("action", "GetWithdrawalDetail");
        map.put("WithdrawalID", "" + itxianId);
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_TIXIAN, map, callback);
    }


    public void quxiaotixian(HttpCallback callback, int itxianId) {
        Map map = new HashMap();
        map.put("action", "CancelWithdrawal");
        map.put("WithdrawalID", "" + itxianId);
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_TIXIAN, map, callback);
    }

}
