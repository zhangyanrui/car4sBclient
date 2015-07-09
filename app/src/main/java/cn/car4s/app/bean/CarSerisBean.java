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

public class CarSerisBean extends BaseBean {

//    "SeriesID": "1",
//            "SeriesName": "途观",
//            "BrandID": "1",
//            "Initial": "T",
//            "ImgPath": "/FileUpload/SeriesImg/201505281159137156.jpg"


    public String BrandID;
    public String SeriesID;
    public String SeriesName;
    public String Initial;
    public String ImgPath;


//    action	GetSeries	必填
//    BrandID	品牌ID
//    Initial	首字母


    public void getCarSersis(HttpCallback callback, CarBrandBean bean) {
        Map map = new HashMap();
        map.put("action", "GetSeries");
        map.put("BrandID", "" + bean.BrandID);
//        map.put("Initial", "" + bean.Initial);
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_SYS_PARA, map, callback);
    }

    static Type list_type = new TypeToken<List<CarSerisBean>>() {
    }.getType();

    public static List<CarSerisBean> getData(String json) {
        List<CarSerisBean> list = null;
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
