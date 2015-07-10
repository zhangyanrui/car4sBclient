package cn.car4s.app.bean;

import cn.car4s.app.AppConfig;
import cn.car4s.app.api.ApiService;
import cn.car4s.app.api.HttpCallback;
import cn.car4s.app.util.NetUtil;
import cn.car4s.app.util.PreferencesUtil;
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
 * Time: 2015/6/23.
 */
public class StationBean extends BaseBean {

    public String StationId;
    public String StationCode;
    public String StationAreaID;
    public String StationName;
    public String StationName_Full;
    public String PhoneNumber;
    public String StationAddress;
    public String Longitude;
    public String Latitude;
    public String Mileage;
    public List<TimeChoose> ServiceTimeList;


//    "StationId": "4",
//            "StationCode": "SYKB0004",
//            "StationAreaID": "1",
//            "StationName": "三洋快保(三部)",
//            "StationName_Full": "三洋快保(三部)",
//            "PhoneNumber": "02150185352",
//            "StationAddress": "上海中天科技商务园",
//            "Longitude": "121.555709",
//            "Latitude": "31.151274",
//            "Mileage": "9.50"


//    action	GetStation	必填
//    StationAreaID	区域ID	不必填
//    Longitude	经度	不必填	header
//    Latitude	纬度	不必填	header


    public static class TimeChoose extends BaseBean {
        public String TimeID;
        public String TimeName;
    }

    public void getStation(HttpCallback callback) {
        String id = PreferencesUtil.getPreferences(AppConfig.SP_KEY_CHOOSEPOSITION_ID, "");
        Map map = new HashMap();
        map.put("action", "GetStation");
        map.put("StationAreaID", "" + id);
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_SYS_PARA, map, callback);
    }

//    action	•	GetStation_B	必填
    public void getStationClientB(HttpCallback callback) {
        Map map = new HashMap();
        map.put("action", "GetStation_B");
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_SYS_PARA, map, callback);
    }


    static Type list_type = new TypeToken<List<StationBean>>() {
    }.getType();

    public static List<StationBean> getData(String json) {
        List<StationBean> list = null;
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


}
