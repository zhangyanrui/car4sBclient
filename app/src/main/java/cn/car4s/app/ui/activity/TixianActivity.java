package cn.car4s.app.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.car4s.app.AppConfig;
import cn.car4s.app.R;
import cn.car4s.app.api.HttpCallback;
import cn.car4s.app.bean.TixianBean;
import cn.car4s.app.bean.TixianListBean;
import cn.car4s.app.ui.adapter.TixianAdapter;
import cn.car4s.app.ui.widget.xlistview.XListView;
import cn.car4s.app.util.ToastUtil;
import com.google.gson.Gson;
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
public class TixianActivity extends BaseActivity implements IBase {
    List<Object> list = new ArrayList<Object>();
    TixianAdapter adapter;
    XListView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tixian);
        initUI();
        initData();
    }

    ImageView mActionbarBack;

    @Override
    public void initUI() {
        ImageView mActionbarBack = (ImageView) findViewById(R.id.btn_actionbar_back_img);
        mActionbarBack.setVisibility(View.VISIBLE);
        mActionbarBack.setImageResource(R.mipmap.ic_loginactivity_back);
        mActionbarBack.setOnClickListener(onClickListener);

        ((TextView) findViewById(R.id.tv_actionbar_title)).setText("提现");


        recyclerView = (XListView) findViewById(R.id.recyclerview);
        recyclerView.setPullRefreshEnable(false);
        recyclerView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                loadData(false);
            }
        });
        adapter = new TixianAdapter(list, this, tixianInterface);
        recyclerView.setAdapter(adapter);

    }

    TixianAdapter.TixianInterface tixianInterface = new TixianAdapter.TixianInterface() {
        @Override
        public void tixian(TixianListBean bean) {
//            mIntent = new Intent(TixianActivity.this, BankActivity.class);
//            startActivityForResult(mIntent, AppConfig.REQUEST_CODE_TIXIAN);
            if (Float.parseFloat(bean.AvailableWithdrawalPoint) <= 0) {
                ToastUtil.showToastShort("积分不足");
            } else {
                mIntent = new Intent(TixianActivity.this, BankActivity.class);
                mIntent.putExtra("allpoint", tixianListBean.AvailableWithdrawalPoint);
                startActivityForResult(mIntent, AppConfig.REQUEST_CODE_TIXIAN);
            }
        }

        @Override
        public void cancel(final TixianBean bean) {
            AlertDialog dialog = new AlertDialog.Builder(TixianActivity.this)
                    .setTitle("确定取消该提现单")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            bean.quxiaotixian(callbackquxiao, bean.WithdrawalID);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.dismiss();
                        }
                    }).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        public void showdetial(TixianBean bean) {
            mIntent = new Intent(TixianActivity.this, BankDetialActivity.class);
            mIntent.putExtra("id", bean.WithdrawalID);
            startActivity(mIntent);
        }

    };

    HttpCallback callbackquxiao = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String result) {
            Log.e("--->", "" + result);
            loadData(true);
        }
    };

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

    int mPageno;

    @Override
    public void initData() {
        loadData(true);
    }

    public void loadData(boolean isrefresh) {
        if (isrefresh)
            mPageno = 1;
        else {
            mPageno++;
        }
        new TixianBean().getTixianList(callback, mPageno);
    }

    HttpCallback callback = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String result) {
            Log.e("--->", "" + result);
            tixianListBean = new Gson().fromJson(result, TixianListBean.class);
            if (mPageno == 1) {
                list.clear();
            }
            list.add(tixianListBean);
            if (tixianListBean.Data.size() < AppConfig.PAGE_COUNT) {
                recyclerView.setPullLoadEnable(false);
            } else {
                recyclerView.setPullLoadEnable(true);
            }
            list.addAll(tixianListBean.Data);
            adapter.notifyDataSetChanged();
        }
    };
    TixianListBean tixianListBean;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConfig.REQUEST_CODE_TIXIAN && resultCode == Activity.RESULT_OK) {
            initData();
        }
    }
}
