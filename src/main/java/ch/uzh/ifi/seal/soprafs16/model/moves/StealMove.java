package ch.uzh.ifi.seal.soprafs16.model.moves;

import ch.uzh.ifi.seal.soprafs16.constant.ActionMoveType;
import ch.uzh.ifi.seal.soprafs16.constant.TreasureType;
import ch.uzh.ifi.seal.soprafs16.model.Move;
import ch.uzh.ifi.seal.soprafs16.model.Target;
import ch.uzh.ifi.seal.soprafs16.model.Treasure;
import ch.uzh.ifi.seal.soprafs16.model.Wagon;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue(value = "steal_move")
public class StealMove extends Move {

    public StealMove() {
        this.setActionMoveType(ActionMoveType.STEAL);
    }

    @Override
    public void executeAction(Target target) {
        super.getUser().getTreasures().add((Treasure) target);
        for (Wagon wagon: super.getGame().getTrain()){
            for (Treasure treasure: wagon.getLowerLevel().getTreasures()){
                if (treasure.getId().equals(target.getId())){
                    wagon.getLowerLevel().getTreasures().remove(treasure);
                    super.getGame().addLog(super.getCharacterType(),super.getUser().getUsername() + " stole a " + treasure.getTreasureType().toString().toLowerCase());
                    return;
                }
            }
            for (Treasure treasure: wagon.getUpperLevel().getTreasures()){
                if (treasure.getId().equals(target.getId())){
                    wagon.getLowerLevel().getTreasures().remove(treasure);
                    super.getGame().addLog(super.getCharacterType(),super.getUser().getUsername() + " stole a " + treasure.getTreasureType().toString().toLowerCase());
                    return;
                }
            }
        }
    }

    @Override
    public List<Target> calculateTargets() {
        List<Target> targets = new ArrayList<>();
        for (Wagon w : super.getGame().getTrain()) {
            if (w.getLowerLevel().getUsers().contains(super.getUser())) {
                targets.addAll(w.getLowerLevel().getTreasures());
                super.setPossibleTargets(targets);
                return targets;
            }

            if (w.getUpperLevel().getUsers().contains(super.getUser())) {
                targets.addAll(w.getUpperLevel().getTreasures());
                super.setPossibleTargets(targets);
                return targets;
            }
        }

        super.setPossibleTargets(targets);
        return targets;
    }

    @Override
    public void resetActionMoveType() {
        super.setActionMoveType(ActionMoveType.STEAL);
    }
}
