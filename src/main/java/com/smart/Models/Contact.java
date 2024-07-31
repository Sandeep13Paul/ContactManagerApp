package com.smart.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cid;

    private String cname;

    private String nickname;

    private String phone;

    @Column(unique = true)
    @Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Invalid Email !!")
    private String email;

    private String work;

    @Column(length = 5000)
    private String description;

    private String cImageUrl;

    @ManyToOne
    @JsonIgnore
    private User user;

    public Contact() {
        super();
    }

    public Contact(int cid, String cname, String nickname, String work, String email, String cImageUrl, String description, String phone, User user) {
        this.cid = cid;
        this.cname = cname;
        this.nickname = nickname;
        this.work = work;
        this.email = email;
        this.cImageUrl = cImageUrl;
        this.description = description;
        this.phone = phone;
        this.user = user;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getcImageUrl() {
        return cImageUrl;
    }

    public void setcImageUrl(String cImageUrl) {
        this.cImageUrl = cImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "cid=" + cid +
                ", cname='" + cname + '\'' +
                ", nickname='" + nickname + '\'' +
                ", work='" + work + '\'' +
                ", email='" + email + '\'' +
                ", cImageUrl='" + cImageUrl + '\'' +
                ", description='" + description + '\'' +
                ", phone='" + phone + '\'' +
                ", user=" + user +
                '}';
    }

}
