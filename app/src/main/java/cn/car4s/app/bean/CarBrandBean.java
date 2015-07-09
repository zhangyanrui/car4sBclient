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

public class CarBrandBean extends BaseBean {

//    "BrandID": "1",
//            "BrandName": "上海大众",
//            "Initial": "S",
//            "ImgPath": "/FileUpload/BrandImg/201505271713542473.jpg"


    public String BrandID;
    public String BrandName;
    public String Initial;
    public String ImgPath;


//    action	GetBrand	必填
//    Initial	首字母	不填获取所有品牌

    public void getCarbrand(HttpCallback callback) {
        Map map = new HashMap();
        map.put("action", "GetBrand");
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_SYS_PARA, map, callback);
    }

    static Type list_type = new TypeToken<List<CarBrandBean>>() {
    }.getType();

    public static List<CarBrandBean> getData(String json) {
        List<CarBrandBean> list = null;
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
