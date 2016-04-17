package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.CharacterType;
import ch.uzh.ifi.seal.soprafs16.model.Move;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.moves.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service("roundService")
public class RoundService {

    /**
     * Resets the player to the initial state before the next round starts. The player gets his 10 action moves (cards)
     * plus blocker moves corresponding to the shots taken by other players into his deck and the number of shots is set to 6
     * @param player
     */
    public void resetPlayer(User player) {
        //Reset shots taken and number of shots to initial values
        player.setNumberOfShots(6);

        //Make sure both deck and hand are empty before adding action moves
        player.getDeckCards().clear();
        player.getHandCards().clear();

        //Set properties bi-directional
        for (Move move : createActionMoves()) {
            move.setUser(player);
            player.getDeckCards().add(move);
        }

        //Add blocker moves
        for (int i = 0; i < player.getShotsTaken(); i++) {
            BlockerMove blockerMove = new BlockerMove();
            blockerMove.setUser(player);
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
}
