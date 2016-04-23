package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.constant.ActionMoveType;
import ch.uzh.ifi.seal.soprafs16.constant.CharacterType;
import ch.uzh.ifi.seal.soprafs16.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs16.service.RoundService;
import ch.uzh.ifi.seal.soprafs16.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(UserServiceController.CONTEXT)
public class UserServiceController
        extends GenericService {

    Logger                 logger  = LoggerFactory.getLogger(UserServiceController.class);

    static final String    CONTEXT = "/users";

    @Autowired
    private UserService userService = new UserService();

    @Autowired
    private RoundService roundService = new RoundService();

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private GameRepository gameRepo;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.Public.class)
    public List<User> listUsers() {
        logger.debug("listUsers");

        List<User> result = new ArrayList<>();
        userRepo.findAll().forEach(result::add);

        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @JsonView(Views.Extended.class)
    public ResponseEntity<User> addUser(@RequestBody User user) {
        logger.debug("addUser: " + user);

        User u = user;
        u.setStatus(UserStatus.ONLINE);
        u.setToken(UUID.randomUUID().toString());
        try {
            u = userRepo.save(u);
        } catch (DataIntegrityViolationException e) {
            logger.error("Username " + u.getUsername() + " already exists");
            logger.info(e.getMessage());
            return ResponseEntity.status(409).body(u);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(u);
    }


    @RequestMapping(method = RequestMethod.GET, value = "{userId}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.Extended.class)
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        logger.debug("getUser: " + userId);

        User user =  userRepo.findOne(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(method = RequestMethod.POST, value = "/login")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.Extended.class)
    public ResponseEntity<User> login(@RequestParam String username) {
        logger.debug("login: " + username);

        User user = userRepo.findByUsername(username);
        if (user != null) {
            userRepo.save(UserService.login(user));
            logger.info("Logged in: "+username);
            return ResponseEntity.ok(user);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/logout")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.Extended.class)
    public ResponseEntity<User> logout(@RequestParam("username") String username) {
        logger.debug("Logout: " + username);

        User user = userRepo.findByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(userRepo.save(UserService.logout(user)));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/character")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.Extended.class)
    public ResponseEntity<User> chooseCharacter(@RequestParam("token") String userToken, @RequestParam("character") CharacterType characterType) {
        logger.info(userToken+"choosed Character: "+characterType);
        User user = userRepo.findByToken(userToken);

        if (user != null) {
            user.setCharacterType(characterType);
            userRepo.save(user);
            return ResponseEntity.ok(user);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/draw")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.Extended.class)
    public ResponseEntity<User> drawCards(@RequestParam("token") String userToken) {
        User user = userRepo.findByToken(userToken);
        Game game = userService.findRunningGame(user);

        if (game == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (game.getPlayers().get(game.getCurrentPlayer()) != user){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else {
            Move move = new Move();
            move.setUser(user);
            move.setCharacterType(user.getCharacterType());
            move.setActionMoveType(ActionMoveType.DRAW);
            roundService.drawDeckCards(user);

            Round round = game.getRounds().get(game.getCurrentRound());
            round.getMoves().add(move);
            roundService.updateGameAfterMove(game);

            gameRepo.save(game);
            return ResponseEntity.ok(user);
        }
    }
}
