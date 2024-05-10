package com.example.iHome.handler;

import com.example.iHome.config.custom.CustomUserDetails;
import com.example.iHome.model.entity.Account;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String redirectURL = "/login";
        try {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            Account account = customUserDetails.getAccount();

            if (account != null) {
                redirectURL = switch (account.getRole().getName().toUpperCase()) {
                    case "ADMIN" -> "/back/dashboard";
                    case "LANDLORD" -> "/back/house";
                    case "TENANT" -> "/";
                    default -> "/login";
                };
            }

            response.sendRedirect(redirectURL);
        } catch (Exception ex) {
            response.sendRedirect(redirectURL);
        }
    }

}

