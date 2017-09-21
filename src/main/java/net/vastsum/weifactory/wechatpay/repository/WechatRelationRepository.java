package net.vastsum.weifactory.wechatpay.repository;


import net.vastsum.weifactory.wechatpay.entity.WechatRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WechatRelationRepository extends JpaRepository<WechatRelation,Integer>{
    public List<WechatRelation> findWechatRelationByUserId(Integer userId);
}
