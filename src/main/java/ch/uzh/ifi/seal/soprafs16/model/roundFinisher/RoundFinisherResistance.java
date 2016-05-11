package ch.uzh.ifi.seal.soprafs16.model.roundFinisher;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.RoundFinisher;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.Wagon;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "resistance")
public class RoundFinisherResistance extends RoundFinisher{
    @Override
    public void finishRound(Game game) {
        for(Wagon wagon: game.getTrain()){
            for(User user: wagon.getLowerLevel().getUsers()){
                user.setShotsTaken(user.getShotsTaken()+1);
                game.addLog(user.getCharacterType(), user.getUsername()+" got shot by the passengers at the end of the round");
            }
        }
        game.addLog(null, "Round has been finished with the resistance event");
    }
}
