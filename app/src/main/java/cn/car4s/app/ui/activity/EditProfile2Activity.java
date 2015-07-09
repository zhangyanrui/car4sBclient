package cn.car4s.app.ui.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
import cn.car4s.app.api.HttpCallback;
import cn.car4s.app.bean.*;
import cn.car4s.app.ui.adapter.DialogTimeAdapter;
import cn.car4s.app.ui.widget.SettingLayoutSmall;
import cn.car4s.app.util.DialogUtil;
import cn.car4s.app.util.LogUtil;
import cn.car4s.app.util.PreferencesUtil;
import cn.car4s.app.util.ToastUtil;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.*;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/4/22.
 */
public class EditProfile2Activity extends BaseActivity implements IBase {
    @InjectView(R.id.btn_actionbar_back_img)
    ImageView mActionbarBack;
    @InjectView(R.id.tv_actionbar_title)
    TextView mActionbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile2);
        ButterKnife.inject(this);
        initData();
        initUI();
    }

    @Override
    public void initUI() {
        mActionbarBack.setVisibility(View.VISIBLE);
        mActionbarBack.setImageResource(R.mipmap.ic_loginactivity_back);
        mActionbarBack.setOnClickListener(onClickListener);
        mActionbarTitle.setText("地址");


        SettingLayoutSmall mLayoutKeyongjifen = (SettingLayoutSmall) findViewById(R.id.setting_keyongjifen);
        SettingLayoutSmall mLayoutdongjiejifen = (SettingLayoutSmall) findViewById(R.id.setting_dongjiejifen);
        SettingLayoutSmall mLayoutfeedback = (SettingLayoutSmall) findViewById(R.id.setting_feedback);
        SettingLayoutSmall mLayoutAboutus = (SettingLayoutSmall) findViewById(R.id.setting_aboutus);
        List<SettingBean> listData = SettingBean.createEdit2User(mCurrentPorvicnebean, mCurrentcitybean, mCurrentareabean, mUserbean);
        mLayoutKeyongjifen.setData(listData.get(0));
        mLayoutdongjiejifen.setData(listData.get(1));
        mLayoutfeedback.setData(listData.get(2));
        mLayoutAboutus.setData(listData.get(3));

        mLayoutKeyongjifen.setOnClickListener(onClickListener);
        mLayoutdongjiejifen.setOnClickListener(onClickListener);
        mLayoutfeedback.setOnClickListener(onClickListener);
        mLayoutAboutus.setOnClickListener(onClickListener);
    }

    List<Object> mList = new ArrayList<Object>();

    private void updateDate(final int type) {
        mList.clear();
        if (type == 0)
            mList.addAll(provinceBeanList);
        else if (type == 1) {
            mList.addAll(mCurrentPorvicnebean.CityList);
        } else {
            mList.addAll(mCurrentcitybean.AreaList);
        }
        View view = LayoutInflater.from(EditProfile2Activity.this).inflate(R.layout.time_picker, null);
        final Dialog dialog = DialogUtil.buildDialog(EditProfile2Activity.this, view, Gravity.CENTER, 0, true);
        ListView listView = (ListView) view.findViewById(R.id.listview);
        DialogTimeAdapter adapter = new DialogTimeAdapter(mList, EditProfile2Activity.this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog.dismiss();

                if (type == 0) {
                    ProvinceBean bean = (ProvinceBean) mList.get(i);
                    mUserbean.ProvinceID = bean.ProvinceID;
                    mUserbean.updateProfile(callbackUpdate, mUserbean);
                } else if (type == 1) {
                    CityBean bean = (CityBean) mList.get(i);
                    mUserbean.CityID = bean.CityID;
                    mUserbean.updateProfile(callbackUpdate, mUserbean);
                } else if (type == 2) {
                    CityBean.AreaBean bean = (CityBean.AreaBean) mList.get(i);
                    mUserbean.AreaID = bean.AreaID;
                    mUserbean.updateProfile(callbackUpdate, mUserbean);
                }

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

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.btn_actionbar_back_img:
                    setResult(Activity.RESULT_OK, null);
                    finish();
                    break;
                case R.id.setting_keyongjifen://
                    updateDate(0);
                    break;
                case R.id.setting_dongjiejifen://
                    updateDate(1);
                    break;
                case R.id.setting_feedback://
                    updateDate(2);
                    break;
                case R.id.setting_aboutus://
                    showNameDialog();
                    break;
            }
        }
    };
    int year, month, day;
    int hour, minute;
    StringBuilder sb;
    private DatePickerDialog.OnDateSetListener Datelistener = new DatePickerDialog.OnDateSetListener() {
        /**params：view：该事件关联的组件
         * params：myyear：当前选择的年
         * params：monthOfYear：当前选择的月
         * params：dayOfMonth：当前选择的日
         */
        @Override
        public void onDateSet(DatePicker view, int myyear, int monthOfYear, int dayOfMonth) {


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
            mUserbean.Birthday = sb.toString();
            mUserbean.updateProfile(callbackUpdate, mUserbean);
//            TimePickerDialog dialog = new TimePickerDialog(EditProfileActivity.this, timeSetListener, hour, minute, true);
//            dialog.show();
        }
    };

//    TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
//        @Override
//        public void onTimeSet(TimePicker timePicker, int hourofdayset, int minuteset) {
//            LogUtil.e("onTimeSet", hourofdayset + "---" + minuteset);
//        }
//    };

    public void showDataDialog() {
        //初始化Calendar日历对象
        Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
        Date mydate = new Date(); //获取当前日期Date对象
        mycalendar.setTime(mydate);////为Calendar对象设置时间为当前日期

        year = mycalendar.get(Calendar.YEAR); //获取Calendar对象中的年
        month = mycalendar.get(Calendar.MONTH);//获取Calendar对象中的月
        day = mycalendar.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天
        hour = mycalendar.get(Calendar.HOUR_OF_DAY);//获取这个月的第几天
        minute = mycalendar.get(Calendar.MINUTE);//获取这个月的第几天
        DatePickerDialog dialog = new DatePickerDialog(EditProfile2Activity.this, Datelistener, year, month, day);
        dialog.show();
    }

    HttpCallback callbackUpdate = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String result) {
            Log.e("--->", "" + result);
            mUserbean.refresh(callbackRefresh, mUserbean.Token);
        }
    };
    HttpCallback callbackRefresh = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(String result) {
            Log.e("--->", "" + result);
            PreferencesUtil.putPreferences(AppConfig.SP_KEY_USERINFO, result);
            initData();
//            initUI();
        }
    };

    public void showNameDialog() {
        View view = View.inflate(this, R.layout.dialog_edit, null);
        final Dialog dialog = DialogUtil.buildDialog(this, view, Gravity.CENTER, R.style.BottomDialogAnimation, true);
        final EditText dialog_edt = (EditText) view.findViewById(R.id.edt);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText("请填写您的小区门牌");
        View dialog_upload_sure = view.findViewById(R.id.dialog_upload_sure);
        dialog_upload_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                String name = dialog_edt.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    mUserbean.Address = name;
                    mUserbean.updateProfile(callbackUpdate, mUserbean);
                }
            }
        });
        View dialog_upload_cancel = view.findViewById(R.id.dialog_upload_cancel);
        dialog_upload_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void showSexDialog() {
        View view = View.inflate(this, R.layout.dialog_sex, null);
        final Dialog dialog = DialogUtil.buildDialog(this, view, Gravity.CENTER, R.style.BottomDialogAnimation, true);
        final RadioGroup rap_tab_radiogroup = (RadioGroup) view.findViewById(R.id.rap_tab_radiogroup);
        final RadioButton radioButtonnan = (RadioButton) view.findViewById(R.id.rdo_tab_1);
        final RadioButton radioButtonnv = (RadioButton) view.findViewById(R.id.rdo_tab_2);
        if ("1".equals(mUserbean.Sex)) {
            rap_tab_radiogroup.check(R.id.rdo_tab_2);
        } else {
            rap_tab_radiogroup.check(R.id.rdo_tab_1);
        }
        View dialog_upload_sure = view.findViewById(R.id.dialog_upload_sure);
        dialog_upload_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (radioButtonnan.isChecked()) {
                    mUserbean.Sex = "0";
                } else {
                    mUserbean.Sex = "1";
                }
                mUserbean.updateProfile(callbackUpdate, mUserbean);
            }
        });
        View dialog_upload_cancel = view.findViewById(R.id.dialog_upload_cancel);
        dialog_upload_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConfig.REQUEST_CODE_RESETPWD && resultCode == Activity.RESULT_OK) {

        }

        if (requestCode == AppConfig.REQUEST_CODE_REGISTER && resultCode == Activity.RESULT_OK) {

        }
    }

    @Override
    public void initData() {
        mUserbean = UserBean.getLocalUserinfo();
        getProCity();
        new StationAreaBean().getAllArea(callbackLoadingProvince);
    }

    HttpCallback callbackLoadingProvince = new HttpCallback() {
        @Override
        public void onFailure(Request request, IOException e) {
            getProCity();
        }

        @Override
        public void onResponse(String result) {
            Log.e("--->", "" + result);
            PreferencesUtil.putPreferences(AppConfig.SP_KEY_PROVICE, result);
        }
    };

    public void getProCity() {
        mProvinceBean = ProvinceBean.getlocalAreaData();
        if (mProvinceBean == null) {
            ToastUtil.showToastShort("获取省市信息失败");
            finish();
            return;
        }
        provinceBeanList = mProvinceBean.ProvinceList;
        for (int i = 0; i < provinceBeanList.size(); i++) {
            ProvinceBean temp = provinceBeanList.get(i);
            if (mUserbean.ProvinceID.equals(temp.ProvinceID)) {
                mCurrentPorvicnebean = temp;
                for (CityBean tempcity : temp.CityList) {
                    if (mUserbean.CityID.equals(tempcity.CityID)) {
                        mCurrentcitybean = tempcity;
                        for (CityBean.AreaBean temparea : tempcity.AreaList) {
                            if (mUserbean.AreaID.equals(temparea.AreaID)) {
                                mCurrentareabean = temparea;
                            }
                        }
                    }
                }
            }
        }
        initUI();
    }

    List<ProvinceBean> provinceBeanList;
    ProvinceBean mCurrentPorvicnebean;
    CityBean mCurrentcitybean;
    CityBean.AreaBean mCurrentareabean;


}
