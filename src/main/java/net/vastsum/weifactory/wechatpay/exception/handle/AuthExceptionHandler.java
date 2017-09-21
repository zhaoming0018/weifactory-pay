package net.vastsum.weifactory.wechatpay.exception.handle;

import net.vastsum.weifactory.wechatpay.exception.WechatAuthException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class AuthExceptionHandler {
    @ExceptionHandler(value = WechatAuthException.class)
    public ModelAndView handleAuthException(){
        return null;
    }
}
