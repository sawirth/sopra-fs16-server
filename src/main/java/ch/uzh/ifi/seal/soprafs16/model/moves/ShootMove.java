package ch.uzh.ifi.seal.soprafs16.model.moves;

import ch.uzh.ifi.seal.soprafs16.constant.ActionMoveType;
import ch.uzh.ifi.seal.soprafs16.model.Move;
import ch.uzh.ifi.seal.soprafs16.model.Target;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity
@DiscriminatorValue(value = "shoot_move")
public class ShootMove extends Move {

    public ShootMove() {
        this.setActionMoveType(ActionMoveType.SHOOT);
    }

    @Override
    public void executeAction() {
        //TODO implement shoot action
    }

    @Override
    public List<Target> calculateTargets() {
        //TODO caculate targets
        return null;
    }
}
