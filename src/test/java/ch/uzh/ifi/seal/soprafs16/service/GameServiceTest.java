package ch.uzh.ifi.seal.soprafs16.service;


import ch.uzh.ifi.seal.soprafs16.constant.*;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.TestHelpers;
import ch.uzh.ifi.seal.soprafs16.model.moves.BlockerMove;
import ch.uzh.ifi.seal.soprafs16.model.moves.HitMove;
import ch.uzh.ifi.seal.soprafs16.model.moves.HorizontalMove;
import ch.uzh.ifi.seal.soprafs16.model.moves.ShootMove;
import ch.uzh.ifi.seal.soprafs16.model.roundFinisher.RoundFinisherAngryMarshal;
import ch.uzh.ifi.seal.soprafs16.model.roundFinisher.RoundFinisherBreak;
import ch.uzh.ifi.seal.soprafs16.model.roundFinisher.RoundFinisherResistance;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

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
    }

    @Test
    public void testUpdateGameAfterMove(){
        //should just add a log to the game log
        gameService.updateGameAfterMove(game);
        assertThat(game.getGameLog().getLogEntryList().size(), is(1));
        game.getActionMoves().pop();

        //again only should add a log
        gameService.updateGameAfterMove(game);
        assertThat(game.getGameLog().getLogEntryList().size(), is(2));
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

    public Move addMoveToStack(User user){
        Move move = new ShootMove();
        move.setCharacterType(user.getCharacterType());
        move.setUser(user);

        return move;
    }
}
