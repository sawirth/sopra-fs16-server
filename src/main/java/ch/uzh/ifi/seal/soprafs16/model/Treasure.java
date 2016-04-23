package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.constant.TreasureType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@DiscriminatorValue(value = "treasure")
@JsonDeserialize(as = Treasure.class)
public class Treasure extends Target implements Serializable {

    @Column
    @JsonView(Views.Internal.class)
    private int value;

    @Column
    @Enumerated
    @JsonView(Views.Public.class)
    private TreasureType treasureType;

    public Treasure(int value, TreasureType treasureType){
        this.value = value;
        this.treasureType = treasureType;
    }

    public Treasure(){
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public TreasureType getTreasureType() {
        return treasureType;
    }

    public void setTreasureType(TreasureType treasureType) {
        this.treasureType = treasureType;
    }
}
