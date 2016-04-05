package ch.uzh.ifi.seal.soprafs16.model;

public class Treasure {
    private int value;
    private TreasureType type;

    public Treasure(int value, TreasureType type){
        this.value = value;
        this.type = type;
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
