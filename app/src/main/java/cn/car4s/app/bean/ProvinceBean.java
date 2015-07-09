package cn.car4s.app.bean;

import android.text.TextUtils;
import cn.car4s.app.AppConfig;
import cn.car4s.app.util.PreferencesUtil;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/6/23.
 */
public class ProvinceBean extends BaseBean {
//    "ProvinceID": "1",
//            "ProvinceName": "北京市",
//            "CityList": [
//    {
//        "CityID": "1",
//            "CityName": "北京市",
//            "AreaList": [
//        {
//            "AreaID": "1",
//                "AreaName": "东城区"
//        },
//        {
//            "AreaID": "2",
//                "AreaName": "西城区"
//        }
//        ]
//    }
//    ]
//},
//        {
//        "ProvinceID": "2",
//        "ProvinceName": "天津市",
//        "CityList": [
//        {
//        "CityID": "2",
//        "CityName": "天津市",
//        "AreaList": [
//        {
//        "AreaID": "19",
//        "AreaName": "和平区"
//        },
//        {
//        "AreaID": "20",
//        "AreaName": "河东区"
//        }
//        ]
//        }
//        ]
//        }
//        ]

    public String ProvinceID;
    public String ProvinceName;
    public List<CityBean> CityList;


    public static String getlocalAreaVserion() {
        String result = null;
        String json = PreferencesUtil.getPreferences(AppConfig.SP_KEY_PROVICE, "");
        if (TextUtils.isEmpty(json)) {
            result = "0";
        } else {
            try {
                JSONObject jsonObject = new JSONObject(json);
                result = jsonObject.getString("Version");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static ProvinceListBean getlocalAreaData() {
        ProvinceListBean bean = null;
        String json = PreferencesUtil.getPreferences(AppConfig.SP_KEY_PROVICE, "");
        try {
            JSONObject jsonObject = new JSONObject(json);
            bean = new Gson().fromJson(jsonObject.toString(), ProvinceListBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bean;
    }
}
