package cn.car4s.app.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.car4s.app.R;
import cn.car4s.app.api.HttpCallback;
import cn.car4s.app.bean.CarBrandBean;
import cn.car4s.app.bean.CarSerisBean;
import cn.car4s.app.bean.JishiBean;
import cn.car4s.app.ui.adapter.CarSersisAdapter;
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
public class ChooseCarStyleActivity extends BaseActivity implements IBase {
    List<Object> list = new ArrayList<Object>();
    CarSersisAdapter adapter;
    @InjectView(R.id.pinnedlv_show)
    ListView recyclerView;

    CarBrandBean mBrandbean;
    int mType;
    String mStateid;
    String mDataid;
    String mTimeid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_carstyle);
        ButterKnife.inject(this);
        mBrandbean = (CarBrandBean) getIntent().getSerializableExtra("bean");
        mType = getIntent().getIntExtra("type", 0);
        mStateid = getIntent().getStringExtra("stateId");
        mDataid = getIntent().getStringExtra("dataId");
        mTimeid = getIntent().getStringExtra("timeId");
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

        if (mType == 0)
            ((TextView) findViewById(R.id.tv_actionbar_title)).setText("车型选择");
        else
            ((TextView) findViewById(R.id.tv_actionbar_title)).setText("预约技师");
        adapter = new CarSersisAdapter(list, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mType == 0) {
                    CarSerisBean bean = (CarSerisBean) list.get(i);
                    mIntent = new Intent();
                    mIntent.putExtra("bean", bean);
                    setResult(Activity.RESULT_OK, mIntent);
                    finish();
                } else {

                }
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
        if (mType == 0)
            new CarSerisBean().getCarSersis(callback, mBrandbean);
        else
            new JishiBean().getJishilist(callback, mStateid, mDataid, mTimeid);
    }

    HttpCallback callback = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String result) {
            Log.e("--->", "" + result);
            if (mType == 0)
                list.addAll(CarSerisBean.getData(result));
            else
                list.addAll(JishiBean.getData(result));
            adapter.notifyDataSetChanged();
        }
    };

}
