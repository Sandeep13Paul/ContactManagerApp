package com.smart.Services;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
public class SessionHelper {
    public void removeMessageFromSession() {
        try {
            System.out.println("Removing message from session");
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            session.removeAttribute("message");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
