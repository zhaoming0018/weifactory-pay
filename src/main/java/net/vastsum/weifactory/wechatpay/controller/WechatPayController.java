package net.vastsum.weifactory.wechatpay.controller;

import VO.PayVO;
import lombok.extern.slf4j.Slf4j;
import net.vastsum.weifactory.wechatpay.config.WechatConfig;
import net.vastsum.weifactory.wechatpay.entity.Orders;
import net.vastsum.weifactory.wechatpay.service.AuthService;
import net.vastsum.weifactory.wechatpay.service.OrdersService;
import net.vastsum.weifactory.wechatpay.service.PayService;
import net.vastsum.weifactory.wechatpay.service.UrlService;
import net.vastsum.weifactory.wechatpay.utils.CookieUtils;
import net.vastsum.weifactory.wechatpay.utils.WxPayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/wechat")
@Slf4j
public class WechatPayController {

    @Autowired
    private WechatConfig wechatConfig;

    @Autowired
    private UrlService urlService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private AuthService authService;

    @Autowired
    private PayService payService;

    @GetMapping(value = "/test")
    public ModelAndView test(){
        log.info("访问测试页面");
        return new ModelAndView("wechat/test");
    }

    @PostMapping(value = "/middle")
    public String middle(
            HttpServletRequest request,
            @RequestParam("batch_id")Integer batchId,
            @RequestParam("expert_id")Integer expertId,
            @RequestParam("deposit")String deposit) throws Exception{
        log.info("进入中间链接");
        CookieUtils.setCookie("batch_id",batchId.toString());
        CookieUtils.setCookie("expert_id",expertId.toString());
        CookieUtils.setCookie("deposit",deposit);
        if(CookieUtils.getCookieByName(request,"openid") == null){
            //获取code的路径
            String redirectUrl = urlService.getCode();
            log.info("无OPENID,即将跳转到：{}",redirectUrl);
            //跳转
            return "redirect:"+redirectUrl;
        }else{
            //根据batch_id获取batch信息
            String redirectUrl = "http://"+wechatConfig.getDomain()+"/wechat/generate";
            log.info("有OPENID,即将跳转到：{}",redirectUrl);
            return "redirect:"+redirectUrl;
        }
    }

    @GetMapping(value = "/generate")
    @ResponseBody
    public Orders generate(HttpServletRequest request){
        String openid = CookieUtils.getCookieByName(request,"openid").getValue();
        Integer batchId = Integer.parseInt(CookieUtils.getCookieByName(request,"batch_id").getValue());
        Integer expertId = Integer.parseInt(CookieUtils.getCookieByName(request,"expert_id").getValue());
        String deposit = CookieUtils.getCookieByName(request,"deposit").getValue();
        log.info("In Cookie:openid={}",openid);
        log.info("In Cookie:batchId={}",batchId);
        log.info("In Cookie:expertId={}",expertId);
        log.info("In Cookie:deposit={}",deposit);
        log.info("即将生成订单。。。");
        Orders orders = ordersService.generateOrder(batchId,deposit,expertId,openid);
        CookieUtils.delete("batch_id");
        CookieUtils.delete("expert_id");
        CookieUtils.delete("deposit");
        log.info("返回订单：\n"+ orders);
        return orders;
    }

    @RequestMapping(value = "/pay")
    public ModelAndView doPay(@RequestParam("orderId")Integer orderId, HashMap<String,String> model){
        log.info("即将进行支付。。。");
        //根据订单号，获取订单信息
        Orders orders = ordersService.findOrder(orderId);
        //生成订单对象
        PayVO payVO = payService.getPayInfo(orders);
        //调用前端支付
        model.put("appId",payVO.getAppId());
        model.put("timeStamp",payVO.getTimeStamp());
        model.put("signType",payVO.getSignType());
        model.put("nonceStr",payVO.getNonceStr());
        model.put("package",payVO.getPackages());
        model.put("paySign",payVO.getPaySign());
        return new ModelAndView("wechat/pay",model);


//        model.put("appId",wechatConfig.getAppid());
//        Long timeStampL = new Date().getTime();
//        model.put("timeStamp",timeStampL.toString());
//        model.put("signType","MD5");
//        String nonceStr = "RSD58Bt2uqtWax4B";
//        String Package = "prepay_id=wx20170920124357f5c08e25250614994162";
//        model.put("nonceStr",nonceStr);
//        model.put("package",Package);
//        String paySign = null;
//        try {
//            paySign = WxPayUtils.generateSign((HashMap<String, String>)model,"MD5",wechatConfig.getApikey());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        model.put("paySign",paySign);
//        return new ModelAndView("wechat/pay",model);
    }

    @GetMapping(value = "/getOpenId")
    public String getOpenId(@RequestParam("code")String code){
        log.info("即将获取openid。。。");
        String openid = authService.getOpenid(code);
        CookieUtils.setCookie("openid",openid);
        String redirectUrl = "http://"+wechatConfig.getDomain()+"/wechat/generate";
        log.info("即将跳转到：{}",redirectUrl);
        return "redirect:"+redirectUrl;
    }

    @RequestMapping(value = "/notify")
    @ResponseBody
    public String getNotify(HttpServletRequest request){
        try {
            BufferedReader br = request.getReader();
            String str;
            StringBuilder wholeStr = new StringBuilder();
            while((str = br.readLine()) != null){
                wholeStr.append(str);
            }
            System.out.println(wholeStr.toString());
            return wholeStr.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
