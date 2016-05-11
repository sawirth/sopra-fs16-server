package ch.uzh.ifi.seal.soprafs16.model.roundFinisher;

import ch.uzh.ifi.seal.soprafs16.constant.TreasureType;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.RoundFinisher;
import ch.uzh.ifi.seal.soprafs16.model.Treasure;
import ch.uzh.ifi.seal.soprafs16.model.User;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "hostage")
public class RoundFinisherHostage extends RoundFinisher{
    @Override
    public void finishRound(Game game) {
        for(User user: game.getTrain().get(0).getLowerLevel().getUsers()){
            user.getTreasures().add(new Treasure(250, TreasureType.MONEYBAG));
            game.addLog(user.getCharacterType(), user.getUsername() + " took the conductor as an hostage and earned 250$ at the end of the round");
        }
        for(User user: game.getTrain().get(0).getUpperLevel().getUsers()){
            user.getTreasures().add(new Treasure(250, TreasureType.MONEYBAG));
            game.addLog(user.getCharacterType(), user.getUsername() + " took the conductor as an hostage and earned 250$ at the end of the round");
        }
        game.addLog(null, "Round has been finished with the hostage event");
    }
}
