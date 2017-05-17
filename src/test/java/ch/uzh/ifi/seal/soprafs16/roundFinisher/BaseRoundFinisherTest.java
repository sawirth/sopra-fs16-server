package ch.uzh.ifi.seal.soprafs16.roundFinisher;

import ch.uzh.ifi.seal.soprafs16.model.*;
import org.junit.Assert;
import org.junit.Before;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;

public abstract class BaseRoundFinisherTest {
    protected Game game;
    protected User firstUser;
    protected User secondUser;

    protected final int FIRST_WAGON = 0;
    protected final int SECOND_WAGON = 1;
    protected final int THIRD_WAGON = 2;
    protected final int LAST_WAGON = 3;
    protected final boolean LOWER_LEVEL = true;
    protected final boolean UPPER_LEVEL = false;

    @Before
    public void setup() {
        game = new Game();
        List<Wagon> train = new ArrayList<>();

        Wagon wagon1 = new Wagon(null, true);
        Wagon wagon2 = new Wagon(null, false);
        Wagon wagon3 = new Wagon(null, false);
        Wagon wagon4 = new Wagon(null, false);

        train.add(0, wagon1);
        train.add(1, wagon2);
        train.add(2, wagon3);
        train.add(3, wagon4);

        game.setTrain(train);

        firstUser = new User("first", "first");
        firstUser.setId(1L);
        secondUser = new User("second", "second");
        secondUser.setId(2L);

        game.getPlayers().add(firstUser);
        game.getPlayers().add(secondUser);
    }

    protected void GivenUserPlacedInWagonAndLevel(User user, int wagonPosition, boolean isOnLowerLevel) {
        Wagon wagon = game.getTrain().get(wagonPosition);
        if (isOnLowerLevel) {
            wagon.getLowerLevel().getUsers().add(user);
        } else {
            wagon.getUpperLevel().getUsers().add(user);
        }
    }

    protected void ThenUserIsInWagonAndLevel(User user , int wagonPosition, boolean isOnLowerLevel) {
        Wagon wagon = game.getTrain().get(wagonPosition);
        List<User> users;
        if (isOnLowerLevel) {
            users = wagon.getLowerLevel().getUsers();
        } else {
            users = wagon.getUpperLevel().getUsers();
        }

        List<Long> userIdsOnLevel = users.stream()
                .map(User::getId)
                .collect(Collectors.toList());

        Assert.assertThat(userIdsOnLevel.contains(user.getId()), is(true));
    }

    protected abstract void WhenFinishingRound();

    protected void WhenFinishingRound(RoundFinisher roundFinisher) {
        roundFinisher.finishRound(this.game);
    }
}
