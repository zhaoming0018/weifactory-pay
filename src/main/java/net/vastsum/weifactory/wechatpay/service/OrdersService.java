package net.vastsum.weifactory.wechatpay.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import net.vastsum.weifactory.wechatpay.config.WechatConfig;
import net.vastsum.weifactory.wechatpay.entity.Orders;
import net.vastsum.weifactory.wechatpay.repository.OrdersRepository;
import net.vastsum.weifactory.wechatpay.utils.UrlRequest;
import net.vastsum.weifactory.wechatpay.utils.WxPayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class OrdersService {

    @Autowired
    private OrdersRepository orderRepo;

    @Autowired
    private WechatConfig wechatConfig;

    public Orders findOrder(Integer id){
        return orderRepo.findOne(id);
    }

    public Orders generateOrder(Integer batchId, String deposit, Integer expertId, String openid){
        String fetchBatchInfoUrl = "http://test.weifactory.vastsum.net/batch/detail/"+batchId;
        JsonParser parser = new JsonParser();
        log.info("即将生成订单。。。");
        try {
            JsonObject jsonObject = (JsonObject) parser.parse(UrlRequest.getRequest(fetchBatchInfoUrl));
            JsonObject data = (JsonObject) jsonObject.get("data");
            log.info("data is : {}",data);
            String body = getOrderBody();
            Orders orders = new Orders();
            BigDecimal totalFee = getTotalFee();
            orders.setOrderNumber(getOutTradeOrder());
            orders.setPrepayId(getPrepayId(openid,body,totalFee));
            orders.setOpenId(openid);
            orders.setOrderPrice(totalFee);
            orders.setOrderBody(body);
            orders.setSn(data.get("deviceId").getAsString());
            orders.setBatchId(batchId);
            orders.setPlantOne(data.get("plantOne").getAsString());
            orders.setPlantTwo(data.get("plantTwo").getAsString());
            orders.setPlantThree(data.get("plantThree").getAsString());
            orders.setModeOne(data.get("cultModelOne").getAsInt());
            orders.setModeTwo(data.get("cultModelTwo").getAsInt());
            orders.setModeThree(data.get("cultModelThree").getAsInt());
            orders.setDeposit(deposit);
            orders.setExpertId(expertId);
            orders.setOrderState(0);
            orders.setOrderStart(new Date());
            log.info("订单构造完毕，即将保存。。。\n{}", orders);
            return orderRepo.save(orders);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getOrderBody(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYMMddhhmmss");
        return "植物工厂托管服务_"+simpleDateFormat.format(new Date());
    }

    private String getOutTradeOrder(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYMMddhhmmss");
        return "MicroFactoryCharge_"+simpleDateFormat.format(new Date());
    }

    private BigDecimal getTotalFee(){
        return new BigDecimal(0.01);
    }

    private String getClientIp(){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String ip;
        if (request.getHeader("x-forwarded-for") == null) {
            ip = request.getRemoteAddr();
        }else{
            ip = request.getHeader("x-forwarded-for");
        }
        log.info("ip is : {}",ip);
        return ip;
    }

    private String getPrepayId(String openid,String body,BigDecimal totalFee){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("appid",wechatConfig.getAppid());
        hashMap.put("mch_id",wechatConfig.getMchid());
        hashMap.put("device_info","WEB");
        hashMap.put("nonce_str",WxPayUtils.getNonceStr() );
        hashMap.put("notify_url","http://saber.nat100.top/wechat/notify");
        hashMap.put("out_trade_no",getOutTradeOrder());
        hashMap.put("trade_type","JSAPI");
        hashMap.put("openid",openid);
        hashMap.put("body",body);
        hashMap.put("total_fee", "1");
        hashMap.put("spbill_create_ip",getClientIp());
        try {
            hashMap.put("sign", WxPayUtils.generateSign(hashMap,"MD5",wechatConfig.getApikey()));
            String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
            String requestXml= WxPayUtils.mapToXml(hashMap);
            log.info("请求的xml:{}",requestXml);
            String response = UrlRequest.postRequest(url,requestXml);
            log.info("response is :\n{}",response);
            StringReader sr = new StringReader(response);
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(sr));
            sr.close();
            //从xml对象中获取prepay_id
            NodeList nodeList = doc.getElementsByTagName("prepay_id");
            String prepay_id = nodeList.item(0).getFirstChild().getNodeValue();
            log.info("Get prepay_id from xml, prepay_id = {}",prepay_id);
            return prepay_id;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Map<String,String> getModel(Map<String,String> model){
        model.put("appId",wechatConfig.getAppid());
        Long timeStampL = new Date().getTime();
        model.put("timeStamp",timeStampL.toString());
        model.put("signType","MD5");
        return model;
    }
}
