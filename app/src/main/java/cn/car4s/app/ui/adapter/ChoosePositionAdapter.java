package cn.car4s.app.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.car4s.app.R;
import cn.car4s.app.bean.StationAreaBean;

import java.util.List;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/4/20.
 */
public class ChoosePositionAdapter extends RecyclerView.Adapter {

    private List<StationAreaBean> list;
    private Context context;
    private String selectedId;

    public ChoosePositionAdapter(Context context, List<StationAreaBean> list, String selectedId) {
        this.context = context;
        this.list = list;
        this.selectedId = selectedId;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }

    //view inflater
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_choose_pos, viewGroup, false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    //set data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        StationAreaBean bean = list.get(i);
        Viewholder holder = (Viewholder) viewHolder;
        holder.textView.setText("" + bean.StationAreaName);

        if (bean.StationAreaID.equals(selectedId)) {
            holder.imageView.setImageResource(R.mipmap.setting_goto);
        } else {
            holder.imageView.setImageResource(R.color.transparent);
        }
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
