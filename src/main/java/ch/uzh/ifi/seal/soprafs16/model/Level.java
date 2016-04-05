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
    private List<Treasure> moneybags;

    @Column
    private int diamonds;

    @Column
    private int cashboxes;

    @OneToMany
    private List<User> users;

    public Level(List<Treasure> moneybags,int diamonds, int cashboxes){

        this.diamonds = diamonds;
        this.cashboxes = cashboxes;
    }

    public Level(){}

    public List<Treasure> getMoneybags() {
        return moneybags;
    }

    public void setMoneybags(List<Treasure> moneybags) {
        this.moneybags = moneybags;
    }

    public int getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(int diamonds) {
        this.diamonds = diamonds;
    }

    public int getCashboxes() {
        return cashboxes;
    }

    public void setCashboxes(int cashboxes) {
        this.cashboxes = cashboxes;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }


}
