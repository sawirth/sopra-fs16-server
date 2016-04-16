package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.constant.TreasureType;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Treasure implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    @JsonView(Views.Public.class)
    private int value;

    @Column
    @JsonView(Views.Public.class)
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
