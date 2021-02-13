package org.emaginalabs.examples.demodcn.events;

import org.emaginalabs.examples.demodcn.model.Notification;

import org.springframework.context.ApplicationEvent;

/**
 * @author Arquitectura
 */
public final class NotificationEvent extends ApplicationEvent {

    private final Notification notification;

    public NotificationEvent(Object source, Notification notification) {
        super(source);
        this.notification = notification;
    }

    public Notification getNotification() {
        return notification;
    }

}
