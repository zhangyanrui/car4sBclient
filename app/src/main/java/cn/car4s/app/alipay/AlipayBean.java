package cn.car4s.app.alipay;

import cn.car4s.app.bean.BaseBean;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/4/22.
 */
public class AlipayBean extends BaseBean {
    public String productname;
    public String productdesc;
    public String price;
    public String tradeNo;
    public String pay_time;

    public AlipayBean(String productname, String productdesc, String price, String tradeNo,String pay_time) {
        this.productname = productname;
        this.productdesc = productdesc;
        this.price = price;
        this.tradeNo = tradeNo;
        this.pay_time = pay_time;
    }
}
