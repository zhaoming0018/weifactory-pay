package net.vastsum.weifactory.wechatpay.entity;

import lombok.Data;

/**
 * http请求返回的结果
 */
@Data
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;
}
