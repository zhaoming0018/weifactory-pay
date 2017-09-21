package net.vastsum.weifactory.wechatpay.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.vastsum.weifactory.wechatpay.config.WechatConfig;
import net.vastsum.weifactory.wechatpay.utils.UrlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class AuthService {
    @Autowired
    private WechatConfig wechatConfig;

    public String getOpenid(String code){
        //TODO:为了节省access_token，可以从session中获取
        StringBuilder sb = new StringBuilder();
        sb.append("https://api.weixin.qq.com/sns/oauth2/access_token?")
            .append("appid=").append(wechatConfig.getAppid())
            .append("&secret=").append(wechatConfig.getAppSecret())
            .append("&code=").append(code)
            .append("&grant_type=authorization_code");
        String response = requestUrl(sb.toString());
        JsonParser parse = new JsonParser();
        JsonObject json = (JsonObject) parse.parse(response);
        System.out.println(json.get("openid"));
        return json.get("openid").getAsString();
    }

    public String requestUrl(String url){
        String response = null;
        try {
            response = UrlRequest.getRequest(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
