package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.model.Move;
import ch.uzh.ifi.seal.soprafs16.model.Round;
import ch.uzh.ifi.seal.soprafs16.model.Views;
import ch.uzh.ifi.seal.soprafs16.model.repositories.RoundRepository;
import com.fasterxml.jackson.annotation.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(RoundServiceController.CONTEXT)
public class RoundServiceController {

    Logger logger = LoggerFactory.getLogger(RoundServiceController.class);
    static final String CONTEXT = "/rounds";

    @Autowired
    private RoundRepository roundRepo;


    @RequestMapping(method = RequestMethod.GET, value = "{roundId}")
    @JsonView(Views.Extended.class)
    public ResponseEntity<Round> getRound(@PathVariable("roundId") Long roundId) {
        logger.info("getRound " + roundId);
        Round round = roundRepo.findOne(roundId);

        if (round == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(round);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    @JsonView(Views.Public.class)
    public ResponseEntity<List<Round>> listRounds() {
        logger.info("listRounds");
        List<Round> result = new ArrayList<>();
        roundRepo.findAll().forEach(result::add);
        return ResponseEntity.ok(result);
    }


    @RequestMapping(method = RequestMethod.GET, value = "{roundId}/moves")
    @JsonView(Views.Public.class)
    public ResponseEntity<List<Move>> getMoves(@PathVariable("roundId") Long roundId) {
        logger.info("getMoves of round " + roundId);
        Round round = roundRepo.findOne(roundId);

        if (round == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(round.getMoves());
        }
    }
}
