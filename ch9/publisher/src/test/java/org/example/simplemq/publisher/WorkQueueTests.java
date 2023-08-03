package org.example.simplemq.publisher;

import lombok.SneakyThrows;
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
 * @createTime : 2023/8/2 17:27
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description :
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WorkQueueTests {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    @SneakyThrows
    public void testSendMessages() {
        //1秒内发送50条消息
        String queueName = "simple.queue";
        for (int i = 0; i < 50; i++) {
            String msg = String.format("work queue test[%d]", i + 1);
            rabbitTemplate.convertAndSend(queueName, msg);
            Thread.sleep(20);
        }
    }
}
