package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.ActionMoveType;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Move;
import ch.uzh.ifi.seal.soprafs16.model.Target;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by David on 27.04.2016.
 */
@Service("gameService")
public class GameService {

    public Game updateGameAfterMove(Game game){
        //Todo load new round or end game if moveStack is empty

        Move move = game.getActionMoves().peek();
        game.getGameLog().addLog(move.getCharacterType(), "It's " +game.getActionMoves().peek().getUser().getUsername()+"'s turn");

        if (move.getActionMoveType() == ActionMoveType.DRAW){
            game.getGameLog().addLog(move.getCharacterType(), move.getUser().getUsername()+" drew cards in planning phase");
            game.getActionMoves().pop();
            return game;
        }

        return game;
    }

    /**
     * Checks if target is in the list of targets
     *
     * @param move
     * @param id
     * @return true if in targets else false
     */
    public Target checkTarget(Move move, Long id){
        List<Target> targets = move.calculateTargets();
        for (Target target: targets){
            if (target.getId().equals(id)){
                return target;
            }
        }

        return null;
    }
}
