package cn.car4s.app.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.car4s.app.R;
import cn.car4s.app.api.HttpCallback;
import cn.car4s.app.bean.UserBean;
import cn.car4s.app.util.ToastUtil;
import com.squareup.okhttp.Request;

import java.io.IOException;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/4/22.
 */
public class FeedbackActivity extends BaseActivity implements IBase {
    @InjectView(R.id.btn_actionbar_back_img)
    ImageView mActionbarBack;
    @InjectView(R.id.tv_actionbar_title)
    TextView mActionbarTitle;


    @InjectView(R.id.edt_feedback_mobile)
    EditText mEdtMobile;
    @InjectView(R.id.edt_feedback_text)
    EditText mEdtText;
    @InjectView(R.id.btn_feedback_commit)
    TextView mBtnCommit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.inject(this);
        initData();
        initUI();
    }

    @Override
    public void initUI() {
        mActionbarBack.setVisibility(View.VISIBLE);
        mActionbarBack.setImageResource(R.mipmap.ic_loginactivity_back);
        mActionbarBack.setOnClickListener(onClickListener);
        mBtnCommit.setOnClickListener(onClickListener);
        mActionbarTitle.setText("意见反馈");

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.btn_actionbar_back_img:
                    finish();
                    break;
                case R.id.btn_feedback_commit:
                    String phone = mEdtMobile.getText().toString().trim();
                    String text = mEdtText.getText().toString().trim();
                    if (TextUtils.isEmpty(phone) && TextUtils.isEmpty(text)) {
                        ToastUtil.showToastShort("您的输入有误，请重新输入");
                    } else {
                        UserBean userBean = new UserBean();
                        userBean.feedback(callback, text, phone);
                    }
                    break;
            }
        }
    };
    HttpCallback callback = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String bean) {
            ToastUtil.showToastShort("提交成功");
            finish();
        }
    };

    @Override
    public void initData() {

    }
}
