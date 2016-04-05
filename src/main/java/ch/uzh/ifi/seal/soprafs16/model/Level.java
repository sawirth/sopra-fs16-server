package ch.uzh.ifi.seal.soprafs16.model;


import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Ada on 28.03.2016.
 */
@Entity
public class Level {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Treasure> treasures;

    @OneToMany
    private List<User> users;

    public Level(List<Treasure> treasures){
        this.treasures=treasures;
    }

    public Level(){}

    public List<Treasure> getTreasures() {
        return treasures;
    }

    public void setTreasures(List<Treasure> treasures) {
        this.treasures = treasures;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
