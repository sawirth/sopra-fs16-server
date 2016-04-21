package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs16.model.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserServiceTest {


    @Test
    public void testLogin() throws Exception {
        User user = new User("sandro", "sw");
        user.setStatus(UserStatus.OFFLINE);
        UserService.login(user);
        assertEquals(UserStatus.ONLINE, user.getStatus());
        assertNotEquals(UserStatus.OFFLINE, user.getStatus());
    }

    @Test
    public void testLogout() throws Exception {
        User user = new User("sandro", "sw");
        user.setStatus(UserStatus.ONLINE);
        UserService.logout(user);
        assertEquals(UserStatus.OFFLINE, user.getStatus());
        assertNotEquals(UserStatus.ONLINE, user.getStatus());
    }
}