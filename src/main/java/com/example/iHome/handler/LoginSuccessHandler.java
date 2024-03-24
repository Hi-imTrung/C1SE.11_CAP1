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
                switch (account.getRole().getName().toUpperCase()) {
                    case "ADMIN":
                        redirectURL = "/back/dashboard";
                        break;
                    case "LANDLORD":
                        redirectURL = "/back/house";
                        break;
                    case "TENANT":
                        redirectURL = "/";
                        break;
                    default:
                        redirectURL = "/login";
                        break;
                }
            }

            response.sendRedirect(redirectURL);
        } catch (Exception ex) {
            response.sendRedirect(redirectURL);
        }
    }

}

