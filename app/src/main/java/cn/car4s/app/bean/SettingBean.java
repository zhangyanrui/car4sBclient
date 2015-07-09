package cn.car4s.app.bean;

import cn.car4s.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/4/22.
 */
public class SettingBean extends BaseBean {

    public int resourseLeft, resouseRight;
    public String title, desc;

    public SettingBean(int resourseLeft, int resouseRight, String title, String desc) {
        this.resourseLeft = resourseLeft;
        this.resouseRight = resouseRight;
        this.title = title;
        this.desc = desc;
    }

    static List<SettingBean> listData = null;

    public static List<SettingBean> createSettingData(UserBean mUserbean) {
        listData = null;
        if (listData == null) {
            listData = new ArrayList<SettingBean>();
            SettingBean bean = new SettingBean(R.mipmap.setting_myjifen, 0, "可用积分", mUserbean.AvailablePoint);
            SettingBean bean2 = new SettingBean(R.mipmap.setting_myjifen, 0, "冻结积分", mUserbean.FreezingPoint);
            SettingBean bean3 = new SettingBean(R.mipmap.setting_feedback, R.mipmap.setting_goto, "意见反馈", "");
            SettingBean bean4 = new SettingBean(R.mipmap.setting_aboutus, R.mipmap.setting_goto, "关于我们", "");
            SettingBean bean5 = new SettingBean(R.mipmap.setting_myjifen, 0, "线下人数", mUserbean.OfflineCount);
            listData.add(bean);
            listData.add(bean2);
            listData.add(bean3);
            listData.add(bean4);
            listData.add(bean5);
        }
        return listData;
    }

    public static List<SettingBean> createSettingDataDetial() {
        listData = null;
        if (listData == null) {
            listData = new ArrayList<SettingBean>();
            SettingBean bean = new SettingBean(R.mipmap.ic_detial_shijian, R.mipmap.setting_goto, "快保网店", "");
            SettingBean bean2 = new SettingBean(R.mipmap.ic_detial_shijian, R.mipmap.setting_goto, "服务时间", "");
            SettingBean bean3 = new SettingBean(R.mipmap.ic_detial_jishi, R.mipmap.setting_goto, "技师", "");
            listData.add(bean);
            listData.add(bean2);
            listData.add(bean3);
        }
        return listData;
    }


    public static List<SettingBean> createEditUser(UserBean mUserbean) {
        listData = null;
        if (listData == null) {
            listData = new ArrayList<SettingBean>();
            SettingBean bean = new SettingBean(0, R.mipmap.setting_goto, "姓名", mUserbean.UserName);
            //0:男;1:女;-1:未填写
            String sex;
            if ("0".equals(mUserbean.Sex)) {
                sex = "男";
            } else if ("1".equals(mUserbean.Sex)) {
                sex = "女";
            } else {
                sex = "";
            }
            SettingBean bean2 = new SettingBean(0, R.mipmap.setting_goto, "性别", sex);
            SettingBean bean3 = new SettingBean(0, R.mipmap.setting_goto, "出生日期", mUserbean.Birthday);
            SettingBean bean4 = new SettingBean(0, R.mipmap.setting_goto, "地址", mUserbean.Address);
            listData.add(bean);
            listData.add(bean2);
            listData.add(bean3);
            listData.add(bean4);
        }
        return listData;
    }

    public static List<SettingBean> createEdit2User(ProvinceBean provinceBean, CityBean cityBean, CityBean.AreaBean areaBean, UserBean userBean) {
        listData = null;
        if (listData == null) {
            listData = new ArrayList<SettingBean>();
            SettingBean bean;
            SettingBean bean2;
            SettingBean bean3;
            SettingBean bean4;
            if (provinceBean == null) {
                bean = new SettingBean(0, R.mipmap.setting_goto, "省", "");
            } else {
                bean = new SettingBean(0, R.mipmap.setting_goto, "省", provinceBean.ProvinceName);
            }
            if (cityBean == null) {
                bean2 = new SettingBean(0, R.mipmap.setting_goto, "市", "");
            } else {
                bean2 = new SettingBean(0, R.mipmap.setting_goto, "市", cityBean.CityName);

            }
            if (areaBean == null) {
                bean3 = new SettingBean(0, R.mipmap.setting_goto, "区", "");
            } else {
                bean3 = new SettingBean(0, R.mipmap.setting_goto, "区", areaBean.AreaName);
            }
            if (userBean == null) {
                bean4 = new SettingBean(0, R.mipmap.setting_goto, "门牌号", "");
            } else {
                bean4 = new SettingBean(0, R.mipmap.setting_goto, "门牌号", userBean.Address);
            }
            listData.add(bean);
            listData.add(bean2);
            listData.add(bean3);
            listData.add(bean4);

        }
        return listData;
    }

}
