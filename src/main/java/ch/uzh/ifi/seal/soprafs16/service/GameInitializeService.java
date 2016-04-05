package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.TreasureType;
import ch.uzh.ifi.seal.soprafs16.model.Treasure;
import ch.uzh.ifi.seal.soprafs16.model.Wagon;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service("gameInitializeService")
public class GameInitializeService {

    /**
     * Creates the train for a game
     * @param numberOfPlayers This defines the number of wagons
     * @return
     */
    public List<Wagon> createTrain(int numberOfPlayers) {
        List<Wagon> allWagons = new ArrayList<>();
        allWagons.addAll(createWagons());

        List<Wagon> train = new ArrayList<>();

        //add locomotive
        List<Treasure> treasures = new ArrayList<>();
        treasures.add(new Treasure(1000,TreasureType.CASHBOX));
        train.add(new Wagon(treasures, true));

        //add random wagons based on the number of players. A wagon for each player
        Collections.shuffle(allWagons);
        for (int i = 0; i < numberOfPlayers; i++) {
            train.add(allWagons.get(i));
        }

        return train;
    }

    /**
     * Creates all possible wagons a game can have
     * @return List<Wagon> A list with all wagons
     */
    private List<Wagon> createWagons() {
        List<Wagon> allWagons = new ArrayList<>();
        List<Treasure> allMoneyBags = new ArrayList<>();

        for(int i=0;i<14;i++){
            //TODO Check which and how many Moneybags exists
            if(i<4){
                allMoneyBags.add(new Treasure(250, TreasureType.MONEYBAG));
            }
            else if(i>=4 && i<10){
                allMoneyBags.add(new Treasure(350, TreasureType.MONEYBAG));
            }
            else if(i>=10){
                allMoneyBags.add(new Treasure(500, TreasureType.MONEYBAG));
            }
        }

        Collections.shuffle(allMoneyBags);
        //TODO Create the six different wagons from which each game selects 4 randomly
        Wagon wagon1 = new Wagon(addTreasures(allMoneyBags,0,2,0,0),false);
        allWagons.add(wagon1);

        Wagon wagon2 = new Wagon(addTreasures(allMoneyBags,2,3,1,0),false);
        allWagons.add(wagon2);

        Wagon wagon3 = new Wagon(addTreasures(allMoneyBags,3,3,1,0),false);
        allWagons.add(wagon3);

        Wagon wagon4 = new Wagon(addTreasures(allMoneyBags,3,5,0,0),false);
        allWagons.add(wagon4);

        Wagon wagon5 = new Wagon(addTreasures(allMoneyBags,5,7,0,0),false);
        allWagons.add(wagon5);

        Wagon wagon6 = new Wagon(addTreasures(allMoneyBags,7,9,2,0),false);
        allWagons.add(wagon6);

        return allWagons;
    }

    /**
     * Generates the treasureList for each Wagon
     *
     * @param moneybags
     * @param fromIndex
     * @param toIndex
     * @param diamonds
     * @param cashboxes
     * @return List<Treasure>
     */
    private List<Treasure> addTreasures(List<Treasure> moneybags, int fromIndex, int toIndex, int diamonds, int cashboxes){
        List<Treasure> treasures = new ArrayList<>();

        //adds moneybags to the list
        for(int i = fromIndex; i < toIndex; i++){
            treasures.add(moneybags.get(i));
        }
        //adds diamonds to the list
        for(int i=0;i<diamonds;i++){
            treasures.add(new Treasure(500,TreasureType.DIAMOND));
        }
        //adds cashboxes to the list
        for(int i=0;i<cashboxes;i++){
            treasures.add(new Treasure(1000,TreasureType.CASHBOX));
        }
        return treasures;
    }
}
