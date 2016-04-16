package ch.uzh.ifi.seal.soprafs16.model.moves;

import ch.uzh.ifi.seal.soprafs16.constant.ActionMoveType;
import ch.uzh.ifi.seal.soprafs16.model.Move;

public class ChangeMarshalPositionMove extends Move {

    public ChangeMarshalPositionMove() {
        this.setActionMoveType(ActionMoveType.SWITCH_MARSHAL);
    }
}
