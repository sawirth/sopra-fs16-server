package ch.uzh.ifi.seal.soprafs16.model.moves;

import ch.uzh.ifi.seal.soprafs16.TestHelpers;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Target;
import ch.uzh.ifi.seal.soprafs16.model.User;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class StealMoveTest {

    @Test
    public void testCalculateTargets() throws Exception {
        Game game = TestHelpers.createGame();
        User player = game.getPlayers().get(0);


        /*
        Wagon 1 upperLevel should be empty
        Wagon 1 lowerLevel should have two moneybags, one diamond and one cashbox
        Wagon 2 upperLevel should have one diamond
         */


        //First test is upperLevel of wagon 1
        StealMove move = new StealMove();
        move.setUser(player);
        move.setGame(game);
        List<Target> targets = move.calculateTargets();
        assertThat(targets.isEmpty(), is(true));

        //Lower level of wagon 1 should have two moneybags, one diamond and one cashbox = 4 treasures
        player = game.getPlayers().get(3);
        move.setUser(player);
        targets = move.calculateTargets();
        assertThat(targets.size(), is(4));
    }
}