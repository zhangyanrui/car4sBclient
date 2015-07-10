package cn.car4s.app.bean;

import android.app.Activity;
import android.content.Intent;
import cn.car4s.app.AppConfig;
import cn.car4s.app.api.ApiService;
import cn.car4s.app.api.HttpCallback;
import cn.car4s.app.ui.activity.LoginActivity;
import cn.car4s.app.util.NetUtil;
import cn.car4s.app.util.PreferencesUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
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
public class UserBean extends BaseBean {

    public String UserID;
    public String PhoneNumber_R;
    public String PhoneNumber_B;
    public String UserName;
    public String Sex;
    public String Birthday;
    public String Address;
    public String ProvinceID;
    public String CityID;
    public String AreaID;
    public String ReferralCode_I;
    public String AvailablePoint;
    public String FreezingPoint;
    public String TotalPoint;
    public String RegisterTime;
    public String ParentUserID;
    public String LastLoginTime;
    public String HeadPicturePath;
    public String Token;


    public String PhoneNumber;
    public String PassWord;
    public String CodeNumber;
    public String ReferralCode;
    public String feedBackText;
    public String OfflineCount;


    public String IsGroup;
    public String IsTechnician;
    public String PendingOrderCount;
    public String StationID;
    public String StationName;


    public UserBean(String phoneNumber, String passWord) {
        PhoneNumber = phoneNumber;
        PassWord = passWord;
    }

    public UserBean(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public UserBean() {
    }

    public UserBean(String phoneNumber, String passWord, String codeNumber, String referralCode) {
        PhoneNumber = phoneNumber;
        PassWord = passWord;
        CodeNumber = codeNumber;
        ReferralCode = referralCode;
    }

    public void login(HttpCallback callback) {
        Map map = new HashMap();
        map.put("action", "Login");
        map.put("PhoneNumber", this.PhoneNumber + "");
        map.put("PassWord", this.PassWord + "");
        map.put("PlatForm", "Android");
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_USER, map, callback);
    }

    public void loginB(HttpCallback callback) {
        Map map = new HashMap();
        map.put("action", "Login");
        map.put("StationID", this.AreaID + "");
        map.put("UserCode", this.PhoneNumber + "");
        map.put("PassWord", this.PassWord + "");
        map.put("PlatForm", "Android");
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_CLIENTB, map, callback);
    }

    //    action	GetUserMessage	必填	post
//    Token	用户密钥	必填	header
    public void refresh(HttpCallback callback, String token) {
        Map map = new HashMap();
        map.put("action", "GetUserMessage");
        map.put("Token", token);
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_USER, map, callback);
    }

    public void refreshClientB(HttpCallback callback, String token) {
        Map map = new HashMap();
        map.put("action", "GetUserMessage");
        map.put("Token", token);
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_CLIENTB, map, callback);
    }


    public void register(HttpCallback callback) {
        Map map = new HashMap();
        map.put("action", "Register");
        map.put("PhoneNumber", this.PhoneNumber + "");
        map.put("PassWord", this.PassWord + "");
        map.put("CodeNumber", this.CodeNumber + "");
        map.put("ReferralCode", this.ReferralCode + "");
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_USER, map, callback);
    }

    public void getYanzhegnma(HttpCallback callback) {
        Map map = new HashMap();
        map.put("action", "GetCode");
        map.put("PhoneNumber", this.PhoneNumber + "");
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_USER, map, callback);
    }

    public void resetPwd(HttpCallback callback) {
        Map map = new HashMap();
        map.put("action", "ResetPassword");
        map.put("PhoneNumber", this.PhoneNumber + "");
        map.put("PassWord", this.PassWord + "");
        map.put("CodeNumber", this.CodeNumber + "");
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_USER, map, callback);
    }


    public void feedback(HttpCallback callback, String text, String contact) {
        Map map = new HashMap();
        map.put("action", "AddFeedback");
        map.put("ContactWay", contact + "");
        map.put("Content", text + "");
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_USER, map, callback);
    }

    public static boolean checkUserLoginStatus() {
        if (!"".equals(PreferencesUtil.getPreferences(AppConfig.SP_KEY_USERINFO, ""))) {
            return true;
        }
        return false;
    }

    public static void toLogin(Activity activity, int requestcode) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivityForResult(intent, requestcode);
    }

    public static UserBean getLocalUserinfo() {
        UserBean bean = null;
        String json = PreferencesUtil.getPreferences(AppConfig.SP_KEY_USERINFO, "");
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray("Data");
            JSONObject temp = array.getJSONObject(0);
            bean = new Gson().fromJson(temp.toString(), UserBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bean;
    }


//    action	UpdateUserMessage	必填
//    Token	用户密钥	必填
//    UserName	用户名
//    Sex	性别	(0:男/1:女/-1:空)
//    Birthday	生日	1990-01-01
//    Address	地址
//    ProvinceID	省份
//    CityID	城市
//    AreaID	区域
//    HeadPicturePath	图片地址


    public void updateProfile(HttpCallback callback, UserBean bean) {
        Map map = new HashMap();
        map.put("action", "UpdateUserMessage");
        map.put("UserName", bean.UserName + "");
        map.put("Sex", bean.Sex + "");
        map.put("Birthday", bean.Birthday + "");
        map.put("Address", bean.Address + "");
        map.put("ProvinceID", bean.ProvinceID + "");
        map.put("CityID", bean.CityID + "");
        map.put("AreaID", bean.AreaID + "");
        map.put("HeadPicturePath", bean.HeadPicturePath + "");
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_USER, map, callback);
    }
//    action	UploadHeadPicture	必填	post
//    Token	用户密钥	必填	header
//    头像图片	必填	body

    public void updateAvaster(AsyncHttpResponseHandler callback, String path) {
        Map map = new HashMap();
        map.put("action", "UploadHeadPicture");
        NetUtil.uploadImg(AppConfig.APP_SERVER + ApiService.INTERFACE_USER, path, callback);
//        NetUtil.doPostMultipart(AppConfig.APP_SERVER + ApiService.INTERFACE_USER, path, callback);
    }

//    地址：Interface_Sys_Para.aspx
//    2.11.2. action名称
//    ·GetBanner
//    2.11.3.参数说明
//    参数名称	说明	备注
//    action	GetBanner	必填
//
//
//    2.11.4. 返回值
//    例：
//    {
//        "Code": "0",
//            "Message": "成功",
//            "Data": [
//        {
//            "BannerID": "3",
//                "ImgPath": "http://localhost:51933/FileUpload/BannerImg/201506241020459829.png",
//                "LinkURL": "www.baidu.com",
//                "Remark": "24小时救援电话：021-55886258"
//        }
//        ]
//    }

    public void getBanner(HttpCallback callback) {
        Map map = new HashMap();
        map.put("action", "GetBanner");
        NetUtil.doPostMap(AppConfig.APP_SERVER + ApiService.INTERFACE_SYS_PARA, map, callback);
    }

    public static class BannerBean extends BaseBean {
        public String BannerID;
        public String ImgPath;
        public String LinkURL;
        public String Remark;
    }

    static Type list_type = new TypeToken<List<BannerBean>>() {
    }.getType();

    public static List<BannerBean> getData(String json) {
        List<BannerBean> list = null;
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
