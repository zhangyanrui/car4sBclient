package cn.car4s.app.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.car4s.app.AppContext;
import cn.car4s.app.R;
import cn.car4s.app.bean.ProductDetialBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/4/20.
 */
public class ProductDetialAdapter extends RecyclerView.Adapter {

    private List<ProductDetialBean> list;
    private Context context;


    public ProductDetialAdapter(List<ProductDetialBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    //view inflater
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_product_detial, viewGroup, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    //set data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ProductDetialBean bean = list.get(i);
        Viewholder holder = (Viewholder) viewHolder;
        holder.textView1.setText("名称:  " + bean.PartsName);
        holder.textView2.setText("数量:  " + bean.Count + " " + bean.PartsUnit);
        holder.textView3.setText("备注:  " + bean.Remark);
        ImageLoader.getInstance().displayImage(bean.ProductPicPath, holder.imageView, AppContext.display_imageloader);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class Viewholder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView1;
        public TextView textView2;
        public TextView textView3;

        public Viewholder(View allView) {
            super(allView);
            imageView = (ImageView) allView.findViewById(R.id.src_right);
            textView1 = (TextView) allView.findViewById(R.id.tv_1);
            textView2 = (TextView) allView.findViewById(R.id.tv_2);
            textView3 = (TextView) allView.findViewById(R.id.tv_3);
        }
    }
}
