package ch.uzh.ifi.seal.soprafs16.roundFinisher;

import ch.uzh.ifi.seal.soprafs16.TestHelpers;
import ch.uzh.ifi.seal.soprafs16.constant.TreasureType;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Treasure;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.Wagon;
import ch.uzh.ifi.seal.soprafs16.model.roundFinisher.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

/**
 * Tests all RoundFinishers and their finishRound methods
 * Created by David on 16.04.2016.
 */
public class RoundFinisherTest {

    private RoundFinisherCrane roundFinisherCrane = new RoundFinisherCrane();
    private RoundFinisherTakeAll roundFinisherTakeAll = new RoundFinisherTakeAll();
    private RoundFinisherBreak roundFinisherBreak = new RoundFinisherBreak();
    private RoundFinisherAngryMarshal roundFinisherAngryMarshal = new RoundFinisherAngryMarshal();
    private RoundFinisherResistance roundFinisherResistance = new RoundFinisherResistance();
    private RoundFinisherHostage roundFinisherHostage = new RoundFinisherHostage();
    private RoundFinisherRevengeMarshal roundFinisherRevengeMarshal = new RoundFinisherRevengeMarshal();
    private RoundFinisherPickPocketing roundFinisherPickPocketing = new RoundFinisherPickPocketing();

    @Test
    public void testRoundFinisherCrane(){
        Game game = TestHelpers.createGame();
        roundFinisherCrane.finishRound(game);
        List<Wagon> train = game.getTrain();

        //no user on upper level of the train except on the last wagon
        for(int i=0;i<train.size()-1;i++){
            Assert.assertThat(train.get(i).getUpperLevel().getUsers(), is(empty()));
        }

        //3 user on the roof of the last wagon
        Assert.assertThat(train.get(train.size()-1).getUpperLevel().getUsers().size(), is(5));

        //user in lower level is not effected
        Assert.assertThat(train.get(1).getLowerLevel().getUsers().size(), is(1));
    }

    @Test
    public void testRoundFinisherTakeAll(){
        Game game = TestHelpers.createGame();
        roundFinisherTakeAll.finishRound(game);

        //Marshal is on first wagon, treasuresList should be of size 5
        Assert.assertThat(game.getTrain().get(0).getLowerLevel().getTreasures().size(), is(5));
        Assert.assertThat(game.getTrain().get(0).getLowerLevel().getTreasures().get(4).getTreasureType(), is(TreasureType.CASHBOX));
    }

    @Test
    public void testRoundFinisherBreak(){
        Game game = TestHelpers.createGame();
        roundFinisherBreak.finishRound(game);

        //one player moved one wagon to the front
        Assert.assertThat(game.getTrain().get(0).getUpperLevel().getUsers().size(), is(4));
        //last wagon has no players on top anymore
        Assert.assertThat(game.getTrain().get(2).getUpperLevel().getUsers().size(), is(0));
    }

    @Test
    public void testRoundFinisherAngryMarshal(){
        Game game = TestHelpers.createGame();
        roundFinisherAngryMarshal.finishRound(game);

        //Every user on first wagon gets an BlockerMove
        List<User> users = game.getTrain().get(0).getUpperLevel().getUsers();
        for(User user: users){
            Assert.assertThat(user.getDeckCards().size(), is(1));
        }

        //Marshal is on second wagon now and not on first
        Assert.assertFalse(game.getTrain().get(0).hasMarshal());
        Assert.assertTrue(game.getTrain().get(1).hasMarshal());
    }

    @Test
    public void testRoundFinisherResistance(){
        Game game = TestHelpers.createGame();
        roundFinisherResistance.finishRound(game);

        //every user on a lower level got a BlockerMove
        for(Wagon wagon: game.getTrain()){
            for(User user: wagon.getLowerLevel().getUsers()){
                Assert.assertThat(user.getDeckCards().size(), is(1));
            }
        }

        //no user got a BlockerMove on the upper level
        for(Wagon wagon: game.getTrain()){
            for(User user: wagon.getUpperLevel().getUsers()){
                Assert.assertThat(user.getDeckCards().size(), is(0));
            }
        }
    }

    @Test
    public void testRoundFinisherHostage(){
        Game game = TestHelpers.createGame();
        roundFinisherHostage.finishRound(game);

        //every user in the first wagon got a treasure
        for(User user: game.getTrain().get(0).getLowerLevel().getUsers()){
            Assert.assertThat(user.getTreasures().size(), is(1));
        }
        for(User user: game.getTrain().get(0).getUpperLevel().getUsers()){
            Assert.assertTrue(user.getTreasures().size()==2 || user.getTreasures().size()==5);
        }
    }

    @Test
    public void testRoundFinisherRevengeMarshal(){
        Game game = TestHelpers.createGame();
        roundFinisherRevengeMarshal.finishRound(game);

        for(User user: game.getTrain().get(0).getUpperLevel().getUsers()){
            Assert.assertTrue(user.getTreasures().size()==1 || user.getTreasures().size()==3);
        }
        for(Treasure treasure: game.getTrain().get(0).getUpperLevel().getUsers().get(0).getTreasures()){
            Assert.assertTrue(treasure.getValue()!=250);
        }

    }

    @Test
    public void testRoundFinisherPickPocketing(){
        Game game = TestHelpers.createGame();
        roundFinisherPickPocketing.finishRound(game);
        //no user on the lower level had an treasure and is alone
        for(Wagon wagon: game.getTrain()){
            for(User user: wagon.getLowerLevel().getUsers()){
                Assert.assertThat(user.getTreasures().size(), is(1));
            }
        }
        //users on the the first wagon aren't affected since they are together on a level
        for(User user: game.getTrain().get(0).getUpperLevel().getUsers()){
            Assert.assertTrue(user.getTreasures().size()==1 ||
                    user.getTreasures().size()==4);
        }

    }
}
