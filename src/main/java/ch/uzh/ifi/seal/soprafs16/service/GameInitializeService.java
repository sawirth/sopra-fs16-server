package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.model.Treasure;
import ch.uzh.ifi.seal.soprafs16.model.TreasureType;
import ch.uzh.ifi.seal.soprafs16.model.Wagon;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("gameInitializeService")
public class GameInitializeService {

    public List<Wagon> createWagons(){
        List<Wagon> allWagons = new ArrayList<>();
        allWagons.add(new Wagon(null,1,2, true));

        return allWagons;
    }
}
