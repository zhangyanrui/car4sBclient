package cn.car4s.app.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.car4s.app.R;
import cn.car4s.app.bean.OrderBean;
import cn.car4s.app.ui.activity.OrderFinishedActivity;
import cn.car4s.app.ui.activity.ProductDetailActivity;
import cn.car4s.app.util.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * User: alex
 * Time: 2014/8/5     10:45
 * Email: xuebo.chang@langtaojin.com
 * Msg:
 */
public class OrderAdapter extends BaseAdapter {

    List<Object> list;
    Context context;
    OrderDo orderDo;
    SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    SimpleDateFormat df2 = new SimpleDateFormat("mm:ss");

    public int mType;

    public OrderAdapter(List<Object> list, Context context, OrderDo orderDo) {
        this.list = list;
        this.context = context;
        this.orderDo = orderDo;
    }

    public long mCurrenttime;

    public void settime(long time) {
        this.mCurrenttime = time;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Viewholder viewholder;
        LogUtil.e("--->", "getview");
        if (view == null) {
            viewholder = new Viewholder();
            view = LayoutInflater.from(context).inflate(R.layout.adapter_item_order, null);
            viewholder.title = (TextView) view.findViewById(R.id.tv_title);
            viewholder.desc1 = (TextView) view.findViewById(R.id.tv_desc1);
            viewholder.layout_all = (LinearLayout) view.findViewById(R.id.layout_all);
            viewholder.layout_zhifu = (LinearLayout) view.findViewById(R.id.layout_zhifu);
            viewholder.layout_bianji = (LinearLayout) view.findViewById(R.id.layout_bianji);
            viewholder.layout_pingjia = (LinearLayout) view.findViewById(R.id.layout_pingjia);
            viewholder.desc2 = (TextView) view.findViewById(R.id.tv_desc2);
            viewholder.tv_timeshengyu = (TextView) view.findViewById(R.id.tv_timeshengyu);
            viewholder.edit = (TextView) view.findViewById(R.id.edit);
            viewholder.cancel = (TextView) view.findViewById(R.id.cancel);
            viewholder.jiesuan = (TextView) view.findViewById(R.id.jiesuan);
            viewholder.pingjia = (TextView) view.findViewById(R.id.pingjia);
            view.setTag(viewholder);
        } else {
            viewholder = (Viewholder) view.getTag();
        }
        Object object = list.get(i);
        final OrderBean bean = (OrderBean) object;
        viewholder.layout_zhifu.setVisibility(View.GONE);
        viewholder.layout_bianji.setVisibility(View.GONE);
        viewholder.layout_pingjia.setVisibility(View.GONE);
        viewholder.tv_timeshengyu.setVisibility(View.GONE);
        if ("2".equals(bean.OrderStatus)) {//daizhifu
            viewholder.layout_zhifu.setVisibility(View.VISIBLE);
            String string = null;
            try {
                Date date = df1.parse(bean.OrderTime);
                long timeserver = date.getTime();
                long timespace = mCurrenttime - timeserver;
                long timeleft = 14 * 60 * 1000 - timespace;
                if (timeleft < 0) {
                    list.remove(i);
                    notifyDataSetChanged();
                }
                string = df2.format(timeleft);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            viewholder.tv_timeshengyu.setText("剩余支付时间: " + string);
            viewholder.tv_timeshengyu.setVisibility(View.VISIBLE);
            viewholder.title.setBackgroundResource(R.drawable.shape_jish_notbusy);
        } else if ("1".equals(bean.OrderStatus)) {
            viewholder.layout_bianji.setVisibility(View.VISIBLE);
            viewholder.tv_timeshengyu.setVisibility(View.GONE);
            viewholder.title.setBackgroundResource(R.drawable.shape_jish_notbusy);
        } else if ("0".equals(bean.OrderStatus)) {//wanchengdingdan
            if ("T".equals(bean.EvaluationFlag)) {
                viewholder.pingjia.setText("您已对该技师评价过");
                viewholder.pingjia.setEnabled(false);
            } else {
                viewholder.pingjia.setText("给技师评价");
            }
            viewholder.layout_pingjia.setVisibility(View.VISIBLE);
            viewholder.tv_timeshengyu.setVisibility(View.GONE);
            viewholder.title.setBackgroundResource(R.drawable.shape_jish_busy);
            viewholder.layout_all.setTag(bean);
            viewholder.layout_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OrderBean bean1 = (OrderBean) view.getTag();
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("type", 3);
                    intent.putExtra("orderid", bean1.OrderID);
                    context.startActivity(intent);
                }
            });
        } else {//下线订单
            viewholder.title.setText("套餐: " + bean.ProductName);
            viewholder.desc1.setText("下单时间: " + bean.OrderTime);
            viewholder.desc2.setText("网点: " + bean.StationName);
            if (mType == 0) {//查询订单
                viewholder.layout_pingjia.setVisibility(View.GONE);
                viewholder.pingjia.setText("");
                viewholder.tv_timeshengyu.setText("手机号: " + bean.PhoneNumber + "\n\n" + "用户: " + bean.UserName + "\n\n" + "技师: " + bean.TechnicianName);
            } else if (mType == 1) {//绩效
                viewholder.title.setText("技师: " + bean.TechnicianName);
                viewholder.desc2.setText("完成订单: " + bean.OrderNum + " 单");
                viewholder.desc1.setText("网点: " + bean.StationName);
                viewholder.tv_timeshengyu.setText("奖励: " + bean.AwardMoney + " 元");
            } else if (mType == 5) {//绩效详情
                viewholder.layout_pingjia.setVisibility(View.GONE);
                viewholder.pingjia.setText("");
                viewholder.tv_timeshengyu.setText("手机号: " + bean.PhoneNumber + "\n\n" + "用户: " + bean.UserName + "\n\n" + "技师: " + bean.TechnicianName
                        + "\n\n" + "奖励金额: " + bean.CommissionAmount);
            } else {//待处理订单
                viewholder.layout_pingjia.setVisibility(View.VISIBLE);
                viewholder.pingjia.setText("完成");
                viewholder.tv_timeshengyu.setText("手机号: " + bean.PhoneNumber + "\n\n" + "用户: " + bean.UserName);
            }
            viewholder.tv_timeshengyu.setVisibility(View.VISIBLE);
            viewholder.title.setBackgroundResource(R.color.transparent);
            viewholder.layout_all.setTag(bean);
            viewholder.layout_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OrderBean bean1 = (OrderBean) view.getTag();
                    if (mType == 1) {
                        Intent intent = new Intent(context, OrderFinishedActivity.class);
                        intent.putExtra("type", 5);
                        intent.putExtra("bean", bean1);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, ProductDetailActivity.class);
                        intent.putExtra("type", 3);
                        intent.putExtra("orderid", bean1.OrderID);
                        context.startActivity(intent);
                    }
                }
            });
        }

        viewholder.edit.setTag(bean);
        viewholder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderBean bean1 = (OrderBean) view.getTag();
                orderDo.edit(bean1);
            }
        });
        viewholder.cancel.setTag(bean);
        viewholder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderBean bean1 = (OrderBean) view.getTag();
                orderDo.cancelOrder(bean1);
            }
        });
        viewholder.jiesuan.setTag(bean);
        viewholder.jiesuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderBean bean1 = (OrderBean) view.getTag();
                orderDo.zhifu(bean1);

            }
        });
        viewholder.pingjia.setTag(bean);
        viewholder.pingjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderBean bean1 = (OrderBean) view.getTag();
                orderDo.pingjia(bean1);
            }
        });


        return view;
    }

    public interface OrderDo {

        public void cancelOrder(OrderBean bean);

        public void zhifu(OrderBean bean);

        public void edit(OrderBean bean);

        public void pingjia(OrderBean bean);
    }

    static class Viewholder {
        TextView title;
        TextView desc1;
        TextView desc2;
        TextView tv_timeshengyu;
        TextView edit;
        TextView cancel;
        TextView jiesuan;
        TextView pingjia;
        LinearLayout layout_all;
        LinearLayout layout_zhifu;
        LinearLayout layout_bianji;
        LinearLayout layout_pingjia;
    }
}
