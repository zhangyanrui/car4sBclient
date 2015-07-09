package cn.car4s.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.car4s.app.AppConfig;
import cn.car4s.app.R;
import cn.car4s.app.api.HttpCallback;
import cn.car4s.app.bean.UserBean;
import cn.car4s.app.bean.WebviewBean;
import cn.car4s.app.util.ToastUtil;
import com.squareup.okhttp.Request;

import java.io.IOException;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/4/22.
 */
public class ResetPwdActivity extends BaseActivity implements IBase {
    @InjectView(R.id.btn_actionbar_back_img)
    ImageView mActionbarBack;
    @InjectView(R.id.tv_actionbar_title)
    TextView mActionbarTitle;
    @InjectView(R.id.layout_actionbar_all)
    RelativeLayout mActionbarBackLayoutall;


    @InjectView(R.id.edt_login_mobile)
    EditText mEdtMobile;
    @InjectView(R.id.btn_login_getyanchengma)
    TextView mBtnGetyanzhengma;
    @InjectView(R.id.edt_login_yanzhengma)
    EditText mEdtYanzhengma;
    @InjectView(R.id.edt_login_tuijianma)
    EditText mEdtTuijianma;
    @InjectView(R.id.edt_login_pwd)
    EditText mEdtPwd;
    @InjectView(R.id.edt_login_pwd_2)
    EditText mEdtPwd2;
    @InjectView(R.id.btn_login)
    TextView mBtnRegister;
    @InjectView(R.id.checkbox)
    CheckBox checkbox;

    @InjectView(R.id.btn_xieyi)
    TextView btn_xieyi;
    @InjectView(R.id.layout_xieyi)
    LinearLayout layout_xieyi;

    Intent intent;


    int mType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpwd);
        ButterKnife.inject(this);
        initData();
        initUI();
    }

    @Override
    public void initUI() {
        mActionbarBackLayoutall.setBackgroundColor(getResources().getColor(R.color.transparent));
        mActionbarBack.setVisibility(View.VISIBLE);
        mActionbarBack.setImageResource(R.mipmap.ic_loginactivity_back);
        mActionbarBack.setOnClickListener(onClickListener);
        if (mType == 0) {
            mActionbarTitle.setText("注册");
            layout_xieyi.setVisibility(View.VISIBLE);
        } else {
            layout_xieyi.setVisibility(View.GONE);
            mActionbarTitle.setText("重置密码");
            mEdtPwd.setHint("新密码");
            mEdtPwd2.setHint("验证新密码");
            mBtnRegister.setText("重置密码");
            mEdtTuijianma.setVisibility(View.GONE);
        }
        btn_xieyi.setOnClickListener(onClickListener);
        mBtnGetyanzhengma.setOnClickListener(onClickListener);
        mBtnRegister.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.btn_xieyi:
                    mIntent = new Intent(ResetPwdActivity.this, WebviewActivity.class);
                    WebviewBean bean2 = new WebviewBean("用户协议", AppConfig.APP_SERVER + AppConfig.LINK_XIEYI, false);
                    mIntent.putExtra(AppConfig.INTENT_PARA_KEY_BEAN, bean2);
                    startActivity(mIntent);
                    break;
                case R.id.btn_actionbar_back_img:
                    finish();
                    break;
                case R.id.btn_login_getyanchengma://get yanzhengma
                    String phonegetyanzhengma = mEdtMobile.getText().toString().trim();
                    if (TextUtils.isEmpty(phonegetyanzhengma)) {
                        ToastUtil.showToastShort("您的输入有误，请重新输入");
                    } else {
                        TimeStart();
                        UserBean bean = new UserBean(phonegetyanzhengma);
                        bean.getYanzhegnma(callbackGetyanzhengma);
                    }
                    break;
                case R.id.btn_login://register
                    String phone = mEdtMobile.getText().toString().trim();
                    String pwd = mEdtPwd.getText().toString().trim();
                    String pwd2 = mEdtPwd2.getText().toString().trim();
                    String yanzhengma = mEdtYanzhengma.getText().toString().trim();
                    String tuijianma = mEdtTuijianma.getText().toString().trim();
                    if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwd2) || TextUtils.isEmpty(yanzhengma) || !pwd.equals(pwd2) || TextUtils.isEmpty(tuijianma)) {
                        ToastUtil.showToastShort("您的输入有误，请重新输入");
                    } else if (!checkbox.isChecked()) {
                        ToastUtil.showToastShort("请阅读并同意用户使用协议");
                    } else {
                        UserBean bean = new UserBean(phone, pwd, yanzhengma, tuijianma);
                        if (mType == 0) {
                            bean.register(callbackRgeister);
                        } else {
                            bean.resetPwd(callbackResetPwd);
                        }
                    }
                    break;

            }
        }
    };
    int time;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mBtnGetyanzhengma.setText(msg.arg1 + "秒后重发");
                    if (msg.arg1 == 0) {
                        mBtnGetyanzhengma.setEnabled(true);
                        mBtnGetyanzhengma.setText("获取验证码");
                    }
                    break;
                default:
                    break;
            }
        }
    };


    public void TimeStart() {
        mBtnGetyanzhengma.setText("60秒后重发");
        mBtnGetyanzhengma.setEnabled(false);
        time = 60;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (time > 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    time--;
                    Message msg = new Message();
                    msg.what = 0;
                    msg.arg1 = time;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    HttpCallback callbackRgeister = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String bean) {
            ToastUtil.showToastShort("注册成功");
            finish();
        }
    };
    HttpCallback callbackResetPwd = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String bean) {
            ToastUtil.showToastShort("操作成功");
            finish();
        }
    };
    HttpCallback callbackGetyanzhengma = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String bean) {


        }
    };

    @Override
    public void initData() {
        mType = getIntent().getIntExtra("usertype", 0);
    }
}
