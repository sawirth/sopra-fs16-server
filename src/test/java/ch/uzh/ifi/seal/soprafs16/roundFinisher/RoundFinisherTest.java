package ch.uzh.ifi.seal.soprafs16.roundFinisher;

import ch.uzh.ifi.seal.soprafs16.constant.TreasureType;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.model.roundFinisher.RoundFinisherCrane;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by David on 16.04.2016.
 */
public class RoundFinisherTest {

    private RoundFinisherCrane roundFinisherCrane = new RoundFinisherCrane();

    @Test
    public void testRoundFinisherCrane(){
        Game game = createGame();
        roundFinisherCrane.finishRound(game);
        List<Wagon> train = game.getTrain();

        //no user on upper level of the train except on the last wagon
        for(int i=0;i<train.size()-1;i++){
            Assert.assertNull(train.get(i).getUpperLevel().getUsers());
        }

        //3 user on the roof of the last wagon
        Assert.assertThat(train.get(train.size()-1).getUpperLevel().getUsers().size(), is(3));

        //user in lower level is not effected
        Assert.assertThat(train.get(1).getLowerLevel().getUsers().size(), is(1));
    }

    private Game createGame(){
        Game game = new Game();
        List<Wagon> train = new ArrayList<>();
        List<Treasure> treasures = new ArrayList<>();
        treasures.add(new Treasure(250, TreasureType.MONEYBAG));
        treasures.add(new Treasure(1000, TreasureType.CASHBOX));
        treasures.add(new Treasure(500, TreasureType.DIAMOND));

        List<User> users1 = new ArrayList<>();
        users1.add(new User());
        users1.add(new User());
        Wagon wagon1 = new Wagon(treasures,false);
        wagon1.getUpperLevel().setUsers(users1);
        train.add(wagon1);

        List<User> users2 = new ArrayList<>();
        users2.add(new User());
        Wagon wagon2 = new Wagon(treasures,false);
        wagon2.getLowerLevel().setUsers(users2);
        train.add(wagon2);

        List<User> users3 = new ArrayList<>();
        users3.add(new User());
        Wagon wagon3 = new Wagon(treasures,false);
        wagon3.getUpperLevel().setUsers(users3);
        train.add(wagon3);

        game.setTrain(train);
        return game;
    }
}
