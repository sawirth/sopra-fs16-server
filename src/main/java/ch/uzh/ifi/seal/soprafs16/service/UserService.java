package ch.uzh.ifi.seal.soprafs16.service;


import ch.uzh.ifi.seal.soprafs16.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs16.model.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("userService")
public class UserService {

    public static User login(User user) {
        if (user != null) {
            user.setToken(UUID.randomUUID().toString());
            user.setStatus(UserStatus.ONLINE);
            return user;
        }

        return null;
    }

    public static User logout(User user) {
        if (user != null) {
            user.setToken(UUID.randomUUID().toString());
            user.setStatus(UserStatus.OFFLINE);
            return user;
        }

        return null;
    }


}
