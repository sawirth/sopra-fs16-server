package ch.uzh.ifi.seal.soprafs16.model.moves;

import ch.uzh.ifi.seal.soprafs16.constant.ActionMoveType;
import ch.uzh.ifi.seal.soprafs16.helper.TargetHelper;
import ch.uzh.ifi.seal.soprafs16.model.Level;
import ch.uzh.ifi.seal.soprafs16.model.Move;
import ch.uzh.ifi.seal.soprafs16.model.Target;
import ch.uzh.ifi.seal.soprafs16.model.Wagon;
import ch.uzh.ifi.seal.soprafs16.service.GameService;

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
    public void executeAction(Target target) {
        GameService gameService = new GameService();
        gameService.switchLevel(super.getGame().getTrain(), (Level) target, super.getUser());

        super.getGame().addLog(super.getCharacterType(),super.getUser().getUsername()+" moved to another wagon");

        //checks for Marshals position
        gameService.checkForMarshalInWagon(super.getGame());
    }

    @Override
    public List<Target> calculateTargets() {
        List<Target> targets = new ArrayList<>();
        List<Wagon> train = super.getGame().getTrain();

        //Find position of wagon that the user is on
        int wagonPosition = TargetHelper.getWagonPositionOfUser(super.getUser(), train);
        boolean isUpperLevel = TargetHelper.isOnUpperLevel(super.getUser(), train);

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
                int counter = 0;
                for (int i = wagonPosition + 1; i < train.size() && counter <= 3; i++) {
                    targets.add(train.get(i).getUpperLevel());
                    counter++;
                }

                //nach vorne bzw. links
                counter = 0;
                for (int i = wagonPosition - 1; i >= 0 && counter <= 3; i--) {
                    targets.add(train.get(i).getUpperLevel());
                    counter++;
                }
            } else {
                targets.add(train.get(wagonPosition + 1).getLowerLevel());
                targets.add(train.get(wagonPosition - 1).getLowerLevel());
            }
        }

        super.setPossibleTargets(targets);
        return targets;
    }

    @Override
    public void resetActionMoveType() {
        super.setActionMoveType(ActionMoveType.HORIZONTAL);
    }
}
