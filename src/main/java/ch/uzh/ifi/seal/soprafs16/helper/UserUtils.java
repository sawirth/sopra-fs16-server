package ch.uzh.ifi.seal.soprafs16.helper;

import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.User;

public class UserUtils {

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
