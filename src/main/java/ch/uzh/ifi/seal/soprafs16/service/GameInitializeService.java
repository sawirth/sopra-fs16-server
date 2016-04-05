package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.model.Wagon;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("gameInitializeService")
public class GameInitializeService {

    private final List<Wagon> allWagons;

    public GameInitializeService() {
        allWagons = new ArrayList<>();
        createWagons();
    }

    private void createWagons() {

    }
}
