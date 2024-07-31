package com.smart.Controller;

import com.smart.Dao.UserRepository;
import com.smart.Helper.Message;
import com.smart.Models.User;
import com.smart.Services.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
public class ForgotController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/forgot")
    public String openEmailForm() {
        return "forgot_form";
    }

    @PostMapping("/send-otp")
    public String send(@RequestParam("email") String email, HttpSession session) {

        User user = this.userRepository.getUserByUserName(email);
        if (user == null) {
            session.setAttribute("message", new Message("User does not exists with this email", "danger"));
            return "redirect:/forgot";
        }
        //generating otp of 4 digit
        String ch = "1234567890";
        int otp = 0;
        for (int i = 0; i < 6; i++) {
            int c = (int)(Math.floor(Math.random() * 10));
            otp = (otp * 10) + (ch.charAt(c) - 48);
        }
        System.out.println(otp);

        String subject = "OTP from SCM";
        String message = "<div style='border: 1px solid #e2e2e2; padding: 20px;'><h1> OTP = <b>" + otp + "</b></h1></div>";
        String to = email;

        boolean flag = this.emailService.sendEmail(subject, message, to);

        if (flag) {
            session.setAttribute("otp", otp);
            session.setAttribute("email", email);
            return "verify_otp";
        }
        else {
            session.setAttribute("message", new Message("Check your email id", "danger"));
            return "redirect:/forgot";
        }
    }

    @PostMapping("/verify-otp")
    public String verifyotp(@RequestParam("otp") int otp, HttpSession session) {
        int myOtp = (int) session.getAttribute("otp");
        String email = (String) session.getAttribute("email");

        if (Objects.equals(myOtp, otp)) {
            User user = this.userRepository.getUserByUserName(email);
            session.setAttribute("user", user);
            return "password_change_form";
        }
        else {
            session.setAttribute("message", new Message("Enter your correct otp", "danger"));
            return "verify_otp";
        }
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("password") String password, HttpSession session) {
        User user = (User) session.getAttribute("user");

        user.setPassword(this.passwordEncoder.encode(password));
        this.userRepository.save(user);
        session.setAttribute("message", new Message("Your Password has been changed", "success"));

        return "redirect:/signin";
    }
}
