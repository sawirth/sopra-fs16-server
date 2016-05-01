package ch.uzh.ifi.seal.soprafs16.helper;

import ch.uzh.ifi.seal.soprafs16.TestHelpers;
import ch.uzh.ifi.seal.soprafs16.constant.CharacterType;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Target;
import ch.uzh.ifi.seal.soprafs16.model.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class TargetHelperTest {

    @Test
    public void testGetWagonPositionOfUser() throws Exception {
        Game game = TestHelpers.createGame();

        //Hans(1) is on first wagon
        User user = game.getPlayers().get(0);
        assertThat(TargetHelper.getWagonPositionOfUser(user, game.getTrain()), is(0));

        //Fritz(6) is on second wagon
        user = game.getPlayers().get(5);
        assertThat(TargetHelper.getWagonPositionOfUser(user, game.getTrain()), is(1));

        //John(8) is on fourth wagon
        user = game.getPlayers().get(7);
        assertThat(TargetHelper.getWagonPositionOfUser(user, game.getTrain()), is(3));
    }

    @Test
    public void testIsOnUpperLevel() throws Exception {
        Game game = TestHelpers.createGame();

        //Hans(1) is on upper level
        assertThat(TargetHelper.isOnUpperLevel(game.getPlayers().get(0), game.getTrain()), is(true));

        //Wayne(5) is on lower level
        assertThat(TargetHelper.isOnUpperLevel(game.getPlayers().get(4), game.getTrain()), is(false));
    }

    @Test
    public void testRemoveBelle() throws Exception {
        User user1 = new User("1", "1");
        user1.setCharacterType(CharacterType.TUCO);
        User user2 = new User("2", "2");
        user2.setCharacterType(CharacterType.GHOST);
        User user3 = new User("3", "3");
        user3.setCharacterType(CharacterType.BELLE);
        User user4 = new User("4", "4");
        user4.setCharacterType(CharacterType.CHEYENNE);
        User user5 = new User("5", "5");
        user5.setCharacterType(CharacterType.DOC);

        List<Target> targets = new ArrayList<>();
        targets.add(user1);
        targets.add(user2);
        targets.add(user3);
        targets.add(user4);
        targets.add(user5);

        assertThat(targets.size(), is(5));
        targets = TargetHelper.removeBelle(targets);
        assertThat(targets.size(), is(4));
    }
}