package cn.car4s.app.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.car4s.app.AppConfig;
import cn.car4s.app.R;
import cn.car4s.app.api.HttpCallback;
import cn.car4s.app.bean.StationBean;
import cn.car4s.app.bean.UserBean;
import cn.car4s.app.util.PreferencesUtil;
import cn.car4s.app.util.ToastUtil;
import com.squareup.okhttp.Request;

import java.io.IOException;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/4/22.
 */
public class LoginActivity extends BaseActivity implements IBase {
    @InjectView(R.id.btn_actionbar_back_img)
    ImageView mActionbarBack;
    @InjectView(R.id.tv_actionbar_title)
    TextView mActionbarTitle;
    @InjectView(R.id.layout_actionbar_all)
    RelativeLayout mActionbarBackLayoutall;


    @InjectView(R.id.edt_login_wangdian)
    EditText mEdtwangdian;
    @InjectView(R.id.edt_login_mobile)
    EditText mEdtMobile;
    @InjectView(R.id.edt_login_pwd)
    EditText mEdtPwd;
    @InjectView(R.id.btn_forgetpwd)
    TextView mForgetPwd;
    @InjectView(R.id.btn_register)
    TextView mBtnRegister;
    @InjectView(R.id.btn_login)
    TextView mBtnLogin;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        initUI();
        initData();
    }

    @Override
    public void initUI() {
        mActionbarBackLayoutall.setBackgroundColor(getResources().getColor(R.color.transparent));
        mActionbarBack.setVisibility(View.VISIBLE);
        mActionbarBack.setImageResource(R.mipmap.ic_loginactivity_back);
        mActionbarBack.setOnClickListener(onClickListener);
        mActionbarTitle.setText("登陆");
        mForgetPwd.setOnClickListener(onClickListener);
        mBtnRegister.setOnClickListener(onClickListener);
        mBtnLogin.setOnClickListener(onClickListener);
        mEdtwangdian.setOnClickListener(onClickListener);


        mEdtMobile.setText(PreferencesUtil.getPreferences(AppConfig.SP_KEY_MOBILE, ""));
        mEdtPwd.setText(PreferencesUtil.getPreferences(AppConfig.SP_KEY_PWD, ""));
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.btn_actionbar_back_img:
                    finish();
                    break;
                case R.id.btn_forgetpwd:
                    intent = new Intent(LoginActivity.this, ResetPwdActivity.class);
                    intent.putExtra("usertype", 1);
                    startActivityForResult(intent, AppConfig.REQUEST_CODE_RESETPWD);
                    break;
                case R.id.btn_register:
                    intent = new Intent(LoginActivity.this, ResetPwdActivity.class);
                    startActivityForResult(intent, AppConfig.REQUEST_CODE_REGISTER);
                    break;
                case R.id.btn_login:
                    if (mSelectedBean == null) {
                        ToastUtil.showToastShort("请选择网点");
                        return;
                    }
                    phone = mEdtMobile.getText().toString().trim();
                    pwd = mEdtPwd.getText().toString().trim();
                    if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(pwd)) {
                        ToastUtil.showToastShort("您的输入有误，请重新输入");
                    } else {
                        UserBean bean = new UserBean(phone, pwd);
                        bean.AreaID = mSelectedBean.StationId;
                        bean.loginB(callback);
                    }
                    break;
                case R.id.edt_login_wangdian:
                    mIntent = new Intent(LoginActivity.this, ChooseStationActivity.class);
                    startActivityForResult(mIntent, AppConfig.REQUEST_CODE_CHOOSE_STATION);
                    break;
            }
        }
    };
    String phone, pwd;
    HttpCallback callback = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String result) {
            Log.e("--->", "" + result);
            PreferencesUtil.putPreferences(AppConfig.SP_KEY_USERINFO, result);

            mUserbean = UserBean.getLocalUserinfo();
            PreferencesUtil.putPreferences(AppConfig.SP_KEY_MOBILE, phone);
            PreferencesUtil.putPreferences(AppConfig.SP_KEY_PWD, pwd);
            PreferencesUtil.putPreferences(AppConfig.SP_KEY_PWD, pwd);
            PreferencesUtil.putPreferences(AppConfig.SP_KEY_ISGROUP, mUserbean.IsGroup);
            Log.e("--->", "" + PreferencesUtil.getPreferences(AppConfig.SP_KEY_MOBILE, ""));
            setResult(Activity.RESULT_OK);
            finish();
        }
    };
    StationBean mSelectedBean;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConfig.REQUEST_CODE_CHOOSE_STATION && resultCode == Activity.RESULT_OK) {
            mSelectedBean = (StationBean) data.getSerializableExtra("bean");
            mEdtwangdian.setText(mSelectedBean.StationName);
        }
    }

    @Override
    public void initData() {

    }
}
