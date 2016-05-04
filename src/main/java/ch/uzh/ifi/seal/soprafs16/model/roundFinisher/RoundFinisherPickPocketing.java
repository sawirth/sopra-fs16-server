package ch.uzh.ifi.seal.soprafs16.model.roundFinisher;

import ch.uzh.ifi.seal.soprafs16.constant.TreasureType;
import ch.uzh.ifi.seal.soprafs16.model.*;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class RoundFinisherPickPocketing extends RoundFinisher{
    @Override
    public void finishRound(Game game) {
        for(Wagon wagon: game.getTrain()){
            if(wagon.getLowerLevel().getUsers().size()==1){
                chooseRandomMoneybag(wagon.getLowerLevel());
            }
            if(wagon.getUpperLevel().getUsers().size()==1){
                chooseRandomMoneybag(wagon.getUpperLevel());
            }
        }
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
            Collections.shuffle(moneyBags);
            if(!moneyBags.isEmpty()){
                //adds the moneybag to the only user on the level
                level.getUsers().get(0).getTreasures().add(moneyBags.get(0));
                level.getTreasures().remove(moneyBags.get(0));
            }
        }
    }
}
