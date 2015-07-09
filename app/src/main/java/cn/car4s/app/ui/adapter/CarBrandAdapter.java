package cn.car4s.app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import cn.car4s.app.AppConfig;
import cn.car4s.app.AppContext;
import cn.car4s.app.R;
import cn.car4s.app.bean.CarBrandBean;
import cn.car4s.app.ui.activity.ChooseCarStyleActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * User: alex
 * Time: 2014/8/5     10:45
 * Email: xuebo.chang@langtaojin.com
 * Msg:
 */
public class CarBrandAdapter extends BaseAdapter implements SectionIndexer {

    List<CarBrandBean> list;
    Context context;

    public CarBrandAdapter(List<CarBrandBean> list, Context context) {
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
            view = LayoutInflater.from(context).inflate(R.layout.adapter_item_car_brand, null);
            viewholder.tv_item_pinendshowcartype_name = (TextView) view.findViewById(R.id.tv_item_pinendshowcartype_name);
            viewholder.tv_item_pinedshowcartype_title = (TextView) view.findViewById(R.id.tv_item_pinedshowcartype_title);
            viewholder.layout_all = (LinearLayout) view.findViewById(R.id.layout_all);
            viewholder.layoutbottom = (LinearLayout) view.findViewById(R.id.layoutbottom);
            viewholder.carimg = (ImageView) view.findViewById(R.id.img_car);
            view.setTag(viewholder);
        } else {
            viewholder = (Viewholder) view.getTag();
        }

        CarBrandBean bean = (CarBrandBean) list.get(i);
        viewholder.tv_item_pinendshowcartype_name.setText(bean.BrandName);
        ImageLoader.getInstance().displayImage(bean.ImgPath, viewholder.carimg, AppContext.display_round_imageloader);
        String catalog = bean.Initial;
        if (i == 0) {
            viewholder.tv_item_pinedshowcartype_title.setVisibility(View.VISIBLE);
            viewholder.tv_item_pinedshowcartype_title.setText(catalog);
        } else {
            String lastCatalog = ((CarBrandBean) (list.get(i - 1))).Initial;
            if (catalog.equals(lastCatalog)) {
                viewholder.tv_item_pinedshowcartype_title.setVisibility(View.GONE);
            } else {
                viewholder.tv_item_pinedshowcartype_title.setVisibility(View.VISIBLE);
                viewholder.tv_item_pinedshowcartype_title.setText(catalog);
            }
        }
        viewholder.layoutbottom.setTag(bean);
        viewholder.layoutbottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarBrandBean bean = (CarBrandBean) view.getTag();
                Intent intent = new Intent(context, ChooseCarStyleActivity.class);
                intent.putExtra("bean", bean);
                ((Activity) context).startActivityForResult(intent, AppConfig.REQUEST_CODE_CHOOSECAR);
            }
        });
        return view;
    }


    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < list.size(); i++) {
            String l = null;
            Object obj = list.get(i);
            l = ((CarBrandBean) obj).Initial;
            char firstChar;
            if (l != null) {
                firstChar = l.toUpperCase().charAt(0);
                if (firstChar == section) {
                    return i;
                }
            }
        }

        return -1;
    }

    @Override
    public int getSectionForPosition(int i) {
        return 0;
    }

    static class Viewholder {
        TextView tv_item_pinendshowcartype_name;
        TextView tv_item_pinedshowcartype_title;
        ImageView carimg;
        LinearLayout layout_all;
        LinearLayout layoutbottom;
    }
}
