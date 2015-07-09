package cn.car4s.app.alipay;

import android.app.Activity;
import android.os.Message;
import android.text.TextUtils;
import cn.car4s.app.AppConfig;
import cn.car4s.app.api.ApiService;
import com.alipay.sdk.app.PayTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/4/22.
 */
public class AlipayUtil {
    //商户PID
    public static final String PARTNER = "2088911886285057";
    //商户收款账号
    public static final String SELLER = "ntbsntbs@163.com";
    //商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAN+n1UIBLS8UI27W/yY3qZaM1Fea8c8LySJkgvboQwL2PlKsaHckR1UBRCRrB5meZZXyFuSJ6ZUBwUKpa0Nk1UFAxXPbJYRuofQIYWR5uPnIIAIvwI6zXw/I68Dy5Vf5nZ10IH2SlMjPazrtCtG1fbEbSxW9LwpBzjfqvBfTr0iLAgMBAAECgYB9N6X6MlZtyUSdRdnMN9XG6EnqUmGXne2aR9PbEOaTvfHer5A9RB57xR21lgbHRxtw+/09LsmMF1NwUxIuxUFvHzAX6AR+I9iEsuKMXyK5oa0YHrYnjqsJb0BngRRgMvVcHa3Q/3P2xprmPhADEr+p9b8g4V9z3ufLmLGA9Fb+AQJBAPMeXpyQjbR9Vc/1ihp0ql8Zq1NbmcrO+HQVz+i6HbonaTqw6eXcoNHrA5H1F3ItlW1y6UYEmRbkf0GFpTIRBUsCQQDrgXgmTJVTbE9UI6Hf5qxsvtOVRNi+WQ5NF6VJfjBmqV2lgpBC1GlVNDeEO8IdHZ4cds/ybt0gj7p/348HXwHBAkEAqB0KA22Z74LzwdfiZaTi4twXMo8W67zu5SboG+AHCmCSTXlkZASKWF4Qm+9FBTksKQDa+gXX7pKWxLIrp1FJvQJAL5llbWhBLjFP/1OMXjLDWt9e6GaX4DEjwNUspSQKKFXHBbLT8Y35FC2PovxpCXV5BYvGD9v0eaZTLZFD2VuMgQJBAIXbsfUUsXeIobuO9lvCg9Z56gpOmtDQ5Ed5JkCw8yQ3OYwnLxrnccVsRDGHZRFg2S94YSHTrXMXMgGzRPca2os=";
    //支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";


    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public static void pay(final Activity context, final android.os.Handler mHandler, AlipayBean bean) {
        // 订单
        String orderInfo = getOrderInfo(bean.productname, TextUtils.isEmpty(bean.productdesc) ? "-" : bean.productdesc, bean.price, bean.tradeNo, bean.pay_time);

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(context);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * create the order info. 创建订单信息
     */
    public static String getOrderInfo(String subject, String body, String price, String tradeno, String paytime) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo(tradeno) + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
//        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
//                + "\"";
        orderInfo += "&notify_url=" + "\"" + AppConfig.APP_SERVER + ApiService.INTERFACE_ZHIFUBAO
                + "\"";
        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
//        orderInfo += "&it_b_pay=\"30m\"";
        orderInfo += "&it_b_pay=\"" + paytime + "\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }


    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    public static String getOutTradeNo(String tradeno) {
//        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
//                Locale.getDefault());
//        Date date = new Date();
//        String key = format.format(date);
//
//        Random r = new Random();
//        key = key + r.nextInt();
//        key = key.substring(0, 15);
        return tradeno;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public static String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public static String getSignType() {
        return "sign_type=\"RSA\"";
    }
}
