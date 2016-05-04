package ch.uzh.ifi.seal.soprafs16.model.roundFinisher;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.RoundFinisher;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.moves.BlockerMove;
import ch.uzh.ifi.seal.soprafs16.service.GameService;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "angry_marshal")
public class RoundFinisherAngryMarshal extends RoundFinisher{
    @Override
    public void finishRound(Game game) {
        int i=0;
        GameService gameService = new GameService();
        while(!game.getTrain().get(i).hasMarshal()){
            i++;
        }
        //gives every user on top of Marshal a BlockerMove
        for(User user: game.getTrain().get(i).getUpperLevel().getUsers()){
            user.setShotsTaken(user.getShotsTaken()+1);
        }

        //Marshal goe one wagon closer to the end of the train if
        if(i<game.getTrain().size()-1){
            game.getTrain().get(i).setHasMarshal(false);
            game.getTrain().get(i+1).setHasMarshal(true);
        }

        //since Marshal changed position in some cases
        gameService.checkForMarshalInWagon(game);
    }
}
