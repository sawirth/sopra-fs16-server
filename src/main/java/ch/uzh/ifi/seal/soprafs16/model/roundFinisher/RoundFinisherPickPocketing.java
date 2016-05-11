package ch.uzh.ifi.seal.soprafs16.model.roundFinisher;

import ch.uzh.ifi.seal.soprafs16.constant.TreasureType;
import ch.uzh.ifi.seal.soprafs16.model.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@DiscriminatorValue(value = "pickpocketing")
public class RoundFinisherPickPocketing extends RoundFinisher{
    @Override
    public void finishRound(Game game) {
        for(Wagon wagon: game.getTrain()){
            if(wagon.getLowerLevel().getUsers().size()==1){
                chooseRandomMoneybag(wagon.getLowerLevel());
                game.addLog(wagon.getLowerLevel().getUsers().get(0).getCharacterType(), wagon.getLowerLevel().getUsers().get(0).getUsername()
                        + " picked up a moneybag at the end of the round");
            }
            if(wagon.getUpperLevel().getUsers().size()==1){
                chooseRandomMoneybag(wagon.getUpperLevel());
                game.addLog(wagon.getLowerLevel().getUsers().get(0).getCharacterType(), wagon.getLowerLevel().getUsers().get(0).getUsername()
                        + " picked up a moneybag at the end of the round");
            }
        }
        game.addLog(null, "Round has been finished with the pickpocketing event");
    }

    /**
     * chooses a random MoneyBag on the level and adds it to the user
     * @param level
     */
    private void chooseRandomMoneybag(Level level){
        List<Treasure> moneyBags = new ArrayList<>();
        if(!level.getTreasures().isEmpty()){
            for(Treasure treasure: level.getTreasures()){
                if(treasure.getTreasureType()==TreasureType.MONEYBAG){
                    moneyBags.add(treasure);
                }
            }
            if(!moneyBags.isEmpty()){
                Collections.shuffle(moneyBags);
                //adds the moneybag to the only user on the level
                level.getUsers().get(0).getTreasures().add(moneyBags.get(0));
                level.getTreasures().remove(moneyBags.get(0));
            }
        }
    }
}
