package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.ActionMoveType;
import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.model.*;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("gameService")
public class GameService {

    public Game updateGameAfterMove(Game game){

        RoundService roundService = new RoundService();

        //if move stack is not empty yet
        if (!game.getActionMoves().isEmpty()){
            Move move = game.getActionMoves().peek();

            if (move.getActionMoveType() == ActionMoveType.DRAW){
                game.addLog(move.getCharacterType(), move.getUser().getUsername()+" drew cards in planning phase");
                game.getActionMoves().pop();
                return updateGameAfterMove(game);
            }
        }

        //check if stack is empty cannot be else since the first if statement has to be checked before
        if (game.getActionMoves().isEmpty()){
            Round round = game.getRounds().get(game.getCurrentRound());

            //check if this round was the last one of the game
            if (game.getRounds().size() == game.getCurrentRound()+1){

                round.getRoundFinisher().finishRound(game);
                game.addLog(null, "Game finished");

                setGameResults(game);

                game.setStatus(GameStatus.FINISHED);
                return game;
            }
            else {
                //check if round has an round finisher
                if (round.getRoundFinisher() != null) {
                    round.getRoundFinisher().finishRound(game);
                } else {
                    game.addLog(null, "Round has been finished with no event");
                }

                //set current round +1
                game.addLog(null, "A new round has started, get ready!");
                game.setCurrentRound(game.getCurrentRound()+1);
                game.setCurrentPlayer(game.getRounds().get(game.getCurrentRound()).getFirstPlayer());

                for (User user: game.getPlayers()){
                    roundService.resetPlayer(user, game);
                    roundService.drawStartCards(user);
                }

                return game;
            }
        }

        game.setCurrentPlayer(game.getPlayers().indexOf(game.getActionMoves().peek().getUser()));

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

    public void checkForMarshalInWagon(Game game){
        for (Wagon wagon: game.getTrain()) {
            //the wagon where the marshal is on
            if (wagon.hasMarshal()){
                List<User> users = wagon.getLowerLevel().getUsers();
                //no event, no add to game log
                if (users.isEmpty()){
                    return;
                }
                else{
                    for (User user: users) {
                        user.setShotsTaken(user.getShotsTaken() + 1);
                    }
                    wagon.getUpperLevel().getUsers().addAll(users);
                    for (User user: users){
                        game.addLog(user.getCharacterType(),user.getUsername()+ " got shot by the marshal and jumped to the top");
                    }
                    wagon.getLowerLevel().getUsers().clear();
                    return;
                }
            }
        }
    }

    /**
     * calculates the gunslingers and adds the userResults to the game
     * sorts the UserResults by the amount of total money
     *
     * @param game
     */
    public void setGameResults(Game game) {
        int fewestNumberOfShots = 6;

        for (int i = 0; i < game.getPlayers().size(); i++) {
            if (game.getPlayers().get(i).getNumberOfShots() < fewestNumberOfShots) {
                fewestNumberOfShots = game.getPlayers().get(i).getNumberOfShots();
            }
        }

        for (User user : game.getPlayers()) {
            if (user.getNumberOfShots() == fewestNumberOfShots) {
                game.getUserResults().add(new UserResult(user, true));
            } else {
                game.getUserResults().add(new UserResult(user, false));
            }
        }

        Collections.sort(game.getUserResults(), (o1, o2) -> {
            Integer x1 = o1.getTotalMoney();
            Integer x2 = o2.getTotalMoney();
            int sComp = x2.compareTo(x1);

            if (sComp != 0) {
                return sComp;
            } else {
                x1 = o1.getNumberOfShotsTaken();
                x2 = o2.getNumberOfShotsTaken();
                return x1.compareTo(x2);
            }
        });
    }
}