package cn.car4s.app.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.car4s.app.AppConfig;
import cn.car4s.app.R;
import cn.car4s.app.api.HttpCallback;
import cn.car4s.app.bean.UserBean;
import cn.car4s.app.ui.activity.*;
import cn.car4s.app.ui.adapter.IvsPagerAdapter;
import cn.car4s.app.util.DeviceUtil;
import cn.car4s.app.util.DialogUtil;
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
public class Tab1Fragment extends BaseFragment implements IBase {
    View rootview;
    ViewPager viewpager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_tab1, null);
        initUI();
        initData();
        return rootview;
    }

    IvsPagerAdapter adapter;
    TextView mActionbarBack;

    @Override
    public void initUI() {
        mActionbarBack = (TextView) rootview.findViewById(R.id.btn_actionbar_back_text);
        mActionbarBack.setVisibility(View.VISIBLE);
        mActionbarBack.setOnClickListener(onClickListener);
        ImageView mActionbarConfirm = (ImageView) rootview.findViewById(R.id.btn_actionbar_conform_img);
        viewpager = (ViewPager) rootview.findViewById(R.id.viewpager);

        mActionbarConfirm.setVisibility(View.VISIBLE);
        mActionbarConfirm.setImageResource(R.mipmap.ic_fragment1_tel);
        mActionbarConfirm.setOnClickListener(onClickListener);
        ((TextView) rootview.findViewById(R.id.tv_actionbar_title)).setText("指尖创业");

        ImageView btn_shengqian = (ImageView) rootview.findViewById(R.id.btn_shengqian);
        ImageView btn_zhengqian = (ImageView) rootview.findViewById(R.id.btn_zhengqian);
        ImageView btn_tiqian = (ImageView) rootview.findViewById(R.id.btn_tiqian);
        btn_shengqian.setOnClickListener(onClickListener);
        btn_zhengqian.setOnClickListener(onClickListener);
        btn_tiqian.setOnClickListener(onClickListener);
        viewpager.getLayoutParams().height = (int) (DeviceUtil.getWidth() * 374 / 640);
    }

    String pos_name;

    @Override
    public void initData() {
        pos_name = PreferencesUtil.getPreferences(AppConfig.SP_KEY_CHOOSEPOSITION_NAME, "");
        if (TextUtils.isEmpty(pos_name)) {
            mActionbarBack.setText("选择区域");
        } else {
            mActionbarBack.setText("区域：" + pos_name);
        }
        loaddata();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.btn_actionbar_back_text:
                    mIntent = new Intent(getActivity(), ChoosePositionActivity.class);
                    Tab1Fragment.this.startActivityForResult(mIntent, AppConfig.REQUEST_CODE_CHOOSE_POS);
                    break;
                case R.id.btn_actionbar_conform_img:
                    DialogUtil.buildTelDialog(getActivity());
                    break;
                case R.id.btn_shengqian:
                    mIntent = new Intent(getActivity(), ShengqianActivity.class);
                    startActivityForResult(mIntent, AppConfig.REQUEST_CODE_PAY);
                    break;
                case R.id.btn_zhengqian:
                    mIntent = new Intent(getActivity(), ZhengqianActivity.class);
                    startActivity(mIntent);
                    break;
                case R.id.btn_tiqian:
                    if (UserBean.checkUserLoginStatus()) {
                        mIntent = new Intent(getActivity(), TixianActivity.class);
                        startActivity(mIntent);
                    } else {
                        UserBean.toLogin(getActivity(), AppConfig.REQUEST_CODE_LOGIN);
                    }
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConfig.REQUEST_CODE_CHOOSE_POS && resultCode == Activity.RESULT_OK) {
            initData();
        }
        if (requestCode == AppConfig.REQUEST_CODE_PAY && resultCode == Activity.RESULT_OK) {
            ((MainTabActivity) getActivity()).changeTotab2();
        }
    }


    public void loaddata() {
        new UserBean().getBanner(callback);
    }

    List<Object> list = new ArrayList<Object>();
    HttpCallback callback = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String result) {
            Log.e("--->", "" + result);
            list.clear();
            list.addAll(UserBean.getData(result));
            adapter = new IvsPagerAdapter(list, getActivity(), 0, null);
            viewpager.setAdapter(adapter);
//            adapter.notifyDataSetChanged();

        }
    };

}
