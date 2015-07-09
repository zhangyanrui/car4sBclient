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
public class JishiBean extends BaseBean {


//    "UserID": "3",
//            "UserCode": "SYKB00010003",
//            "UserName": "范冰冰",
//            "StationID": "1",
//            "Sex": "0",
//            "PhoneNumber": "02150185352",
//            "HeadPicturePath": "http://localhost:54001/FileUpload/HeadPicImg/201506051451056663.jpg",
//            "Level": "0",
//            "Remark": "我是一个技师"


    public String UserID;
    public String UserCode;
    public String UserName;
    public String StationID;
    public String Sex;
    public String PhoneNumber;
    public String HeadPicturePath;
    public String Level;
    public String Remark;
    public String IsBusy;

//    action	GetTechnicianList
//    StationID	网点ID

    public void getJishilist(HttpCallback callback, String stationid, String data, String time) {
        Map map = new HashMap();
        map.put("action", "GetTechnicianList");
        map.put("StationID", "" + stationid);
        map.put("ServiceData", "" + data);
        map.put("ServiceTimeID", "" + time);
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_PRODUCT, map, callback);
    }

    static Type list_type = new TypeToken<List<JishiBean>>() {
    }.getType();

    public static List<JishiBean> getData(String json) {
        List<JishiBean> list = null;
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

    public void getPingjiaPara(HttpCallback callback) {
        Map map = new HashMap();
        map.put("action", "GetEvaluationPara");
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_SYS_PARA, map, callback);
    }
//    4.10.2. action名称
//    OrderEvaluation
//    4.10.3.参数说明
//    参数名称	说明	备注	传参方式
//    action	CancelOrder	必填
//    Token	用户密钥	必填	header
//    OrderID	订单ID	必填
//    Evaluation	评价字符串	必填	评价ID:得分;评价ID：得分;
//    EvaluationContent	评价内容


    public void pingjiajishi(HttpCallback callback, String orderid, String rate, String content) {
        Map map = new HashMap();
        map.put("action", "OrderEvaluation");
        map.put("OrderID", orderid);
        map.put("Evaluation", rate);
        map.put("EvaluationContent", content);
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_PRODUCT, map, callback);
    }
}
