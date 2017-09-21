package VO;

import lombok.Data;

import java.util.HashMap;

@Data
public class PayVO {
    private String appId;
    private String timeStamp;
    private String nonceStr;
    private String packages;
    private String signType;
    private String paySign;
    public HashMap<String,String> toHashMap(){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("appId",appId);
        hashMap.put("timeStamp",timeStamp);
        hashMap.put("nonceStr",nonceStr);
        hashMap.put("package",packages);
        hashMap.put("signType",signType);
        return hashMap;
    }
}
