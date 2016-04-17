package ch.uzh.ifi.seal.soprafs16.roundFinisher;

import ch.uzh.ifi.seal.soprafs16.constant.TreasureType;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.model.moves.BlockerMove;
import ch.uzh.ifi.seal.soprafs16.model.roundFinisher.RoundFinisherAngryMarshal;
import ch.uzh.ifi.seal.soprafs16.model.roundFinisher.RoundFinisherBreak;
import ch.uzh.ifi.seal.soprafs16.model.roundFinisher.RoundFinisherCrane;
import ch.uzh.ifi.seal.soprafs16.model.roundFinisher.RoundFinisherTakeAll;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Tests all RoundFinishers and their finishRound methods
 * Created by David on 16.04.2016.
 */
public class RoundFinisherTest {

    private RoundFinisherCrane roundFinisherCrane = new RoundFinisherCrane();
    private RoundFinisherTakeAll roundFinisherTakeAll = new RoundFinisherTakeAll();
    private RoundFinisherBreak roundFinisherBreak = new RoundFinisherBreak();
    private RoundFinisherAngryMarshal roundFinisherAngryMarshal = new RoundFinisherAngryMarshal();

    @Test
    public void testRoundFinisherCrane(){
        Game game = createGame();
        roundFinisherCrane.finishRound(game);
        List<Wagon> train = game.getTrain();

        //no user on upper level of the train except on the last wagon
        for(int i=0;i<train.size()-1;i++){
            Assert.assertThat(train.get(i).getUpperLevel().getUsers(), is(empty()));
        }

        //3 user on the roof of the last wagon
        Assert.assertThat(train.get(train.size()-1).getUpperLevel().getUsers().size(), is(4));

        //user in lower level is not effected
        Assert.assertThat(train.get(1).getLowerLevel().getUsers().size(), is(1));
    }

    @Test
    public void testRoundFinisherTakeAll(){
        Game game = createGame();
        roundFinisherTakeAll.finishRound(game);

        //Marshal is on first wagon, treasuresList should be of size 4
        Assert.assertThat(game.getTrain().get(0).getLowerLevel().getTreasures().size(), is(4));
        Assert.assertThat(game.getTrain().get(0).getLowerLevel().getTreasures().get(3).getType(), is(TreasureType.CASHBOX));
    }

    @Test
    public void testRoundFinisherBreak(){
        Game game = createGame();
        roundFinisherBreak.finishRound(game);

        //one player moved one wagon to the front
        Assert.assertThat(game.getTrain().get(0).getUpperLevel().getUsers().size(), is(3));
        //last wagon has no players on top anymore
        Assert.assertThat(game.getTrain().get(2).getUpperLevel().getUsers().size(), is(0));
    }

    @Test
    public void testRoundFinisherAngryMarshal(){
        Game game = createGame();
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

    private Game createGame(){
        Game game = new Game();
        List<Wagon> train = new ArrayList<>();

        List<User> users1 = new ArrayList<>();
        users1.add(new User("Hans","Hansi"));
        users1.add(new User("Dave","dave"));
        Wagon wagon1 = new Wagon(createTreasures(),true);
        wagon1.getUpperLevel().setUsers(users1);
        train.add(wagon1);

        List<User> users2 = new ArrayList<>();
        users2.add(new User("Wayne","Wayne"));
        List<User> users3 = new ArrayList<>();
        users3.add(new User("Fritz","Fritz"));
        Wagon wagon2 = new Wagon(createTreasures(),false);
        wagon2.getLowerLevel().setUsers(users2);
        wagon2.getUpperLevel().setUsers(users3);
        train.add(wagon2);

        List<User> users4 = new ArrayList<>();
        users4.add(new User("Sigmund","Siggi"));
        Wagon wagon3 = new Wagon(createTreasures(),false);
        wagon3.getUpperLevel().setUsers(users4);
        train.add(wagon3);

        game.setTrain(train);
        return game;
    }

    private List<Treasure> createTreasures(){
        List<Treasure> treasures = new ArrayList<>();
        treasures.add(new Treasure(250, TreasureType.MONEYBAG));
        treasures.add(new Treasure(1000, TreasureType.CASHBOX));
        treasures.add(new Treasure(500, TreasureType.DIAMOND));
        return treasures;
    }
}
