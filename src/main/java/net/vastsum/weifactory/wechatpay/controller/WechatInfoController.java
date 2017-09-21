package net.vastsum.weifactory.wechatpay.controller;

import net.vastsum.weifactory.wechatpay.entity.WechatRelation;
import net.vastsum.weifactory.wechatpay.repository.WechatRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wechatInfo")
public class WechatInfoController {

    @Autowired
    private WechatRelationRepository wechatRelationRepo;

    @GetMapping("/relations")
    public List<WechatRelation> getAll(){
        return wechatRelationRepo.findAll();
    }

    @PostMapping("/addRelation")
    public WechatRelation addRelation
            (@RequestParam("user_id")Integer userId,
             @RequestParam("openid")String openid,
             @RequestParam(value = "detail_info",required = false,defaultValue = "0")Boolean detail_info){
        WechatRelation relation = new WechatRelation();
        relation.setUserId(userId);
        relation.setOpenid(openid);
        relation.setDetailInfo(detail_info);
        return wechatRelationRepo.save(relation);
    }

    @GetMapping("/relation/{userid}")
    public List<WechatRelation> getRelation(@PathVariable("userid")Integer userid)
    {
        return wechatRelationRepo.findWechatRelationByUserId(userid);
    }

    @PostMapping("/updateRelation/{id}")
    public WechatRelation updateRelation
            (@PathVariable("id")Integer id,
             @RequestParam(value = "user_id",required = false)Integer userId,
             @RequestParam(value = "openid",required = false)String openid,
             @RequestParam(value = "detail_info",required = false)Boolean detail_info){
        WechatRelation relation = new WechatRelation();
        relation.setWechatRelationId(id);
        relation.setUserId(userId);
        relation.setOpenid(openid);
        relation.setDetailInfo(detail_info);
        return wechatRelationRepo.save(relation);
    }
}
