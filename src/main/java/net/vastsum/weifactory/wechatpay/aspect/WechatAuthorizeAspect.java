package net.vastsum.weifactory.wechatpay.aspect;

import net.vastsum.weifactory.wechatpay.exception.WechatAuthException;
import net.vastsum.weifactory.wechatpay.utils.CookieUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


public class WechatAuthorizeAspect {

    private Logger logger = LoggerFactory.getLogger(WechatAuthorizeAspect.class);

    @Pointcut("execution(public * net.vastsum.weifactory.wechatpay.controller.WechatInfoController.*(..))")
    public void point(){}

    @Before("point()")
    public void auth(){
        ServletRequestAttributes attributes =  (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Cookie cookie = CookieUtils.getCookieByName(request,"openid");
        if(cookie == null){
            logger.info("没有进行微信认证");
            throw new WechatAuthException();
        }
    }
}
