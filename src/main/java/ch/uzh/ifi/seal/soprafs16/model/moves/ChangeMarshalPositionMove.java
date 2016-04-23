package ch.uzh.ifi.seal.soprafs16.model.moves;

import ch.uzh.ifi.seal.soprafs16.constant.ActionMoveType;
import ch.uzh.ifi.seal.soprafs16.model.Move;
import ch.uzh.ifi.seal.soprafs16.model.Target;
import ch.uzh.ifi.seal.soprafs16.model.Wagon;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.ArrayList;
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
        List<Target> targets = new ArrayList<>();
        List<Wagon> train = super.getGame().getTrain();

        int position = 0;
        for (Wagon w : train) {
            if (w.hasMarshal()) {
                break;
            }
            position++;
        }

        if (position == 0) {
            targets.add(train.get(position + 1).getLowerLevel());
        } else if (position == train.size() - 1) {
            targets.add(train.get(position - 1).getLowerLevel());
        } else {
            targets.add(train.get(position - 1).getLowerLevel());
            targets.add(train.get(position + 1).getLowerLevel());
        }

        return targets;
    }
}
