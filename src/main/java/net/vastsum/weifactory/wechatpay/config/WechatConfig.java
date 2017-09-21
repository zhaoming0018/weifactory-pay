package net.vastsum.weifactory.wechatpay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "wechat")
@Component
@Data
public class WechatConfig {
    private String appid;
    private String appSecret;
    private String domain;
    private String mchid;
    private String apikey;
}
