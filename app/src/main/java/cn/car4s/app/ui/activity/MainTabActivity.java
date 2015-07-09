package cn.car4s.app.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.widget.RadioGroup;
import cn.car4s.app.AppConfig;
import cn.car4s.app.R;
import cn.car4s.app.bean.UserBean;
import cn.car4s.app.ui.fragment.Tab1Fragment;
import cn.car4s.app.ui.fragment.Tab2Fragment;
import cn.car4s.app.ui.fragment.Tab3Fragment;
import cn.car4s.app.util.LogUtil;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

public class MainTabActivity extends BaseActivity implements IBase, TencentLocationListener {
    private FragmentManager fm;
    private FragmentTabHost tabhost;
    TencentLocationManager locationManager;
    PushAgent mPushAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobclickAgent.updateOnlineConfig(this);
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable(mRegisterCallback);
        mUserbean = UserBean.getLocalUserinfo();
        if (mUserbean != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mPushAgent.addAlias(mUserbean.UserID, "UserID");
                        LogUtil.e("alias", "" + mUserbean.UserID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        initUI();
        TencentLocationRequest request = TencentLocationRequest.create();
        locationManager = TencentLocationManager.getInstance(this);
        int error = locationManager.requestLocationUpdates(request, this);
        LogUtil.e("onLocationChanged", error + "");
    }


    IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback() {

        @Override
        public void onRegistered(String registrationId) {
            String device_token = UmengRegistrar.getRegistrationId(MainTabActivity.this);
            LogUtil.e("onRegistered", "--->" + device_token);
            mUserbean = UserBean.getLocalUserinfo();
            if (mUserbean != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mPushAgent.addAlias(mUserbean.UserID, "UserID");
                            LogUtil.e("alias", "" + mUserbean.UserID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        }

    };

    @Override
    public void initUI() {
        fm = getSupportFragmentManager();
        tabhost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabhost.setup(MainTabActivity.this, fm, android.R.id.tabcontent);

        tabhost.addTab(tabhost.newTabSpec("tab1").setIndicator("1"), Tab1Fragment.class, null);
        tabhost.addTab(tabhost.newTabSpec("tab2").setIndicator("2"), Tab2Fragment.class, null);
        tabhost.addTab(tabhost.newTabSpec("tab3").setIndicator("3"), Tab3Fragment.class, null);
//        tabhost.addTab(tabhost.newTabSpec("tab4").setIndicator("4"), Tab4Fragment.class, null);
        radioGroup = (RadioGroup) findViewById(R.id.rap_tab_radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.rdo_tab_1:
                        tabhost.setCurrentTabByTag("tab1");
                        break;
                    case R.id.rdo_tab_2:
                        if (UserBean.checkUserLoginStatus())
                            tabhost.setCurrentTabByTag("tab2");
                        else {
                            UserBean.toLogin(MainTabActivity.this, AppConfig.REQUEST_CODE_LOGIN_FROMTAB2);
                            change();
                        }
                        break;
                    case R.id.rdo_tab_3:
                        if (UserBean.checkUserLoginStatus())
                            tabhost.setCurrentTabByTag("tab3");
                        else {
                            UserBean.toLogin(MainTabActivity.this, AppConfig.REQUEST_CODE_LOGIN_FROMTAB3);
                            change();
                        }
                        break;
                    case R.id.rdo_tab_4:
                        tabhost.setCurrentTabByTag("tab4");
                        break;
                }
            }
        });
    }

    RadioGroup radioGroup;

    public void change() {
        radioGroup.check(R.id.rdo_tab_1);
    }

    public void changeTotab2() {
        radioGroup.check(R.id.rdo_tab_2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConfig.REQUEST_CODE_LOGIN_FROMTAB2 && resultCode == Activity.RESULT_OK) {
            tabhost.setCurrentTabByTag("tab2");
            radioGroup.check(R.id.rdo_tab_2);
        } else if (requestCode == AppConfig.REQUEST_CODE_LOGIN_FROMTAB3 && resultCode == Activity.RESULT_OK) {
            tabhost.setCurrentTabByTag("tab3");
            radioGroup.check(R.id.rdo_tab_3);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int error, String s) {
        LogUtil.e("onLocationChanged", tencentLocation.getCity());
        if (TencentLocation.ERROR_OK == error) {
            // 定位成功
            LogUtil.e("onLocationChanged", tencentLocation.getCity());
        } else {
            // 定位失败
        }
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }
}
