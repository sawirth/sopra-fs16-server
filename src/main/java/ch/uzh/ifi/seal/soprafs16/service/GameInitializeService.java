package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.MoveType;
import ch.uzh.ifi.seal.soprafs16.constant.TreasureType;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.model.moves.BlockerMove;
import ch.uzh.ifi.seal.soprafs16.model.repositories.MoveRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service("gameInitializeService")
public class GameInitializeService {

    /**
     * Creates a train for a specific game
     *
     * @param players
     * @return
     */
    public List<Wagon> createTrain(List<User> players) {
        List<Wagon> allWagons = new ArrayList<>();
        allWagons.addAll(createWagons());

        List<Wagon> train = new ArrayList<>();

        //add locomotive
        List<Treasure> treasures = new ArrayList<>();
        treasures.add(new Treasure(1000,TreasureType.CASHBOX));
        train.add(new Wagon(treasures, true));

        //add random wagons based on the number of players. A wagon for each player
        Collections.shuffle(allWagons);
        for (int i = 0; i < players.size(); i++) {
            train.add(allWagons.get(i));
        }

        //adds users to train
        addUsersToTrain(train,players);

        //Reset the shots taken for each player
        for (User player : players) {
            player.setShotsTaken(0);
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

        for (int i = 0; i < 14; i++) {
            //TODO Check which and how many Moneybags exists
            if (i < 4) {
                allMoneyBags.add(new Treasure(250, TreasureType.MONEYBAG));
            } else if (i >= 4 && i < 10) {
                allMoneyBags.add(new Treasure(350, TreasureType.MONEYBAG));
            } else if (i >= 10) {
                allMoneyBags.add(new Treasure(500, TreasureType.MONEYBAG));
            }
        }

        Collections.shuffle(allMoneyBags);

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
        for (int i = fromIndex; i < toIndex; i++) {
            treasures.add(moneybags.get(i));
        }
        //adds diamonds to the list
        for (int i = 0; i < diamonds; i++) {
            treasures.add(new Treasure(500, TreasureType.DIAMOND));
        }
        //adds cashboxes to the list
        for (int i = 0; i < cashboxes; i++) {
            treasures.add(new Treasure(1000, TreasureType.CASHBOX));
        }
        return treasures;
    }

    /**
     * Adds players to the train
     *
     * @param train List of wagons
     * @param players List of players
     * @return List<Wagon>
     */
    private void addUsersToTrain(List<Wagon> train, List<User> players){
        List<User> playersLastWagon = new ArrayList<>();
        List<User> playersSecondLastWagon = new ArrayList<>();

        for (int i = 0; i < players.size(); i++) {
            if (i % 2 == 0) {
                playersLastWagon.add(players.get(i));
            } else {
                playersSecondLastWagon.add(players.get(i));
            }
        }

        train.get(train.size() - 1).getLowerLevel().setUsers(playersLastWagon);
        train.get(train.size() - 2).getLowerLevel().setUsers(playersSecondLastWagon);
    }

    /**
     * Adds the first treasure to every user in a game
     * @param users
     */
    public void giveUsersTreasure(List<User> users){
        for(User user : users){
            List<Treasure> treasures = new ArrayList<>();
            treasures.add(new Treasure(250,TreasureType.MONEYBAG));
            user.setTreasures(treasures);
        }
    }

    /**
     * Initializes the roundcards for a game
     *
     */
    public List<Round> initializeRounds(int numberOfRounds, Game game){
        List<Round> normalRounds = new ArrayList<>();
        List<Round> endRounds = new ArrayList<>();
        List<Round> allRounds = new ArrayList<>();

        //TODO add AngryMarshalEvent
        normalRounds.add(new Round(4, game, createMoveTypes(1,1,2,3,0)));

        //TODO add CraneEvent
        normalRounds.add(new Round(4, game, createMoveTypes(1,2,1,1,0)));

        //TODO add BreakEvent
        normalRounds.add(new Round(4, game, createMoveTypes(1,1,1,1,0)));

        //TODO add TakeAllEvent
        normalRounds.add(new Round(5, game, createMoveTypes(1,2,4,1,0)));

        //TODO add ResistanceEvent
        normalRounds.add(new Round(5, game, createMoveTypes(1,1,2,1,1)));

        //TODO no event
        normalRounds.add(new Round(4, game, createMoveTypes(1,4,1,0,0)));

        //TODO no event
        normalRounds.add(new Round(5, game, createMoveTypes(1,2,1,2,1)));

        //TODO add PickpocketingEvent
        endRounds.add(new Round(4, game, createMoveTypes(1,1,2,1,0)));

        //TODO add RevengeMarshalEvent
        endRounds.add(new Round(4, game, createMoveTypes(1,2,1,1,0)));

        //TODO add HostageEvent
        endRounds.add(new Round(4, game, createMoveTypes(1,2,1,1,0)));

        //Shuffles the rounds
        Collections.shuffle(normalRounds);
        Collections.shuffle(endRounds);

        //gets the desired number of rounds and adds them to the list that is given back
        for(int i=0;i<numberOfRounds-1;i++){
            allRounds.add(normalRounds.get(i));
        }
        allRounds.add(endRounds.get(0));

        return allRounds;
    }

    /**
     * Creates a List with moveTypes and gives it back
     * 1 stands for moveType.VISIBLE
     * 2 stands for moveType.HIDDEN
     * 3 stands for moveType.REVERSE
     * 4 stands for moveType.DOUBLE
     *
     * @param a
     * @param b
     * @param c
     * @param d
     * @param e
     * @return List<Round>
     */
    public List<MoveType> createMoveTypes(int a, int b, int c, int d, int e){
        List<Integer> list = new ArrayList<>();
        list.add(a); list.add(b); list.add(c); list.add(d); list.add(e);

        List<MoveType> moveTypes = new ArrayList<>();
        for(Integer integer: list){
            switch (integer) {
                case 1:
                    moveTypes.add(MoveType.VISIBLE);
                    break;
                case 2:
                    moveTypes.add(MoveType.HIDDEN);
                    break;
                case 3:
                    moveTypes.add(MoveType.REVERSE);
                    break;
                case 4:
                    moveTypes.add(MoveType.DOUBLE);
                    break;
                case 0:
                    break;
            }
        }
        return moveTypes;
    }
}
