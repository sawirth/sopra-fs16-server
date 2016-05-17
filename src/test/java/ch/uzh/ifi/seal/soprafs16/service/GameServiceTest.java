package ch.uzh.ifi.seal.soprafs16.service;


import ch.uzh.ifi.seal.soprafs16.TestHelpers;
import ch.uzh.ifi.seal.soprafs16.constant.*;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Move;
import ch.uzh.ifi.seal.soprafs16.model.Round;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.moves.BlockerMove;
import ch.uzh.ifi.seal.soprafs16.model.moves.ShootMove;
import ch.uzh.ifi.seal.soprafs16.model.roundFinisher.RoundFinisherBreak;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class GameServiceTest {

    private Game game;
    private GameService gameService;
    private User user;

    @Before
    public void setup() throws Exception{
        game = TestHelpers.createGame();
        gameService = new GameService();

        game.setId(1L);

        //initialises the first round
        List<MoveType> moveTypeList = new ArrayList<>();
        moveTypeList.add(MoveType.VISIBLE);
        moveTypeList.add(MoveType.DOUBLE);
        moveTypeList.add(MoveType.REVERSE);
        Round round = new Round(4, RoundType.ANGRY_MARSHAL, game, moveTypeList, new RoundFinisherBreak());
        round.setFirstPlayer(0);
        round.setCurrentMoveType(0);

        //initialises the second round
        List<MoveType> moveTypeList2 = new ArrayList<>();
        moveTypeList2.add(MoveType.VISIBLE);
        moveTypeList2.add(MoveType.DOUBLE);
        moveTypeList2.add(MoveType.REVERSE);
        Round round2 = new Round(4, RoundType.RESISTANCE, game, moveTypeList, new RoundFinisherBreak());
        round2.setFirstPlayer(1);
        round2.setCurrentMoveType(0);

        game.setCurrentPlayer(0);
        game.setCurrentRound(0);
        game.getRounds().add(round);
        game.getRounds().add(round2);

        Stack<Move> moves = new Stack<>();
        Move move = new BlockerMove();
        move.setCharacterType(CharacterType.CHEYENNE);
        move.setUser(game.getPlayers().get(0));
        move.setActionMoveType(ActionMoveType.DRAW);

        //adds three moves to the stack of the game to simulate the played moves in the planning phase
        moves.add(move);
        moves.add(addMoveToStack(game.getPlayers().get(0)));
        moves.add(addMoveToStack(game.getPlayers().get(0)));

        for(User player: game.getPlayers()){
            player.setCharacterType(CharacterType.BELLE);
        }

        game.setActionMoves(moves);

        game.getPlayers().get(0).setNumberOfShots(2);
        game.getPlayers().get(0).setShotsTaken(3);
        game.getPlayers().get(1).setNumberOfShots(0);
        game.getPlayers().get(1).setShotsTaken(5);
        game.getPlayers().get(2).setNumberOfShots(6);
        game.getPlayers().get(2).setShotsTaken(6);
        game.getPlayers().get(3).setNumberOfShots(4);
        game.getPlayers().get(3).setShotsTaken(2);
        game.getPlayers().get(4).setNumberOfShots(3);
        game.getPlayers().get(4).setShotsTaken(1);
        game.getPlayers().get(5).setNumberOfShots(0);
        game.getPlayers().get(5).setShotsTaken(2);
        game.getPlayers().get(6).setNumberOfShots(1);
        game.getPlayers().get(6).setShotsTaken(3);
        game.getPlayers().get(7).setNumberOfShots(5);
        game.getPlayers().get(7).setShotsTaken(4);
    }

    @Test
    public void testUpdateGameAfterMove(){

        gameService.updateGameAfterMove(game);
        game.getActionMoves().pop();

        gameService.updateGameAfterMove(game);
        game.getActionMoves().pop();

        //since the last move in the stack is a draw move it should be removed automatically
        gameService.updateGameAfterMove(game);
        assertThat(game.getActionMoves(), is(empty()));

        //started next round
        assertThat(game.getCurrentRound(), is(1));
        //current player is 1
        assertThat(game.getCurrentPlayer(), is(1));

        gameService.updateGameAfterMove(game);
        assertThat(game.getStatus(), is(GameStatus.FINISHED));
    }

    @Test
    public void testSwitchLevel() throws Exception {
        user = game.getPlayers().get(0);
        gameService.switchLevel(game.getTrain(), game.getTrain().get(1).getUpperLevel(), user);
        assertThat(game.getTrain().get(1).getUpperLevel().getUsers().contains(user), is(true));
    }

    @Test
    public void testCheckForMarshalInWagon(){
        game.getTrain().get(0).setHasMarshal(true);

        //adds another user to the first lower level
        User user = new User("Heino", "Heino");
        game.getTrain().get(0).getLowerLevel().getUsers().add(user);
        gameService.checkForMarshalInWagon(game);

        //first there were 3 users on top now there are 5
        assertThat(game.getTrain().get(0).getUpperLevel().getUsers().size(), is(5));
        assertThat(game.getGameLog().getLogEntryList().size(), is(2));
        assertThat(game.getTrain().get(0).getLowerLevel().getUsers().size(), is(0));

        //checks another level so marshal is going to be set to the last wagon
        game.getTrain().get(0).setHasMarshal(false);
        game.getTrain().get(1).setHasMarshal(false);
        game.getTrain().get(2).setHasMarshal(true);

        assertThat(game.getTrain().get(2).getUpperLevel().getUsers().size(), is(0));
        assertThat(game.getTrain().get(2).getLowerLevel().getUsers().size(), is(0));
    }

    @Test
    public void testSetGameResults(){

        gameService.setGameResults(game);

        assertThat(game.getUserResults().get(0).getTreasures().size(), is(3));
        assertThat(game.getUserResults().get(0).getGunslinger(), is(false));
        assertThat(game.getUserResults().get(0).getTotalMoney(), is(2050));
        assertThat(game.getUserResults().get(0).getNumberOfShotsTaken(), is(3));

        assertThat(game.getUserResults().get(1).getGunslinger(), is(false));
        assertThat(game.getUserResults().get(1).getTotalMoney(), is(2050));
        assertThat(game.getUserResults().get(1).getNumberOfShotsTaken(), is(6));

        assertThat(game.getUserResults().get(2).getGunslinger(), is(true));
        assertThat(game.getUserResults().get(2).getTotalMoney(), is(1500));

        assertThat(game.getUserResults().get(3).getGunslinger(), is(true));
        assertThat(game.getUserResults().get(3).getTotalMoney(), is(1000));

    }

    public Move addMoveToStack(User user){
        Move move = new ShootMove();
        move.setCharacterType(user.getCharacterType());
        move.setUser(user);

        return move;
    }
}
