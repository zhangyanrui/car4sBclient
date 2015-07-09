package cn.car4s.app.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.car4s.app.AppConfig;
import cn.car4s.app.R;
import cn.car4s.app.api.HttpCallback;
import cn.car4s.app.bean.JishiBean;
import cn.car4s.app.bean.JishiPingjiaBean;
import cn.car4s.app.ui.widget.RatingLayout;
import cn.car4s.app.util.LogUtil;
import cn.car4s.app.util.PreferencesUtil;
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
public class PingjiaJishiActivity extends BaseActivity implements IBase {
    @InjectView(R.id.btn_actionbar_back_img)
    ImageView mActionbarBack;
    @InjectView(R.id.tv_actionbar_title)
    TextView mActionbarTitle;
    @InjectView(R.id.layout_all)
    LinearLayout layout_all;

    //
    @InjectView(R.id.edt_feedback_text)
    EditText mEdtText;
    @InjectView(R.id.btn_feedback_commit)
    TextView mBtnCommit;
//    @InjectView(R.id.ratingbar1)
//    RatingBar ratingBar1;
//    @InjectView(R.id.ratingbar2)
//    RatingBar ratingBar2;
//    @InjectView(R.id.ratingbar3)
//    RatingBar ratingBar3;
//    @InjectView(R.id.ratingbar4)
//    RatingBar ratingBar4;

//    @InjectView(R.id.tv_1)
//    TextView tv1;
//    @InjectView(R.id.tv_2)
//    TextView tv2;
//    @InjectView(R.id.tv_3)
//    TextView tv3;
//    @InjectView(R.id.tv_4)
//    TextView tv4;
//
//    @InjectView(R.id.listview)
//    ListView listView;
//    jishipingjiaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jishipingjia);
        ButterKnife.inject(this);
        ordreid = getIntent().getStringExtra("id");
        initUI();
        initData();
    }

    String ordreid;

    @Override
    public void initUI() {
        mActionbarBack.setVisibility(View.VISIBLE);
        mActionbarBack.setImageResource(R.mipmap.ic_loginactivity_back);
        mActionbarBack.setOnClickListener(onClickListener);
        mBtnCommit.setOnClickListener(onClickListener);
        mActionbarTitle.setText("技师评价");
//        adapter = new jishipingjiaAdapter(list, this);
//        listView.setAdapter(adapter);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.btn_actionbar_back_img:
                    finish();
                    break;
                case R.id.btn_feedback_commit:
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < list.size(); i++) {
                        JishiPingjiaBean bean = (JishiPingjiaBean) list.get(i);
                        sb.append(bean.EvaluationID + ":" + bean.score);
                        if (i != list.size() - 1)
                            sb.append(";");
                    }

                    String text = mEdtText.getText().toString().trim();
                    LogUtil.e("--->", sb.toString() + "    " + text);
                    new JishiBean().pingjiajishi(callback, ordreid, sb.toString(), text);
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
    HttpCallback callbackpara = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String bean) {
            PreferencesUtil.putPreferences(AppConfig.SP_KEY_PINGJIA, bean);
            list.clear();
            list.addAll(JishiPingjiaBean.getData(bean));
//            adapter.notifyDataSetChanged();
            updateUI();
        }
    };
    List<Object> list = new ArrayList<Object>();

    public void updateUI() {

        for (int i = 0; i < list.size(); i++) {
            JishiPingjiaBean bean = (JishiPingjiaBean) list.get(i);
//            tv1.setText(bean.EvaluationType);
            RatingLayout ratingLayout = new RatingLayout(this);
            ratingLayout.setData(bean);
            layout_all.addView(ratingLayout);

        }

    }

    @Override
    public void initData() {
        new JishiBean().getPingjiaPara(callbackpara);
    }


}
