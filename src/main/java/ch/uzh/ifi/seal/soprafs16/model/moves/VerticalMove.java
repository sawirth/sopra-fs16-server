package ch.uzh.ifi.seal.soprafs16.model.moves;

import ch.uzh.ifi.seal.soprafs16.constant.ActionMoveType;
import ch.uzh.ifi.seal.soprafs16.model.Move;

public class VerticalMove extends Move {

    public VerticalMove() {
        this.setActionMoveType(ActionMoveType.VERTICAL);
    }
}
