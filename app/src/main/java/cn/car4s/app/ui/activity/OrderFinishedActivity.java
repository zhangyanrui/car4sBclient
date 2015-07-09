package cn.car4s.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.car4s.app.AppConfig;
import cn.car4s.app.R;
import cn.car4s.app.api.HttpCallback;
import cn.car4s.app.bean.OrderBean;
import cn.car4s.app.ui.adapter.OrderAdapter;
import cn.car4s.app.ui.widget.xlistview.XListView;
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
public class OrderFinishedActivity extends BaseActivity implements IBase {
    XListView listView;
    List<Object> list = new ArrayList<Object>();
    OrderAdapter adapter;

    @InjectView(R.id.btn_actionbar_back_img)
    ImageView mActionbarBack;
    @InjectView(R.id.tv_actionbar_title)
    TextView mActionbarTitle;

    int mType;//0 finishorder 1 orderxianxia

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_finished);
        ButterKnife.inject(this);
        mType = getIntent().getIntExtra("type", 0);
        initUI();
        initData();
    }


    @Override
    public void initUI() {
        if (mType == 1) {
            mActionbarTitle.setText("下线订单");
        } else {
            mActionbarTitle.setText("已完成订单");
        }
        mActionbarBack.setVisibility(View.VISIBLE);
        mActionbarBack.setImageResource(R.mipmap.ic_loginactivity_back);
        mActionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        listView = (XListView) findViewById(R.id.listview);
        listView.setXListViewListener(new MyRefreshListener());
        adapter = new OrderAdapter(list, OrderFinishedActivity.this, orderDo);
        listView.setAdapter(adapter);
    }


    HttpCallback callback = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {
            listView.stopRefresh();
            listView.stopLoadMore(false);
        }

        @Override
        public void onResponse(String result) {
            listView.stopRefresh();
            listView.stopLoadMore(false);
            Log.e("--->", "" + result);
            if (mPageNo == 1) {
                list.clear();
            }
            List<OrderBean> listtemp = new OrderBean().getData(result);
            list.addAll(listtemp);
            if (listtemp.size() < AppConfig.PAGE_COUNT) {
                listView.setPullLoadEnable(false);
            } else {
                listView.setPullLoadEnable(true);
            }
            adapter.notifyDataSetChanged();
            if (list.size() == 0) {
                ToastUtil.showToastShort("暂无订单");
            }
        }
    };


    int mPageNo;

    public void load(Boolean isRefresh) {
        if (isRefresh) {
            mPageNo = 1;
        } else {
            mPageNo++;
        }
        if (mType == 0)
            new OrderBean().getorderList(callback, mPageNo, 0);
        else {
            new OrderBean().getorderListXianxia(callback, mPageNo);
        }
    }

    @Override
    public void initData() {
        load(true);
    }

    class MyRefreshListener implements XListView.IXListViewListener {
        @Override
        public void onRefresh() {
            load(true);
        }

        @Override
        public void onLoadMore() {
            load(false);
        }
    }

    OrderAdapter.OrderDo orderDo = new OrderAdapter.OrderDo() {

        @Override
        public void cancelOrder(OrderBean bean) {
            new OrderBean().cancelOrder(callbackCancelorder, bean.OrderID);
        }

        @Override
        public void zhifu(OrderBean orderBean) {
            Intent intent = new Intent(OrderFinishedActivity.this, ProductDetailActivity.class);
            intent.putExtra("type", 1);
            intent.putExtra("orderid", orderBean.OrderID);
            startActivity(intent);
        }

        @Override
        public void edit(OrderBean bean) {
            Intent intent = new Intent(OrderFinishedActivity.this, ProductDetailActivity.class);
            intent.putExtra("type", 2);
            intent.putExtra("orderid", bean.OrderID);
            startActivity(intent);
        }

        @Override
        public void pingjia(OrderBean bean) {
            Intent intent = new Intent(OrderFinishedActivity.this, PingjiaJishiActivity.class);
            intent.putExtra("id", bean.OrderID);
            startActivity(intent);
        }
    };
    HttpCallback callbackCancelorder = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {
        }

        @Override
        public void onResponse(String result) {
            Log.e("--->", "" + result);
            load(true);
        }
    };

}
