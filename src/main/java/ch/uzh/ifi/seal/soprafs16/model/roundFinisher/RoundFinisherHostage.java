package ch.uzh.ifi.seal.soprafs16.model.roundFinisher;

import ch.uzh.ifi.seal.soprafs16.constant.TreasureType;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.RoundFinisher;
import ch.uzh.ifi.seal.soprafs16.model.Treasure;
import ch.uzh.ifi.seal.soprafs16.model.User;

import javax.persistence.Entity;

@Entity
public class RoundFinisherHostage extends RoundFinisher{
    @Override
    public void finishRound(Game game) {
        for(User user: game.getTrain().get(0).getLowerLevel().getUsers()){
            user.getTreasures().add(new Treasure(250, TreasureType.MONEYBAG));
        }
        for(User user: game.getTrain().get(0).getUpperLevel().getUsers()){
            user.getTreasures().add(new Treasure(250, TreasureType.MONEYBAG));
        }
    }
}
