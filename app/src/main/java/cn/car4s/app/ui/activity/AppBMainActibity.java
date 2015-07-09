package cn.car4s.app.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.car4s.app.R;
import cn.car4s.app.bean.ShengqianGridBean;
import cn.car4s.app.ui.adapter.ShengqianAdapter;
import cn.car4s.app.ui.widget.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/4/22.
 */
public class AppBMainActibity extends BaseActivity implements IBase {


    @InjectView(R.id.btn_actionbar_back_img)
    ImageView mActionbarBack;
    @InjectView(R.id.tv_actionbar_title)
    TextView mActionbarTitle;
    @InjectView(R.id.recyclerview)
    RecyclerView recyclerView;

    List<ShengqianGridBean> list = new ArrayList<ShengqianGridBean>();
    ShengqianAdapter adapter;
    RecyclerItemClickListener itemlistener;

    @InjectView(R.id.tv_wangdian)
    TextView mtvWangdian;
    @InjectView(R.id.tv_yonghu)
    TextView mtvYonghu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmain);
        ButterKnife.inject(this);
        initUI();
        initData();
    }

    @Override
    public void initUI() {
        mActionbarBack.setVisibility(View.VISIBLE);
        mActionbarBack.setImageResource(R.mipmap.ic_loginactivity_back);
        mActionbarBack.setOnClickListener(onClickListener);
        mActionbarTitle.setText("指尖创业");

        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ShengqianAdapter(list, this);
        recyclerView.setAdapter(adapter);
        itemlistener = new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                ShengqianGridBean bean = list.get(position);
//                if (bean.isSelcted) {
//                    mIntent = new Intent(AppBMainActibity.this, CarBaoyangActivity.class);
//                    mIntent.putExtra("type", 2);
//                    startActivityForResult(mIntent, AppConfig.REQUEST_CODE_PAY);
//                } else {
//                    ToastUtil.showToastShort("暂未开放");
//                }
            }
        });
        recyclerView.addOnItemTouchListener(itemlistener);
        list.addAll(ShengqianGridBean.createbduanmain());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void initData() {

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.btn_actionbar_back_img:
                    break;
            }
        }
    };
}
