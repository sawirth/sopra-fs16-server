package ch.uzh.ifi.seal.soprafs16.model.moves;

import ch.uzh.ifi.seal.soprafs16.TestHelpers;
import ch.uzh.ifi.seal.soprafs16.constant.CharacterType;
import ch.uzh.ifi.seal.soprafs16.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ShootMoveTest {

    private Game game;
    private Move move;
    private List<Target> targets;

    @Before
    public void resetGame() {
                /*
        This creates a train with the following setup

        Hans(1), Dave(2), Sandro(3)         Fritz(6)                              Sigmund(7)
        --------------------------    ----------------------    -------------    ----------------

               Sevi(4)                       Wayne(5)                               John(8)
        --------------------------    ----------------------    -------------    ----------------


         The number in the brackets is the ID and also the position in game.getPlayers()

         */

        game = TestHelpers.createGame();
        move = new ShootMove();
        move.setGame(game);
        targets = new ArrayList<>();
    }

    @Test
    public void testUpperLevelLoc() throws Exception {
        //Hans(1) can shoot on Fritz
        move.setUser(game.getPlayers().get(0));
        targets = move.calculateTargets();
        assertThat(targets.size(), is(1));
        assertThat(targets.get(0).getId(), is(6L));
    }

    @Test
    public void testLowerLevelLoc() throws Exception {
        //Sevi(4): can shoot on Wayne(5)
        move.setUser(game.getPlayers().get(3));
        targets = move.calculateTargets();
        assertThat(targets.size(), is(1));
        assertThat(targets.get(0).getId(), is(5L));
    }

    @Test
    public void testUpperLevelMiddle() throws Exception {
        //Fritz(6): can shoot on Hans(1), Dave(2), Sandro(3) and Sigmund(7)
        move.setUser(game.getPlayers().get(5));
        targets = move.calculateTargets();
        assertThat(targets.size(), is(4));
    }

    @Test
    public void testLowerLevelMiddle() throws Exception {
        //Wayne(5) can shoot on Sevi(4)
        move.setUser(game.getPlayers().get(4));
        targets = move.calculateTargets();
        assertThat(targets.size(), is(1));
        assertThat(targets.get(0).getId(), is(4L));
    }

    @Test
    public void testUpperLevelEnd() throws Exception {
        //Sigmund(7) can shoot on Fritz(6)
        move.setUser(game.getPlayers().get(6));
        targets = move.calculateTargets();
        assertThat(targets.size(), is(1));
        assertThat(targets.get(0).getId(), is(6L));
    }

    @Test
    public void testLowerLevelEnd() throws Exception {
        //John(8) can shoot on no one since in the wagon before is empty on the lower level
        move.setUser(game.getPlayers().get(7));
        targets = move.calculateTargets();
        assertThat(targets.isEmpty(), is(true));
    }

    @Test
    public void testTucoSpecialAbilityDownwards() throws Exception {
        //Hans(1) can shoot on Fritz(6) and also on Sevi(4) below
        move.setUser(game.getPlayers().get(1));
        move.getUser().setCharacterType(CharacterType.TUCO);
        targets = move.calculateTargets();
        assertThat(targets.size(), is(2));
        assertThat(targets.get(0).getId(), is(6L));
        assertThat(targets.get(1).getId(), is(4L));
    }

    @Test
    public void testTucoSpecialAbilityUpwards() throws Exception {
        //John(8) as Tuco: can shoot on Sigmund(7) who is above him
        move.setUser(game.getPlayers().get(7));
        move.getUser().setCharacterType(CharacterType.TUCO);
        targets = move.calculateTargets();
        assertThat(targets.size(), is(1));
        assertThat(targets.get(0).getId(), is(7L));
    }

    @Test
    public void testBelleSpecialAbility() throws Exception {
        /*
        Hans(1) now plays at Belle which means Fritz(6) can no longer shoot
        on him because there two other players on the same level
        he can only shoot on Dave(2), Sandro(3) and Sigmund(7)
        */
        move.setUser(game.getPlayers().get(5));
        game.getPlayers().get(0).setCharacterType(CharacterType.BELLE);
        targets = move.calculateTargets();
        assertThat(targets.size(), is(3));
    }

    @Test
    public void testUpperLevelEmptyWagonBetween() throws Exception {
        //When we remove Fritz(6) from the second wagon, Sigmund(7) is then
        // able to shoot on Hans(1), Dave(2) and Sandro(3)
        move.setUser(game.getPlayers().get(6));
        game.getTrain().get(1).getUpperLevel().getUsers().clear();
        targets = move.calculateTargets();
        assertThat(targets.size(), is(3));

        //if we now change the user to Sandro(3), the only target must be Sigmund(7)
        move.setUser(game.getPlayers().get(2));
        targets = move.calculateTargets();
        assertThat(targets.size(), is(1));
        assertThat(targets.get(0).getId(), is(7L));
    }

    @Test
    public void testMiddleEmptyWagonLeft() throws Exception {
        //first we need to add an empty wagon on second position
        Wagon wagon = new Wagon(new ArrayList<>(), false);
        game.getTrain().add(1, wagon);

        //now Fritz(6) can still shoot on Hans(1), Dave(2), Sandro(3) and Sigmund(7)
        move.setUser(game.getPlayers().get(5));
        targets = move.calculateTargets();
        assertThat(targets.size(), is(4));

        //Wayne(5) on the lowerLevel can now shoot on noone since both the wagon before and after him are empty
        move.setUser(game.getPlayers().get(4));
        targets = move.calculateTargets();
        assertThat(targets.isEmpty(), is(true));
    }

    @Test
    public void testExecuteAction() throws Exception {
        User shooter = new User("Shooter", "shooter");
        shooter.setNumberOfShots(6);
        User victim = new User("Victim", "victim");
        victim.setShotsTaken(0);
        move.setUser(shooter);
        move.executeAction(victim);

        assertThat(shooter.getNumberOfShots(), is(5));
        assertThat(victim.getShotsTaken(), is(1));
    }

    @Test
    public void testDjangoShootLeft() throws Exception {
        //Sigmund(7) schiesst als Django auf Fritz(6), d.h. er muss danach auf Level 2 sein
        move.setUser(game.getPlayers().get(6));
        move.getUser().setCharacterType(CharacterType.DJANGO);
        Target victim = game.getPlayers().get(5);
        move.executeAction(victim);

        assertThat(game.getTrain().get(0).getUpperLevel().getUsers().contains(victim), is(true));
    }

    @Test
    public void testDjangoShootRight() throws Exception {
        //Hans(1) schiesst als Django auf Fritz(6), d.h. er muss danach auf Level 6 sein
        move.setUser(game.getPlayers().get(0));
        move.getUser().setCharacterType(CharacterType.DJANGO);
        User victim = game.getPlayers().get(5);
        move.executeAction(victim);

        assertThat(game.getTrain().get(2).getUpperLevel().getUsers().contains(victim), is(true));
    }

    @Test
    public void testDjangoShootBegin() throws Exception {
        //Fritz(6) schiesst als Django auf Hans(1). Da dieser auf dem ersten Wagon ist, wird er nicht verschoben
        move.setUser(game.getPlayers().get(5));
        move.getUser().setCharacterType(CharacterType.DJANGO);
        User victim = game.getPlayers().get(0);
        move.executeAction(victim);

        assertThat(game.getTrain().get(0).getUpperLevel().getUsers().contains(victim), is(true));
    }

    @Test
    public void testDjangoShootEnd() throws Exception {
        //Fritz(6) schiesst als Django auf Sigmund(7). Da dieser auf dem letzten Wagon ist, wird er nicht verschoben
        move.setUser(game.getPlayers().get(5));
        move.getUser().setCharacterType(CharacterType.DJANGO);
        User victim = game.getPlayers().get(6);
        move.executeAction(victim);

        assertThat(game.getTrain().get(3).getUpperLevel().getUsers().contains(victim), is(true));
    }

    @Test
    public void testDjangoShootLeftLower() throws Exception {
        //John(8) schiesst als Django auf Wayne(5), d.h. er muss danach im unteren Level des ersten Wagons sein
        move.setUser(game.getPlayers().get(7));
        move.getUser().setCharacterType(CharacterType.DJANGO);
        User victim = game.getPlayers().get(4);
        game.getTrain().get(0).setHasMarshal(false);
        move.executeAction(victim);

        assertThat(game.getTrain().get(0).getLowerLevel().getUsers().contains(victim), is(true));
    }

    @Test
    public void testDjangoShootRightLower() throws Exception {
        //Sevi(4) schiesst als Django auf Wayne(5) d.h. er muss danach im dritten Wagons auf dem unteren Level sein
        move.setUser(game.getPlayers().get(3));
        move.getUser().setCharacterType(CharacterType.DJANGO);
        User victim = game.getPlayers().get(4);
        move.executeAction(victim);

        assertThat(game.getTrain().get(2).getLowerLevel().getUsers().contains(victim), is(true));
    }
}