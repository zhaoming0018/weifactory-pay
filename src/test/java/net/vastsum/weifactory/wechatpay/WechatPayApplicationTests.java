package net.vastsum.weifactory.wechatpay;

import net.vastsum.weifactory.wechatpay.utils.UrlRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatPayApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void requestTest() throws Exception {
		String postData = "<xml>\n<a>ssssss</a></xml>";
		String rest = UrlRequest.postRequest("http://saber.nat100.top/wechat/notify",postData);
		System.out.println("获得了："+rest);
	}

}
