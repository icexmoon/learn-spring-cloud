package org.example.simplemq.publisher;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 魔芋红茶
 * @version : 1.0
 * @Project : simplemq
 * @Package : org.example.simplemq.publisher
 * @ClassName : .java
 * @createTime : 2023/8/2 19:26
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description :
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PublishDescribeTests {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testPublishMessage() {
        String msg = "Hello, everyone!";
        String exchangeName = "exchange.fanout";
        rabbitTemplate.convertAndSend(exchangeName, "", msg);
    }

    @Test
    public void testDirectExchangeMessageSend() {
        doDirectExchangeMsgSend("red");
        doDirectExchangeMsgSend("blue");
        doDirectExchangeMsgSend("yellow");
    }

    private void doDirectExchangeMsgSend(String routingKey) {
        String exchangeName = "exchange.direct";
        String msg = String.format("Hello, %s!", routingKey);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, msg);
    }

    @Test
    public void testSendTopicMessages() {
        rabbitTemplate.convertAndSend("exchange.topic",
                "history.china.archeology",
                "《明朝那些事》");
        rabbitTemplate.convertAndSend("exchange.topic",
                "history.japan.archeology",
                "《日本战国史》");
        rabbitTemplate.convertAndSend("exchange.topic",
                "history.world.current",
                "《二战全景解读》");
    }

    @Test
    public void testSendObjectMessage(){
        Map<String, Object> msg = new HashMap<>();
        msg.put("name", "icexmoon");
        msg.put("age", 28);
        msg.put("phone", "123456");
        rabbitTemplate.convertAndSend("queue.object", msg);
    }
}
