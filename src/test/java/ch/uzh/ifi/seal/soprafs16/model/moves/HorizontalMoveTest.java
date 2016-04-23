package ch.uzh.ifi.seal.soprafs16.model.moves;

import ch.uzh.ifi.seal.soprafs16.TestHelpers;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Target;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HorizontalMoveTest {

    @Test
    public void testCalculateTargets() throws Exception {
        Game game = TestHelpers.createGame();
        HorizontalMove move = new HorizontalMove();
        List<Target> targets = new ArrayList<>();

        move.setGame(game);
        move.setUser(game.getPlayers().get(0));

        //Hans: First user is on the upper level of the first waggon so he can go on wagon 2 and 3
        targets = move.calculateTargets();
        assertThat(targets.size(), is(2));
        assertThat(targets.get(0).getId(), is(4L));
        assertThat(targets.get(1).getId(), is(6L));

        //Sevi: 4th user is on lower level of the first wagon so he can only go to the second wagon
        move.setUser(game.getPlayers().get(3));
        targets = move.calculateTargets();
        assertThat(targets.size(), is(1));
        assertThat(targets.get(0).getId(), is(3L));

        //Wayne: 5th user is on lower level of second wagon so he can go to the 1st or 3rd wagon
        move.setUser(game.getPlayers().get(4));
        targets = move.calculateTargets();
        assertThat(targets.size(), is(2));
        assertThat(targets.get(0).getId(), is(5L));
        assertThat(targets.get(1).getId(), is(1L));

        //Fritz: 6th user is on upper level of second wagon so he can go to the 1st or 3rd wagon
        move.setUser(game.getPlayers().get(5));
        targets = move.calculateTargets();
        assertThat(targets.size(), is(2));
        assertThat(targets.get(0).getId(), is(6L));
        assertThat(targets.get(1).getId(), is(2L));

        //Sigmund: 7th user is on upper level of last wagon so he can go to wagon 1 and 2
        move.setUser(game.getPlayers().get(6));
        targets = move.calculateTargets();
        assertThat(targets.size(), is(2));
        assertThat(targets.get(0).getId(), is(4L));
        assertThat(targets.get(1).getId(), is(2L));

        //John: 8th user is on lower level of last wagon so he can go to wagon 2
        move.setUser(game.getPlayers().get(7));
        targets = move.calculateTargets();
        assertThat(targets.size(), is(1));
        assertThat(targets.get(0).getId(), is(3L));
    }
}