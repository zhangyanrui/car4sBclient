package cn.car4s.app.bean;

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
public class ProductBean extends BaseBean {


//    参数名称	说明	备注	传参方式
//    action	GetProduct	必填
//    Token	用户密钥		header
//    RecommendFlag	今日推荐标志	T
//    PageCode	页码	Int类型，默认1
//    SeriesID	车系ID	默认0
//    ProductType	产品类型	0:大保;1:小保

    public int mProductType;
    public int mCarbrandType;
    public int mPageNo;
    public boolean isHot;

    public ProductBean(int mProductType, int mCarbrandType, int mPageNo, boolean isHot) {
        this.mProductType = mProductType;
        this.mCarbrandType = mCarbrandType;
        this.mPageNo = mPageNo;
        this.isHot = isHot;
    }

    public void getProductList(HttpCallback callback, ProductBean bean) {
        Map map = new HashMap();
        map.put("action", "GetProduct");
        map.put("Token", "");
        if (bean.isHot) {
            map.put("RecommendFlag", "T");
        } else {
            map.put("SeriesID", "" + bean.mCarbrandType);
            map.put("ProductType", "" + bean.mProductType);
        }
        map.put("PageCode", "" + bean.mPageNo);
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_PRODUCT, map, callback);
    }

    //    action	GetProductDetail
//    ProductID	产品ID
    public void getProductDetial(HttpCallback callback, ProductBean bean) {
        Map map = new HashMap();
        map.put("action", "GetProductDetail");
        map.put("ProductID", "" + bean.ProductID);
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_PRODUCT, map, callback);
    }

    static Type list_type = new TypeToken<List<ProductBean>>() {
    }.getType();

    public static List<ProductBean> getData(String json) {
        List<ProductBean> list = null;
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

    public int ProductID;
    public String ProductName;
    public String ProductPicPath;
    public String SalesPrice;
    public String DiscountPrice;
    public String ProductType;
    public String Description;
    public String AvailablePoint;
    public String FreezingPoint;
    public String PointRatio;

//    "ProductID": "19",
//            "ProductName": "总部套餐",
//            "ProductPicPath": "http://localhost:54001/FileUpload/ProductImg/201506081905303216.jpg",
//            "SalesPrice": "1234.00",
//            "DiscountPrice": "988.00",
//            "ProductType": "大保",
//            "Description": "范德萨发发发"


    public List<ProductDetialBean> ProductDetailList;

//    {
//        "Code":"0",
//            "Message":"成功",
//            "ProductID":"19",
//            "ProductName":"总部套餐",
//            "SalesPrice":"1234.00",
//            "DiscountPrice":"988.00",
//            "ProductDetailList":[
//        {
//            "ProductDetailID":"102",
//                "PartsName":"密封垫圈",
//                "PartsUnit":"瓶",
//                "Count":"1.00",
//                "Price":"7.14",
//                "TotalPrice":"7.14",
//                "ProductPicPath":"http://localhost:60943/FileUpload/ProductDetailImg/201506111531479556.JPG",
//                "Remark":"gffg"
//        },
//        {
//            "ProductDetailID":"101",
//                "PartsName":"密封圈",
//                "PartsUnit":"瓶",
//                "Count":"1.00",
//                "Price":"5.29",
//                "TotalPrice":"5.29",
//                "ProductPicPath":"",
//                "Remark":""
//        }
//        ]
//    }


}
