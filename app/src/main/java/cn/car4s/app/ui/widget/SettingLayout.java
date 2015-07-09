package cn.car4s.app.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.car4s.app.R;
import cn.car4s.app.bean.SettingBean;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/4/22.
 */
public class SettingLayout extends LinearLayout {
    Context context;
    View rootView;
    SettingBean bean;
    ImageView left, right;
    TextView tv_title, tv_descption;

    public SettingLayout(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public SettingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }


    public void initView() {
        rootView = LayoutInflater.from(context).inflate(R.layout.widget_setttinglayout, null);
        left = (ImageView) rootView.findViewById(R.id.src_left);
        right = (ImageView) rootView.findViewById(R.id.src_right);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        tv_descption = (TextView) rootView.findViewById(R.id.tv_desc);
        this.addView(rootView);
    }

    public void setData(SettingBean bean) {
        if (bean.resourseLeft == 0) {
            left.setVisibility(GONE);
        }
        left.setImageResource(bean.resourseLeft);
        right.setImageResource(bean.resouseRight);
        tv_title.setText(bean.title);
        tv_descption.setText(bean.desc);
    }
}
