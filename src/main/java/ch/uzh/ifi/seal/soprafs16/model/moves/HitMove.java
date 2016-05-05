package ch.uzh.ifi.seal.soprafs16.model.moves;

import ch.uzh.ifi.seal.soprafs16.constant.ActionMoveType;
import ch.uzh.ifi.seal.soprafs16.constant.CharacterType;
import ch.uzh.ifi.seal.soprafs16.constant.TreasureType;
import ch.uzh.ifi.seal.soprafs16.helper.TargetHelper;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.service.GameService;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue(value = "hit_move")
public class HitMove extends Move {

    public HitMove() {
        this.setActionMoveType(ActionMoveType.HIT);
    }

    @Column
    private int phaseOfMove=0;

    @ManyToOne
    private Target userTarget;

    @Override
    public void executeAction(Target target) {
        switch (phaseOfMove) {
            case 0:
                executeActionChooseUser(target);
                break;
            case 1:
                executeActionChooseTreasure(target);
                break;
            case 2:
                executeActionChooseLevel(target);
                break;
        }
    }

    @Override
    public List<Target> calculateTargets() {
        List<Target> targets = new ArrayList<>();
        switch (phaseOfMove) {
            case 0: return calculateUserTargets(targets);
            case 1: return calculateTreasureTargets(targets);
            case 2: return calculateLevelTargets(targets);
        }
        return targets;
    }


    private List<Target> calculateUserTargets(List<Target> targets){
        Game game = super.getGame();
        User user = super.getUser();
        List<Wagon> train = game.getTrain();

        //Find Level of user
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

    private List<Target> calculateTreasureTargets(List<Target> targets){
        targets.addAll(((User) userTarget).getTreasures());
        super.setPossibleTargets(targets);
        return targets;
    }

    private List<Target> calculateLevelTargets(List<Target> targets){
        int wagonPosition = TargetHelper.getWagonPositionOfUser((User) userTarget, super.getGame().getTrain());
        boolean isUpperLevel = TargetHelper.isOnUpperLevel((User) userTarget, super.getGame().getTrain());

        List<Wagon> train = super.getGame().getTrain();

        if (wagonPosition == 0) {
            //Lokomotive
            if (isUpperLevel) {
                targets.add(train.get(wagonPosition + 1).getUpperLevel());
            }
            else {
                targets.add(train.get(wagonPosition + 1).getLowerLevel());
            }
        } else if (wagonPosition == train.size() - 1) {
            //letzter Wagen
            if (isUpperLevel) {
                targets.add(train.get(wagonPosition - 1).getUpperLevel());
            }
            else {
                targets.add(train.get(wagonPosition - 1). getLowerLevel());
            }
        } else {
            //dazwischen
            if (isUpperLevel) {
                //nach hinten bzw. rechts
                targets.add(train.get(wagonPosition + 1).getUpperLevel());
                targets.add(train.get(wagonPosition - 1).getUpperLevel());
            } else {
                targets.add(train.get(wagonPosition + 1).getLowerLevel());
                targets.add(train.get(wagonPosition - 1).getLowerLevel());
            }
        }

        super.setPossibleTargets(targets);
        return targets;
    }


    private void executeActionChooseLevel(Target target) {
        GameService gameService = new GameService();
        gameService.switchLevel(super.getGame().getTrain(), (Level) target,(User) userTarget);
    }

    private void executeActionChooseTreasure(Target target) {
        ((User) userTarget).getTreasures().remove(target);

        //checks if user has character cheyenne for her special move
        if (super.getCharacterType() == CharacterType.CHEYENNE && ((Treasure) target).getTreasureType()== TreasureType.MONEYBAG){
            super.getUser().getTreasures().add((Treasure) target);
        }
        else {
            int wagonPosition = TargetHelper.getWagonPositionOfUser((User) userTarget, super.getGame().getTrain());
            //gets the level the user stands on
            if (TargetHelper.isOnUpperLevel((User) userTarget, super.getGame().getTrain())) {
                Level level = super.getGame().getTrain().get(wagonPosition).getUpperLevel();
                level.getTreasures().add((Treasure) target);
            }
            else{
                Level level = super.getGame().getTrain().get(wagonPosition).getLowerLevel();
                level.getTreasures().add((Treasure) target);
            }
        }
    }

    private void executeActionChooseUser(Target target) {
        this.setUserTarget(target);
    }

    @Override
    public void resetActionMoveType() {
        super.setActionMoveType(ActionMoveType.HIT);
    }

    public int getPhaseOfMove() {
        return phaseOfMove;
    }

    public void setPhaseOfMove(int pahseOfMove) {
        this.phaseOfMove = pahseOfMove;
    }

    public Target getUserTarget() {
        return userTarget;
    }

    public void setUserTarget(Target userTarget) {
        this.userTarget = userTarget;
    }
}
