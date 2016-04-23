package ch.uzh.ifi.seal.soprafs16.model.moves;

import ch.uzh.ifi.seal.soprafs16.constant.ActionMoveType;
import ch.uzh.ifi.seal.soprafs16.model.Move;
import ch.uzh.ifi.seal.soprafs16.model.Target;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity
@DiscriminatorValue(value = "marshal_move")
public class ChangeMarshalPositionMove extends Move {

    public ChangeMarshalPositionMove() {
        this.setActionMoveType(ActionMoveType.SWITCH_MARSHAL);
    }

    @Override
    public void executeAction() {
        //TODO implement switch marshal action
    }

    @Override
    public List<Target> calculateTargets() {
        //TODO calculate targets
        return null;
    }
}
