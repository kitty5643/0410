/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.MessageUser;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author student
 */
@Service
public class MessageUserService implements UserDetailsService {

    @Autowired
    MessageUserRepository ticketUserRepo;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        MessageUser ticketUser = ticketUserRepo.findByUsername(username);
        if (ticketUser == null) {
            throw new UsernameNotFoundException("User '" + username + "' not found.");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : ticketUser.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return new User(ticketUser.getUsername(), ticketUser.getPassword(), authorities);
    }
}
