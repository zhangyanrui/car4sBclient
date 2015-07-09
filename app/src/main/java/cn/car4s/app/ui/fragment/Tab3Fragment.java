package cn.car4s.app.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.car4s.app.AppConfig;
import cn.car4s.app.AppContext;
import cn.car4s.app.R;
import cn.car4s.app.api.HttpCallback;
import cn.car4s.app.bean.SettingBean;
import cn.car4s.app.bean.UserBean;
import cn.car4s.app.bean.WebviewBean;
import cn.car4s.app.ui.activity.*;
import cn.car4s.app.ui.widget.SettingLayout;
import cn.car4s.app.util.DialogUtil;
import cn.car4s.app.util.LogUtil;
import cn.car4s.app.util.PreferencesUtil;
import cn.car4s.app.util.UtilImage;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/4/22.
 */
public class Tab3Fragment extends BaseFragment implements IBase {
    View rootview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_tab3, null);
        initData();
        initUI();
        return rootview;
    }

    ImageView useravaster;

    @Override
    public void initUI() {
        ((TextView) rootview.findViewById(R.id.tv_actionbar_title)).setText(getString(R.string.tab3));
        rootview.findViewById(R.id.layout_actionbar_all).setBackgroundResource(R.color.transparent);
        SettingLayout mLayoutKeyongjifen = (SettingLayout) rootview.findViewById(R.id.setting_keyongjifen);
        SettingLayout mLayoutdongjiejifen = (SettingLayout) rootview.findViewById(R.id.setting_dongjiejifen);
        SettingLayout mLayoutfeedback = (SettingLayout) rootview.findViewById(R.id.setting_feedback);
        SettingLayout mLayoutAboutus = (SettingLayout) rootview.findViewById(R.id.setting_aboutus);
        SettingLayout mLayoutXianxian = (SettingLayout) rootview.findViewById(R.id.setting_xianxianshu);
        mLayoutfeedback.setOnClickListener(onClickListener);
        mLayoutAboutus.setOnClickListener(onClickListener);

        List<SettingBean> listData = SettingBean.createSettingData(mUserbean);
        mLayoutKeyongjifen.setData(listData.get(0));
        mLayoutdongjiejifen.setData(listData.get(1));
        mLayoutfeedback.setData(listData.get(2));
        mLayoutAboutus.setData(listData.get(3));
        mLayoutXianxian.setData(listData.get(4));


        useravaster = (ImageView) rootview.findViewById(R.id.img_tab3_useravaster);
        ImageLoader.getInstance().displayImage(mUserbean.HeadPicturePath, useravaster, AppContext.display_avaster_imageloader);
        useravaster.setOnClickListener(onClickListener);
        TextView username = (TextView) rootview.findViewById(R.id.tv_tab3_username);
        TextView btn_editprofile = (TextView) rootview.findViewById(R.id.btn_tab3_editprofile);
        ImageLoader.getInstance().displayImage(mUserbean.HeadPicturePath, useravaster, AppContext.display_avaster_imageloader);
        username.setText(mUserbean.UserName + "");
        btn_editprofile.setOnClickListener(onClickListener);
    }

    @Override
    public void initData() {
        mUserbean = UserBean.getLocalUserinfo();
        if (mUserbean != null)
            mUserbean.refresh(callbackRefresh, mUserbean.Token);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.setting_feedback:
                    mIntent = new Intent(getActivity(), FeedbackActivity.class);
                    startActivity(mIntent);
                    break;
                case R.id.setting_aboutus:
                    mIntent = new Intent(getActivity(), WebviewActivity.class);
                    WebviewBean bean = new WebviewBean("关于我们", AppConfig.APP_SERVER + AppConfig.LINK_ABOUT_US, false);
                    mIntent.putExtra(AppConfig.INTENT_PARA_KEY_BEAN, bean);
                    startActivity(mIntent);
                    break;
                case R.id.btn_tab3_editprofile:
                    mIntent = new Intent(getActivity(), EditProfileActivity.class);
                    startActivityForResult(mIntent, AppConfig.REQUEST_CODE_EDITPROFILE);
                    break;
                case R.id.img_tab3_useravaster:
                    DialogUtil.showUploadDialog(getActivity(), DialogListener);
                    break;
            }
        }
    };
    String mUploadPicPath;

    View.OnClickListener DialogListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object o = v.getTag();
            if (o != null && o instanceof Dialog) {
                ((Dialog) o).cancel();
            }
            switch (v.getId()) {
                case R.id.dialog_upload_take:
                    mUploadPicPath = UtilImage.callCamera(Tab3Fragment.this);
                    break;
                case R.id.dialog_upload_abulmb:
                    Intent intent = new Intent(getActivity(), SelectPhotoActivity.class);
                    intent.putExtra("issns", false);
                    intent.putExtra("maxCount", 1);
                    startActivityForResult(intent, SelectPhotoActivity.SHOW_LIB);
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.e("--->", "---");
        if (resultCode == 200 && requestCode == SelectPhotoActivity.SHOW_LIB) {
            mUploadPicPath = data.getStringExtra("loadimagepath");
            new UserBean().updateAvaster(callback, mUploadPicPath);
        }
        if (requestCode == UtilImage.SHOW_CAM && resultCode == Activity.RESULT_OK) {
            UtilImage.rotateAndReplaceOldImageFile(mUploadPicPath);
            new UserBean().updateAvaster(callback, mUploadPicPath);
        }
        if (requestCode == AppConfig.REQUEST_CODE_EDITPROFILE && resultCode == Activity.RESULT_OK) {
            initData();
            if (mUserbean != null) {
                mUserbean.refresh(callbackRefresh, mUserbean.Token);
            } else {
                ((MainTabActivity) getActivity()).change();
//                UserBean.toLogin(getActivity(), AppConfig.REQUEST_CODE_LOGIN_FROMTAB3);
            }
        }
    }

    String headurl;
    AsyncHttpResponseHandler callback = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {
            LogUtil.e("---->", "onSuccess");
            String jsonResult = new String(bytes);
            if (!TextUtils.isEmpty(jsonResult)) {
                JSONObject object = null;
                try {
                    object = new JSONObject(jsonResult);
                    headurl = object.getString("HeadPicturePath");
                    ImageLoader.getInstance().displayImage(headurl, useravaster, AppContext.display_avaster_imageloader);
                    LogUtil.e("---->", "onSuccess " + headurl);
                    mUserbean.HeadPicturePath = headurl;
                    mUserbean.updateProfile(callbackUpdate, mUserbean);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        @Override
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
            LogUtil.e("---->", "onFailure");
        }
    };

    HttpCallback callbackUpdate = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String result) {
            Log.e("--->", "" + result);
            mUserbean.refresh(callbackRefresh, mUserbean.Token);
        }
    };
    HttpCallback callbackRefresh = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String result) {
            Log.e("--->", "" + result);
            PreferencesUtil.putPreferences(AppConfig.SP_KEY_USERINFO, result);
//            initData();
            mUserbean = UserBean.getLocalUserinfo();
            initUI();
        }
    };
}
