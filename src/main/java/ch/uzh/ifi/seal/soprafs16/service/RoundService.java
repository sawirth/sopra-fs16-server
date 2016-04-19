package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.CharacterType;
import ch.uzh.ifi.seal.soprafs16.constant.MoveType;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Move;
import ch.uzh.ifi.seal.soprafs16.model.Round;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.moves.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Service("roundService")
public class RoundService {

    /**
     * Resets the player to the initial state before the next round starts. The player gets his 10 action moves (cards)
     * plus blocker moves corresponding to the shots taken by other players into his deck and the number of shots is set to 6
     * @param player
     */
    public void resetPlayer(User player, Game game) {
        //Make sure both deck and hand are empty before adding action moves
        player.getDeckCards().clear();
        player.getHandCards().clear();

        //Set properties bi-directional
        for (Move move : createActionMoves()) {
            move.setUser(player);
            move.setCharacterType(player.getCharacterType());
            move.setGame(game);
            player.getDeckCards().add(move);
        }

        //Add blocker moves
        for (int i = 0; i < player.getShotsTaken(); i++) {
            BlockerMove blockerMove = new BlockerMove();
            blockerMove.setUser(player);
            blockerMove.setCharacterType(player.getCharacterType());
            blockerMove.setGame(game);
            player.getDeckCards().add(blockerMove);
        }

        //Shuffle
        Collections.shuffle(player.getDeckCards());
    }

    /**
     * Creates a list with the 10 action moves each player has in his deck at the beginning of the round
     * @return
     */
    private List<Move> createActionMoves() {
        List<Move> moves = new ArrayList<>();

        /*
        Each player has the following action moves
        - 2 steal
        - 1 hit
        - 2 vertical
        - 2 horizontal
        - 2 shoot
        - 1 move marshal
         */
        moves.add(new StealMove());
        moves.add(new StealMove());
        moves.add(new HitMove());
        moves.add(new VerticalMove());
        moves.add(new VerticalMove());
        moves.add(new HorizontalMove());
        moves.add(new HorizontalMove());
        moves.add(new ShootMove());
        moves.add(new ShootMove());
        moves.add(new ChangeMarshalPositionMove());
        return moves;
    }

    /**
     * The player draws 6 (or 7 for Doc) from his deck. This method is only called at the beginning of a new round
     * @param player
     */
    public void drawStartCards(User player) {

        //Remove card from deck and put into handCards
        for (int i = 0; i < 6; i++) {
            player.getHandCards().add(player.getDeckCards().remove(0));
        }

        //Special case for Doc as he can draw 7 cards each round
        if (player.getCharacterType().equals(CharacterType.DOC)) {
            player.getHandCards().add(player.getDeckCards().remove(0));
        }
    }

    /**
     * Shifts the move from the handCards of the user to the list of moves of the round
     * @param move Move that shifts
     * @param round Round that move will be added to
     */
    public void shiftMove(Move move, Round round) {
        Move m = move;

        //Find where the move is in the list
        User user = move.getUser();
        int i = 0;
        for (Move handCard : user.getHandCards()) {
            if (handCard.getId() == m.getId()) {
               break;
            }
            i++;
        }

        //Remove the move (pun intended)
        user.getHandCards().remove(i);

        //Add to round
        m.setRound(round);
        round.getMoves().add(m);
    }

    /**
     * This method checks if the user is allowed to make the move. It checks if the user is the owner of the move and
     * if the move is in his hands
     * @param move The Move the user wants to make
     * @param user The user which makes the move
     * @return True if allowed, otherwise false
     */
    public boolean isUserAllowedToMakeMove(Move move, User user) {

        //Check token
        if (move.getUser().getId() != user.getId()) {
            return false;
        }

        //check if user is the current user
        if (move.getGame().getPlayers().get(move.getGame().getCurrentPlayer())!=user){
            return false;
        }

        //Check if in hand
        if (user.getHandCards().contains(move)) {
            return true;
        } else {
            return false;
        }
    }

    public Game updateGameAfterMove(Game game) {

        Round round = game.getRounds().get(game.getCurrentRound());
        MoveType moveType = round.getMoveTypes().get(round.getCurrentMoveType());

        //if the current move is a visible moveType
        if(moveType==MoveType.VISIBLE){
            game.setCurrentPlayer(game.getNextPlayer());
            //sets the current MoveType to the next MoveType
            if (game.getCurrentPlayer()==round.getFirstPlayer()){
                round.setCurrentMoveType(round.getCurrentMoveType()+1);
            }
        }

        //if the current move is a hidden moveType
        else if (moveType==MoveType.HIDDEN){
            game.setCurrentPlayer(game.getNextPlayer());
            //sets the current MoveType to the next MoveType
            if (game.getCurrentPlayer()==round.getFirstPlayer()){
                round.setCurrentMoveType(round.getCurrentMoveType()+1);
            }
        }

        //if the current move is a reverse moveType
        else if (moveType==MoveType.REVERSE){
            //sets the current player to the player last in the list
            game.setCurrentPlayer((game.getCurrentPlayer()+game.getPlayers().size()-1)
                    % game.getPlayers().size());
            if (game.getCurrentPlayer()==round.getFirstPlayer()){
                round.setCurrentMoveType(round.getCurrentMoveType()+1);
            }
        }

        //if the current move is a double MoveType
        else if (moveType==MoveType.DOUBLE){
            //checks if the move before this one was made by the same user
            //-2 since the move was already shifted to the list of moves in the round
            if (round.getMoves().get(round.getMoves().size()-2).getUser()
                    == game.getPlayers().get(game.getCurrentPlayer())) {
                game.setCurrentPlayer(game.getNextPlayer());
            }
            else {
            }
        }

        if (round.getNUMBER_OF_MOVES()*game.getPlayers().size()==round.getMoves().size()){
            //TODO end planning phase
            // no player is allowed to play a card anymore
        }

        return game;
    }
}
