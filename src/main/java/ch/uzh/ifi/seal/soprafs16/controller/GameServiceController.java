package ch.uzh.ifi.seal.soprafs16.controller;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ch.uzh.ifi.seal.soprafs16.GameConstants;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Move;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;


@RestController
public class GameServiceController
        extends GenericService {

    Logger                 logger  = LoggerFactory.getLogger(GameServiceController.class);

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private GameRepository gameRepo;

    private final String   CONTEXT = "/game";

    /*
     * Context: /game
     */

    @RequestMapping(value = CONTEXT)
    @ResponseStatus(HttpStatus.OK)
    public List<Game> listGames() {
        logger.debug("listGames");
        List<Game> result = new ArrayList<>();
        gameRepo.findAll().forEach(result::add);
        return result;
    }

    @RequestMapping(value = CONTEXT + "/new", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Game createGame(@RequestBody User user) {
        logger.info("Create new game");

        Game game = new Game();
        User owner = userRepo.findOne(user.getId());

        if (owner != null) {
            game.setOwner(owner.getUsername());
            game.setStatus(GameStatus.PENDING);
            game.setCurrentPlayer(1);
            game.getPlayers().add(owner);
            game = gameRepo.save(game);

            logger.info("Game " + game.getId() + " successfully created");
            return game;
        }

        return null;
    }

    /*
     * Context: /game/{game-id}
     */
    @RequestMapping(value = CONTEXT + "/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    public Game getGame(@PathVariable Long gameId) {
        logger.info("getGame: " + gameId);
        Game game = gameRepo.findOne(gameId);
        return game;
    }

    @RequestMapping(value = CONTEXT + "/{gameId}/start", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void startGame(@PathVariable Long gameId, @RequestBody User user) {
        Game game = gameRepo.findOne(gameId);
        User owner = userRepo.findOne(user.getId());

        if (owner != null && game != null && game.getOwner().equals(owner.getUsername())) {
            game.setStatus(GameStatus.RUNNING);
            gameRepo.save(game);
            logger.info("Game " + game.getId() + " started");
        }
    }

    @RequestMapping(value = CONTEXT + "/{gameId}/stop", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void stopGame(@PathVariable Long gameId, @RequestParam("token") String userToken) {
        logger.info("stopGame: " + gameId);

        Game game = gameRepo.findOne(gameId);
        User owner = userRepo.findByToken(userToken);

        if (owner != null && game != null && game.getOwner().equals(owner.getUsername())) {
            // TODO: Stop game
        }
    }

    /*
     * Context: /game/{game-id}/move
     */
    @RequestMapping(value = CONTEXT + "/{gameId}/move")
    @ResponseStatus(HttpStatus.OK)
    public List<Move> listMoves(@PathVariable Long gameId) {
        logger.debug("listMoves");

        Game game = gameRepo.findOne(gameId);
        if (game != null) {
            return game.getMoves();
        }

        return null;
    }

    @RequestMapping(value = CONTEXT + "/{gameId}/move", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void addMove(@RequestBody Move move) {
        logger.debug("addMove: " + move);
        // TODO Mapping into Move + execution of move
    }

    @RequestMapping(value = CONTEXT + "/{gameId}/move/{moveId}")
    @ResponseStatus(HttpStatus.OK)
    public Move getMove(@PathVariable Long gameId, @PathVariable Integer moveId) {
        logger.debug("getMove: " + gameId);

        Game game = gameRepo.findOne(gameId);
        if (game != null) {
            return game.getMoves().get(moveId);
        }

        return null;
    }

    /*
     * Context: /game/{game-id}/player
     */
    @RequestMapping(value = CONTEXT + "/{gameId}/player")
    @ResponseStatus(HttpStatus.OK)
    public List<User> listPlayers(@PathVariable Long gameId) {
        logger.info("listPlayers");

        Game game = gameRepo.findOne(gameId);
        if (game != null) {
            return game.getPlayers();
        }

        return null;
    }

    @RequestMapping(value = CONTEXT + "/{gameId}/player", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String addPlayer(@PathVariable Long gameId, @RequestParam("token") String userToken) {
        logger.info("addPlayer: " + userToken);

        Game game = gameRepo.findOne(gameId);
        User player = userRepo.findByToken(userToken);

        if (game != null && player != null && game.getPlayers().size() < GameConstants.MAX_PLAYERS) {
            game.getPlayers().add(player);
            game = gameRepo.save(game);
            logger.info("Game: " + game.getId() + " - player added: " + player.getUsername());
            return CONTEXT + "/" + gameId + "/player/" + (game.getPlayers().size());
        } else {
            logger.error("Error adding player with token: " + userToken);
        }
        return null;
    }

    @RequestMapping(value = CONTEXT + "/{gameId}/player/{playerId}")
    @ResponseStatus(HttpStatus.OK)
    public User getPlayer(@PathVariable Long gameId, @PathVariable Integer playerId) {
        logger.info("getPlayer: " + gameId);

        Game game = gameRepo.findOne(gameId);
        return game.getPlayers().get(--playerId);
    }

}