package org.example.shopping.user.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 魔芋红茶
 * @version : 1.0
 * @Project : shopping
 * @Package : org.example.shopping.user.config
 * @ClassName : .java
 * @createTime : 2023/7/17 20:26
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description :
 */
@Component
@ConfigurationProperties(prefix = "pattern")
@Getter
@Setter
public class PatternProperties {
    private String dateformat;
    private String common;
    private String commonAndDev;
    private String commonAndLocal;
}
