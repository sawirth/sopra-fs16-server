package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.MoveRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs16.service.RoundService;
import com.fasterxml.jackson.annotation.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(MoveServiceController.CONTEXT)
public class MoveServiceController {

    Logger logger = LoggerFactory.getLogger(MoveServiceController.class);
    static final String CONTEXT = "/moves";

    @Autowired
    private MoveRepository moveRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoundService roundService;

    @Autowired
    private GameRepository gameRepo;


    @RequestMapping(method = RequestMethod.GET, value = "{moveId}")
    @JsonView(Views.Extended.class)
    public ResponseEntity<Move> getMove(@PathVariable Long moveId) {
        logger.info("Get Move " + moveId);
        Move move = moveRepo.findOne(moveId);

        if (move != null) {
            List<Target> targets = move.calculateTargets();
            moveRepo.save(move);
            return ResponseEntity.ok(move);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * This endpoint is used by the user to make his turn. The move will be removed from his hand and put into the list
     * of moves of the current round
     * @param moveId The id of the move
     * @return HttpStatus
     */
    @RequestMapping(method = RequestMethod.POST, value = "{moveId}")
    @ResponseBody
    @JsonView(Views.Extended.class)
    public ResponseEntity<User> makeMove(@PathVariable Long moveId, @RequestParam String token) {

        //Check that token is of the same user as the move
        Move move = moveRepo.findOne(moveId);
        User user = userRepo.findByToken(token);

        if (user == null) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }

        if (move == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        }

        if (!roundService.isUserAllowedToMakeMove(move, user)) {
            logger.info("User " + user.getId() + " is not allowed to make Move " + move.getId());
            return ResponseEntity.badRequest().body(user);
        }

        Game game = move.getGame();
        Round round = game.getRounds().get(game.getCurrentRound());

        if(round.isActionPhase()){
            logger.info("Action phase has begun: User isn't allowed to make move");
            return ResponseEntity.badRequest().body(user);
        }

        //shift Move
        roundService.shiftMove(move, round);

        game = roundService.updateGameAfterMove(game);
        user = userRepo.save(user);

        gameRepo.save(game);
        logger.info("User " + user.getId() + " makes Move " + move.getId() + ": " + move.getActionMoveType());

        //TODO Stop round on last move and start action phase

        return ResponseEntity.ok(user);
    }
}
