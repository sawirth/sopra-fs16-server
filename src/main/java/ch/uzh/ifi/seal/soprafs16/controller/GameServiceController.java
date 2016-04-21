package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.GameConstants;
import ch.uzh.ifi.seal.soprafs16.constant.CharacterType;
import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.helper.UserUtils;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.RoundRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs16.service.GameInitializeService;
import ch.uzh.ifi.seal.soprafs16.service.RoundService;
import com.fasterxml.jackson.annotation.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@RestController
public class GameServiceController
        extends GenericService {

    Logger logger  = LoggerFactory.getLogger(GameServiceController.class);

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoundRepository roundRepo;

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private GameInitializeService gameInitializeService;

    @Autowired
    private RoundService roundService;

    private static final String   CONTEXT = "/games";


    @RequestMapping(value = CONTEXT)
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.Public.class)
    public List<Game> listGames() {
        logger.debug("listGames");
        List<Game> result = new ArrayList<>();
        gameRepo.findAll().forEach(result::add);
        return result;
    }

    @RequestMapping(value = CONTEXT + "/new", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @JsonView(Views.Public.class)
    public ResponseEntity<Game> createGame(@RequestParam("token") String token) {

        Game game = new Game();
        User owner = userRepo.findByToken(token);

        if (owner == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!UserUtils.isInOpenGame(owner)) {
            owner.setCharacterType(CharacterType.CHEYENNE);
            game.setOwner(owner.getUsername());
            game.setStatus(GameStatus.PENDING);
            game.setCurrentPlayer(0);
            game.getPlayers().add(owner);
            game.addLog(owner.getCharacterType(), "Game created by " + owner.getUsername());
            game = gameRepo.save(game);

            logger.info("Game " + game.getId() + " successfully created");
            return ResponseEntity.ok(game);
        } else if (owner.getGames().isEmpty()) {
            logger.info("User already created or joined a game");
            return new ResponseEntity<>(HttpStatus.PRECONDITION_REQUIRED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(value = CONTEXT + "/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.Extended.class)
    public Game getGame(@PathVariable Long gameId) {
        logger.info("getGame: " + gameId);
        return gameRepo.findOne(gameId);
    }


    @RequestMapping(value = CONTEXT + "/{gameId}/start", method = RequestMethod.POST)
    @JsonView(Views.Extended.class)
    public ResponseEntity<Game> startGame(@PathVariable Long gameId, @RequestParam("token") String userToken) {
        Game game = gameRepo.findOne(gameId);
        User owner = userRepo.findByToken(userToken);

        if (game == null || owner == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (game.getOwner().equals(owner.getUsername()) && game.getPlayers().size() >= GameConstants.MIN_PLAYERS
                && game.getStatus() == GameStatus.PENDING) {

            game.setStatus(GameStatus.RUNNING);
            game.addLog(owner.getCharacterType(), owner.getUsername() + " has started the game");
            //initializes the train, rounds for this game, and gives users treasures
            game.setTrain(gameInitializeService.createTrain(game.getPlayers()));
            gameInitializeService.giveUsersTreasure(game.getPlayers());

            //initializes the rounds with the number of rounds that will be played
            for (Round round : gameInitializeService.initializeRounds(5, game)) {
                round.setGame(game);
                round = roundRepo.save(round);
                game.getRounds().add(round);
            }

            //Draw cards for first round
            Iterator<User> iter = game.getPlayers().iterator();

            while (iter.hasNext()) {
                User u = iter.next();
                roundService.resetPlayer(u, game);
                roundService.drawStartCards(u);
                //Reset shots taken and number of shots to initial values
                u.setNumberOfShots(6);
            }

            game.addLog(game.getPlayers().get(0).getCharacterType(), "It's " + game.getPlayers().get(0).getUsername() + "'s turn");
            game = gameRepo.save(game);
            logger.info("Game " + game.getId() + " started");
            return ResponseEntity.ok(game);
        }
        else if(game.getPlayers().size() < GameConstants.MIN_PLAYERS){
            logger.error("Couldn't start game: Number of Minimum players required");
            return new ResponseEntity<>(HttpStatus.PRECONDITION_REQUIRED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @RequestMapping(value = CONTEXT + "/{gameId}/stop", method = RequestMethod.PUT)
    public HttpStatus stopGame(@PathVariable Long gameId, @RequestParam("token") String userToken) {

        Game game = gameRepo.findOne(gameId);
        User owner = userRepo.findByToken(userToken);

        if (game == null || owner == null) {
            return HttpStatus.NOT_FOUND;
        }

        if (game.getOwner().equals(owner.getUsername())) {
            game.setStatus(GameStatus.FINISHED);
            gameRepo.save(game);
            logger.info("Game " + gameId + " finished");
            return HttpStatus.OK;
        } else {
            logger.info("Game " + gameId + " cannot be stopped. User is not  the owner");
            return HttpStatus.BAD_REQUEST;
        }
    }


    @RequestMapping(value = CONTEXT + "/{gameId}/player")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.Public.class)
    public List<User> listPlayers(@PathVariable Long gameId) {
        logger.info("listPlayers");

        Game game = gameRepo.findOne(gameId);
        if (game != null) {
            return game.getPlayers();
        }

        return new ArrayList<>();
    }


    @RequestMapping(value = CONTEXT + "/{gameId}/player", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.Public.class)
    public ResponseEntity<Game> addPlayer(@PathVariable Long gameId, @RequestParam("token") String userToken) {

        Game game = gameRepo.findOne(gameId);
        User player = userRepo.findByToken(userToken);

        if (game == null || player == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (game.getPlayers().size() < GameConstants.MAX_PLAYERS && !UserUtils.isInOpenGame(player)) {
            List<CharacterType> allCharacters = new ArrayList<>();
            allCharacters.add(CharacterType.BELLE);
            allCharacters.add(CharacterType.DOC);
            allCharacters.add(CharacterType.GHOST);
            allCharacters.add(CharacterType.JANGO);
            allCharacters.add(CharacterType.TUCO);

            for(User user : game.getPlayers()){
                allCharacters.remove(user.getCharacterType());
            }

            player.setCharacterType(allCharacters.get(0));
            game.getPlayers().add(player);
            game.setCurrentPlayer(0);
            game.addLog(player.getCharacterType(), player.getUsername() + " joined the game");
            game = gameRepo.save(game);
            logger.info("Game: " + game.getId() + " - player added: " + player.getUsername());
            return ResponseEntity.ok(game);
        } else if (game.getPlayers().size() == GameConstants.MAX_PLAYERS) {
            logger.error("Already max number of players in chosen game");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else if (player.getGames().get(0).getId() == gameId) {
            //TODO change player games to single field not a list
            return ResponseEntity.ok(game);
        } else {
            logger.error("Error adding player with token, since he already joined a game: " + userToken);
            return new ResponseEntity<>(HttpStatus.PRECONDITION_REQUIRED);
        }
    }


    @RequestMapping(value = CONTEXT + "/{gameId}/player/{playerId}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.Extended.class)
    public User getPlayer(@PathVariable Long gameId, @PathVariable Integer playerId) {
        logger.info("getPlayer: " + gameId);
        Game game = gameRepo.findOne(gameId);
        int id = playerId;
        return game.getPlayers().get(--id);
    }


    @RequestMapping(value = CONTEXT + "/{gameId}/player/remove", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.Public.class)
    public ResponseEntity<Game> removePlayer(@PathVariable Long gameId, @RequestParam("token") String userToken) {
        logger.info("remove Player: " + userToken);

        Game game = gameRepo.findOne(gameId);
        User user = userRepo.findByToken(userToken);

        if (user.getUsername().equals(game.getOwner())) {
            if (game.getPlayers().size() == 1) {
                gameRepo.delete(game);
                user.setGames(null);
                userRepo.save(user);
            } else {
                game.setOwner(game.getPlayers().get(1).getUsername());
                game.getPlayers().remove(0);
                game = gameRepo.save(game);
                return ResponseEntity.ok(game);
            }
        } else {
            for (int i = 0; i < game.getPlayers().size(); i++) {
                if (game.getPlayers().get(i).equals(user)) {
                    game.getPlayers().remove(i);
                    game = gameRepo.save(game);
                    logger.info("Player removed" + userToken);
                    return ResponseEntity.ok(game);
                }
            }
        }

        return new ResponseEntity<>(HttpStatus.MULTI_STATUS);
    }


    @RequestMapping(value = CONTEXT + "/{gameId}/log", method = RequestMethod.GET)
    public ResponseEntity<List<LogEntry>> getGameLog(@PathVariable Long gameId) {
        Game game = gameRepo.findOne(gameId);

        if (game != null) {
            return ResponseEntity.ok(game.getGameLog().getLogEntryList());
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}