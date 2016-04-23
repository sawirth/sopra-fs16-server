package ch.uzh.ifi.seal.soprafs16.model.moves;

import ch.uzh.ifi.seal.soprafs16.constant.ActionMoveType;
import ch.uzh.ifi.seal.soprafs16.helper.TargetHelper;
import ch.uzh.ifi.seal.soprafs16.model.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue(value = "hit_move")
public class HitMove extends Move {

    public HitMove() {
        this.setActionMoveType(ActionMoveType.HIT);
    }

    @Override
    public void executeAction() {
        //TODO implement hit action
    }

    @Override
    public List<Target> calculateTargets() {
        Game game = super.getGame();
        User user = super.getUser();
        List<Wagon> train = game.getTrain();

        //Find Level of user
        List<Target> targets = new ArrayList<>();
        for (Wagon w : train) {
            if (w.getUpperLevel().getUsers().contains(user)) {
                targets.addAll(w.getUpperLevel().getUsers());
                targets.remove(user);
                break;
            }

            if (w.getLowerLevel().getUsers().contains(user)) {
                targets.addAll(w.getLowerLevel().getUsers());
                targets.remove(user);
                break;
            }
        }


        if (targets.size() > 1) {
            targets = TargetHelper.removeBelle(targets);
        }

        super.setPossibleTargets(targets);
        return targets;
    }
}
