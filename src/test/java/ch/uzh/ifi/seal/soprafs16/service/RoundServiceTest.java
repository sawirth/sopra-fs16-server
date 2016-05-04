package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.ActionMoveType;
import ch.uzh.ifi.seal.soprafs16.constant.CharacterType;
import ch.uzh.ifi.seal.soprafs16.constant.MoveType;
import ch.uzh.ifi.seal.soprafs16.constant.RoundType;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.model.moves.HorizontalMove;
import ch.uzh.ifi.seal.soprafs16.model.moves.VerticalMove;
import ch.uzh.ifi.seal.soprafs16.model.roundFinisher.RoundFinisherBreak;
import org.apache.commons.logging.Log;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RoundServiceTest {

    private RoundService roundService;
    private User player;

    @Before
    public void setUp() throws Exception {
        roundService = new RoundService();

        this.player = new User("Sandro", "sw");
    }

    @Test
    public void testResetPlayer() throws Exception {

        //Reset player at the beginning of a round
        roundService.resetPlayer(this.player, null);

        //In the first round the user should have 10 moves in his deck and no blocker moves
        assertThat(this.player.getDeckCards().size(), is(10));
        assertThat(this.player.getShotsTaken(), is(0));

        //Shoot 3 times on player and reset; the deck must now be of size 13
        this.player.setShotsTaken(3);
        roundService.resetPlayer(this.player, null);
        assertThat(this.player.getDeckCards().size(), is(13));

        //The deck must contain 3 blocker moves
        int countBlocker = 0;
        for (Move move : player.getDeckCards()) {
            if (move.getClass().getName().contains("BlockerMove")) {
                countBlocker++;
            }
        }
        assertThat(countBlocker, is(3));
    }

    @Test
    public void testDrawStartCards() throws Exception {

        //In this case the player has 6 cards at the beginning and the deck contains the other 4 cards
        this.player.setShotsTaken(0);
        roundService.resetPlayer(this.player, null);
        player.setCharacterType(CharacterType.BELLE);
        roundService.drawStartCards(this.player);
        assertThat(player.getHandCards().size(), is(6));
        assertThat(player.getDeckCards().size(), is(4));

        //If the user plays as Doc, he has 7 cards at the beginning and the deck contains the other 3 cards
        player.setCharacterType(CharacterType.DOC);
        roundService.resetPlayer(this.player, null);
        roundService.drawStartCards(this.player);
        assertThat(player.getHandCards().size(), is(7));
        assertThat(player.getDeckCards().size(), is(3));

        //Move must have correct CharacterType
        assertThat(player.getHandCards().get(0).getCharacterType(), is(CharacterType.DOC));
    }

    @Test
    public void testIsUserAllowedToMakeMove() throws Exception {
        User player = new User("Sandro", "sw");
        player.setId(1L);

        User player2 = new User("Horst", "hh");
        player2.setId(2L);

        Game game = new Game();
        game.setId(1L);
        game.getPlayers().add(player);
        game.getPlayers().add(player2);
        game.setCurrentPlayer(0);

        //User allowed
        Move allowedMove = new HorizontalMove();
        allowedMove.setGame(game);
        allowedMove.setUser(player);

        Move falseMove = new VerticalMove();
        allowedMove.setUser(player);
        player.getHandCards().add(falseMove);

        //The user has now one move in his hand but it's not the move he wants to make so the method should return false
        assertThat(roundService.isUserAllowedToMakeMove(allowedMove, player), is(false));

        //Now we add the desired move to the hand and the user should now be able to make the move
        player.getHandCards().add(allowedMove);
        assertThat(roundService.isUserAllowedToMakeMove(allowedMove, player), is(true));

        //This makes sure that not another user can make the move
        assertThat(roundService.isUserAllowedToMakeMove(allowedMove, player2), is(false));
    }

    @Test
    public void testShiftMove() throws Exception {
        List<MoveType> moveTypeList = new ArrayList<>();
        moveTypeList.add(MoveType.HIDDEN);
        Round round = new Round(1, null, null, moveTypeList, null);
        Move move = new HorizontalMove();
        move.setId(1L);
        move.setGame(createGame());

        User user = new User("Sandro", "sw");
        user.getHandCards().add(move);
        move.setUser(user);

        //At this point the list of moves from the round must be empty
        assertThat(round.getMoves().isEmpty(), is(true));

        //Now the move is shifted to the round
        roundService.shiftMove(move, round);
        assertThat(round.getMoves().size(), is(1));

        //Since the user only had one handCard, this list must now be empty
        assertThat(user.getHandCards().isEmpty(), is(true));
    }

    @Test
    public void testShiftMoveGhost() throws Exception {
        //Setup
        Game game = createGame();
        User player = game.getPlayers().get(0);
        player.setCharacterType(CharacterType.GHOST);
        Move move = new HorizontalMove();
        move.setActionMoveType(ActionMoveType.HIT);
        move.setUser(player);
        move.setGame(game);
        player.getHandCards().add(move);

        //ShiftMove -> ActionType must change from HIT to HIDDEN
        roundService.shiftMove(move, game.getRounds().get(0));
        Assert.assertThat(game.getRounds().get(0).getMoves().get(0).getActionMoveType(), is(ActionMoveType.HIDDEN));
    }

    @Test
    public void testShiftMoveHidden() throws Exception {
        //Setup game
        Game game = createGame();
        List<MoveType> moveTypeList = new ArrayList<>();
        moveTypeList.add(MoveType.HIDDEN);
        Round round = new Round(1, null, game, moveTypeList, null);

        game.getRounds().clear();
        game.getRounds().add(round);

        User player = game.getPlayers().get(0);
        Move move = new HorizontalMove();
        move.setUser(player);
        move.setGame(game);
        move.setActionMoveType(ActionMoveType.SHOOT);
        player.getHandCards().add(move);

        roundService.shiftMove(move, round);
        assertThat(game.getRounds().get(0).getMoves().get(0).getActionMoveType(), is(ActionMoveType.HIDDEN));

    }

    @Test
    public void testUpdateGameAfterMove(){
        Game game = createGame();
        Round round = game.getRounds().get(0);

        //first three moves are made of MoveType visible
        for (int i=0;i<3;i++){
            makeMove(game);
            assertThat(game.getCurrentPlayer(), is((i+1)%3));
        }
        assertThat(round.getCurrentMoveType(), is(1));

        //next six moves are made of MoveType double
        for (int i=0;i<3;i++){
            makeMove(game);
            assertThat(game.getCurrentPlayer(), is(i));
            makeMove(game);
            assertThat(game.getCurrentPlayer(), is((i+1)%3));
        }
        assertThat(round.getCurrentMoveType(), is(2));

        //next three moves are made of MoveType reverse
        makeMove(game);
        assertThat(game.getCurrentPlayer(), is(2));
        makeMove(game);
        assertThat(game.getCurrentPlayer(), is(1));
        makeMove(game);
        assertThat(game.getCurrentPlayer(), is(0));

        //end of planning phase so currentMoveType doesn't change anymore
        assertThat(round.getCurrentMoveType(), is(2));
        assertThat(round.isActionPhase(), is(true));

        //stack on game is the same as the array list on the round
        for (int i=0; i<round.getMoves().size(); i++) {
            Move move = game.getActionMoves().peek();
            Assert.assertTrue(round.getMoves().get(i) == game.getActionMoves().pop());
        }
    }

    @Test
    public void testDrawCardsMove() throws Exception {
        User user = new User("sandro", "sw");
        for (int i = 0; i < 5; i++) {
            user.getDeckCards().add(new HorizontalMove());
        }

        //The user has now 5 cards in his deck and makes a draw move which means he can draw 3 cards
        roundService.drawDeckCards(user);
        assertThat(user.getHandCards().size(), is(3));
        assertThat(user.getDeckCards().size(), is(2));

        //Now if the user again makes a draw move he can only draw two more cards
        roundService.drawDeckCards(user);
        assertThat(user.getHandCards().size(), is(5));
        assertThat(user.getDeckCards().isEmpty(), is(true));
    }

    private void makeMove(Game game){
        Round round = game.getRounds().get(0);
        Move move = new HorizontalMove();
        move.setUser(game.getPlayers().get(game.getCurrentPlayer()));
        round.getMoves().add(move);
        roundService.updateGameAfterMove(game);
    }

    private Game createGame(){
        Game game = new Game();
        game.setId(1L);

        List<MoveType> moveTypeList = new ArrayList<>();
        moveTypeList.add(MoveType.VISIBLE);
        moveTypeList.add(MoveType.DOUBLE);
        moveTypeList.add(MoveType.REVERSE);

        Round round = new Round(4, RoundType.ANGRY_MARSHAL, game, moveTypeList, new RoundFinisherBreak());
        round.setFirstPlayer(0);
        round.setCurrentMoveType(0);

        User player = new User("Sandro", "sw");
        player.setId(1L);
        User player2 = new User("Horst", "hh");
        player2.setId(2L);
        User player3 = new User("Horst", "h2");
        player3.setId(3L);

        game.getPlayers().add(player);
        game.getPlayers().add(player2);
        game.getPlayers().add(player3);
        game.setCurrentPlayer(0);
        game.setCurrentRound(0);
        game.getRounds().add(round);

        return game;
    }
}