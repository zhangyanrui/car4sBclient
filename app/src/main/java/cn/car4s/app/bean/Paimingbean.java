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
public class Paimingbean extends BaseBean {
//    "UserID": "48",
//            "PhoneNumber": "1599660****",
//            "UserName": "无",
//            "Point": "100.00"
//
//            "OfflineCount": "4"                //推荐人数

    public String PhoneNumber;
    public String UserName;
    public String OfflineCount;
    public String Point;

    //
    public int position;
    public int type;

    static Type list_type = new TypeToken<List<Paimingbean>>() {
    }.getType();

    public static List<Paimingbean> getData(String json) {
        List<Paimingbean> list = null;
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
//    地址：Interface_Sys_Para.aspx
//    2.15.2. action名称
//    ·GetRecommendRankingList
//    2.15.3.参数说明
//    参数名称	说明	备注
//    action	GetRecommendRankingList	必填

    public void tuijianpaiming(HttpCallback callback) {
        Map map = new HashMap();
        map.put("action", "GetRecommendRankingList");
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_SYS_PARA, map, callback);
    }

    public void fanlipaiming(HttpCallback callback) {
        Map map = new HashMap();
        map.put("action", "GetRebateRankingList");
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_SYS_PARA, map, callback);
    }

}
