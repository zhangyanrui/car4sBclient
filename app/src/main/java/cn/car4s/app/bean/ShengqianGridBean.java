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
public class ShengqianGridBean extends BaseBean {

    public int resourseLeft, resourseUnselected;
    public String title;
    public boolean isSelcted;


    public ShengqianGridBean(int resourseLeft, int resourseUnselected, String title, boolean isSelcted) {
        this.resourseLeft = resourseLeft;
        this.resourseUnselected = resourseUnselected;
        this.title = title;
        this.isSelcted = isSelcted;
    }

    static List<ShengqianGridBean> listData = null;

    public static List<ShengqianGridBean> createSettingData() {
        listData = null;
        if (listData == null) {
            listData = new ArrayList<ShengqianGridBean>();
            ShengqianGridBean bean = new ShengqianGridBean(R.mipmap.ic_shengqian_0_selected, R.mipmap.ic_shengqian_0_unselected, "汽车", true);
            ShengqianGridBean bean2 = new ShengqianGridBean(R.mipmap.ic_shengqian_1_selected, R.mipmap.ic_shengqian_1_unselected, "餐饮", false);
            ShengqianGridBean bean3 = new ShengqianGridBean(R.mipmap.ic_shengqian_2_selected, R.mipmap.ic_shengqian_2_unselected, "娱乐", false);
            ShengqianGridBean bean4 = new ShengqianGridBean(R.mipmap.ic_shengqian_3_selected, R.mipmap.ic_shengqian_3_unselected, "电子产品", false);
            ShengqianGridBean bean5 = new ShengqianGridBean(R.mipmap.ic_shengqian_4_selected, R.mipmap.ic_shengqian_4_unselected, "服装", false);
            ShengqianGridBean bean6 = new ShengqianGridBean(R.mipmap.ic_shengqian_5_selected, R.mipmap.ic_shengqian_5_unselected, "酒店", false);
            listData.add(bean);
            listData.add(bean2);
            listData.add(bean3);
            listData.add(bean4);
            listData.add(bean5);
            listData.add(bean6);
        }
        return listData;
    }

    public static List<ShengqianGridBean> createbduanmain() {
        listData = null;
        if (listData == null) {
            listData = new ArrayList<ShengqianGridBean>();
            ShengqianGridBean bean = new ShengqianGridBean(R.mipmap.ic_shengqian_0_selected, R.mipmap.ic_shengqian_0_unselected, "汽车", true);
            ShengqianGridBean bean2 = new ShengqianGridBean(R.mipmap.ic_shengqian_1_selected, R.mipmap.ic_shengqian_1_unselected, "餐饮", false);
            ShengqianGridBean bean3 = new ShengqianGridBean(R.mipmap.ic_shengqian_2_selected, R.mipmap.ic_shengqian_2_unselected, "娱乐", false);
            ShengqianGridBean bean4 = new ShengqianGridBean(R.mipmap.ic_shengqian_3_selected, R.mipmap.ic_shengqian_3_unselected, "电子产品", false);
            listData.add(bean);
            listData.add(bean2);
            listData.add(bean3);
            listData.add(bean4);
        }
        return listData;
    }
}
