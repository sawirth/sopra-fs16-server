package ch.uzh.ifi.seal.soprafs16.helper;

import ch.uzh.ifi.seal.soprafs16.constant.CharacterType;
import ch.uzh.ifi.seal.soprafs16.model.Target;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.Wagon;

import java.util.List;

public class TargetHelper {

    private TargetHelper() {
    }

    public static int getWagonPositionOfUser(User user, List<Wagon> train) {

        int wagonPosition = 0;
        for (Wagon w : train) {
            if (w.getLowerLevel().getUsers().contains(user) || w.getUpperLevel().getUsers().contains(user)) {
                break;
            }

            wagonPosition++;
        }

        return wagonPosition;
    }

    public static boolean isOnUpperLevel(User user, List<Wagon> train) {
        boolean isOnUpperLevel = false;

        for (Wagon w : train) {
            if (w.getUpperLevel().getUsers().contains(user)) {
                isOnUpperLevel = true;
                break;
            }
        }

        return isOnUpperLevel;
    }

    public static List<Target> removeBelle(List<Target> targets) {
        for (int i = 0; i < targets.size(); i++) {
            User u = (User) targets.get(i);
            if (u.getCharacterType() == CharacterType.BELLE) {
                targets.remove(i);
                break;
            }
        }

        return targets;
    }
}
