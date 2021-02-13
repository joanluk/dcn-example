package org.emaginalabs.examples.demodcn.amqp.config;

import java.util.concurrent.CountDownLatch;

import org.emaginalabs.examples.demodcn.model.Notification;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

/**
 * @author Arquitectura
 */
@Component
@Slf4j
public class Consumer {

    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(Notification notification) {
        log.info("Received <" + notification + ">");
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}