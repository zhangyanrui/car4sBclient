package cn.car4s.app.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import cn.car4s.app.R;
import cn.car4s.app.bean.Paimingbean;
import cn.car4s.app.util.DeviceUtil;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/4/22.
 */
public class PaimingLayout extends LinearLayout {
    Context context;
    View rootView;
    Paimingbean bean;
    ImageView left, right;
    TextView tv_title, tv_descption, tv_1, tv_2, tv_3, tv_4;
    RatingBar bar;

    public PaimingLayout(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public PaimingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }


    public void initView() {
        rootView = LayoutInflater.from(context).inflate(R.layout.item_paiming, null);
        tv_1 = (TextView) rootView.findViewById(R.id.tv_1);
        tv_2 = (TextView) rootView.findViewById(R.id.tv_2);
        tv_3 = (TextView) rootView.findViewById(R.id.tv_3);
        tv_4 = (TextView) rootView.findViewById(R.id.tv_4);
        tv_1.getLayoutParams().width = (int) (DeviceUtil.getWidth() / 10 * 2);
        tv_2.getLayoutParams().width = (int) (DeviceUtil.getWidth() / 10 * 3);
        tv_3.getLayoutParams().width = (int) (DeviceUtil.getWidth() / 10 * 3);
        tv_4.getLayoutParams().width = (int) (DeviceUtil.getWidth() / 10 * 2);
        this.addView(rootView);
    }

    public void setData(Paimingbean bean) {
        this.bean = bean;
        if (bean.type == 0) {
            tv_1.setText("第" + (bean.position + 1) + "名");
            tv_2.setText(bean.UserName);
            tv_3.setText(bean.PhoneNumber);
            tv_4.setText(bean.Point + "分");
        } else if (bean.type == 1) {
            tv_1.setText("第" + (bean.position + 1) + "名");
            tv_2.setText(bean.UserName);
            tv_3.setText(bean.PhoneNumber);
            tv_4.setText(bean.OfflineCount + "人");
        } else {
            tv_1.setText("排名");
            tv_2.setText("姓名");
            tv_3.setText("手机号");
            tv_4.setText("积分");
        }
    }

    public Paimingbean getBean() {
        return bean;
    }
}
