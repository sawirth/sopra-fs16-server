package ch.uzh.ifi.seal.soprafs16.model.roundFinisher;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.RoundFinisher;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.moves.BlockerMove;

/**
 * Created by David on 17.04.2016.
 */
public class RoundFinisherAngryMarshal extends RoundFinisher{
    @Override
    public void finishRound(Game game) {
        int i=0;
        while(game.getTrain().get(i).hasMarshal()==false){
            i++;
        }
        //gives every user on top of Marshal a BlockerMove
        for(User user: game.getTrain().get(i).getUpperLevel().getUsers()){
            user.getDeckCards().add(new BlockerMove());
        }

        if(i<game.getTrain().size()-1){
            game.getTrain().get(i).setHasMarshal(false);
            game.getTrain().get(i+1).setHasMarshal(true);
        }
    }
}
