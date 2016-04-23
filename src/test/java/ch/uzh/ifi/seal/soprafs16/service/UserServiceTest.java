package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.User;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class UserServiceTest {

    private UserService userService;

    @Before
    public void setUp() throws Exception {
        userService = new UserService();
    }

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

    @Test
    public void testFindGameOfUser() throws Exception {
        User user = new User("Sandro", "sw");

        for (int i = 0; i < 5; i++) {
            Game game = new Game();
            game.setStatus(GameStatus.FINISHED);
            user.getGames().add(game);
            assertNull(userService.findRunningGame(user));
        }

        Game game = new Game();
        game.setStatus(GameStatus.RUNNING);
        user.getGames().add(game);

        assertThat(userService.findRunningGame(user), is(game));
    }
}