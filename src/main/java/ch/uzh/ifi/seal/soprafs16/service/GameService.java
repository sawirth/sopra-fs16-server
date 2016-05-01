package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.ActionMoveType;
import ch.uzh.ifi.seal.soprafs16.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("gameService")
public class GameService {

    public Game updateGameAfterMove(Game game){

        RoundService roundService = new RoundService();

        //if move stack is not empty yet
        if (!game.getActionMoves().isEmpty()){
            Move move = game.getActionMoves().peek();
            game.addLog(move.getCharacterType(), "It's " +game.getActionMoves().peek().getUser().getUsername()+"'s turn");

            if (move.getActionMoveType() == ActionMoveType.DRAW){
                game.addLog(move.getCharacterType(), move.getUser().getUsername()+" drew cards in planning phase");
                game.getActionMoves().pop();
                game.addLog(move.getCharacterType(), "It's " +game.getActionMoves().peek().getUser().getUsername()+"'s turn");
            }
        }

        //check if stack is empty cannot be else since the first if statement has to be checked before
        if (game.getActionMoves().isEmpty()){
            Round round = game.getRounds().get(game.getCurrentRound());

            //check if this round was the last one of the game
            if (game.getRounds().size()==game.getCurrentRound()-1){
                //TODO finish game

                round.getRoundFinisher().finishRound(game);
                game.addLog(null, "Round has been finished with event "+round.getRoundType().toString());
                game.addLog(null, "Game finished");
                return game;
            }
            else {
                if (round.getRoundFinisher() != null) {
                    round.getRoundFinisher().finishRound(game);
                    game.addLog(null, "Round has been finished with event " + round.getRoundType().toString());
                } else {
                    game.addLog(null, "Round has been finished");
                }


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

    /**
     * Removes the user from the current level and adds it to another
     * @param train The train from the game
     * @param level Level to which the user is added
     * @param user The user who switches the level
     */
    public void switchLevel(List<Wagon> train, Level level, User user) {
        //Remove from current level
        Level oldLevel = new Level();
        for (Wagon wagon : train) {
            if (wagon.getUpperLevel().getUsers().contains(user)) {
                oldLevel = wagon.getUpperLevel();
                break;
            } else if (wagon.getLowerLevel().getUsers().contains(user)) {
                oldLevel = wagon.getLowerLevel();
                break;
            }
        }

        if (!oldLevel.getUsers().isEmpty()) {
            oldLevel.getUsers().remove(user);
        }

        //Add to new level
        level.getUsers().add(user);
    }
}
