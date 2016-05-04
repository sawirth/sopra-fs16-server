package ch.uzh.ifi.seal.soprafs16.model.moves;

import ch.uzh.ifi.seal.soprafs16.constant.ActionMoveType;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.service.GameService;

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
    public void executeAction(Target target) {
        Game game = super.getGame();
        GameService gameService = new GameService();
        List<Wagon> train = game.getTrain();
        Level level = (Level) target;

        //find and remove marshal from wagon
        for (Wagon wagon : train) {
            if (wagon.hasMarshal()) {
                wagon.setHasMarshal(false);
                break;
            }
        }

        //change position
        level.getWagon().setHasMarshal(true);
        game.addLog(super.getCharacterType(),super.getUser().getUsername()+" switched Marshals Position");

        //check for Marshals position
        gameService.checkForMarshalInWagon(game);
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

        super.setPossibleTargets(targets);
        return targets;
    }

    @Override
    public void resetActionMoveType() {
        super.setActionMoveType(ActionMoveType.SWITCH_MARSHAL);
    }
}
