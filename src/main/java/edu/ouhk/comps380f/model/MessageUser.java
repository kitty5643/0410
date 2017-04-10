/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ouhk.comps380f.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author student
 */
public class MessageUser {

    private String username;
    private String password;
    private List<String> roles = new ArrayList<>();

    // ... getters and setters for username and password

    public String getUsername() {
        return username;
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

    public List<String> getRoles() {
        return roles;
    }

    public void addRole(String role) {
        this.roles.add(role);
    }

    public boolean hasRole(String role) {
        return this.roles.contains(role);
    }
}
