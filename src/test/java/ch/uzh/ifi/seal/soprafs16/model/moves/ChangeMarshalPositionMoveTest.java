package ch.uzh.ifi.seal.soprafs16.model.moves;

import ch.uzh.ifi.seal.soprafs16.TestHelpers;
import ch.uzh.ifi.seal.soprafs16.constant.CharacterType;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Target;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ChangeMarshalPositionMoveTest {

    private Game game;
    private ChangeMarshalPositionMove move;

    @Before
    public void before() throws Exception {
        game = TestHelpers.createGame();
        move = new ChangeMarshalPositionMove();
        move.setGame(game);
    }

    @Test
    public void testCalculateTargets() throws Exception {
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

    @Test
    public void testExecuteAction() throws Exception {
        //first wagon has marshal so he can switch from level 1 to 3
        move.setUser(game.getPlayers().get(0));
        move.getUser().setCharacterType(CharacterType.BELLE);
        assertThat(game.getTrain().get(0).hasMarshal(), is(true));

        move.executeAction(game.getTrain().get(1).getLowerLevel());
        assertThat(game.getTrain().get(0).hasMarshal(), is(false));
        assertThat(game.getTrain().get(1).hasMarshal(), is(true));
    }
}