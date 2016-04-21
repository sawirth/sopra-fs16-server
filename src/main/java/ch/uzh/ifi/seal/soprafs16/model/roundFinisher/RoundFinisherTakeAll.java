package ch.uzh.ifi.seal.soprafs16.model.roundFinisher;

import ch.uzh.ifi.seal.soprafs16.constant.TreasureType;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.RoundFinisher;
import ch.uzh.ifi.seal.soprafs16.model.Treasure;

/**
 * Created by David on 16.04.2016.
 */
public class RoundFinisherTakeAll extends RoundFinisher {
    @Override
    public void finishRound(Game game) {
        int i=0;
        while(game.getTrain().get(i).hasMarshal()==false){
            i++;
        }
        game.getTrain().get(i).getLowerLevel().getTreasures().add(new Treasure(1000, TreasureType.CASHBOX));
    }
}
