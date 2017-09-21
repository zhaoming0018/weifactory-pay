<script>
    function onBridgeReady(){
        //构造订单对象
        var orderObj = {
            "appId":"${appId}",
            "timeStamp":"${timeStamp}",
            "nonceStr":"${nonceStr}",
            "package":"${package}",
            "signType":"MD5",
            "paySign":"${paySign}"
        };
        //调用微信支付
        WeixinJSBridge.invoke(
                'getBrandWCPayRequest', orderObj,
                function(res){
                    if(res.err_msg === "get_brand_wcpay_request:ok" ) {}
                }
        );
    }
    //触发支付调用函数
    if (typeof WeixinJSBridge === "undefined"){
        if( document.addEventListener ){
            document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
        }else if (document.attachEvent){
            document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
            document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
        }
    }else{
        onBridgeReady();
    }
</script>