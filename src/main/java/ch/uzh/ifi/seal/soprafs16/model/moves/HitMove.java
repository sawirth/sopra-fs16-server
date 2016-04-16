package ch.uzh.ifi.seal.soprafs16.model.moves;

import ch.uzh.ifi.seal.soprafs16.constant.ActionMoveType;
import ch.uzh.ifi.seal.soprafs16.model.Move;

public class HitMove extends Move {

    public HitMove() {
        this.setActionMoveType(ActionMoveType.HIT);
    }
}
