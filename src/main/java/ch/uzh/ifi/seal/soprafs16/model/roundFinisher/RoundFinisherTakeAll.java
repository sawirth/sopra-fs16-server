package ch.uzh.ifi.seal.soprafs16.model.roundFinisher;

import ch.uzh.ifi.seal.soprafs16.constant.TreasureType;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.RoundFinisher;
import ch.uzh.ifi.seal.soprafs16.model.Treasure;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "take_all")
public class RoundFinisherTakeAll extends RoundFinisher {
    @Override
    public void finishRound(Game game) {
        int i=0;
        while(!game.getTrain().get(i).hasMarshal()){
            i++;
        }
        game.getTrain().get(i).getLowerLevel().getTreasures().add(new Treasure(1000, TreasureType.CASHBOX));
        game.addLog(null, "The second cashbox was placed in wagon next to the marshal at the end of the round");
        game.addLog(null, "Round has been finished with the take-everything event");
    }
}
