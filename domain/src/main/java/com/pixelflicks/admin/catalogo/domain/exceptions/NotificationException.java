package com.pixelflicks.admin.catalogo.domain.exceptions;

import com.pixelflicks.admin.catalogo.domain.validation.Error;
import com.pixelflicks.admin.catalogo.domain.validation.handler.Notification;

import java.util.List;

public class NotificationException extends DomainException{
    public NotificationException(String aMessage, Notification notification) {
        super(aMessage, notification.getErrors());
    }
}
