package net.vastsum.weifactory.wechatpay.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Component
@Data
public class Orders {
    @Id
    @GeneratedValue
    private Integer orderId;
    /** 订单编号 **/
    private String orderNumber;
    /** 微信生成的订单预付编号 **/
    private String prepayId;
    /** 用于用户标识的用户id **/
    private String openId;
    /** 订单价格 **/
    private BigDecimal orderPrice;
    /** 订单标题 **/
    private String orderBody;
    /** 机器的序列号 **/
    private String sn;
    /** 批次编号 **/
    private Integer batchId;
    /** 第一个植物 **/
    private String plantOne;
    /** 第二个植物 **/
    private String plantTwo;
    /** 第三个植物 **/
    private String plantThree;
    /** 第一个栽培模式 **/
    private Integer modeOne;
    /** 第二个栽培模式 **/
    private Integer modeTwo;
    /** 第三个栽培模式 **/
    private Integer modeThree;
    /** 托管方式 **/
    private String deposit;
    /** 专家编号 **/
    private Integer expertId;
    /** 订单状态 **/
    private Integer orderState;
    /** 下订单时间 **/
    private Date orderStart;
    /** 完结订单时间 **/
    private Date orderEnd;
}
