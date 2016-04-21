package ch.uzh.ifi.seal.soprafs16.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class RoundFinisher implements Serializable{
    @Id
    @GeneratedValue
    private Long id;

    public void finishRound(Game game){
        /*
        INFO: Ich musste das abstract entfernen als Quick-Fix, da Hibernate die ganze Zeit motzt, dass es keine abstrakten Klassen
        instanzieren kann und ich nicht rausgefunden habe, wie es gehen würde
        */

    }

    public Long getId() {
        return id;
    }
}
