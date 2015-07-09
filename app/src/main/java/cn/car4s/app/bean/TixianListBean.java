package cn.car4s.app.bean;

import java.util.List;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/4/22.
 */
public class TixianListBean extends BaseBean {

    //    {
//        "Code": "0",
//            "Message": "成功",
//            "Total": "0",
//            "AvailablePoint": "300.00",
//            "FreezingPoint": "0.00",
//            "GuaranteedPoint": "1000.00",
//            "LowestLimit": "100.00",
//            "AvailableWithdrawalPoint": "0.00",
//            "Data": []
//    }
    public String AvailablePoint;
    public String FreezingPoint;
    public String GuaranteedPoint;
    public String LowestLimit;
    public String AvailableWithdrawalPoint;
    public List<TixianBean> Data;
}
