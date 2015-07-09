package cn.car4s.app.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.car4s.app.R;
import cn.car4s.app.api.HttpCallback;
import cn.car4s.app.bean.OrderBean;
import cn.car4s.app.ui.activity.IBase;
import cn.car4s.app.ui.activity.OrderFinishedActivity;
import cn.car4s.app.ui.activity.ProductDetailActivity;
import cn.car4s.app.ui.adapter.OrderAdapter;
import cn.car4s.app.ui.widget.xlistview.XListView;
import cn.car4s.app.util.LogUtil;
import cn.car4s.app.util.ToastUtil;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/4/22.
 */
public class Tab2Fragment extends BaseFragment implements IBase {
    View rootview;
    XListView listView;
    List<Object> list = new ArrayList<Object>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_tab2, null);
        LogUtil.e("--->", "onCreateView");
        flag = true;
        initUI();
        initData();
        return rootview;
    }

    OrderAdapter adapter;

    @Override
    public void initUI() {
        ((TextView) rootview.findViewById(R.id.tv_actionbar_title)).setText(getString(R.string.tab2));
        ((TextView) rootview.findViewById(R.id.btn_weiwanchengorder)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent m = new Intent(getActivity(), OrderFinishedActivity.class);
                startActivity(m);
            }
        });
        ((TextView) rootview.findViewById(R.id.btn_yiwanchengorder)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent m = new Intent(getActivity(), OrderFinishedActivity.class);
                m.putExtra("type", 1);
                startActivity(m);
            }
        });
        listView = (XListView) rootview.findViewById(R.id.listview);
        listView.setXListViewListener(new MyRefreshListener());
        list.clear();
        adapter = new OrderAdapter(list, getActivity(), orderDo);
        listView.setAdapter(adapter);
    }


    @Override
    public void onPause() {
        LogUtil.e("--->", "onPause");
        super.onPause();
        flag = false;
    }

    boolean flag = false;

    public void TimeStart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (flag) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Message msg = new Message();
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    LogUtil.e("--->", "handleMessage");
                    long time = new Date().getTime();
                    adapter.settime(time);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
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
            List<OrderBean> listnet = new OrderBean().getData(result);
            if (listnet.size() < 20) {
                listView.setPullLoadEnable(false);
            } else {
                listView.setPullLoadEnable(true);
            }
            list.addAll(listnet);
            flag = true;
            TimeStart();
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
        new OrderBean().getorderList(callback, mPageNo, -1);
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
            Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
            intent.putExtra("type", 1);
            intent.putExtra("orderid", orderBean.OrderID);
            startActivityForResult(intent, 1001);
        }

        @Override
        public void edit(OrderBean bean) {
            Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
            intent.putExtra("type", 2);
            intent.putExtra("orderid", bean.OrderID);
            startActivityForResult(intent, 1001);
        }

        @Override
        public void pingjia(OrderBean bean) {

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        flag = true;
        initData();
    }
}
