package ch.uzh.ifi.seal.soprafs16.model.roundFinisher;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.RoundFinisher;
import ch.uzh.ifi.seal.soprafs16.model.User;

import java.util.List;


public class RoundFinisherBreak extends RoundFinisher{
    @Override
    public void finishRound(Game game) {
        for(int i=1;i<game.getTrain().size();i++){
            List<User> users = game.getTrain().get(i).getUpperLevel().getUsers();
            if(users!=null){
                game.getTrain().get(i-1).getUpperLevel().getUsers().addAll(users);
                game.getTrain().get(i).getUpperLevel().getUsers().clear();
            }
        }
    }
}
