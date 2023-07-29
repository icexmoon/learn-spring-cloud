package org.example.shopping.feignapi.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 魔芋红茶
 * @version : 1.0
 * @Project : shopping
 * @Package : org.example.shopping.order.config
 * @ClassName : .java
 * @createTime : 2023/7/21 11:55
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description :
 */
public class DefaultFeignConfiguration {
    @Bean
    public Logger.Level loggerLevel(){
        return Logger.Level.BASIC;
    }
}
