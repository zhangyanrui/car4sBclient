package cn.car4s.app.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.car4s.app.AppConfig;
import cn.car4s.app.R;
import cn.car4s.app.api.HttpCallback;
import cn.car4s.app.bean.CarBrandBean;
import cn.car4s.app.ui.adapter.CarBrandAdapter;
import cn.car4s.app.ui.widget.SideBar;
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
public class ChooseCarbrandActivity extends BaseActivity implements IBase {
    List<CarBrandBean> list = new ArrayList<CarBrandBean>();
    CarBrandAdapter adapter;
    @InjectView(R.id.pinnedlv_show)
    ListView recyclerView;
    @InjectView(R.id.sidebar_show)
    SideBar indexBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_carbrand);
        ButterKnife.inject(this);
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

        ((TextView) findViewById(R.id.tv_actionbar_title)).setText("选择品牌");

        adapter = new CarBrandAdapter(list, this);
        recyclerView.setAdapter(adapter);
        indexBar.setListView(recyclerView);
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
        new CarBrandBean().getCarbrand(callback);
    }

    HttpCallback callback = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String result) {
            Log.e("--->", "" + result);
            list.addAll(CarBrandBean.getData(result));
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == AppConfig.REQUEST_CODE_CHOOSECAR) {
            setResult(Activity.RESULT_OK, data);
            finish();
        }
    }
}
