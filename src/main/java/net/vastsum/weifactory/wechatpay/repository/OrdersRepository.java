package net.vastsum.weifactory.wechatpay.repository;

import net.vastsum.weifactory.wechatpay.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders,Integer> {

}
