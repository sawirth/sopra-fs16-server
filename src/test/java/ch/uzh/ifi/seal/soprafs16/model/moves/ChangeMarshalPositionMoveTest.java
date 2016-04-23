package ch.uzh.ifi.seal.soprafs16.model.moves;

import ch.uzh.ifi.seal.soprafs16.TestHelpers;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Target;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class ChangeMarshalPositionMoveTest {

    @Test
    public void testCalculateTargets() throws Exception {
        Game game = TestHelpers.createGame();
        ChangeMarshalPositionMove move = new ChangeMarshalPositionMove();
        move.setGame(game);
        List<Target> targets;

        //First wagon has marshal
        targets = move.calculateTargets();
        assertThat(targets.get(0).getId(), is(3L));

        //Middle wagon has marshal
        game.getTrain().get(0).setHasMarshal(false);
        game.getTrain().get(1).setHasMarshal(true);
        targets = move.calculateTargets();
        assertThat(targets.get(0).getId(), is(1L));
        assertThat(targets.get(1).getId(), is(5L));

        //Last wagon has marshal
        game.getTrain().get(1).setHasMarshal(false);
        game.getTrain().get(2).setHasMarshal(true);
        targets = move.calculateTargets();
        assertThat(targets.get(0).getId(), is(3L));
    }
}