package org.example.simplemq.consumer;

import com.rabbitmq.client.*;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 魔芋红茶
 * @version : 1.0
 * @Project : simplemq
 * @Package : org.example.simplemq.consumer
 * @ClassName : .java
 * @createTime : 2023/8/2 10:14
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description :
 */
@Log4j2
public class ConsumerTests {
    @Test
    @SneakyThrows
    public void testConsumeMessage(){
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
        // 声明队列
        String queueName = "simple.queue";
        channel.queueDeclare(queueName, false, false ,false, null);
        // 获取消息
        channel.basicConsume(queueName, true,  new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                log.info(String.format("Received msg [%s] from RabbitMQ.", msg));
            }
        });
        log.info("Already added msg receiver to RabbitMQ client.");
    }
}
