package ch.uzh.ifi.seal.soprafs16.model.roundFinisher;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.RoundFinisher;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.Wagon;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue(value = "crane")
public class RoundFinisherCrane extends RoundFinisher {

    @Override
    public void finishRound(Game game) {
        List<Wagon> train = game.getTrain();
        List<User> users = new ArrayList<>();
        for(Wagon wagon: train){
            if(wagon.getUpperLevel().getUsers()!=null){
                users.addAll(wagon.getUpperLevel().getUsers());
            }
            wagon.getUpperLevel().getUsers().clear();
        }
        train.get(train.size()-1).getUpperLevel().setUsers(users);

        for (User user: users){
            game.addLog(user.getCharacterType(), user.getUsername() + " took the crane to the end");
        }

        game.setTrain(train);

        game.addLog(null, "Round has been finished with the crane event");
    }
}
