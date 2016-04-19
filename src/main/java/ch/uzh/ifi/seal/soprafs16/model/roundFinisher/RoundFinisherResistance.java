package ch.uzh.ifi.seal.soprafs16.model.roundFinisher;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.RoundFinisher;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.Wagon;
import ch.uzh.ifi.seal.soprafs16.model.moves.BlockerMove;

/**
 * Created by David on 17.04.2016.
 */
public class RoundFinisherResistance extends RoundFinisher{
    @Override
    public void finishRound(Game game) {
        for(Wagon wagon: game.getTrain()){
            for(User user: wagon.getLowerLevel().getUsers()){
                user.getDeckCards().add(new BlockerMove());
            }
        }
    }
}
