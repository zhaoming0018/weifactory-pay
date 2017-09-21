package net.vastsum.weifactory.wechatpay.service;

import VO.PayVO;
import net.vastsum.weifactory.wechatpay.config.WechatConfig;
import net.vastsum.weifactory.wechatpay.entity.Orders;
import net.vastsum.weifactory.wechatpay.utils.WxPayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;

@Service
public class PayService {
    @Autowired
    private WechatConfig wechatConfig;

    public PayVO getPayInfo(Orders orders){
        PayVO payVO = new PayVO();
        payVO.setAppId(wechatConfig.getAppid());
        payVO.setNonceStr(WxPayUtils.getNonceStr());
        payVO.setPackages("prepay_id="+ orders.getPrepayId());
        payVO.setTimeStamp(String.valueOf(new Date().getTime()));
        payVO.setSignType("MD5");
        HashMap<String,String> hashMap = payVO.toHashMap();
        try {
            payVO.setPaySign(WxPayUtils.generateSign(hashMap,payVO.getSignType(),wechatConfig.getApikey()));
            return payVO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
