//package com.smart.Controller;
//
//import com.smart.Dao.UserRepository;
//import com.smart.Helper.Message;
//import com.smart.Models.User;
//import jakarta.servlet.http.HttpSession;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@Controller
//public class HomeController {
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @GetMapping("/")
//    public String home(Model model) {
//        model.addAttribute("title", "Home | Smart Contact Manager");
//        return "home";
//    }
//
//    @GetMapping("/about")
//    public String about(Model model) {
//        model.addAttribute("title", "About | Smart Contact Manager");
//        return "about";
//    }
//
//    @GetMapping("/signup")
//    public String signup(Model model) {
//        model.addAttribute("title", "Register | Smart Contact Manager");
//        model.addAttribute("user", new User());
//        return "signup";
//    }
//
//    @PostMapping("/register")
//    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, @RequestParam(value = "agreement", defaultValue = "false") boolean agree, Model model, HttpSession session) {
//        try {
//            if (!agree) {
//                System.out.println("You have not agreed to the terms");
//                throw new Exception("You have not agreed to the terms");
//            }
//
//            if (result.hasErrors()) {
//                session.setAttribute("message", new Message("Somthing went wrong", "alert-danger"));
//                model.addAttribute("user", user);
//                session.removeAttribute("message");
//                return "signup";
//            }
//
//            user.setRole("ROLE_USER");
//            user.setEnabled(true);
//            user.setPassword(passwordEncoder.encode(user.getPassword()));
//            User res = this.userRepository.save(user);
//
//            model.addAttribute("user", new User());
//            session.setAttribute("message", new Message("Successfully Registered", "alert-success"));
////            return "signup";
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            model.addAttribute("user", user);
//            session.setAttribute("message", new Message("Somthing went wrong" + e.getMessage(), "alert-danger"));
////            return "signup";
//        }
//
//        return "signup";
//    }
//
//    @GetMapping("/signin")
//    public String customLogin(Model model) {
//        model.addAttribute("title", "Login | Smart Contact Manager");
//        return "login";
//    }
//}

package com.smart.Controller;

import com.smart.Dao.UserRepository;
import com.smart.Helper.Message;
import com.smart.Models.User;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home | Smart Contact Manager");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About | Smart Contact Manager");
        return "about";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "Register | Smart Contact Manager");
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, @RequestParam(value = "agreement", defaultValue = "false") boolean agree, Model model, HttpSession session) {
        try {
            if (!agree) {
                throw new Exception("You have not agreed to the terms");
            }

            if (result.hasErrors()) {
                model.addAttribute("user", user);
                session.setAttribute("message", new Message("Something went wrong", "alert-danger"));
                return "signup";
            }

            user.setRole("ROLE_USER");
            user.setEnabled(true);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User res = this.userRepository.save(user);

            model.addAttribute("user", new User());
            session.setAttribute("message", new Message("Successfully Registered", "alert-success"));
            return "login"; // Redirect to login page after successful registration
        }
        catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Something went wrong: " + e.getMessage(), "alert-danger"));
            return "signup";
        }
        finally {
            // Ensure the message is removed from the session after being set
            session.removeAttribute("message");
        }
    }

    @GetMapping("/signin")
    public String customLogin(Model model) {
        model.addAttribute("title", "Login | Smart Contact Manager");
        return "login";
    }
}
