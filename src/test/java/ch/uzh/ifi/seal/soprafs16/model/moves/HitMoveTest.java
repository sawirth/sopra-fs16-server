package ch.uzh.ifi.seal.soprafs16.model.moves;

import ch.uzh.ifi.seal.soprafs16.TestHelpers;
import ch.uzh.ifi.seal.soprafs16.constant.CharacterType;
import ch.uzh.ifi.seal.soprafs16.helper.TargetHelper;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Level;
import ch.uzh.ifi.seal.soprafs16.model.Target;
import ch.uzh.ifi.seal.soprafs16.model.User;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class HitMoveTest {

    @Test
    public void testCalculateUserTargets() throws Exception {
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

    @Test
    public void testCalculateTreasureTargets(){
        Game game = TestHelpers.createGame();
        HitMove move = new HitMove();
        move.setUserTarget(game.getPlayers().get(0));
        move.setGame(game);

        //since its in the second phase
        move.setPhaseOfMove(1);
        List<Target> targets = move.calculateTargets();
        assertThat(targets.size(), is(4));
        assertThat(move.getPhaseOfMove(), is(1));

        //test when a player has no treasures
        game.getPlayers().get(0).getTreasures().clear();
        List<Target> targets1 = move.calculateTargets();

        //must be one since the user can choose one level he wants the other player move to
        assertThat(targets1.size(), is(1));
        assertThat(targets1.get(0), is(game.getTrain().get(1).getUpperLevel()));
        assertThat(move.getPhaseOfMove(), is(2));
    }

    @Test
    public void testCalculateLevelTargets(){
        Game game = TestHelpers.createGame();
        HitMove move1 = new HitMove();
        move1.setGame(game);
        move1.setPhaseOfMove(2);
        move1.setUserTarget(game.getPlayers().get(0));
        List<Target> targets = move1.calculateTargets();

        //can only be one wagon since the player that got hit is on the locomotive
        assertThat(targets.size(), is(1));
        assertThat(targets.get(0), is(game.getTrain().get(1).getUpperLevel()));

        //another test with two possible targets,adds another user
        User user = new User("Hanno", "hanno");
        user.setId(9L);
        game.getPlayers().add(user);
        game.getTrain().get(1).getLowerLevel().getUsers().add(user);

        HitMove move2 = new HitMove();
        move2.setUserTarget(user);
        move2.setPhaseOfMove(2);
        move2.setGame(game);
        targets = move2.calculateTargets();

        assertThat(targets.size(), is(2));
        assertThat(targets.get(0), is(game.getTrain().get(2).getLowerLevel()));
        assertThat(targets.get(1), is(game.getTrain().get(0).getLowerLevel()));

        //another test with one possible targets at the end of the train,adds another user
        User user2 = new User("Hanno", "hanno");
        user2.setId(10L);
        game.getPlayers().add(user2);
        game.getTrain().get(3).getLowerLevel().getUsers().add(user2);

        HitMove move3 = new HitMove();
        move3.setUserTarget(user2);
        move3.setPhaseOfMove(2);
        move3.setGame(game);
        targets = move3.calculateTargets();

        assertThat(targets.size(), is(1));
        assertThat(targets.get(0), is(game.getTrain().get(2).getLowerLevel()));
    }

    @Test
    public void testExecuteActionChooseTreasure(){
        Game game = TestHelpers.createGame();
        HitMove move = new HitMove();
        User user = new User("Louisa","Lou");
        user.setId(9L);
        user.setCharacterType(CharacterType.DOC);
        move.setUser(user);
        move.setGame(game);
        move.setPhaseOfMove(1);
        move.setUserTarget(game.getPlayers().get(0));
        move.executeAction(game.getPlayers().get(0).getTreasures().get(0));
        Level levelOfTarget = game.getTrain().get(0).getUpperLevel();

        assertThat(levelOfTarget.getTreasures().size(), is(1));
        assertThat(levelOfTarget.getTreasures().get(0).getValue(), is(300));
        assertThat(game.getPlayers().get(0).getTreasures().size(), is(3));

        //test special ability of cheyenne
        user.setCharacterType(CharacterType.CHEYENNE);
        move.executeAction(game.getPlayers().get(0).getTreasures().get(0));

        assertThat(levelOfTarget.getTreasures().size(), is(1));
        assertThat(user.getTreasures().size(), is(1));
        assertThat(user.getTreasures().get(0).getValue(), is(250));
        assertThat(game.getPlayers().get(0).getTreasures().size(), is(2));
    }
}