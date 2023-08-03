package org.example.simplemq.consumer;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 魔芋红茶
 * @version : 1.0
 * @Project : simplemq
 * @Package : org.example.simplemq.consumer
 * @ClassName : .java
 * @createTime : 2023/8/2 12:07
 * @Email : icexmoon@qq.com
 * @Website : https://icexmoon.cn
 * @Description :
 */
@Component
@Log4j2
public class RabbitMQListeners {
    private static final String QUEUE_NAME = "simple.queue";

    //    @RabbitListener(queues = "simple.queue")
//    void handleMessage(String msg){
//        log.info(String.format("Received message [%s] from RabbitMQ.", msg));
//    }
    @SneakyThrows
    @RabbitListener(queues = RabbitMQListeners.QUEUE_NAME)
    void consumer1(String msg) {
        //消费者1，每秒消耗50条消息
        log.info(String.format("consumer1 received message: %s", msg));
        Thread.sleep(20);
    }

    @SneakyThrows
    @RabbitListener(queues = RabbitMQListeners.QUEUE_NAME)
    void consumer2(String msg) {
        //消费者2，每秒消耗10条消息
        log.info(String.format("consumer2 received message: %s", msg));
        Thread.sleep(100);
    }

    @RabbitListener(queues = WebConfig.QUEUE1)
    void consumeQueue1(String msg) {
        log.info(String.format("Received message[%s] from Queue[%s]", msg, WebConfig.QUEUE1));
    }

    @RabbitListener(queues = WebConfig.QUEUE2)
    void consumeQueue2(String msg) {
        log.info(String.format("Received message[%s] from Queue[%s]", msg, WebConfig.QUEUE2));
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("direct.queue1"),
            key = {"red", "yellow"},
            exchange = @Exchange(value = "exchange.direct", type = ExchangeTypes.DIRECT)
    ))
    void directConsumer1(String msg) {
        log.info(String.format("Consumer1 has received message[%s] from queue[%s]", msg, "direct.queue1"));
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("direct.queue2"),
            key = {"red", "blue"},
            exchange = @Exchange(value = "exchange.direct", type = ExchangeTypes.DIRECT)
    ))
    void directConsumer2(String msg) {
        log.info(String.format("Consumer2 has received message[%s] from queue[%s]", msg, "direct.queue2"));
    }

    /**
     * 消费中国历史书籍
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("topic.queue1"),
            exchange = @Exchange(value = "exchange.topic", type = ExchangeTypes.TOPIC),
            key = "history.china.#"
    ))
    void topicMessageConsumer1(String msg){
        log.info(String.format("Consumer1 received message[%s].", msg));
    }

    /**
     * 消费所有的历史书籍
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("topic.queue2"),
            exchange = @Exchange(value = "exchange.topic", type = ExchangeTypes.TOPIC),
            key = "history.#"
    ))
    void topicMessageConsumer2(String msg){
        log.info(String.format("Consumer2 received message[%s].", msg));
    }

    /**
     * 消费所有的古代史书籍
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("topic.queue3"),
            exchange = @Exchange(value = "exchange.topic", type = ExchangeTypes.TOPIC),
            key = "#.archeology"
    ))
    void topicMessageConsumer3(String msg){
        log.info(String.format("Consumer3 received message[%s].", msg));
    }
}
