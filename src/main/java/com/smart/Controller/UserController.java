package com.smart.Controller;

import com.smart.Dao.ContactRepository;
import com.smart.Dao.UserRepository;
import com.smart.Helper.Message;
import com.smart.Models.Contact;
import com.smart.Models.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //adding common data
    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String username = principal.getName();

        User user = this.userRepository.getUserByUserName(username);

        model.addAttribute("user", user);
    }

    @GetMapping("/index")
    public String dashboard(Model model) {
        model.addAttribute("title", "User Dashboard | Smart Contact Manager");
        return "normal/user_dashboard";
    }

    @GetMapping("/add-contact")
    public String openAddForm(Model model) {
        model.addAttribute("title", "Add Contact | Smart Contact Manager");
        model.addAttribute("contact", new Contact());
        return "normal/add_form";
    }

    @PostMapping("/process-contact")
    public String process(@ModelAttribute Contact contact, @RequestParam("profileImage")MultipartFile file, Principal principal, HttpSession session) {
        try {
            String name = principal.getName();
            User user = this.userRepository.getUserByUserName(name);

            if (file.isEmpty()) {
                System.out.println("file not uploaded correctly");
                contact.setcImageUrl("register.png");
            }
            else {
                //upload file to folder then set the name in contact
                contact.setcImageUrl(file.getOriginalFilename());

                File file1 = new ClassPathResource("static/images").getFile();
                Path path = Paths.get(file1.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("Image uploaded");
            }

            contact.setUser(user);
            user.getContact().add(contact);
            this.userRepository.save(user);
            //success message

            session.setAttribute("message", new Message("Your Contact is added!! Add more..", "success"));
            return "normal/add_form";

        } catch (Exception e) {
            e.printStackTrace();
            //error message
            session.setAttribute("message", new Message("Something Went Wrong!! Try Again..", "danger"));
            return "normal/add_form";
        }
    }

    @GetMapping("/show-contacts/{page}")
    public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal) {
        model.addAttribute("title", "View Contact | Smart Contact Manager");
        String username = principal.getName();

        User user = this.userRepository.getUserByUserName(username);

        Pageable pageable = PageRequest.of(page, 4);

        Page<Contact> contacts = this.contactRepository.findContactsByUser(user.getId(), pageable);

        model.addAttribute("contacts", contacts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", contacts.getTotalPages());
        return "normal/show_contacts";
    }

    @GetMapping("/contact/{cid}")
    public String showContact(@PathVariable("cid") Integer cid, Model model, Principal principal) {

        Contact contact = this.contactRepository.getContactByContactID(cid);

        String username = principal.getName();
        User user = this.userRepository.getUserByUserName(username);

        if (user.getId() == contact.getUser().getId()) {
            model.addAttribute("title", "Contact Dashboard | Smart Contact Manager");

            model.addAttribute("contact", contact);

            return "normal/contact_detail";
        }
        else {
            return "normal/show_contacts";
        }

    }

    @GetMapping("/delete/{cid}")
    public String deleteContact(@PathVariable("cid") Integer cid, Model model, Principal principal, HttpSession session) {
        Contact contact = this.contactRepository.getContactByContactID(cid);
        User user = this.userRepository.getUserByUserName(principal.getName());

        if (user.getId() == contact.getUser().getId()) {

            contact.setUser(null);
            // Delete associated image file
            String imagePath = contact.getcImageUrl(); // Assuming imagePath is a field in the Contact entity

            if (imagePath != null && !imagePath.isEmpty() && !imagePath.equals("register.png")) {
                try {
                    File saveFile = new ClassPathResource("static/images").getFile();
                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + imagePath);
                    Files.deleteIfExists(path);
                    System.out.println("Deleted image file: " + imagePath);
                } catch (IOException e) {
                    // Handle file deletion failure
                    e.printStackTrace();
                }
            }
            this.contactRepository.delete(contact);
            session.setAttribute("message", new Message("Contact Deleted Successfully...", "success"));
            return "redirect:/user/show-contacts/0";
        }
        else {
            return "redirect:/user/show-contacts/0";
        }
    }

    @PostMapping("/update-contact/{cid}")
    public String updateContact(@PathVariable("cid") Integer cid, Model model) {
        model.addAttribute("title", "Update Contact Dashboard | Smart Contact Manager");

        Contact contact = this.contactRepository.getContactByContactID(cid);
        model.addAttribute("contact", contact);
        return "normal/update_form.html";
    }

    @PostMapping("/process-update/{cid}")
    public String updated(@PathVariable("cid") int cid, @ModelAttribute Contact contact, @RequestParam("profileImage")MultipartFile file, Model model, HttpSession session, Principal principal) {
        try {

            Contact oldContact = this.contactRepository.getContactByContactID(cid);


            if (!file.isEmpty()) {
                //file rewrite

                //delete old photo
                File deleteFile = new ClassPathResource("static/images").getFile();
                File file2 = new File(deleteFile, oldContact.getcImageUrl());
                file2.delete();

                //update new file
                File file1 = new ClassPathResource("static/images").getFile();
                Path path = Paths.get(file1.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                contact.setcImageUrl(file.getOriginalFilename());
            }
            else {

                contact.setcImageUrl(oldContact.getcImageUrl());

            }
            contact.setCid(cid);
            User user = this.userRepository.getUserByUserName(principal.getName());
            contact.setUser(user);
            this.contactRepository.save(contact);
            session.setAttribute("message", new Message("Your contact is updated Successfully", "success"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/user/show-contacts/" + cid;
    }

    //your profile
    @GetMapping("/profile")
    public String yourProfile(Model model, Principal principal) {
        model.addAttribute("title", "Profile | Smart Contact Manager");
        User user = this.userRepository.getUserByUserName(principal.getName());
        model.addAttribute("user", user);
        return "normal/profile";
    }

    //settings

    @GetMapping("/settings")
    public String open(Model model) {

        model.addAttribute("title", "Profile | Smart Contact Manager");
        return "normal/setting";
    }

    @PostMapping("/change-password")
    public String change(@RequestParam("oldpass") String oldPassword, @RequestParam("newpass") String newPassword, Principal principal, HttpSession session) {

        User user = this.userRepository.getUserByUserName(principal.getName());

        if (this.passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(this.passwordEncoder.encode(newPassword));
            this.userRepository.save(user);
            session.setAttribute("message", new Message("Your Password is updated Successfully", "success"));
        }
        else {
            session.setAttribute("message", new Message("Please Enter your Correct Old Password", "danger"));
            return "redirect:/user/settings";
        }

        return "redirect:/user/profile";
    }
}
