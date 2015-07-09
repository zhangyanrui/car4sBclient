package cn.car4s.app.bean;

import android.text.TextUtils;
import cn.car4s.app.AppConfig;
import cn.car4s.app.api.ApiService;
import cn.car4s.app.api.HttpCallback;
import cn.car4s.app.util.NetUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/4/22.
 */
public class OrderBean extends BaseBean {


//    参数名称	说明	备注	传参方式
//    action	AddOrder	必填
//    Token	用户密钥	必填	header
//    StationID	网点ID	必填
//    SeriesID	车系ID	必填
//    ServiceData	预约服务日期	必填	格式：yyyy-MM-dd
//    ServiceTimeID	预约服务时间ID	必填
//    TechnicianID	技师ID	必填
//    ProductID	产品ID	必填


    public String StationID;
    public String SeriesID;
    public String ServiceData;
    public String ServiceTimeID;
    public String TechnicianID;
    public String ProductID;


//    "OrderID": "14",
//            "OrderCode": "20150614000003",
//            "StationName": "三洋快保(一部)",
//            "OrderTime": "2015-06-14 18:24",
//            "ProductName": "野帝手自一体1.4T-普通(S)",
//            "ProductPicPath": "http://localhost:60359/FileUpload/ProductImg/201506131820411639.png",
//            "SalesPrice": "356.00",
//            "DiscountPrice": "280.40",
//            "EvaluationFlag": "F"
//    "PhoneNumber_Sub": "13472858805",
//            "ContributionPoint": "5.65"

    public String PhoneNumber_Sub;
    public String ContributionPoint;


    public String OrderID;
    public String OrderCode;
    public String StationName;
    public String OrderTime;
    public String ProductName;
    public String ProductPicPath;
    public String SalesPrice;
    public String DiscountPrice;
    public String EvaluationFlag;
    public String OrderStatus;
    public String TechnicianName;
    public String ServiceTime;
    public String LastPaymentTime;
    public String Description;

    public long time_daizhifu;

    public String AvailablePoint;
    public String FreezingPoint;
    public String PointDedCount;
    public String PointDedMoney;
    public String ReceiveMoney;
    public List<ProductDetialBean> ProductDetailList;
//            "OrderID": "11",
//            "OrderCode": "20150613000011",
//            "StationID": "2",
//            "StationName": "测试",
//            "SeriesID": "1",
//            "SeriesName": "测试",
//            "TechnicianID": "2",
//            "TechnicianName": "测试",
//            "ServiceData": "2015-06-16",
//            "ServiceTimeID": "11",
//            "ServiceTime": "10:00~11:00",
//            "OrderType": "大保",
//            "ProductID": "28",
//            "ProductName": "经典明锐手自一体1.6新款-半合成(S)",
//            "SalesPrice": "625.00",
//            "DiscountPrice": "545.00",
//            "Description": "",
//            "AvailablePoint": "0.00",
//            "FreezingPoint": "0.00",
//            "ProductDetailList": [
//    {
//        "ProductDetailID": "12",
//            "PartsName": "Y004款机油(4L)",
//            "PartsUnit": "",
//            "Count": "1.00",
//            "Price": "112.00",
//            "TotalPrice": "112.00",
//            "ProductPicPath": "http://localhost:60277/FileUpload/ProductDetailImg/201506131529326632.png",
//            "Remark": ""
//    }
//    ]


    public OrderBean() {
    }

    public boolean isJifenSelcted;
    public String jifenValue;

    public OrderBean(String stationID, String seriesID, String serviceData, String serviceTimeID, String technicianID, String productID, boolean jifen, String jifenValue) {

        StationID = stationID;
        SeriesID = seriesID;
        ServiceData = serviceData;
        ServiceTimeID = serviceTimeID;
        TechnicianID = technicianID;
        ProductID = productID;
        isJifenSelcted = jifen;
        this.jifenValue = jifenValue;
    }

    public void addorder(HttpCallback callback, OrderBean bean) {
        Map map = new HashMap();
        map.put("action", "AddOrder");
        map.put("StationID", "" + bean.StationID);
        if (TextUtils.isEmpty(bean.SeriesID) || "0".equals(bean.SeriesID)) {
            bean.SeriesID = "1";
        }
        map.put("SeriesID", "" + bean.SeriesID);
        map.put("ServiceData", "" + bean.ServiceData);
        map.put("ServiceTimeID", "" + bean.ServiceTimeID);
        map.put("TechnicianID", "" + bean.TechnicianID);
        map.put("ProductID", "" + ProductID);
        if (isJifenSelcted) {
            map.put("PointUseFlag", "T");
            map.put("Point", jifenValue);
        }
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_PRODUCT, map, callback);
    }


//    action	UpdateOrder	必填
//    Token	用户密钥	必填
//    OrderID	订单ID	必填
//    ServiceData	预约服务日期	必填(yyyy-MM-dd)
//    ServiceTimeID	预约服务时间ID
//    TechnicianID	技师ID

    public void updateOrder(HttpCallback callback, OrderBean bean) {
        Map map = new HashMap();
        map.put("action", "UpdateOrder");
        map.put("OrderID", "" + bean.OrderID);
        map.put("ServiceData", "" + bean.ServiceData);
        map.put("ServiceTimeID", "" + bean.ServiceTimeID);
        map.put("TechnicianID", "" + bean.TechnicianID);
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_PRODUCT, map, callback);
    }

    //    action	GetOrderList	必填
//    OrderStatus	订单状态	必填
//    0：已完成；
//            1：未使用；2：未付款
//    PageCode	页码	页码,默认1,每页20条
    public void getorderList(HttpCallback callback, int page, int orderType) {
        Map map = new HashMap();
        map.put("action", "GetOrderList");
        if (orderType != -1)
            map.put("OrderStatus", "" + orderType);
        map.put("PageCode", "" + page);
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_PRODUCT, map, callback);
    }
//    action	GetOrderList_Sub	必填
//    Token	用户密钥	必填	header
//    PageCode	页码	页码,默认1,每页20条

    public void getorderListXianxia(HttpCallback callback, int page) {
        Map map = new HashMap();
        map.put("action", "GetOrderList_Sub");
        map.put("PageCode", "" + page);
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_PRODUCT, map, callback);
    }

    static Type list_type = new TypeToken<List<OrderBean>>() {
    }.getType();

    public static List<OrderBean> getData(String json) {
        List<OrderBean> list = null;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray("Data");
            list = new Gson().fromJson(array.toString(), list_type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


//    action	CancelOrder	必填
//    Token	用户密钥	必填	header
//    OrderID	订单ID	必填

    public void cancelOrder(HttpCallback callback, String orderid) {
        Map map = new HashMap();
        map.put("action", "CancelOrder");
        map.put("OrderID", "" + orderid);
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_PRODUCT, map, callback);
    }

    //    action	GetOrderDetail	必填
//    Token	用户密钥	必填	header
//    OrderID	订单ID	必填
    public void getOrderDetial(HttpCallback callback, String orderid) {
        Map map = new HashMap();
        map.put("action", "GetOrderDetail");
        map.put("OrderID", "" + orderid);
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_PRODUCT, map, callback);
    }


}