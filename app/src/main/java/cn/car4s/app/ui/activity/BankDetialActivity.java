package cn.car4s.app.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.car4s.app.R;
import cn.car4s.app.api.HttpCallback;
import cn.car4s.app.bean.TixianBean;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;

import java.io.IOException;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/4/22.
 */
public class BankDetialActivity extends BaseActivity implements IBase {

    @InjectView(R.id.btn_login)
    TextView txt;
    ImageView mActionbarBack;

    int tixianId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_detial);
        ButterKnife.inject(this);
        tixianId = getIntent().getIntExtra("id", 0);
        initUI();
        initData();
    }


    @Override
    public void initUI() {
        ImageView mActionbarBack = (ImageView) findViewById(R.id.btn_actionbar_back_img);
        mActionbarBack.setVisibility(View.VISIBLE);
        mActionbarBack.setImageResource(R.mipmap.ic_loginactivity_back);
        mActionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ((TextView) findViewById(R.id.tv_actionbar_title)).setText("提现详情");

        txt.setText("");
        if (tixianbean != null) {
            txt.append("申请编号:" + tixianbean.ApplicationNo + "\n");
            txt.append("申请时间:" + tixianbean.ApplicationTime + "\n");
            txt.append("提现积分:" + tixianbean.WithdrawalPoint + "\n");
            txt.append("转账方式:" + tixianbean.TransferMode + "\n");
            txt.append("账户:" + tixianbean.AccountNumber + "\n");
            txt.append("开户行:" + tixianbean.AccountBankName + "\n");
            txt.append("联系人:" + tixianbean.ContactName + "\n");
            txt.append("联系电话:" + tixianbean.ContactNumber + "\n");
            txt.append("开户姓名:" + tixianbean.AccountName + "\n");
            txt.append("身份证:" + tixianbean.IDCardNo + "\n");
            txt.append("其他说明:" + tixianbean.Description + "\n");
        }
    }


    @Override
    public void initData() {
        new TixianBean().getTixianDetial(callback, tixianId);
    }


    HttpCallback callback = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String result) {
            Log.e("--->", "" + result);
            tixianbean = new Gson().fromJson(result, TixianBean.class);
            initUI();

        }
    };
    TixianBean tixianbean;

}
