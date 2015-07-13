package cn.car4s.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.car4s.app.AppConfig;
import cn.car4s.app.R;
import cn.car4s.app.api.HttpCallback;
import cn.car4s.app.bean.OrderBean;
import cn.car4s.app.bean.ShengqianGridBean;
import cn.car4s.app.bean.UserBean;
import cn.car4s.app.ui.adapter.ShengqianAdapter;
import cn.car4s.app.ui.widget.RecyclerItemClickListener;
import cn.car4s.app.util.PreferencesUtil;
import cn.car4s.app.util.ToastUtil;
import com.squareup.okhttp.Request;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/4/22.
 */
public class AppBMainActibity extends BaseActivity implements IBase {


    @InjectView(R.id.btn_actionbar_back_text)
    TextView mActionbarBack;
    @InjectView(R.id.btn_actionbar_conform_text)
    TextView mActionbarRight;
    @InjectView(R.id.tv_actionbar_title)
    TextView mActionbarTitle;
    @InjectView(R.id.recyclerview)
    RecyclerView recyclerView;

    List<ShengqianGridBean> list = new ArrayList<ShengqianGridBean>();
    ShengqianAdapter adapter;
    RecyclerItemClickListener itemlistener;

    @InjectView(R.id.tv_wangdian)
    TextView mtvWangdian;
    @InjectView(R.id.tv_yonghu)
    TextView mtvYonghu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmain);
        ButterKnife.inject(this);
        initUI();
//        initData();
    }

    @Override
    public void initUI() {
        mActionbarBack.setVisibility(View.VISIBLE);
        mActionbarBack.setText("登陆");
        mActionbarBack.setOnClickListener(onClickListener);
        mActionbarRight.setVisibility(View.VISIBLE);
        mActionbarRight.setText("刷新");
        mActionbarRight.setOnClickListener(onClickListener);
        mActionbarTitle.setText("指尖创业");

        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ShengqianAdapter(list, this);
        recyclerView.setAdapter(adapter);
        itemlistener = new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        if (mUserbean == null) {
                            mIntent = new Intent(AppBMainActibity.this, LoginActivity.class);
                            startActivity(mIntent);
                            return;
                        }
                        Intent m = new Intent(AppBMainActibity.this, OrderFinishedActivity.class);
                        m.putExtra("type", 3);
                        startActivity(m);
                        break;
                    case 1:
                        if (mUserbean == null) {
                            mIntent = new Intent(AppBMainActibity.this, LoginActivity.class);
                            startActivity(mIntent);
                            return;
                        }
                        mIntent = new Intent(AppBMainActibity.this, QueryActivity.class);
                        startActivity(mIntent);
                        break;
                    case 2:
                        if (mUserbean == null) {
                            mIntent = new Intent(AppBMainActibity.this, LoginActivity.class);
                            startActivity(mIntent);
                            return;
                        }
                        mIntent = new Intent(AppBMainActibity.this, QueryActivity.class);
                        mIntent.putExtra("type", 1);
                        startActivity(mIntent);
                        break;
                    case 3:
                        ToastUtil.showToastShort("暂未上线");
                        break;
                }
            }
        });
        recyclerView.addOnItemTouchListener(itemlistener);
        list.addAll(ShengqianGridBean.createbduanmain());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void initData() {
        mUserbean = UserBean.getLocalUserinfo();
        if (mUserbean == null) {
            mActionbarBack.setText("登陆");
            mtvWangdian.setText("");
            mtvYonghu.setText("请登陆");
            list.get(0).title = "待处理订单";
            adapter.notifyDataSetChanged();
        } else {
            mActionbarBack.setText("注销");
            mtvWangdian.setText("登陆网点: " + mUserbean.StationName);
            mtvYonghu.setText("当前用户: " + mUserbean.UserName);
            new OrderBean().getPendingorderListClientb(callback, 1, PreferencesUtil.getPreferences(AppConfig.SP_KEY_MOBILE, ""));
        }
    }

    HttpCallback callback = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String result) {
            int number = 0;
            if (!TextUtils.isEmpty(result)) {
                try {
                    number = new JSONObject(result).getInt("Total");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (number != 0) {
                list.get(0).title = "待处理订单(" + number + ")";
            } else {
                list.get(0).title = "待处理订单";

            }
            adapter.notifyDataSetChanged();
        }
    };
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.btn_actionbar_back_text:
                    if (mUserbean != null) {
                        PreferencesUtil.putPreferences(AppConfig.SP_KEY_USERINFO, "");
                    }
                    mIntent = new Intent(AppBMainActibity.this, LoginActivity.class);
                    startActivity(mIntent);
                    break;
                case R.id.btn_actionbar_conform_text://refresh
                    if (mUserbean != null)
                        mUserbean.refreshClientB(callbackRefresh, mUserbean.Token);
                    break;
            }
        }
    };


    HttpCallback callbackRefresh = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String result) {
            Log.e("--->", "" + result);
            ToastUtil.showToastShort("刷新完成");
            PreferencesUtil.putPreferences(AppConfig.SP_KEY_USERINFO, result);
            initData();
//            initUI();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}
