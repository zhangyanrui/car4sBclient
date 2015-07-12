package cn.car4s.app.ui.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.car4s.app.AppConfig;
import cn.car4s.app.R;
import cn.car4s.app.alipay.AlipayBean;
import cn.car4s.app.alipay.AlipayUtil;
import cn.car4s.app.alipay.PayResult;
import cn.car4s.app.api.HttpCallback;
import cn.car4s.app.bean.*;
import cn.car4s.app.ui.adapter.DialogTimeAdapter;
import cn.car4s.app.ui.adapter.ProductDetialAdapter;
import cn.car4s.app.ui.widget.SettingLayoutSmall;
import cn.car4s.app.util.DeviceUtil;
import cn.car4s.app.util.DialogUtil;
import cn.car4s.app.util.LogUtil;
import cn.car4s.app.util.ToastUtil;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/4/22.
 */
public class ProductDetailActivity extends BaseActivity implements IBase {
    @InjectView(R.id.btn_actionbar_back_img)
    ImageView mActionbarBack;
    @InjectView(R.id.tv_actionbar_title)
    TextView mActionbarTitle;
    @InjectView(R.id.layout_actionbar_all)
    RelativeLayout mActionbarBackLayoutall;
    @InjectView(R.id.wangdian)
    SettingLayoutSmall mlayoutwangdian;
    @InjectView(R.id.shijian)
    SettingLayoutSmall mlayoutShijian;
    @InjectView(R.id.jishi)
    SettingLayoutSmall mlayoutjishi;
    @InjectView(R.id.recyclerview)
    RecyclerView recyclerView;


    @InjectView(R.id.chanpin)
    TextView mChanpin;
    @InjectView(R.id.yuanjia)
    TextView mYuanjia;
    @InjectView(R.id.xianjia)
    TextView mXianjia;
    @InjectView(R.id.allprice)
    TextView mAllprice;
    @InjectView(R.id.commit)
    Button mcommit;

    @InjectView(R.id.layout_zhekou)
    LinearLayout layout_zhekou;
    @InjectView(R.id.checkbox)
    CheckBox checkbox;
    @InjectView(R.id.tv_jifendikoudesc)
    TextView mZhekouDesc;


    ProductBean mproductBean = null;

    List<ProductDetialBean> list = new ArrayList<ProductDetialBean>();
    ProductDetialAdapter adapter;

    int mSerisId;
    int mType;//0 detial;1,dingdan 2,edit 3查看完成的订单
    String mOrderIdIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetial);
        ButterKnife.inject(this);
        mproductBean = (ProductBean) getIntent().getSerializableExtra("bean");
        mSerisId = getIntent().getIntExtra("serisid", 1);
        mType = getIntent().getIntExtra("type", 0);
        mOrderIdIntent = getIntent().getStringExtra("orderid");
        initUI();
        initData();
    }

    @Override
    public void initUI() {
        mActionbarBack.setVisibility(View.VISIBLE);
        mActionbarBack.setImageResource(R.mipmap.ic_loginactivity_back);
        mActionbarBack.setOnClickListener(onClickListener);
        mActionbarTitle.setText("商品详情");
        //
        mlayoutwangdian.setData(SettingBean.createSettingDataDetial().get(0));
        mlayoutShijian.setData(SettingBean.createSettingDataDetial().get(1));
        mlayoutjishi.setData(SettingBean.createSettingDataDetial().get(2));
        mlayoutwangdian.setOnClickListener(onClickListener);
        mlayoutShijian.setOnClickListener(onClickListener);
        mlayoutjishi.setOnClickListener(onClickListener);
        mcommit.setOnClickListener(onClickListener);
        //
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ProductDetialAdapter(list, this);
        recyclerView.setAdapter(adapter);

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Float fprice = Float.parseFloat(mproductBean.DiscountPrice);
//                Float fjifen = Float.parseFloat(mproductBean.AvailablePoint);
                if (b) {
                    BigDecimal b1 = new BigDecimal(Float.toString(fprice));
                    BigDecimal b2 = new BigDecimal(Float.toString(jifendiqian));
                    Float less = b1.subtract(b2).floatValue();
                    if (less < 0) {
                        less = 0f;
                    }
                    mAllprice.setText("实际应付款: " + less);
                } else {
                    mAllprice.setText("实际应付款: " + mproductBean.DiscountPrice);
                }
            }
        });
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.btn_actionbar_back_img:
                    finish();
                    break;
                case R.id.wangdian://
                    mIntent = new Intent(ProductDetailActivity.this, ChooseStationActivity.class);
                    startActivityForResult(mIntent, AppConfig.REQUEST_CODE_CHOOSE_STATION);
                    break;
                case R.id.shijian:
                    if (TextUtils.isEmpty(mlayoutwangdian.getBean().desc)) {
                        ToastUtil.showToastShort("请先选择网店");
                        return;
                    }
                    isFirst = true;
                    //初始化Calendar日历对象
                    Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
                    Date mydate = new Date(); //获取当前日期Date对象
                    mycalendar.setTime(mydate);////为Calendar对象设置时间为当前日期

                    year = mycalendar.get(Calendar.YEAR); //获取Calendar对象中的年
                    month = mycalendar.get(Calendar.MONTH);//获取Calendar对象中的月
                    day = mycalendar.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天
                    DatePickerDialog dialog = new DatePickerDialog(ProductDetailActivity.this, Datelistener, year, month, day);
                    dialog.getDatePicker().setMinDate(mydate.getTime());
                    dialog.show();
                    break;

                case R.id.jishi://
                    if (TextUtils.isEmpty(mlayoutwangdian.getBean().desc)) {
                        ToastUtil.showToastShort("请先选择网店");
                        return;
                    } else if (TextUtils.isEmpty(mlayoutShijian.getBean().desc)) {
                        ToastUtil.showToastShort("请先选择时间");
                        return;
                    }
                    mIntent = new Intent(ProductDetailActivity.this, ChooseCarStyleActivity.class);
                    mIntent.putExtra("type", 1);
                    mIntent.putExtra("stateId", stationBean.StationId);
                    mIntent.putExtra("dataId", sb.toString());
                    mIntent.putExtra("timeId", mSelectedTimeId);
                    startActivityForResult(mIntent, 200);
                    break;
                case R.id.commit://
                    if (mType == 0) {//get order
                        if (TextUtils.isEmpty(mlayoutwangdian.getBean().desc)) {
                            ToastUtil.showToastShort("请先选择网店");
                            return;
                        } else if (TextUtils.isEmpty(mlayoutShijian.getBean().desc)) {
                            ToastUtil.showToastShort("请先选择时间");
                            return;
                        } else if (TextUtils.isEmpty(mlayoutjishi.getBean().desc)) {
                            ToastUtil.showToastShort("请先选择技师");
                            return;
                        } else if (UserBean.checkUserLoginStatus()) {
                            OrderBean commitBean = new OrderBean(stationBean.StationId, "" + mSerisId, sb.toString(), mSelectedTimeId, jishiBean.UserID, "" + mproductBean.ProductID, checkbox.isChecked(), "" + jifen);
                            commitBean.addorder(callbackAddorder, commitBean);
                        } else {
                            UserBean.toLogin(ProductDetailActivity.this, AppConfig.REQUEST_CODE_LOGIN);
                        }
                    } else if (mType == 1) {//pay
                        if (UserBean.checkUserLoginStatus()) {
                            AlipayBean alipayBean = new AlipayBean(orderBean.ProductName, orderBean.Description, orderBean.ReceiveMoney, orderBean.OrderCode, orderBean.LastPaymentTime);
                            AlipayUtil.pay(ProductDetailActivity.this, mHandler, alipayBean);
                        } else {
                            UserBean.toLogin(ProductDetailActivity.this, AppConfig.REQUEST_CODE_LOGIN);
                        }
                    } else if (mType == 2) {//xiugai
                        if (UserBean.checkUserLoginStatus()) {
                            OrderBean tempbean = new OrderBean();
                            tempbean.OrderID = orderBean.OrderID;
                            tempbean.ServiceData = sb.toString();
                            tempbean.ServiceTimeID = mSelectedTimeId;
                            tempbean.TechnicianID = jishiBean.UserID;
                            tempbean.updateOrder(callbackUpdateOrder, tempbean);
                        } else {
                            UserBean.toLogin(ProductDetailActivity.this, AppConfig.REQUEST_CODE_LOGIN);
                        }
                    }
                    break;

            }
        }
    };
    int year, month, day;
    StringBuilder sb;
    private boolean isFirst;
    private DatePickerDialog.OnDateSetListener Datelistener = new DatePickerDialog.OnDateSetListener() {
        /**params：view：该事件关联的组件
         * params：myyear：当前选择的年
         * params：monthOfYear：当前选择的月
         * params：dayOfMonth：当前选择的日
         */
        @Override
        public void onDateSet(DatePicker view, int myyear, int monthOfYear, int dayOfMonth) {
            LogUtil.e("------", "onDateSet");
            if (!isFirst) {
                return;
            }
            isFirst = false;
            //修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
            year = myyear;
            month = monthOfYear + 1;
            day = dayOfMonth;
            sb = new StringBuilder();
            sb.append(year);
            sb.append("-");
            if (month < 10) {
                sb.append("0");
                sb.append(month);
            } else
                sb.append(month);
            sb.append("-");
            if (day < 10) {
                sb.append("0");
                sb.append(day);
            } else
                sb.append(day);
            LogUtil.e("selectedData", sb.toString());

            //更新日期
            updateDate();

        }

        //当DatePickerDialog关闭时，更新日期显示
        private void updateDate() {
            View view = LayoutInflater.from(ProductDetailActivity.this).inflate(R.layout.time_picker, null);
            final Dialog dialog = DialogUtil.buildDialog(ProductDetailActivity.this, view, Gravity.CENTER, 0, true);
            ListView listView = (ListView) view.findViewById(R.id.listview);

            List<Object> temp = new ArrayList<Object>();
            temp.addAll(stationBean.ServiceTimeList);
            DialogTimeAdapter adapter = new DialogTimeAdapter(temp, ProductDetailActivity.this);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    dialog.dismiss();
                    StationBean.TimeChoose timeChoose = stationBean.ServiceTimeList.get(i);
                    mSelectedTimeId = timeChoose.TimeID;
                    mlayoutShijian.getBean().desc = sb.toString() + " " + timeChoose.TimeName;
                    mlayoutShijian.setData(mlayoutShijian.getBean());
                }
            });
            TextView cancel = (TextView) view.findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            TextView sure = (TextView) view.findViewById(R.id.sure);
            sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    };
    private String mSelectedTimeId;

    HttpCallback callbackGetOrderdetial = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String result) {
            orderBean = new Gson().fromJson(result, OrderBean.class);
            changeToOrder();
        }
    };
    HttpCallback callbackStation = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String result) {
            Log.e("--->", "this is" + result);
            stationBean = StationBean.getData(result).get(0);
        }
    };
    HttpCallback callback = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String result) {
            ProductBean resultbean = new Gson().fromJson(result, ProductBean.class);
            updateUI(resultbean);
        }
    };
    HttpCallback callbackUpdateOrder = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String result) {
            ToastUtil.showToastShort("修改完成");
            finish();
        }
    };
    HttpCallback callbackAddorder = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String result) {
            LogUtil.e("--->", "" + result);
            orderBean = new Gson().fromJson(result, OrderBean.class);
            if ("1".equals(orderBean.OrderStatus)) {
                paySuccess();
            } else if ("2".equals(orderBean.OrderStatus)) {
                mType = 1;
                changeToOrder();
            }
        }
    };
    OrderBean orderBean;

    public void changeToOrder() {
        mChanpin.setText("产品:" + orderBean.ProductName);
        mYuanjia.setText("原价:" + orderBean.SalesPrice);
        mXianjia.setText("现价:" + orderBean.DiscountPrice);
        mAllprice.setText("实际应付款: " + orderBean.ReceiveMoney);
        if (TextUtils.isEmpty(orderBean.AvailablePoint) || "0".equals(orderBean.AvailablePoint)) {
            layout_zhekou.setVisibility(View.GONE);
        } else {
            layout_zhekou.setVisibility(View.VISIBLE);
            String label = "使用卡内" + orderBean.PointDedCount + "积分抵扣" + orderBean.PointDedMoney + "元(1积分="
                    + rate + "元,可用积分" + orderBean.AvailablePoint + ",冻结积分" + orderBean.FreezingPoint + ")";
            mZhekouDesc.setText(label);
        }
        list.clear();
        list.addAll(orderBean.ProductDetailList);
        recyclerView.getLayoutParams().height = DeviceUtil.getPxFromDip(80) * list.size();
        adapter.notifyDataSetChanged();

        switch (mType) {
            case 0:
                break;
            case 1:
                mActionbarTitle.setText("订单详情");
                mcommit.setText("立即支付");
                mlayoutwangdian.setOnClickListener(null);
                mlayoutShijian.setOnClickListener(null);
                mlayoutjishi.setOnClickListener(null);
                mlayoutwangdian.getBean().resouseRight = 0;
                mlayoutwangdian.getBean().desc = orderBean.StationName;
                mlayoutwangdian.setData(mlayoutwangdian.getBean());
                mlayoutShijian.getBean().resouseRight = 0;
                mlayoutShijian.getBean().desc = orderBean.ServiceData + " " + orderBean.ServiceTime;
                mlayoutShijian.setData(mlayoutShijian.getBean());
                mlayoutjishi.getBean().resouseRight = 0;
                mlayoutjishi.getBean().desc = orderBean.TechnicianName;
                mlayoutjishi.setData(mlayoutjishi.getBean());
                checkbox.setClickable(false);
                break;
            case 2:
                mActionbarTitle.setText("编辑订单");
                mcommit.setText("修改");
                stationBean = new StationBean();
                stationBean.StationId = orderBean.StationID;
                new StationBean().getStation(callbackStation);
                mlayoutwangdian.getBean().desc = orderBean.StationName;
                mlayoutwangdian.setData(mlayoutwangdian.getBean());
                mlayoutShijian.getBean().desc = orderBean.ServiceData + " " + orderBean.ServiceTime;
                sb = new StringBuilder(orderBean.ServiceData);
                mSelectedTimeId = orderBean.ServiceTimeID;
                mlayoutShijian.setData(mlayoutShijian.getBean());
                mlayoutjishi.getBean().desc = orderBean.TechnicianName;
                jishiBean = new JishiBean();
                jishiBean.UserID = orderBean.TechnicianID;
                mlayoutjishi.setData(mlayoutjishi.getBean());
                checkbox.setClickable(false);
                break;
            case 3:
                mActionbarTitle.setText("详情");
                mcommit.setVisibility(View.GONE);
                mlayoutwangdian.setOnClickListener(null);
                mlayoutShijian.setOnClickListener(null);
                mlayoutjishi.setOnClickListener(null);
                mlayoutwangdian.getBean().resouseRight = 0;
                mlayoutwangdian.getBean().desc = orderBean.StationName;
                mlayoutwangdian.setData(mlayoutwangdian.getBean());
                mlayoutShijian.getBean().resouseRight = 0;
                mlayoutShijian.getBean().desc = orderBean.ServiceData + " " + orderBean.ServiceTime;
                mlayoutShijian.setData(mlayoutShijian.getBean());
                mlayoutjishi.getBean().resouseRight = 0;
                mlayoutjishi.getBean().desc = orderBean.TechnicianName;
                mlayoutjishi.setData(mlayoutjishi.getBean());
                checkbox.setClickable(false);
                layout_zhekou.setVisibility(View.GONE);
                break;
        }
    }

    private float rate = 1f;//jifen/rmb=1;
    float jifen;
    float jifendiqian;

    public void updateUI(ProductBean bean) {
        mproductBean = bean;
        mChanpin.setText("产品:" + bean.ProductName);
        mYuanjia.setText("原价:" + bean.SalesPrice);
        mXianjia.setText("现价:" + bean.DiscountPrice);
        mAllprice.setText("实际应付款: " + bean.DiscountPrice);
        if (TextUtils.isEmpty(bean.AvailablePoint) || "0.00".equals(bean.AvailablePoint)) {
            layout_zhekou.setVisibility(View.GONE);
        } else {
            layout_zhekou.setVisibility(View.VISIBLE);
            rate = Float.parseFloat(bean.PointRatio);
            DecimalFormat decimalFormat = new DecimalFormat(".00");
            jifen = Float.parseFloat(bean.AvailablePoint);
            jifendiqian = jifen * rate;
            jifendiqian = Float.parseFloat(decimalFormat.format(jifendiqian));
            String label;
            if (jifendiqian < Float.parseFloat(bean.DiscountPrice)) {
                label = "使用卡内" + jifen + "积分抵扣" + jifendiqian + "元(1积分="
                        + rate + "元,可用积分" + bean.AvailablePoint + ",冻结积分" + bean.FreezingPoint + ")";
            } else {
                jifen = Float.parseFloat(bean.DiscountPrice) / rate;
                jifendiqian = Float.parseFloat(bean.DiscountPrice);
                label = "使用卡内" + jifen + "积分抵扣" + decimalFormat.format(jifendiqian) + "元(1积分="
                        + rate + "元,可用积分" + bean.AvailablePoint + ",冻结积分" + bean.FreezingPoint + ")";
            }
            mZhekouDesc.setText(label);
        }
        list.clear();
        list.addAll(bean.ProductDetailList);
        recyclerView.getLayoutParams().height = DeviceUtil.getPxFromDip(80) * list.size();
        adapter.notifyDataSetChanged();
    }

    StationBean stationBean;
    JishiBean jishiBean;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConfig.REQUEST_CODE_CHOOSE_STATION && resultCode == Activity.RESULT_OK) {
            stationBean = (StationBean) data.getSerializableExtra("bean");
            mlayoutwangdian.getBean().desc = stationBean.StationName;
            mlayoutwangdian.setData(mlayoutwangdian.getBean());
        }

        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            jishiBean = (JishiBean) data.getSerializableExtra("bean");
            mlayoutjishi.getBean().desc = jishiBean.UserName;
            mlayoutjishi.setData(mlayoutjishi.getBean());
        }
    }

    @Override
    public void initData() {
        if (mType == 0)
            mproductBean.getProductDetial(callback, mproductBean);
        else {
            new OrderBean().getOrderDetialClientB(callbackGetOrderdetial, mOrderIdIntent);
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        paySuccess();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(ProductDetailActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(ProductDetailActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };


    public void paySuccess() {
        Toast.makeText(ProductDetailActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_OK);
        finish();
    }
}
