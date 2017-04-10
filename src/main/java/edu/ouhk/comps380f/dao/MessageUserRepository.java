/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.MessageUser;
import java.util.List;

/**
 *
 * @author student
 */
public interface MessageUserRepository {

    public void create(MessageUser user);

    public List<MessageUser> findAll();

    public MessageUser findByUsername(String username);

    public void deleteByUsername(String username);
}
