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

    @Test
    public void testExecuteTargets() throws Exception {
        Game game = TestHelpers.createGame();
        User player = game.getPlayers().get(3);
        player.setCharacterType(CharacterType.DOC);

        StealMove move = new StealMove();
        move.setUser(player);
        move.setGame(game);

        List<Target> targets = move.calculateTargets();
        move.executeAction(targets.get(1));

        //user is on lower level in first wagon, this level had 4 treasures now the moneybag with value 250 is going to be removed
        assertThat(game.getTrain().get(0).getLowerLevel().getTreasures().size(), is(3));
        //those Treasures are still in the list
        assertThat(game.getTrain().get(0).getLowerLevel().getTreasures().get(0).getValue(), is(300));
        assertThat(game.getTrain().get(0).getLowerLevel().getTreasures().get(1).getValue(), is(1000));
        assertThat(game.getTrain().get(0).getLowerLevel().getTreasures().get(2).getValue(), is(500));

        //the player now has one treasure with value 250
        assertThat(player.getTreasures().size(), is(1));
        assertThat(player.getTreasures().get(0).getValue(), is(250));

        move.executeAction(targets.get(0));
        move.executeAction(targets.get(2));
        move.executeAction(targets.get(3));
        assertThat(player.getTreasures().size(), is(4));
    }
}