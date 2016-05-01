package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.TestHelpers;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class GameServiceTest {

    private Game game;
    private GameService gameService;
    private User user;

    @Before
    public void before() {
        game = TestHelpers.createGame();
        gameService = new GameService();
    }

    @Test
    public void testSwitchLevel() throws Exception {
        user = game.getPlayers().get(0);
        gameService.switchLevel(game.getTrain(), game.getTrain().get(1).getUpperLevel(), user);
        assertThat(game.getTrain().get(1).getUpperLevel().getUsers().contains(user), is(true));
    }
}