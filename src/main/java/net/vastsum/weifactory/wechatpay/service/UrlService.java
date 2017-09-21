package net.vastsum.weifactory.wechatpay.service;

import net.vastsum.weifactory.wechatpay.config.WechatConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class UrlService {

    @Autowired
    private WechatConfig wechatConfig;

    //获取微信开发中用于得到code的地址
    public String getCode(){
        String returnUrl = "http://"+wechatConfig.getDomain()+"/wechat/getOpenId";
        StringBuilder sb = new StringBuilder();
        sb.append("https://open.weixin.qq.com/connect/oauth2/authorize?");
        sb.append("appid=").append(wechatConfig.getAppid());
        try {
            sb.append("&redirect_uri=").append(URLEncoder.encode(returnUrl,"UTF-8"));
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        sb.append("&response_type=code&scope=snsapi_base");
        sb.append("#wechat_redirect");
        return sb.toString();
    }

}
