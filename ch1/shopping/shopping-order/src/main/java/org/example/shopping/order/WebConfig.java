package org.example.shopping.order;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 魔芋红茶
 * @version : 1.0
 * @Project : shopping
 * @Package : org.example.shopping.order
 * @ClassName : .java
 * @createTime : 2023/7/14 9:29
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description :
 */
@Configuration
public class WebConfig {
    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
