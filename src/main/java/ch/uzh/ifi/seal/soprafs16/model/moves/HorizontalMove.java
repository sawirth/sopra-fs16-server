package ch.uzh.ifi.seal.soprafs16.model.moves;

import ch.uzh.ifi.seal.soprafs16.constant.ActionMoveType;
import ch.uzh.ifi.seal.soprafs16.model.Move;
import ch.uzh.ifi.seal.soprafs16.model.Target;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity
@DiscriminatorValue(value = "horizontal_move")
public class HorizontalMove extends Move {

    public HorizontalMove() {
        this.setActionMoveType(ActionMoveType.HORIZONTAL);
    }

    @Override
    public void executeAction() {
        //TODO implement horizontal move action
    }

    @Override
    public List<Target> calculateTargets() {
        //TODO calculate targets
        return null;
    }
}
