package cn.car4s.app.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.car4s.app.R;
import cn.car4s.app.api.HttpCallback;
import cn.car4s.app.bean.TixianBean;
import cn.car4s.app.bean.TixianListBean;
import cn.car4s.app.ui.adapter.DialogTimeAdapter;
import cn.car4s.app.ui.adapter.TixianAdapter;
import cn.car4s.app.util.DialogUtil;
import cn.car4s.app.util.ToastUtil;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/4/22.
 */
public class BankActivity extends BaseActivity implements IBase {
    List<Object> list = new ArrayList<Object>();
    TixianAdapter adapter;
    ListView recyclerView;
    @InjectView(R.id.edt_1)
    TextView edt1;
    @InjectView(R.id.edt_2)
    TextView edt2;
    @InjectView(R.id.edt_3)
    EditText edt3;
    @InjectView(R.id.edt_4)
    EditText edt4;
    @InjectView(R.id.edt_5)
    EditText edt5;
    @InjectView(R.id.edt_6)
    EditText edt6;
    @InjectView(R.id.edt_7)
    EditText edt7;
    @InjectView(R.id.edt_8)
    EditText edt8;
    @InjectView(R.id.edt_9)
    EditText edt9;
    @InjectView(R.id.btn_feedback_commit)
    TextView commit;
    @InjectView(R.id.keyitixian)
    TextView allfenshu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        ButterKnife.inject(this);
        availabepoint = getIntent().getStringExtra("allpoint");
        initUI();
        initData();
    }

    ImageView mActionbarBack;
    private String availabepoint;
    List<Object> pointlist;

    @Override
    public void initUI() {
        ImageView mActionbarBack = (ImageView) findViewById(R.id.btn_actionbar_back_img);
        mActionbarBack.setVisibility(View.VISIBLE);
        mActionbarBack.setImageResource(R.mipmap.ic_loginactivity_back);
        mActionbarBack.setOnClickListener(onClickListener);
        edt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSexDialog();
            }
        });
        allfenshu.setText("当前可提现积分: " + availabepoint);
        float fenshu = Float.parseFloat(availabepoint);
        int temp = (int) (fenshu / 100);
        pointlist = new ArrayList<Object>();
        for (int i = 0; i < temp; i++) {
            pointlist.add((i + 1) * 100);
        }
        edt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDate();
            }
        });
        ((TextView) findViewById(R.id.tv_actionbar_title)).setText("提现流程");
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edt1string = edt1.getText().toString().trim();
                String edt2string = edt2.getText().toString().trim();
                String edt3string = edt3.getText().toString().trim();
                String edt4string = edt4.getText().toString().trim();
                String edt5string = edt5.getText().toString().trim();
                String edt6string = edt6.getText().toString().trim();
                String edt7string = edt7.getText().toString().trim();
                String edt8string = edt8.getText().toString().trim();
                String edt9string = edt9.getText().toString().trim();

                tixianBean = new TixianBean();
                tixianBean.WithdrawalPoint = edt1string;
                tixianBean.TransferMode = mcurrentTransfer;
                tixianBean.AccountNumber = edt3string;
                tixianBean.AccountBankName = edt4string;
                tixianBean.ContactName = edt5string;
                tixianBean.ContactNumber = edt6string;
                tixianBean.AccountName = edt7string;
                tixianBean.IDCardNo = edt8string;
                tixianBean.Description = edt9string;
                tixianBean.applyTixian(callback, tixianBean);

            }
        });
    }

    private void updateDate() {
        View view = LayoutInflater.from(BankActivity.this).inflate(R.layout.time_picker, null);
        final Dialog dialog = DialogUtil.buildDialog(BankActivity.this, view, Gravity.CENTER, 0, true);
        ListView listView = (ListView) view.findViewById(R.id.listview);

        DialogTimeAdapter adapter = new DialogTimeAdapter(pointlist, BankActivity.this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog.dismiss();
                String point = "" + pointlist.get(i);
                edt1.setText(point);
            }
        });
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        TextView sure = (TextView) view.findViewById(R.id.sure);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    TixianBean tixianBean;
    String mcurrentTransfer = "";

    public void showSexDialog() {
        View view = View.inflate(this, R.layout.dialog_sex, null);
        final Dialog dialog = DialogUtil.buildDialog(this, view, Gravity.CENTER, R.style.BottomDialogAnimation, true);
        final RadioGroup rap_tab_radiogroup = (RadioGroup) view.findViewById(R.id.rap_tab_radiogroup);
        final TextView textView = (TextView) view.findViewById(R.id.dialog_sex_title);
        textView.setText("请选择转账方式");
        final RadioButton radioButtonnan = (RadioButton) view.findViewById(R.id.rdo_tab_1);
        radioButtonnan.setText("支付宝");
        final RadioButton radioButtonnv = (RadioButton) view.findViewById(R.id.rdo_tab_2);
        radioButtonnv.setText("银行转账");
        if ("2".equals(mcurrentTransfer)) {
            rap_tab_radiogroup.check(R.id.rdo_tab_2);
        } else {
            rap_tab_radiogroup.check(R.id.rdo_tab_1);
        }
        View dialog_upload_sure = view.findViewById(R.id.dialog_upload_sure);
        dialog_upload_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (radioButtonnan.isChecked()) {
                    mcurrentTransfer = "1";
                    edt2.setText("支付宝");
                } else {
                    mcurrentTransfer = "2";
                    edt2.setText("银行转账");
                }
            }
        });
        View dialog_upload_cancel = view.findViewById(R.id.dialog_upload_cancel);
        dialog_upload_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.btn_actionbar_back_img:
                    finish();
                    break;
            }
        }
    };


    @Override
    public void initData() {
    }


    HttpCallback callback = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String result) {
            Log.e("--->", "" + result);
            ToastUtil.showToastShort("申请已提交");
            setResult(Activity.RESULT_OK);
            finish();
        }
    };
    TixianListBean tixianListBean;
}
