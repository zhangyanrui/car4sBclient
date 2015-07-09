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
import cn.car4s.app.bean.JishiPingjiaBean;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/4/22.
 */
public class RatingLayout extends LinearLayout {
    Context context;
    View rootView;
    JishiPingjiaBean bean;
    ImageView left, right;
    TextView tv_title, tv_descption;
    RatingBar bar;

    public RatingLayout(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public RatingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }


    public void initView() {
        rootView = LayoutInflater.from(context).inflate(R.layout.rating_jish, null);
//        left = (ImageView) rootView.findViewById(R.id.src_left);
//        right = (ImageView) rootView.findViewById(R.id.src_right);
        tv_title = (TextView) rootView.findViewById(R.id.title);
        bar = (RatingBar) rootView.findViewById(R.id.ratingbar);
        bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                bean.score = (int) v;
            }
        });
        this.addView(rootView);
    }

    public void setData(JishiPingjiaBean bean) {
        this.bean = bean;
        tv_title.setText(bean.EvaluationType);
    }

    public JishiPingjiaBean getBean() {
        return bean;
    }
}
