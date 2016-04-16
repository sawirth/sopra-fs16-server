package ch.uzh.ifi.seal.soprafs16.model.moves;

import ch.uzh.ifi.seal.soprafs16.constant.ActionMoveType;
import ch.uzh.ifi.seal.soprafs16.model.Move;

public class ShootMove extends Move {

    public ShootMove() {
        this.setActionMoveType(ActionMoveType.SHOOT);
    }
}
