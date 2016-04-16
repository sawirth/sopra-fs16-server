package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.model.Move;
import ch.uzh.ifi.seal.soprafs16.model.Views;
import ch.uzh.ifi.seal.soprafs16.model.repositories.MoveRepository;
import com.fasterxml.jackson.annotation.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(MoveServiceController.CONTEXT)
public class MoveServiceController {

    Logger logger = LoggerFactory.getLogger(MoveServiceController.class);
    static final String CONTEXT = "/moves";

    @Autowired
    private MoveRepository moveRepo;


    @RequestMapping(method = RequestMethod.GET, value = "{moveId}")
    @JsonView(Views.Extended.class)
    public ResponseEntity<Move> getMove(@PathVariable Long moveId) {
        logger.info("Get Move " + moveId);
        Move move = moveRepo.findOne(moveId);

        if (move != null) {
            return ResponseEntity.ok(move);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
