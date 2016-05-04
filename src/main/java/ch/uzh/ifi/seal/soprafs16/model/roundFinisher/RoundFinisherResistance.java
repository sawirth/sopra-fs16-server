package ch.uzh.ifi.seal.soprafs16.model.roundFinisher;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.RoundFinisher;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.Wagon;
import ch.uzh.ifi.seal.soprafs16.model.moves.BlockerMove;

import javax.persistence.Entity;

@Entity
public class RoundFinisherResistance extends RoundFinisher{
    @Override
    public void finishRound(Game game) {
        for(Wagon wagon: game.getTrain()){
            for(User user: wagon.getLowerLevel().getUsers()){
                user.setShotsTaken(user.getShotsTaken()+1);
            }
        }
    }
}
