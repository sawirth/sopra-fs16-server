package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.constant.TreasureType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Treasure {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private int value;

    @Column
    private TreasureType type;

    public Treasure(int value, TreasureType type){
        this.value = value;
        this.type = type;
    }

    public Treasure(){

    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public TreasureType getType() {
        return type;
    }

    public void setType(TreasureType type) {
        this.type = type;
    }
}
