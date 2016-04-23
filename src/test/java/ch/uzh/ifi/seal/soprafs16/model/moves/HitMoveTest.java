package ch.uzh.ifi.seal.soprafs16.model.moves;

import ch.uzh.ifi.seal.soprafs16.TestHelpers;
import ch.uzh.ifi.seal.soprafs16.constant.CharacterType;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Target;
import ch.uzh.ifi.seal.soprafs16.model.User;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HitMoveTest {

    @Test
    public void testCalculateTargets() throws Exception {
        Game game = TestHelpers.createGame();
        User player = game.getPlayers().get(0);
        HitMove move = new HitMove();

        //Hans(1): can hit Dave(1) and Sandro(3)
        move.setUser(player);
        move.setGame(game);
        List<Target> targets = move.calculateTargets();
        assertThat(targets.size(), is(2));

        //Hans(1): can only hit Dave(1) if Sandro(3) plays as Belle
        game.getPlayers().get(2).setCharacterType(CharacterType.BELLE);
        targets = move.calculateTargets();
        assertThat(targets.size(), is(1));
        assertThat(targets.get(0).getId(), is(2L));

        //Wayne(5): no targets as he's alone on the lower level of the second wagon
        move.setUser(game.getPlayers().get(4));
        targets = move.calculateTargets();
        assertThat(targets.isEmpty(), is(true));
    }
}