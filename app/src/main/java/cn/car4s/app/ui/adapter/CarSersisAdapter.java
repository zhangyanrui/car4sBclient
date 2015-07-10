package cn.car4s.app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.car4s.app.AppContext;
import cn.car4s.app.R;
import cn.car4s.app.bean.CarSerisBean;
import cn.car4s.app.bean.JishiBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * User: alex
 * Time: 2014/8/5     10:45
 * Email: xuebo.chang@langtaojin.com
 * Msg:
 */
public class CarSersisAdapter extends BaseAdapter {

    List<Object> list;
    Context context;

    public CarSersisAdapter(List<Object> list, Context context) {
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
            view = LayoutInflater.from(context).inflate(R.layout.adapter_item_car_seris, null);
            viewholder.tv_item_pinendshowcartype_name = (TextView) view.findViewById(R.id.tv_item_pinendshowcartype_name);
            viewholder.tv_item_pinedshowcartype_title = (TextView) view.findViewById(R.id.tv_item_pinedshowcartype_title);
            viewholder.layout_all = (LinearLayout) view.findViewById(R.id.layout_all);
            viewholder.carimg = (ImageView) view.findViewById(R.id.img_car);
            viewholder.img_yuyue = (TextView) view.findViewById(R.id.img_yuyue);
            view.setTag(viewholder);
        } else {
            viewholder = (Viewholder) view.getTag();
        }
        Object object = list.get(i);
        if (object instanceof CarSerisBean) {
            CarSerisBean bean = (CarSerisBean) object;
            viewholder.tv_item_pinendshowcartype_name.setText(bean.SeriesName);
            ImageLoader.getInstance().displayImage(bean.ImgPath, viewholder.carimg, AppContext.display_round_imageloader);
        } else if (object instanceof JishiBean) {
            JishiBean bean = (JishiBean) object;
            ImageLoader.getInstance().displayImage(bean.HeadPicturePath, viewholder.carimg, AppContext.display_avaster_imageloader);
            viewholder.tv_item_pinendshowcartype_name.setText(bean.UserName);
            viewholder.img_yuyue.setVisibility(View.VISIBLE);
//            if ("F".equals(bean.IsBusy)) {
//                viewholder.img_yuyue.setBackgroundResource(R.drawable.shape_jish_notbusy);
//                viewholder.img_yuyue.setText("预约");
//            } else {
//                viewholder.img_yuyue.setBackgroundResource(R.drawable.shape_jish_busy);
//                viewholder.img_yuyue.setText("忙碌");
//            }
            viewholder.img_yuyue.setTag(bean);
            viewholder.img_yuyue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JishiBean bean = (JishiBean) view.getTag();
                    Intent intent = new Intent();
                    intent.putExtra("bean", bean);
                    ((Activity) context).setResult(Activity.RESULT_OK, intent);
                    ((Activity) context).finish();
//                    if ("F".equals(bean.IsBusy)) {
//                        Intent intent = new Intent();
//                        intent.putExtra("bean", bean);
//                        ((Activity) context).setResult(Activity.RESULT_OK, intent);
//                        ((Activity) context).finish();
//                    } else {
//                        return;
//                    }
                }
            });


        }
        return view;
    }


    static class Viewholder {
        TextView tv_item_pinendshowcartype_name;
        TextView tv_item_pinedshowcartype_title;
        ImageView carimg;
        TextView img_yuyue;
        LinearLayout layout_all;
    }
}
