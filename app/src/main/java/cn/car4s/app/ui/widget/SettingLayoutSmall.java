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
public class SettingLayoutSmall extends LinearLayout {
    Context context;
    View rootView;
    SettingBean bean;
    ImageView left, right;
    TextView tv_title, tv_descption;

    public SettingLayoutSmall(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public SettingLayoutSmall(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        initView();
    }


    public void initView() {
        rootView = LayoutInflater.from(context).inflate(R.layout.widget_setttinglayout_small, null);
        left = (ImageView) rootView.findViewById(R.id.src_left);
        right = (ImageView) rootView.findViewById(R.id.src_right);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        tv_descption = (TextView) rootView.findViewById(R.id.tv_desc);
        this.addView(rootView);
    }

    public SettingBean getBean() {
        return bean;
    }

    public void setData(SettingBean bean) {
        this.bean = bean;
        left.setImageResource(bean.resourseLeft);
        if (bean.resouseRight == 0) {
            right.setVisibility(INVISIBLE);
        } else {
            right.setVisibility(VISIBLE);
            right.setImageResource(bean.resouseRight);
        }
        tv_title.setText(bean.title);
        tv_descption.setText(bean.desc);
    }
}
