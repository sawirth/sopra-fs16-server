package ch.uzh.ifi.seal.soprafs16.model.moves;

import ch.uzh.ifi.seal.soprafs16.TestHelpers;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Target;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class VerticalMoveTest {

    @Test
    public void testCalculateTargets() throws Exception {
        Game game = TestHelpers.createGame();
        VerticalMove move = new VerticalMove();
        List<Target> targets = new ArrayList<>();
        move.setGame(game);

        //Hans(1): can switch to lower level of wagon 1 (id = 1)
        move.setUser(game.getPlayers().get(0));
        targets = move.calculateTargets();
        assertThat(targets.get(0).getId(), is(1L));

        //Wayne(5): can swtich to upper level of wagon 2 (id = 4)
        move.setUser(game.getPlayers().get(4));
        targets = move.calculateTargets();
        assertThat(targets.get(0).getId(), is(4L));
    }
}