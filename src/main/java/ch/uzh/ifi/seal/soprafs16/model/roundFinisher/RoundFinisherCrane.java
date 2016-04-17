package ch.uzh.ifi.seal.soprafs16.model.roundFinisher;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.RoundFinisher;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.Wagon;

import java.util.ArrayList;
import java.util.List;


public class RoundFinisherCrane extends RoundFinisher {

    @Override
    public void finishRound(Game game) {
        List<Wagon> train = game.getTrain();
        List<User> users = new ArrayList<>();
        for(Wagon wagon: train){
            if(wagon.getUpperLevel().getUsers()!=null){
                users.addAll(wagon.getUpperLevel().getUsers());
            }
            wagon.getUpperLevel().setUsers(null);
        }
        train.get(train.size()-1).getUpperLevel().setUsers(users);
        game.setTrain(train);
    }
}
