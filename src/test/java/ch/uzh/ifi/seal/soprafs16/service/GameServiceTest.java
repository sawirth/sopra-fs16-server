package ch.uzh.ifi.seal.soprafs16.service;


import ch.uzh.ifi.seal.soprafs16.constant.*;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.model.moves.BlockerMove;
import ch.uzh.ifi.seal.soprafs16.model.moves.HitMove;
import ch.uzh.ifi.seal.soprafs16.model.moves.HorizontalMove;
import ch.uzh.ifi.seal.soprafs16.model.moves.ShootMove;
import ch.uzh.ifi.seal.soprafs16.model.roundFinisher.RoundFinisherAngryMarshal;
import ch.uzh.ifi.seal.soprafs16.model.roundFinisher.RoundFinisherResistance;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GameServiceTest {

    private Game game;
    private GameService gameService;

    @Before
    public void setup() throws Exception{
        game = new Game();
        gameService = new GameService();

        game.setId(1L);

        //initialises the first round
        List<MoveType> moveTypeList = new ArrayList<>();
        moveTypeList.add(MoveType.VISIBLE);
        moveTypeList.add(MoveType.DOUBLE);
        moveTypeList.add(MoveType.REVERSE);

        Round round = new Round(4, RoundType.ANGRY_MARSHAL, game, moveTypeList, new RoundFinisher());
        round.setFirstPlayer(0);
        round.setCurrentMoveType(0);

        //initialises the second round
        List<MoveType> moveTypeList2 = new ArrayList<>();
        moveTypeList2.add(MoveType.VISIBLE);
        moveTypeList2.add(MoveType.DOUBLE);
        moveTypeList2.add(MoveType.REVERSE);

        Round round2 = new Round(4, RoundType.RESISTANCE, game, moveTypeList, new RoundFinisher());
        round2.setFirstPlayer(1);
        round2.setCurrentMoveType(0);


        User player1 = new User("Sandro", "sw");
        player1.setId(1L);
        player1.setCharacterType(CharacterType.CHEYENNE);
        User player2 = new User("Horst", "hh");
        player2.setId(2L);
        player2.setCharacterType(CharacterType.TUCO);
        User player3 = new User("Horst", "h2");
        player3.setId(3L);
        player3.setCharacterType(CharacterType.BELLE);

        game.getPlayers().add(player1);
        game.getPlayers().add(player2);
        game.getPlayers().add(player3);

        game.setCurrentPlayer(0);
        game.setCurrentRound(0);
        game.getRounds().add(round);
        game.getRounds().add(round2);

        Stack<Move> moves = new Stack<>();
        Move move = new BlockerMove();
        move.setCharacterType(CharacterType.CHEYENNE);
        move.setUser(player1);
        move.setActionMoveType(ActionMoveType.DRAW);

        //adds three moves to the stack of the game to simulate the played moves in the planning phase
        moves.add(move);
        moves.add(addMoveToStack(player2));
        moves.add(addMoveToStack(player3));

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

        //started next round
        assertThat(game.getCurrentRound(), is(1));
        //current player is 1
        assertThat(game.getCurrentPlayer(), is(1));

        gameService.updateGameAfterMove(game);
        assertThat(game.getStatus(), is(GameStatus.FINISHED));

    }

    public Move addMoveToStack(User user){
        Move move = new ShootMove();
        move.setCharacterType(user.getCharacterType());
        move.setUser(user);

        return move;
    }

}
