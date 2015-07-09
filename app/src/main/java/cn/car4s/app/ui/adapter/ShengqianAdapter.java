package cn.car4s.app.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.car4s.app.R;
import cn.car4s.app.bean.ShengqianGridBean;

import java.util.List;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/4/20.
 */
public class ShengqianAdapter extends RecyclerView.Adapter {

    private List<ShengqianGridBean> list;
    private Context context;


    public ShengqianAdapter(List<ShengqianGridBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    //view inflater
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_shegnqiang_grid, viewGroup, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    //set data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ShengqianGridBean bean = list.get(i);
        Viewholder holder = (Viewholder) viewHolder;
        holder.textView.setText("" + bean.title);

        if (bean.isSelcted)
            holder.imageView.setImageResource(bean.resourseLeft);
        else
            holder.imageView.setImageResource(bean.resourseUnselected);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class Viewholder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public Viewholder(View allView) {
            super(allView);
            imageView = (ImageView) allView.findViewById(R.id.src_right);
            textView = (TextView) allView.findViewById(R.id.tv_title);
        }
    }
}
