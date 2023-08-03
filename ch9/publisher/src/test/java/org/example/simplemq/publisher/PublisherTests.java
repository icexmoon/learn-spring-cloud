package org.example.simplemq.publisher;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 魔芋红茶
 * @version : 1.0
 * @Project : simplemq
 * @Package : org.example.simplemq.publisher
 * @ClassName : .java
 * @createTime : 2023/8/2 9:50
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description :
 */
@Log4j2
public class PublisherTests {
    @Test
    @SneakyThrows
    public void testSendMessage() {
        // 设置到 RabbitMQ 的连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.0.88");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("itcast");
        connectionFactory.setPassword("123321");
        // 创建连接
        Connection connection = connectionFactory.newConnection();
        // 创建通道
        Channel channel = connection.createChannel();
        // 创建队列
        String queueName = "simple.queue";
        channel.queueDeclare(queueName, false, false, false, null);
        // 发送消息
        String msg = "Hello World!";
        channel.basicPublish("", queueName, null, msg.getBytes(StandardCharsets.UTF_8));
        log.info(String.format("message [%s] was sent to RabbitMQ.", msg));
    }
}
