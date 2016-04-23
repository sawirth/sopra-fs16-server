package ch.uzh.ifi.seal.soprafs16.model;


import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@DiscriminatorValue(value = "level")
@JsonDeserialize(as = Level.class)
public class Level extends Target implements Serializable{

    @OneToMany(cascade = CascadeType.ALL)
    @JsonView(Views.Public.class)
    private List<Treasure> treasures;

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonView(Views.Public.class)
    private List<User> users;

    @OneToOne
    @JsonView(Views.Internal.class)
    private Wagon wagon;

    public Level(List<Treasure> treasures){
        this.treasures=treasures;
        users = new ArrayList<>();
    }

    public Level(){
        users = new ArrayList<>();
        treasures=new ArrayList<>();
    }

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

    public Wagon getWagon() {
        return wagon;
    }

    public void setWagon(Wagon wagon) {
        this.wagon = wagon;
    }
}
