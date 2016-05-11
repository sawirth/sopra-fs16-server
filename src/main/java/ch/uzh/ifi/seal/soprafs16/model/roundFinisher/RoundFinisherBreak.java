package ch.uzh.ifi.seal.soprafs16.model.roundFinisher;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.RoundFinisher;
import ch.uzh.ifi.seal.soprafs16.model.User;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity
@DiscriminatorValue(value = "break")
public class RoundFinisherBreak extends RoundFinisher{
    @Override
    public void finishRound(Game game) {
        for(int i=1;i<game.getTrain().size();i++){
            List<User> users = game.getTrain().get(i).getUpperLevel().getUsers();
            if(users!=null){
                game.getTrain().get(i-1).getUpperLevel().getUsers().addAll(users);
                for(User user: users){
                    game.addLog(user.getCharacterType(), user.getUsername() + " got shifted one wagon closer to the locomotive at the end of the round");
                }
                game.getTrain().get(i).getUpperLevel().getUsers().clear();
            }
        }
        game.addLog(null, "Round has been finished with the break event");
    }
}
