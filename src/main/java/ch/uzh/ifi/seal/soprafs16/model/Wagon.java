package ch.uzh.ifi.seal.soprafs16.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import ch.uzh.ifi.seal.soprafs16.model.Treasure;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;


@Entity
public class Wagon implements Serializable {

    @Id
    @GeneratedValue
    @JsonView(Views.Public.class)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonView(Views.Public.class)
    @JoinColumn(name = "LEVEL_ID")
    private Level lowerLevel;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonView(Views.Public.class)
    private Level upperLevel;

    @Column
    @JsonView(Views.Public.class)
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

    public Boolean hasMarshal() {
        return hasMarshal;
    }

    public void setHasMarshal(Boolean hasMarshal) {
        this.hasMarshal = hasMarshal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
