package ch.uzh.ifi.seal.soprafs16.model;


import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.persistence.*;
import java.util.List;


@Entity
public class Level {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Treasure> treasures;

    @OneToMany(cascade = CascadeType.ALL)
    private List<User> users;

    @OneToOne
    private Wagon wagon;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Wagon getWagon() {
        return wagon;
    }

    public void setWagon(Wagon wagon) {
        this.wagon = wagon;
    }
}
