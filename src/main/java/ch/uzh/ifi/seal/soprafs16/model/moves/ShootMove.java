package ch.uzh.ifi.seal.soprafs16.model.moves;

import ch.uzh.ifi.seal.soprafs16.constant.ActionMoveType;
import ch.uzh.ifi.seal.soprafs16.constant.CharacterType;
import ch.uzh.ifi.seal.soprafs16.helper.TargetHelper;
import ch.uzh.ifi.seal.soprafs16.model.Move;
import ch.uzh.ifi.seal.soprafs16.model.Target;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.Wagon;
import ch.uzh.ifi.seal.soprafs16.service.GameService;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue(value = "shoot_move")
public class ShootMove extends Move {

    public ShootMove() {
        this.setActionMoveType(ActionMoveType.SHOOT);
    }

    @Override
    public void executeAction(Target target) {
        User victim = (User) target;

        if (victim == null) {
            return;
        }

        victim.setShotsTaken(victim.getShotsTaken() + 1);
        super.getUser().setNumberOfShots(super.getUser().getNumberOfShots() - 1);

        //Django special ability: Move victim one wagon in direction of the shot
        if (super.getUser().getCharacterType() == CharacterType.DJANGO) {
            List<Wagon> train = super.getGame().getTrain();
            int shooterPosition = TargetHelper.getWagonPositionOfUser(super.getUser(), train);
            int victimPosition = TargetHelper.getWagonPositionOfUser((User) target, train);

            //Only move victim if not on first or last wagon
            if (!(victimPosition == super.getGame().getTrain().size() - 1) && victimPosition != 0) {
                GameService gameService = new GameService();
                boolean isUpperLevel = TargetHelper.isOnUpperLevel((User) target, train);
                if (shooterPosition < victimPosition) {
                    //Move towards end of train
                    if (isUpperLevel) {
                        gameService.switchLevel(train, train.get(victimPosition + 1).getUpperLevel(), (User) target);
                    } else {
                        gameService.switchLevel(train, train.get(victimPosition + 1).getLowerLevel(), (User) target);
                    }
                } else {
                    //Move towards begin of train
                    if (isUpperLevel) {
                        gameService.switchLevel(train, train.get(victimPosition - 1).getUpperLevel(), (User) target);
                    } else {
                        gameService.switchLevel(train, train.get(victimPosition - 1).getLowerLevel(), (User) target);
                    }
                }
                gameService.checkForMarshalInWagon(super.getGame());
            }
        }


        super.getGame().addLog(super.getCharacterType(),super.getUser().getUsername()+ " shot at " + ((User) target).getUsername());
    }

    @Override
    public List<Target> calculateTargets() {
        List<Target> targets = new ArrayList<>();
        List<Wagon> train = super.getGame().getTrain();

        int wagonPosition = TargetHelper.getWagonPositionOfUser(super.getUser(), train);
        boolean isUpperLevel = TargetHelper.isOnUpperLevel(super.getUser(), train);

        int pointer;
        if (wagonPosition == 0) {
            //Lokomotive
            if (isUpperLevel) {
                pointer = 1;
                while (pointer < train.size() -1 && train.get(pointer).getUpperLevel().getUsers().isEmpty()) {
                    pointer++;
                }
                targets.addAll(train.get(pointer).getUpperLevel().getUsers());
            } else {
                targets.addAll(train.get(wagonPosition + 1).getLowerLevel().getUsers());
            }
        } else if (wagonPosition == train.size() - 1) {
            //letzter Wagon
            if (isUpperLevel) {
                pointer = wagonPosition - 1;
                while (pointer > 0 && train.get(pointer).getUpperLevel().getUsers().isEmpty()) {
                    pointer--;
                }
                targets.addAll(train.get(pointer).getUpperLevel().getUsers());
            } else {
                targets.addAll(train.get(wagonPosition - 1).getLowerLevel().getUsers());
            }
        } else {
            //dazwischen
            int left = wagonPosition - 1;
            int right = wagonPosition + 1;
            if (isUpperLevel) {
                while (left > 0 && train.get(left).getUpperLevel().getUsers().isEmpty()) {
                    left--;
                }

                while (right < train.size() - 1 && train.get(right).getUpperLevel().getUsers().isEmpty()) {
                    right++;
                }

                targets.addAll(train.get(left).getUpperLevel().getUsers());
                targets.addAll(train.get(right).getUpperLevel().getUsers());
            } else {
                targets.addAll(train.get(wagonPosition - 1).getLowerLevel().getUsers());
                targets.addAll(train.get(wagonPosition + 1).getLowerLevel().getUsers());
            }
        }

        //Remove Belle if she's not the only target
        if (targets.size() > 1) {
            targets = TargetHelper.removeBelle(targets);
        }

        //Tuco special case: Add targets on other level of same wagon:
        if (super.getUser().getCharacterType() == CharacterType.TUCO) {
            if (isUpperLevel) {
                targets.addAll(train.get(wagonPosition).getLowerLevel().getUsers());
            } else {
                targets.addAll(train.get(wagonPosition).getUpperLevel().getUsers());
            }
        }

        super.setPossibleTargets(targets);
        return targets;
    }

    @Override
    public void resetActionMoveType() {
        super.setActionMoveType(ActionMoveType.SHOOT);
    }
}
