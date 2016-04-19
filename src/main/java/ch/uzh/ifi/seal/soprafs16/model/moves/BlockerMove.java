package ch.uzh.ifi.seal.soprafs16.model.moves;

import ch.uzh.ifi.seal.soprafs16.constant.ActionMoveType;
import ch.uzh.ifi.seal.soprafs16.model.Move;

public class BlockerMove extends Move {

    public BlockerMove() {
        this.setActionMoveType(ActionMoveType.BLOCKER);
    }
}
