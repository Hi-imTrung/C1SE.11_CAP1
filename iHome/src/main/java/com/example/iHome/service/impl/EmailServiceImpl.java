package com.example.iHome.service.impl;

import com.example.iHome.model.dto.EmailBookingDTO;
import com.example.iHome.model.dto.EmailTemplateDTO;
import com.example.iHome.model.dto.MailDTO;
import com.example.iHome.service.EmailService;
import com.example.iHome.util.ConstantUtil;
import com.example.iHome.util.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Override
    public void sendForRegister(EmailTemplateDTO emailTemplateDTO) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            String linkActive = ConstantUtil.HOST_URL + "/register/verify?id=" + emailTemplateDTO.getId();
            Context context = getContext(emailTemplateDTO, linkActive);

            String html = templateEngine.process("mail/welcome_user.html", context);

            helper.setFrom(mailFrom);
            helper.setTo(emailTemplateDTO.getEmail());
            helper.setSubject("Đăng Ký Tài Khoản Thành Công");
            helper.setText(html, true);
            javaMailSender.send(message);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void sendForgotPassword(EmailTemplateDTO emailTemplateDTO) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            //enCoder base64
            Date date = new Date();

            MailDTO<EmailTemplateDTO> mailDTO = new MailDTO<>();
            mailDTO.setTime(DateUtil.convertDateToString(date, "HH:mm:ss dd-MM-yyyy"));
            mailDTO.setData(emailTemplateDTO);
            mailDTO.setMinutes(30);

            ObjectMapper objectMapper = new ObjectMapper();
            String result = objectMapper.writeValueAsString(mailDTO);
            String linkActive = ConstantUtil.HOST_URL + "/forgot-password/confirm?ref=" + encoderStringToBase64(result);

            Context context = getContext(emailTemplateDTO, linkActive);

            String html = templateEngine.process("mail/forgot_password.html", context);

            helper.setFrom(mailFrom);
            helper.setTo(emailTemplateDTO.getEmail());
            helper.setSubject("Yêu Cầu Cấp Lại Mật Khẩu.");
            helper.setText(html, true);

            javaMailSender.send(message);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    @Override
    public void sendForCheckout(EmailBookingDTO emailBookingDTO, String type) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            Map<String, Object> properties =  new HashMap<>();
            properties.put("fullName", emailBookingDTO.getFullName());
            properties.put("email", emailBookingDTO.getEmail());
            properties.put("emailContact", "Email: ihome.support@gmail.com");
            properties.put("phoneContact", "Hotline: 0123-456-789");
            properties.put("name", emailBookingDTO.getName());
            properties.put("price", emailBookingDTO.getPrice());
            properties.put("address", emailBookingDTO.getAddress());
            properties.put("date", emailBookingDTO.getDate());

            Context context = new Context();
            context.setVariables(properties);

            String html;

            switch (type) {
                case "BOOKING":
                    html = templateEngine.process("mail/checkout_success.html", context);
                    helper.setSubject("Đăng Ký Nhà Trọ Thành Công");
                    break;
                case "BILL":
                    properties.put("newValue", emailBookingDTO.getNewValue());
                    properties.put("oldValue", emailBookingDTO.getOldValue());
                    properties.put("type", emailBookingDTO.getType());
                    context.setVariables(properties);
                    html = templateEngine.process("mail/bill.html", context);
                    helper.setSubject("Thông Báo Tiền Điện Nước");
                    break;
                default:
                    html = templateEngine.process("mail/booking_owner.html", context);
                    helper.setSubject("Thông Báo Đăng Ký Nhà Trọ");
                    break;
            }

            helper.setFrom(mailFrom);
            helper.setTo(emailBookingDTO.getEmail());
            helper.setText(html, true);
            javaMailSender.send(message);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Context getContext(EmailTemplateDTO emailTemplateDTO, String linkActive) {
        Map<String, Object> properties =  new HashMap<>();
        properties.put("fullName", emailTemplateDTO.getFullName());
        properties.put("email", emailTemplateDTO.getEmail());
        properties.put("link", linkActive);
        properties.put("emailContact", "Email: ihome.support@gmail.com");
        properties.put("phoneContact", "Hotline: 0123-456-789");

        Context context = new Context();
        context.setVariables(properties);
        return context;
    }

    public String encoderStringToBase64(String text) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(text.getBytes(StandardCharsets.UTF_8) );
    }

}
