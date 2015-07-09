package cn.car4s.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import cn.car4s.app.R;
import cn.car4s.app.bean.JishiPingjiaBean;
import cn.car4s.app.util.LogUtil;

import java.util.List;

/**
 * User: alex
 * Time: 2014/8/5     10:45
 * Email: xuebo.chang@langtaojin.com
 * Msg:
 */
public class jishipingjiaAdapter extends BaseAdapter {

    List<Object> list;
    Context context;

    public jishipingjiaAdapter(List<Object> list, Context context) {
        this.list = list;
        this.context = context;
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
            view = LayoutInflater.from(context).inflate(R.layout.rating_jish, null);
            viewholder.tv_item_pinendshowcartype_name = (TextView) view.findViewById(R.id.title);
            viewholder.bar = (RatingBar) view.findViewById(R.id.ratingbar);
            viewholder.layout_all = (LinearLayout) view.findViewById(R.id.layout_all);
            view.setTag(viewholder);
        } else {
            viewholder = (Viewholder) view.getTag();
        }
        Object object = list.get(i);
        if (object instanceof JishiPingjiaBean) {
            final JishiPingjiaBean temp = (JishiPingjiaBean) object;
            viewholder.tv_item_pinendshowcartype_name.setText("" + temp.EvaluationType);
//            viewholder.bar.setRating(temp.score);
            viewholder.bar.setTag(temp);
            viewholder.bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    LogUtil.e("--->", "getView " + v);
                    JishiPingjiaBean bean= (JishiPingjiaBean) ratingBar.getTag();
                    bean.score = (int) v;
                }
            });
        }
        return view;
    }


    static class Viewholder {
        TextView tv_item_pinendshowcartype_name;
        RatingBar bar;
        LinearLayout layout_all;
    }
}
