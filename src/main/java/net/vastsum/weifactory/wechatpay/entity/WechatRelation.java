package net.vastsum.weifactory.wechatpay.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Component
@Data
public class WechatRelation {
    @Id
    @GeneratedValue
    private Integer wechatRelationId;

    private Integer userId;

    private String openid;

    private Boolean detailInfo;
}
