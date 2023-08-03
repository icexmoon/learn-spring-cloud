package org.example.simplemq.publisher;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 魔芋红茶
 * @version : 1.0
 * @Project : simplemq
 * @Package : org.example.simplemq.publisher
 * @ClassName : .java
 * @createTime : 2023/8/2 11:50
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description :
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Log4j2
public class SpringAMQPTests {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMessage(){
        String msg = "Hello World, again!";
        String queueName = "simple.queue";
        rabbitTemplate.convertAndSend(queueName, msg);
        log.info(String.format("Already sent message [%s] to RabbitMQ.", msg));
    }
}
