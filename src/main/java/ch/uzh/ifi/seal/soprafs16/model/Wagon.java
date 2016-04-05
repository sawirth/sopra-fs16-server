package ch.uzh.ifi.seal.soprafs16.model;

import java.util.List;

/**
 * Created by Ada on 28.03.2016.
 */
public class Wagon {
    private Level lowerLevel;
    private Level upperLevel;
    private Boolean hasMarshal;

    public Wagon(List<Treasure> moneybags, int diamonds, int cashboxes, boolean hasMarshal){
        lowerLevel = new Level(moneybags, diamonds, cashboxes);
        upperLevel = new Level();
        this.hasMarshal = hasMarshal;
    }

    public Level getLowerLevel() {
        return lowerLevel;
    }

    public void setLowerLevel(Level lowerLevel) {
        this.lowerLevel = lowerLevel;
    }

    public Level getUpperLevel() {
        return upperLevel;
    }

    public void setUpperLevel(Level upperLevel) {
        this.upperLevel = upperLevel;
    }

    public Boolean getHasMarshal() {
        return hasMarshal;
    }

    public void setHasMarshal(Boolean hasMarshal) {
        this.hasMarshal = hasMarshal;
    }
}
