package com.maletify.gestorempaque.notificacion.domain.services;

import com.maletify.gestorempaque.notificacion.domain.model.Recordatorio;

public interface NotificationSender {

    boolean send(Recordatorio recordatorio);
}