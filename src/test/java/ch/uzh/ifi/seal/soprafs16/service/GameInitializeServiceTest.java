package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.TestHelpers;
import ch.uzh.ifi.seal.soprafs16.constant.RoundType;
import ch.uzh.ifi.seal.soprafs16.constant.TreasureType;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Treasure;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.Wagon;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;


public class GameInitializeServiceTest {

    private List<User> players;
    GameInitializeService gameInitializeService;

    @Before
    public void setUp() throws Exception {
        players = new ArrayList<>();
        players.add(new User("Sandro", "sw"));
        players.add(new User("Horst", "ho"));
        players.add(new User("Michi", "mm"));

        gameInitializeService = new GameInitializeService();
    }

    @Test
    public void testCreateTrain() throws Exception {
        //Build train
        List<Wagon> train = new ArrayList<>();
        train.addAll(gameInitializeService.createTrain(this.players));

        //Since the list of players contains 3 players, the train must be of size 4 because one wagon for each player plus the locomotive
        assertEquals(4, train.size());

        //The first wagon is the locomotive and must contain the marshal and a cashbox with value of 1000
        assertTrue(train.get(0).hasMarshal());
        assertEquals(1, train.get(0).getLowerLevel().getTreasures().size());
        assertEquals(1000, train.get(0).getLowerLevel().getTreasures().get(0).getValue());
        assertThat(train.get(0).getLowerLevel().getTreasures().get(0).getTreasureType(), is(TreasureType.CASHBOX));

        //Level must have reference to wagon
        assertNotNull(train.get(0).getLowerLevel().getWagon());

        //No wagon does contain the marshal
        for (int i = 1; i < train.size(); i++) {
            assertFalse(train.get(i).hasMarshal());
        }

        //The lower level of each wagon must have at least one treasure
        for (int i = 1; i < train.size(); i++) {
            Wagon wagon = train.get(i);
            assertThat(wagon.getLowerLevel().getTreasures().size(), is(not(0)));

            //Each treasure must be of type moneybag or diamond
            for (Treasure treasure : wagon.getLowerLevel().getTreasures()) {
                assertThat(treasure.getTreasureType(), anyOf(equalTo(TreasureType.MONEYBAG), equalTo(TreasureType.DIAMOND)));
            }
        }

        //Because there are three players in on this train, two must be in the last wagon and one in the second last
        assertThat(train.get(this.players.size()).getLowerLevel().getUsers().size(), is(2));
        assertThat(train.get(this.players.size() - 1).getLowerLevel().getUsers().size(), is(1));

        players.remove(0);
        train.clear();
        train.addAll(gameInitializeService.createTrain(players));
        assertThat(train.size(), is(4));
    }

    @Test
    public void testGiveUsersTreasure() throws Exception {
        //Give each user a moneybag with value 250
        gameInitializeService.giveUsersTreasure(this.players);
        for (User player : this.players) {
            assertThat(player.getTreasures().size(), is(1));
            assertThat(player.getTreasures().get(0).getTreasureType(), is(equalTo(TreasureType.MONEYBAG)));
            assertThat(player.getTreasures().get(0).getValue(), is(250));
        }
    }

    @Test
    public void testInitializeRounds(){
        Game game = TestHelpers.createGame();
        game.getRounds().addAll(gameInitializeService.initializeRounds(5, game));

        assertThat(game.getRounds().size(), is(5));
        assertThat(game.getRounds().get(0).getFirstPlayer(), is(0));
        assertThat(game.getRounds().get(1).getFirstPlayer(), is(1));
        assertThat(game.getRounds().get(2).getFirstPlayer(), is(2));
        assertThat(game.getRounds().get(3).getFirstPlayer(), is(3));

        game.getRounds().clear();
        game.getRounds().addAll(gameInitializeService.initializeRounds(2, game));
        assertThat(game.getRounds().get(0).getFirstPlayer(), is(0));
        assertThat(game.getRounds().get(1).getFirstPlayer(), is(1));
        assertThat(game.getRounds().get(0).getRoundType(), is(RoundType.BREAK));
        assertThat(game.getRounds().get(1).getRoundType(), is(RoundType.REVENGE_MARSHAL));

    }
}