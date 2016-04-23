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
        List<Target> targets = new ArrayList<>();
        List<Wagon> train = super.getGame().getTrain();

        //Find position of wagon that the user is on
        int wagonPosition = 0;
        boolean isUpperLevel = false;
        for (Wagon w : train) {
            if (w.getLowerLevel().getUsers().contains(super.getUser())) {
                break;
            }

            if (w.getUpperLevel().getUsers().contains(super.getUser())) {
                isUpperLevel = true;
                break;
            }
            wagonPosition++;
        }


        if (wagonPosition == 0) {
        //Lokomotive
            if (isUpperLevel) {
                for (int i = 1; i < train.size() && i <= 3; i++) {
                    targets.add(train.get(i).getUpperLevel());
                }
            } else {
                targets.add(train.get(wagonPosition + 1).getLowerLevel());
            }
        } else if (wagonPosition == train.size() - 1) {
            //letzter Wagen
            if (isUpperLevel) {
                for (int i = wagonPosition - 1; i >= 0 && i >= wagonPosition - 3; i--) {
                    targets.add(train.get(i).getUpperLevel());
                }
            } else {
                targets.add(train.get(wagonPosition - 1). getLowerLevel());
            }
        } else {
            //dazwischen
            if (isUpperLevel) {
                //nach hinten bzw. rechts
                for (int i = wagonPosition + 1; i < train.size() && i <= 3; i++) {
                    targets.add(train.get(i).getUpperLevel());
                }

                //nach vorne bzw. links
                for (int i = wagonPosition - 1; i >= 0 && i >= wagonPosition - 3; i--) {
                    targets.add(train.get(i).getUpperLevel());
                }
            } else {
                targets.add(train.get(wagonPosition + 1).getLowerLevel());
                targets.add(train.get(wagonPosition - 1).getLowerLevel());
            }
        }
        return targets;
    }
}
