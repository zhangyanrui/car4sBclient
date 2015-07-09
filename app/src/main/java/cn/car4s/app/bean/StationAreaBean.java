package cn.car4s.app.bean;

import android.util.Log;
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
 * Time: 2015/6/23.
 */
public class StationAreaBean extends BaseBean {

    public String StationAreaID;
    public String StationAreaName;

    public boolean isSeletced;


    public void getStationArea(HttpCallback callback) {
        Map map = new HashMap();
        map.put("action", "GetStationArea");
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_SYS_PARA, map, callback);
    }

    static Type list_type = new TypeToken<List<StationAreaBean>>() {
    }.getType();

    public static List<StationAreaBean> getData(String json) {
        List<StationAreaBean> list = null;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray("Data");
            list = new Gson().fromJson(array.toString(), list_type);
            Log.e("--->", "" + list.get(0).StationAreaName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

//    action	GetAllArea	必填
//    Version	版本信息	1.0.0.0

    public void getAllArea(HttpCallback callback) {
        Map map = new HashMap();
        map.put("action", "GetAllArea");
        map.put("Version", ProvinceBean.getlocalAreaVserion());
//        map.put("Version", "0");
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_SYS_PARA, map, callback);
    }


}
