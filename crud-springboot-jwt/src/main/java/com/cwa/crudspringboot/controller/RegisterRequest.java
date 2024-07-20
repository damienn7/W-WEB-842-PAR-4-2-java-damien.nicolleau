package com.cwa.crudspringboot.controller;

import org.springframework.stereotype.Controller;

import com.cwa.crudspringboot.entity.Card;

@Controller
public class RegisterRequest {
    private String username;
    private String password;
    private String role;
    private String displayName;
    private String url;
    private String color;
    private String location;
    private String contact;
    private Boolean isConfigured;
    private Card card;

    public RegisterRequest() {}

    public RegisterRequest(String username, String password, String displayName, String url, String color, String location, String contact,Card card) {
        this.username = username;
        this.password = password;
        this.role = "ROLE_USER";
        this.card = card;
        this.displayName = displayName;
        this.url = url;
        this.color = color;
        this.location = location;
        this.contact = contact;
        this.isConfigured = false;
        this.card = card;
    }

    public String getUsername() {
        return username;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Boolean getIsConfigured() {
        return isConfigured;
    }

    public void setIsConfigured(Boolean isConfigured) {
        this.isConfigured = isConfigured;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    
}
