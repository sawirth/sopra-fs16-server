package ch.uzh.ifi.seal.soprafs16.model.moves;

import ch.uzh.ifi.seal.soprafs16.constant.ActionMoveType;
import ch.uzh.ifi.seal.soprafs16.model.Move;
import ch.uzh.ifi.seal.soprafs16.model.Target;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue(value = "blocker_move")
public class BlockerMove extends Move {

    public BlockerMove() {
        this.setActionMoveType(ActionMoveType.BLOCKER);
    }

    @Override
    public void executeAction(Target target) {
    }

    @Override
    public List<Target> calculateTargets() {
        return new ArrayList<>();
    }

    @Override
    public void resetActionMoveType() {
    }
}
