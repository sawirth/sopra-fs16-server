package ch.uzh.ifi.seal.soprafs16;

import ch.uzh.ifi.seal.soprafs16.constant.TreasureType;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Treasure;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.Wagon;

import java.util.ArrayList;
import java.util.List;

public class TestHelpers {

    public static Game createGame() {
        Game game = new Game();
        List<Wagon> train = new ArrayList<>();

        List<User> users1 = new ArrayList<>();
        User user1 = new User("Hans","Hansi");
        user1.setId(1L);
        user1.setTreasures(createTreasures());

        User user2 = new User("Dave","Dave");
        user2.setId(2L);
        user2.setTreasures(createTreasures2());

        User user3 = new User("Sandro", "sw");
        user3.setId(3L);
        user3.setTreasures(createTreasures());

        users1.add(user1);
        users1.add(user2);
        users1.add(user3);

        List<User> users2 = new ArrayList<>();
        User user4 = new User("Severin", "Sevi");
        user4.setId(4L);
        users2.add(user4);
        Wagon wagon1 = new Wagon(createTreasures(),true);
        wagon1.getUpperLevel().setUsers(users1);
        wagon1.getLowerLevel().setUsers(users2);
        train.add(wagon1);

        List<User> users3 = new ArrayList<>();
        User user5 = new User("Wayne", "Wayne");
        user5.setId(5L);
        users3.add(user5);
        List<User> users4 = new ArrayList<>();
        User user6 = new User("Fritz","Fritz");
        user6.setId(6L);
        users4.add(user6);
        Wagon wagon2 = new Wagon(createTreasures(),false);
        wagon2.getLowerLevel().setUsers(users3);
        wagon2.getUpperLevel().setUsers(users4);
        wagon2.getUpperLevel().setTreasures(createTreasures2());
        train.add(wagon2);

        List<User> users5 = new ArrayList<>();
        users5.add(new User("Sigmund","Siggi"));
        Wagon wagon3 = new Wagon(createTreasures(),false);
        wagon3.getUpperLevel().setUsers(users5);
        train.add(wagon3);

        game.setTrain(train);
        game.getPlayers().addAll(users1);
        game.getPlayers().addAll(users2);
        game.getPlayers().addAll(users3);
        game.getPlayers().addAll(users4);
        game.getPlayers().addAll(users5);
        return game;
    }


    private static List<Treasure> createTreasures(){
        List<Treasure> treasures = new ArrayList<>();
        treasures.add(new Treasure(300, TreasureType.MONEYBAG));
        treasures.add(new Treasure(250, TreasureType.MONEYBAG));
        treasures.add(new Treasure(1000, TreasureType.CASHBOX));
        treasures.add(new Treasure(500, TreasureType.DIAMOND));
        return treasures;
    }

    //to test for another user that only has a diamond
    private static List<Treasure> createTreasures2(){
        List<Treasure> treasures = new ArrayList<>();
        treasures.add(new Treasure(500, TreasureType.DIAMOND));
        return treasures;
    }
}
