package com.example.iHome.service;

import com.example.iHome.model.dto.EmailBookingDTO;
import com.example.iHome.model.dto.EmailTemplateDTO;

public interface EmailService {

    void sendForRegister(EmailTemplateDTO emailTemplateDTO);

    void sendForgotPassword(EmailTemplateDTO emailTemplateDTO);

    void sendForCheckout(EmailBookingDTO emailBookingDTO, String type);

}
