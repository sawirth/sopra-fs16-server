package ch.uzh.ifi.seal.soprafs16.service;


import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs16.model.Game;
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

    public Game findRunningGame(User user){
        if (user.getGames() == null || user.getGames().isEmpty()) {
            return null;
        }

        for (int i=0; i<user.getGames().size(); i++) {
            Game game = user.getGames().get(i);
            if (game.getStatus() == GameStatus.RUNNING) {
                return game;
            }
        }

        return null;
    }

    public Game findPendingGame(User user) {
        if (user.getGames() == null || user.getGames().isEmpty()) {
            return null;
        }

        for (int i=0; i<user.getGames().size(); i++) {
            Game game = user.getGames().get(i);
            if (game.getStatus() == GameStatus.PENDING) {
                return game;
            }
        }

        return null;
    }


    /**
     * Returns true if the User is currently in a running or pending game
     * @param user
     * @return
     */
    public static boolean isInOpenGame(User user) {
        for (Game game : user.getGames()) {
            if (game.getStatus() == GameStatus.RUNNING || game.getStatus() == GameStatus.PENDING) {
                return true;
            }
        }
        return false;
    }
}
