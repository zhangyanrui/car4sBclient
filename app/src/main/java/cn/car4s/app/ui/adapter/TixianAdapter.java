package cn.car4s.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.car4s.app.R;
import cn.car4s.app.bean.TixianBean;
import cn.car4s.app.bean.TixianListBean;

import java.util.List;

/**
 * User: alex
 * Time: 2014/8/5     10:45
 * Email: xuebo.chang@langtaojin.com
 * Msg:
 */
public class TixianAdapter extends BaseAdapter {

    List<Object> list;
    Context context;
    TixianInterface tixianInterface;

    public TixianAdapter(List<Object> list, Context context, TixianInterface tixianInterface) {
        this.list = list;
        this.context = context;
        this.tixianInterface = tixianInterface;
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
        if (view == null) {
            viewholder = new Viewholder();
            view = LayoutInflater.from(context).inflate(R.layout.adapter_item_tixian, null);
            viewholder.title = (TextView) view.findViewById(R.id.tv_title);
            viewholder.desc1 = (TextView) view.findViewById(R.id.tv_desc1);
            viewholder.layout_all = (LinearLayout) view.findViewById(R.id.layout_all);
            viewholder.layout_zhifu = (LinearLayout) view.findViewById(R.id.layout_zhifu);
            viewholder.layout_bianji = (LinearLayout) view.findViewById(R.id.layout_bianji);
            viewholder.layout_pingjia = (LinearLayout) view.findViewById(R.id.layout_pingjia);
            viewholder.desc2 = (TextView) view.findViewById(R.id.tv_desc2);
            viewholder.tv_timeshengyu = (TextView) view.findViewById(R.id.tv_timeshengyu);
            viewholder.tv_timeshengyu2 = (TextView) view.findViewById(R.id.tv_timeshengyu2);
            viewholder.edit = (TextView) view.findViewById(R.id.edit);
            viewholder.cancel = (TextView) view.findViewById(R.id.cancel);
            viewholder.jiesuan = (TextView) view.findViewById(R.id.jiesuan);
            viewholder.pingjia = (TextView) view.findViewById(R.id.pingjia);
            view.setTag(viewholder);
        } else {
            viewholder = (Viewholder) view.getTag();
        }
        Object object = list.get(i);
        viewholder.layout_zhifu.setVisibility(View.GONE);
        viewholder.layout_bianji.setVisibility(View.VISIBLE);
        viewholder.layout_pingjia.setVisibility(View.GONE);
        viewholder.tv_timeshengyu2.setVisibility(View.VISIBLE);
        viewholder.edit.setVisibility(View.VISIBLE);
        if (object instanceof TixianListBean) {
            TixianListBean bean = (TixianListBean) object;
            viewholder.title.setText("可用积分: " + bean.AvailablePoint);
            viewholder.desc1.setText("冻结积分: " + bean.FreezingPoint);
            viewholder.desc2.setText("保底积分: " + bean.GuaranteedPoint);
            viewholder.tv_timeshengyu.setText("最低体现额: " + bean.LowestLimit);
            viewholder.tv_timeshengyu2.setText("可提现积分: " + bean.AvailableWithdrawalPoint);
            viewholder.edit.setText("申请体现");
            viewholder.edit.setTag(bean);
            viewholder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TixianListBean temp = (TixianListBean) view.getTag();
                    tixianInterface.tixian(temp);
                }
            });
        } else if (object instanceof TixianBean) {
            TixianBean bean = (TixianBean) object;
            viewholder.title.setText("申请时间: " + bean.ApplicationTime);
            viewholder.desc1.setText("提现积分: " + bean.WithdrawalPoint + "      折现金额:" + bean.WithdrawalAmount + "\n\n折现方式:" + bean.TransferMode);
            viewholder.desc2.setText("状态: " + bean.Status);
            if ("0".equals(bean.Status)) {//weiqueren
                viewholder.desc2.setText("状态: 未确认");
                viewholder.tv_timeshengyu.setText("");
//                viewholder.tv_timeshengyu.setVisibility(View.GONE);
            } else {
                viewholder.desc2.setText("状态: 已确认");
//                viewholder.tv_timeshengyu.setVisibility(View.VISIBLE);
                viewholder.tv_timeshengyu.setText("确认时间: " + bean.qurenshijian);
                viewholder.edit.setVisibility(View.GONE);
            }
            viewholder.tv_timeshengyu2.setVisibility(View.GONE);
            viewholder.edit.setText("取消");
            viewholder.edit.setTag(bean);
            viewholder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TixianBean temp = (TixianBean) view.getTag();
                    tixianInterface.cancel(temp);
                }
            });
            viewholder.layout_all.setTag(bean);
            viewholder.layout_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TixianBean temp = (TixianBean) view.getTag();
                    tixianInterface.showdetial(temp);
                }
            });

        }
        return view;
    }

    public interface TixianInterface {
        void tixian(TixianListBean bean);

        void cancel(TixianBean bean);

        void showdetial(TixianBean bean);

    }


    static class Viewholder {
        TextView title;
        TextView desc1;
        TextView desc2;
        TextView tv_timeshengyu;
        TextView tv_timeshengyu2;
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
