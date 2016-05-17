package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.MoveType;
import ch.uzh.ifi.seal.soprafs16.constant.RoundType;
import ch.uzh.ifi.seal.soprafs16.constant.TreasureType;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.model.roundFinisher.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

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

        if (players.size() == 2){                               //if only two players play the game, append three wagons
            for (int i = 0; i < 3; i++) {
                train.add(allWagons.get(i));
            }
        } else {
            for (int i = 0; i < players.size(); i++) {          //else append as many wagons as there are players
                train.add(allWagons.get(i));
            }
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
        Stack<Treasure> allMoneyBags = new Stack<>();

        for (int i = 0; i < 18; i++) {
            if (i < 8) {
                allMoneyBags.push(new Treasure(250, TreasureType.MONEYBAG));
            } else if (i >= 8 && i < 10) {
                allMoneyBags.push(new Treasure(300, TreasureType.MONEYBAG));
            } else if (i >= 10 && i < 12) {
                allMoneyBags.push(new Treasure(350, TreasureType.MONEYBAG));
            } else if (i >= 12 && i < 14) {
                allMoneyBags.push(new Treasure(400, TreasureType.MONEYBAG));
            } else if (i >= 14 && i < 16) {
                allMoneyBags.push(new Treasure(450, TreasureType.MONEYBAG));
            } else {
                allMoneyBags.push(new Treasure(500, TreasureType.MONEYBAG));
            }
        }

        Collections.shuffle(allMoneyBags);
        List<Treasure> tempMoneyBags = new ArrayList<>();

        //Wagon 1 has 3 Moneybags
        for (int i = 0; i < 3; i++) {
            tempMoneyBags.add(allMoneyBags.pop());
        }
        Wagon wagon1 = new Wagon(generateWagonTreasures(tempMoneyBags, 0),false);
        allWagons.add(wagon1);

        //Wagon 2 has 1 Moneybag
        tempMoneyBags.clear();
        tempMoneyBags.add(allMoneyBags.pop());
        Wagon wagon2 = new Wagon(generateWagonTreasures(tempMoneyBags, 0),false);
        allWagons.add(wagon2);

        //Wagon 3 has 3 Diamonds
        tempMoneyBags.clear();
        Wagon wagon3 = new Wagon(generateWagonTreasures(tempMoneyBags, 3),false);
        allWagons.add(wagon3);

        //Wagon 4 has 1 Moneybag and 1 Diamond
        tempMoneyBags.add(allMoneyBags.pop());
        Wagon wagon4 = new Wagon(generateWagonTreasures(tempMoneyBags, 1),false);
        allWagons.add(wagon4);

        //Wagon 5 has 4 Moneybags and 1 Diamond
        tempMoneyBags.clear();
        for (int i = 0; i < 4; i++) {
            tempMoneyBags.add(allMoneyBags.pop());
        }
        Wagon wagon5 = new Wagon(generateWagonTreasures(tempMoneyBags, 1),false);
        allWagons.add(wagon5);

        //Wagon 6 has 3 Moneybags and 1 Diamond
        tempMoneyBags.clear();
        for (int i = 0; i < 3; i++) {
            tempMoneyBags.add(allMoneyBags.pop());
        }
        Wagon wagon6 = new Wagon(generateWagonTreasures(tempMoneyBags, 1),false);
        allWagons.add(wagon6);

        return allWagons;
    }

    private List<Treasure> generateWagonTreasures(List<Treasure> moneybags, int numDiamonds) {
        List<Treasure> treasures = new ArrayList<>();
        treasures.addAll(moneybags);

        for (int i = 0; i < numDiamonds; i++) {
            treasures.add(new Treasure(500, TreasureType.DIAMOND));
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

        //Load a fast game if the number of rounds is only 1
        if (numberOfRounds == 2) {
            allRounds.add(new Round(3, RoundType.FAST_GAME, game, createMoveTypes(1, 4, 0, 0, 0), new RoundFinisherBreak()));
            allRounds.add(new Round(2, RoundType.FAST_GAME2, game, createMoveTypes(3, 2, 0, 0, 0), new RoundFinisherRevengeMarshal()));

        } else {
            normalRounds.add(new Round(4, RoundType.ANGRY_MARSHAL, game, createMoveTypes(1, 1, 2, 3, 0), new RoundFinisherAngryMarshal()));

            normalRounds.add(new Round(4, RoundType.CRANE, game, createMoveTypes(1, 2, 1, 1, 0), new RoundFinisherCrane()));

            normalRounds.add(new Round(4, RoundType.BREAK, game, createMoveTypes(1, 1, 1, 1, 0), new RoundFinisherBreak()));

            normalRounds.add(new Round(5, RoundType.TAKE_ALL, game, createMoveTypes(1, 2, 4, 1, 0), new RoundFinisherTakeAll()));

            normalRounds.add(new Round(5, RoundType.RESISTANCE, game, createMoveTypes(1, 1, 2, 1, 1), new RoundFinisherResistance()));

            normalRounds.add(new Round(4, RoundType.NO_EVENT1, game, createMoveTypes(1, 4, 1, 0, 0), null));

            normalRounds.add(new Round(5, RoundType.NO_EVENT2, game, createMoveTypes(1, 2, 1, 2, 1), null));

            endRounds.add(new Round(4, RoundType.PICK_POCKETING, game, createMoveTypes(1, 1, 2, 1, 0), new RoundFinisherPickPocketing()));

            endRounds.add(new Round(4, RoundType.REVENGE_MARSHAL, game, createMoveTypes(1, 1, 2, 1, 0), new RoundFinisherRevengeMarshal()));

            endRounds.add(new Round(4, RoundType.HOSTAGE, game, createMoveTypes(1, 1, 2, 1, 0), new RoundFinisherHostage()));


            //Shuffles the rounds
            Collections.shuffle(normalRounds);
            Collections.shuffle(endRounds);

            //gets the desired number of rounds and adds them to the list that is given back
            for (int i = 0; i < numberOfRounds - 1; i++) {
                allRounds.add(normalRounds.get(i));
            }
            allRounds.add(endRounds.get(0));
        }

        //sets the first player of a round
        for(int i=0;i<allRounds.size();i++){
            allRounds.get(i).setFirstPlayer((i+game.getPlayers().size()) % game.getPlayers().size());
            allRounds.get(i).setCurrentMoveType(0);
        }

        return allRounds;
    }

    /**
     * Creates a List with moveTypes and gives it back
     * 1 stands for moveType.VISIBLE
     * 2 stands for moveType.HIDDEN
     * 3 stands for moveType.REVERSE
     * 4 stands for moveType.DOUBLE
     * 0 stands for an empty move
     *
     * @param firstMove
     * @param secondMove
     * @param thirdMove
     * @param fourthMove
     * @param fithMove
     * @return List<Round>
     */
    private List<MoveType> createMoveTypes(int firstMove, int secondMove,
                                           int thirdMove, int fourthMove, int fithMove){
        List<Integer> list = new ArrayList<>();
        list.add(firstMove); list.add(secondMove);
        list.add(thirdMove); list.add(fourthMove); list.add(fithMove);

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

                default:
                    break;
            }
        }
        return moveTypes;
    }
}
