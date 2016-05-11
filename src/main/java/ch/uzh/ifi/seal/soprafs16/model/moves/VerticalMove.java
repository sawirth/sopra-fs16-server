package ch.uzh.ifi.seal.soprafs16.model.moves;

import ch.uzh.ifi.seal.soprafs16.constant.ActionMoveType;
import ch.uzh.ifi.seal.soprafs16.helper.TargetHelper;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.service.GameService;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue(value = "vertical_move")
public class VerticalMove extends Move {

    public VerticalMove() {
        this.setActionMoveType(ActionMoveType.VERTICAL);
    }

    @Override
    public void executeAction(Target target) {
        GameService gameService = new GameService();
        gameService.switchLevel(super.getGame().getTrain(), (Level) target, super.getUser());

        super.getGame().addLog(super.getCharacterType(),super.getUser().getUsername()+ " moved to another level in his wagon");

        //checks for Marshals position
        gameService.checkForMarshalInWagon(super.getGame());

    }

    @Override
    public List<Target> calculateTargets() {
        List<Target> targets = new ArrayList<>();
        User user = super.getUser();
        List<Wagon> train = super.getGame().getTrain();

        int wagonPosition = TargetHelper.getWagonPositionOfUser(user, train);
        if (TargetHelper.isOnUpperLevel(user, train)) {
            targets.add(train.get(wagonPosition).getLowerLevel());
        } else {
            targets.add(train.get(wagonPosition).getUpperLevel());
        }

        super.setPossibleTargets(targets);
        return targets;
    }

    @Override
    public void resetActionMoveType() {
        super.setActionMoveType(ActionMoveType.VERTICAL);
    }
}
