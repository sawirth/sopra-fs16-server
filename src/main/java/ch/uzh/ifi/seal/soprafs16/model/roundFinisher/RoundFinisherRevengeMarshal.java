package ch.uzh.ifi.seal.soprafs16.model.roundFinisher;

import ch.uzh.ifi.seal.soprafs16.constant.TreasureType;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.RoundFinisher;
import ch.uzh.ifi.seal.soprafs16.model.Treasure;
import ch.uzh.ifi.seal.soprafs16.model.User;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity
@DiscriminatorValue(value = "revenge-marshal")
public class RoundFinisherRevengeMarshal extends RoundFinisher{
    @Override
    public void finishRound(Game game) {
        int i=0;
        while(!game.getTrain().get(i).hasMarshal()){
            i++;
        }
        for(User user: game.getTrain().get(i).getUpperLevel().getUsers()){
            List<Treasure> treasures = user.getTreasures();
            Treasure minMoneybag=null;

            for(Treasure treasure: treasures){
                //check for the first occurrence of a moneybag
                if(minMoneybag==null && treasure.getTreasureType()==TreasureType.MONEYBAG){
                    minMoneybag=treasure;
                }
                //make minMoneybag the smallest moneybag
                else if(treasure.getTreasureType()==TreasureType.MONEYBAG
                        && treasure.getValue()<minMoneybag.getValue()){
                    minMoneybag=treasure;
                }
            }

            //removes the smallest moneybag
            if(minMoneybag!=null){
                user.getTreasures().remove(minMoneybag);
            }

            game.addLog(user.getCharacterType(), user.getUsername() + " lost his smallest moneybag at the end of the round");
        }

        game.addLog(null, "Round has been finished with the revenge-marshal event");
    }
}
