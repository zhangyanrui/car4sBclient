package cn.car4s.app.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.car4s.app.R;
import cn.car4s.app.bean.StationBean;

import java.util.List;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/4/20.
 */
public class ChooseStationAdapter extends RecyclerView.Adapter {

    private List<StationBean> list;
    private Context context;
    private String selectedId;

    public ChooseStationAdapter(Context context, List<StationBean> list) {
        this.context = context;
        this.list = list;
    }

    //view inflater
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_choose_station, viewGroup, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    //set data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        StationBean bean = list.get(i);
        Viewholder holder = (Viewholder) viewHolder;
        holder.textView.setText("" + bean.StationName);
        if (!"-1".equals(bean.Mileage) && !"0.00".equals(bean.Mileage)) {
            holder.textViewdesc.setText("" + bean.Mileage);
        } else {
            holder.textViewdesc.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class Viewholder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public TextView textViewdesc;

        public Viewholder(View allView) {
            super(allView);
            imageView = (ImageView) allView.findViewById(R.id.src_right);
            textView = (TextView) allView.findViewById(R.id.tv_title);
            textViewdesc = (TextView) allView.findViewById(R.id.tv_desc);
        }
    }
}
