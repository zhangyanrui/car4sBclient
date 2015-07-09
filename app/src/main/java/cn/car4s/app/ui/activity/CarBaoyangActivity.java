package cn.car4s.app.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import cn.car4s.app.bean.CarSerisBean;
import cn.car4s.app.bean.ProductBean;
import cn.car4s.app.bean.UserBean;
import cn.car4s.app.ui.adapter.ProductAdapter;
import cn.car4s.app.ui.widget.EndlessRecyclerOnScrollListener;
import cn.car4s.app.ui.widget.RecyclerItemClickListener;
import cn.car4s.app.util.LogUtil;
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
public class CarBaoyangActivity extends BaseActivity implements IBase {
    @InjectView(R.id.btn_actionbar_back_img)
    ImageView mActionbarBack;
    @InjectView(R.id.tv_actionbar_title)
    TextView mActionbarTitle;
    @InjectView(R.id.recyclerview)
    RecyclerView recyclerView;

    @InjectView(R.id.btn_chooseCarbrand)
    RelativeLayout mBtnChooseCarbrand;
    @InjectView(R.id.layout_top_dabao)
    LinearLayout mLayoutTopDabao;
    @InjectView(R.id.layout_top_baoyang)
    LinearLayout mLayoutTopBaoyang;
    @InjectView(R.id.btn_dabao)
    TextView mBtnDabao;
    @InjectView(R.id.btn_xiaobao)
    TextView mBtnXiaobao;
    @InjectView(R.id.tv_top_carseris)
    TextView mTextTopCarseris;


    List<ProductBean> list = new ArrayList<ProductBean>();
    ProductAdapter adapter;
    RecyclerItemClickListener itemlistener;

    private int mType;//2hot  0dabao  1xiaobao

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carbaoyang);
        mType = getIntent().getIntExtra("type", 2);
        ButterKnife.inject(this);
        initUI();
        initData();
    }

    @Override
    public void initUI() {
        mActionbarBack.setVisibility(View.VISIBLE);
        mActionbarBack.setImageResource(R.mipmap.ic_loginactivity_back);
        mActionbarBack.setOnClickListener(onClickListener);
        if (mType == 0) {
            mActionbarTitle.setText("大保");
            mLayoutTopDabao.setVisibility(View.VISIBLE);
        } else if (mType == 1) {
            mActionbarTitle.setText("小保");
            mLayoutTopDabao.setVisibility(View.VISIBLE);
        } else if (mType == 2) {
            mActionbarTitle.setText("汽车保养");
            mLayoutTopBaoyang.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ProductAdapter(list, this);
        recyclerView.setAdapter(adapter);
        itemlistener = new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (UserBean.checkUserLoginStatus()) {
                    ProductBean bean = list.get(position);
                    mIntent = new Intent(CarBaoyangActivity.this, ProductDetailActivity.class);
                    mIntent.putExtra("bean", bean);
                    mIntent.putExtra("serisid", serisId);
                    startActivityForResult(mIntent, AppConfig.REQUEST_CODE_PAY);
                } else {
                    UserBean.toLogin(CarBaoyangActivity.this, AppConfig.REQUEST_CODE_LOGIN);
                }
            }
        });
        recyclerView.addOnItemTouchListener(itemlistener);
        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                LogUtil.e("---<", "onLoadMore" + current_page);
                if (mHasNext) {
                    loadData(false);
                }
            }
        });

        mBtnChooseCarbrand.setOnClickListener(onClickListener);
        mBtnDabao.setOnClickListener(onClickListener);
        mBtnXiaobao.setOnClickListener(onClickListener);
    }


    HttpCallback callback = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String result) {
            Log.e("--->", "" + result);
            if (mCurrentPage == 1) {
                list.clear();
            }
            List<ProductBean> listnet = ProductBean.getData(result);
            if (listnet.size() < AppConfig.PAGE_COUNT) {
                mHasNext = false;
            } else {
                mHasNext = true;
            }
            list.addAll(listnet);
            adapter.notifyDataSetChanged();
        }
    };


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.btn_actionbar_back_img:
                    finish();
                    break;
                case R.id.btn_dabao:
                    mIntent = new Intent(CarBaoyangActivity.this, CarBaoyangActivity.class);
                    mIntent.putExtra("type", 0);
                    startActivity(mIntent);
                    break;
                case R.id.btn_xiaobao:
                    mIntent = new Intent(CarBaoyangActivity.this, CarBaoyangActivity.class);
                    mIntent.putExtra("type", 1);
                    startActivity(mIntent);
                    break;
                case R.id.btn_chooseCarbrand:
                    mIntent = new Intent(CarBaoyangActivity.this, ChooseCarbrandActivity.class);
                    startActivityForResult(mIntent, AppConfig.REQUEST_CODE_CHOOSECAR);
                    break;
            }
        }
    };

    private ProductBean mProductBean = null;
    private int mCurrentPage = 1;

    @Override
    public void initData() {
        if (mType == 2)
            mProductBean = new ProductBean(mType, 0, mCurrentPage, true);
        else
            mProductBean = new ProductBean(mType, 0, mCurrentPage, false);
        loadData(true);
    }

    public void loadData(boolean isRefresh) {
        if (isRefresh) {
            mCurrentPage = 1;
            mHasNext = true;
        } else {
            mCurrentPage++;
        }
        mProductBean.mPageNo = mCurrentPage;
        mProductBean.getProductList(callback, mProductBean);
    }

    int serisId;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == AppConfig.REQUEST_CODE_CHOOSECAR) {
            CarSerisBean carSerisBean = (CarSerisBean) data.getSerializableExtra("bean");
            mTextTopCarseris.setText(carSerisBean.SeriesName);
            serisId = Integer.parseInt(carSerisBean.SeriesID);
            mProductBean.mCarbrandType = Integer.parseInt(carSerisBean.SeriesID);
            loadData(true);
        }
    }
}
