package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.ActionMoveType;
import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by David on 27.04.2016.
 */
@Service("gameService")
public class GameService {

    public Game updateGameAfterMove(Game game){

        RoundService roundService = new RoundService();

        //if move stack is not empty yet
        if (!game.getActionMoves().isEmpty()){
            Move move = game.getActionMoves().peek();

            if (move.getActionMoveType() == ActionMoveType.DRAW){
                game.addLog(move.getCharacterType(),"It's " + move.getUser().getUsername() +"'s turn");
                game.addLog(move.getCharacterType(), move.getUser().getUsername()+" drew cards in planning phase");
                game.getActionMoves().pop();
            }
        }

        //check if stack is empty cannot be else since the first if statement has to be checked before
        if (game.getActionMoves().isEmpty()){
            Round round = game.getRounds().get(game.getCurrentRound());

            //check if this round was the last one of the game
            if (game.getRounds().size() == game.getCurrentRound()+1){
                //TODO finish game

                round.getRoundFinisher().finishRound(game);
                game.addLog(null, "Round has been finished with event "+round.getRoundType().toString());
                game.addLog(null, "Game finished");
                game.setStatus(GameStatus.FINISHED);
                return game;
            }
            else {
                round.getRoundFinisher().finishRound(game);
                game.addLog(null, "Round has been finished with event "+round.getRoundType().toString());

                //set current round +1
                game.addLog(null, "New round has started YUHU");
                game.setCurrentRound(game.getCurrentRound()+1);
                game.setCurrentPlayer(game.getRounds().get(game.getCurrentRound()).getFirstPlayer());

                for (User user: game.getPlayers()){
                    roundService.resetPlayer(user, game);
                    roundService.drawStartCards(user);
                }

                game.addLog(game.getPlayers().get(game.getCurrentPlayer()).getCharacterType(),
                        "It's "+ game.getPlayers().get(game.getCurrentPlayer()).getUsername()+"'s turn");

                return game;
            }
        }

        game.addLog(game.getActionMoves().peek().getCharacterType(), "It's " + game.getActionMoves().peek().getUser().getUsername()+"'s turn");

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
