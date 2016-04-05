package ch.uzh.ifi.seal.soprafs16.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import ch.uzh.ifi.seal.soprafs16.model.Treasure;

/**
 * Created by Ada on 28.03.2016.
 */
@Entity
public class Wagon implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Level lowerLevel;

    @OneToOne(cascade = CascadeType.ALL)
    private Level upperLevel;

    @Column
    private Boolean hasMarshal;

    public Wagon(List<Treasure> treasures, boolean hasMarshal){
        lowerLevel = new Level(treasures);
        upperLevel = new Level();
        this.hasMarshal = hasMarshal;
    }

    public Wagon() {
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
