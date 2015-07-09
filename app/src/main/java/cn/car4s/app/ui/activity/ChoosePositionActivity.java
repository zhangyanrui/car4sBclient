package cn.car4s.app.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.car4s.app.AppConfig;
import cn.car4s.app.R;
import cn.car4s.app.api.HttpCallback;
import cn.car4s.app.bean.StationAreaBean;
import cn.car4s.app.ui.adapter.ChoosePositionAdapter;
import cn.car4s.app.ui.widget.RecyclerItemClickListener;
import cn.car4s.app.util.PreferencesUtil;
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
public class ChoosePositionActivity extends BaseActivity implements IBase {
    List<StationAreaBean> list = new ArrayList<StationAreaBean>();
    ChoosePositionAdapter adapter;
    RecyclerView recyclerView;
    RecyclerItemClickListener itemlistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseposition);
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

        String name = PreferencesUtil.getPreferences(AppConfig.SP_KEY_CHOOSEPOSITION_NAME, "");
        String id = PreferencesUtil.getPreferences(AppConfig.SP_KEY_CHOOSEPOSITION_ID, "");
        if (TextUtils.isEmpty(name)) {
            ((TextView) findViewById(R.id.tv_actionbar_title)).setText("请选择区域");
        } else
            ((TextView) findViewById(R.id.tv_actionbar_title)).setText("当前区域：" + name);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ChoosePositionAdapter(this, list, id);
        recyclerView.setAdapter(adapter);
        itemlistener = new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                StationAreaBean bean = list.get(position);
                PreferencesUtil.putPreferences(AppConfig.SP_KEY_CHOOSEPOSITION_NAME, bean.StationAreaName);
                PreferencesUtil.putPreferences(AppConfig.SP_KEY_CHOOSEPOSITION_ID, bean.StationAreaID);
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
        recyclerView.addOnItemTouchListener(itemlistener);

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
        new StationAreaBean().getStationArea(callback);
    }

    HttpCallback callback = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String result) {
            Log.e("--->", "" + result);
            list.addAll(StationAreaBean.getData(result));
            adapter.notifyDataSetChanged();
        }
    };

}
