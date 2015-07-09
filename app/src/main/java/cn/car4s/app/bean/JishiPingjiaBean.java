package cn.car4s.app.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/4/22.
 */
public class JishiPingjiaBean extends BaseBean {


    public String EvaluationID;
    public String EvaluationType;
    public  int score;

    static Type list_type = new TypeToken<List<JishiPingjiaBean>>() {
    }.getType();
    public static List<JishiPingjiaBean> getData(String json) {
        List<JishiPingjiaBean> list = null;
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
