package cn.car4s.app.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.car4s.app.AppConfig;
import cn.car4s.app.R;
import cn.car4s.app.api.HttpCallback;
import cn.car4s.app.bean.Paimingbean;
import cn.car4s.app.bean.UserBean;
import cn.car4s.app.ui.widget.PaimingLayout;
import cn.car4s.app.util.UtilShare;
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
public class ZhengqianActivity extends BaseActivity implements IBase {
    @InjectView(R.id.btn_actionbar_back_img)
    ImageView mActionbarBack;
    @InjectView(R.id.tv_actionbar_title)
    TextView mActionbarTitle;
    @InjectView(R.id.layout_actionbar_all)
    RelativeLayout mActionbarBackLayoutall;

    @InjectView(R.id.layout_all1)
    LinearLayout layout_all1;
    @InjectView(R.id.layout_all2)
    LinearLayout layout_all2;
    @InjectView(R.id.btn_dabao)
    TextView mBtnFanli;
    @InjectView(R.id.btn_xiaobao)
    TextView mBtntuijian;

    @InjectView(R.id.brand)
    PaimingLayout brand;
    @InjectView(R.id.share_layout)
    LinearLayout mShareLayout;
    @InjectView(R.id.share_qq)
    TextView mShareBtnQQ;
    @InjectView(R.id.share_weixin)
    TextView mShareBtnWX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhengqian);
        ButterKnife.inject(this);
        initUI();
        initData();
    }

    int mtype = 0;
    Paimingbean paimingbean = new Paimingbean();


    @Override
    public void initUI() {
        mActionbarBack.setVisibility(View.VISIBLE);
        mActionbarBack.setImageResource(R.mipmap.ic_loginactivity_back);
        mActionbarBack.setOnClickListener(onClickListener);

        mShareBtnWX.setOnClickListener(onClickListener);
        mShareBtnQQ.setOnClickListener(onClickListener);

        paimingbean.type = 2;
        brand.setData(paimingbean);

        if (mtype == 0) {
            mActionbarTitle.setText("当月返利排名");
            layout_all1.setVisibility(View.VISIBLE);
            layout_all2.setVisibility(View.GONE);
        } else {
            mActionbarTitle.setText("当月推荐排名");
            layout_all2.setVisibility(View.VISIBLE);
            layout_all1.setVisibility(View.GONE);
        }

        mBtnFanli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mtype = 0;
                initUI();
            }
        });
        mBtntuijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mtype = 1;
                initUI();
                if (list2.size() == 0)
                    new Paimingbean().tuijianpaiming(callback2);
            }
        });
    }


    List<Paimingbean> list = new ArrayList<Paimingbean>();
    List<Paimingbean> list2 = new ArrayList<Paimingbean>();

    public void updateUI() {
        for (int i = 0; i < list.size(); i++) {
            Paimingbean bean = (Paimingbean) list.get(i);
            bean.position = i;
            bean.type = 0;
            PaimingLayout ratingLayout = new PaimingLayout(this);
            ratingLayout.setData(bean);
            layout_all1.addView(ratingLayout);
        }
    }

    public void updateUI2() {
        for (int i = 0; i < list2.size(); i++) {
            Paimingbean bean = (Paimingbean) list2.get(i);
            bean.position = i;
            bean.type = 1;
            PaimingLayout ratingLayout = new PaimingLayout(this);
            ratingLayout.setData(bean);
            layout_all2.addView(ratingLayout);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.btn_actionbar_back_img:
                    finish();
                    break;
                case R.id.share_qq:
                    if (UserBean.checkUserLoginStatus()) {
                        mUserbean = UserBean.getLocalUserinfo();
                        UtilShare.shareQQ(mUserbean.ReferralCode_I);
                    } else {
                        UserBean.toLogin(ZhengqianActivity.this, AppConfig.REQUEST_CODE_LOGIN);
                    }
                    break;
                case R.id.share_weixin:
                    if (UserBean.checkUserLoginStatus()) {
                        mUserbean = UserBean.getLocalUserinfo();
                        UtilShare.shareWXFriend(mUserbean.ReferralCode_I);
                    } else {
                        UserBean.toLogin(ZhengqianActivity.this, AppConfig.REQUEST_CODE_LOGIN);
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
        public void onResponse(String result) {
            list.clear();
            list.addAll(Paimingbean.getData(result));
            updateUI();
        }
    };
    HttpCallback callback2 = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String result) {
            list2.clear();
            list2.addAll(Paimingbean.getData(result));
            updateUI2();
        }
    };


    @Override
    public void initData() {
        new Paimingbean().fanlipaiming(callback);

    }
}
