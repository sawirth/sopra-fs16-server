package ch.uzh.ifi.seal.soprafs16.service;

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
        //allWagons.addAll(createWagons());

        List<Wagon> train = new ArrayList<>();

        //add locomotive
        train.add(new Wagon(new ArrayList<>(), 0, 1, true));

        //add random wagons based on the number of players. A wagon for each player
        /*Collections.shuffle(allWagons);
        for (int i = 0; i < numberOfPlayers; i++) {
            train.add(allWagons.get(i));
        }*/

        return train;
    }

    /**
     * Creates all possible wagons a game can have
     * @return List<Wagon> A list with all wagons
     */
    private List<Wagon> createWagons() {
        List<Wagon> allWagons = new ArrayList<>();
        //TODO Create the six different wagons from which each game selects 4 randomly


        return allWagons;
    }
}
