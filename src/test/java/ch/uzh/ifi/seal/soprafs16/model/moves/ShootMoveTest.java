package ch.uzh.ifi.seal.soprafs16.model.moves;

import ch.uzh.ifi.seal.soprafs16.TestHelpers;
import ch.uzh.ifi.seal.soprafs16.constant.CharacterType;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Target;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class ShootMoveTest {

    @Test
    public void testCalculateTargets() throws Exception {
        Game game = TestHelpers.createGame();
        ShootMove move = new ShootMove();
        move.setGame(game);
        List<Target> targets = new ArrayList<>();

        //Hans(1): can shoot on Fritz(6)
        move.setUser(game.getPlayers().get(0));
        targets = move.calculateTargets();
        assertThat(targets.size(), is(1));
        assertThat(targets.get(0).getId(), is(6L));

        //Sevi(4): can shoot on Wayne(5)
        move.setUser(game.getPlayers().get(3));
        targets = move.calculateTargets();
        assertThat(targets.size(), is(1));
        assertThat(targets.get(0).getId(), is(5L));

        //Wayne(5): can shoot on Sevi(4) and John(8)
        move.setUser(game.getPlayers().get(4));
        targets = move.calculateTargets();
        assertThat(targets.size(), is(2));
        assertThat(targets.get(0).getId(), is(4L));
        assertThat(targets.get(1).getId(), is(8L));

        //Fritz(6): can shoot on Hans(1), Dave(2), Sandro(3) and Sigmund(7)
        move.setUser(game.getPlayers().get(5));
        targets = move.calculateTargets();
        assertThat(targets.size(), is(4));

        //Special case Belle with Fritz(6): can shoot on Hans(1), Dave(2) and Sandro(3) as Sigmund plays as Belle
        game.getPlayers().get(6).setCharacterType(CharacterType.BELLE);
        targets = move.calculateTargets();
        assertThat(targets.size(), is(3));

        //Sigmund(7): can shoot on Fritz(6)
        move.setUser(game.getPlayers().get(6));
        targets = move.calculateTargets();
        assertThat(targets.size(), is(1));
        assertThat(targets.get(0).getId(), is(6L));

        //Sigmund(7) as Tuco: can shoot on Fritz(6) and John(8)
        move.getUser().setCharacterType(CharacterType.TUCO);
        targets = move.calculateTargets();
        assertThat(targets.size(), is(2));
        assertThat(targets.get(1).getId(), is(8L));

        //John(8): can shoot on Wayne(5)
        move.setUser(game.getPlayers().get(7));
        targets = move.calculateTargets();
        assertThat(targets.size(), is(1));
        assertThat(targets.get(0).getId(), is(5L));

        //John(8) as Tuco: can shoot on Wayne(5) and Sigmund(7)
        move.getUser().setCharacterType(CharacterType.TUCO);
        targets = move.calculateTargets();
        assertThat(targets.size(), is(2));
        assertThat(targets.get(1).getId(), is(7L));
    }
}