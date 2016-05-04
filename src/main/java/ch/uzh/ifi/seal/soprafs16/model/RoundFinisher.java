package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.model.roundFinisher.RoundFinisherBreak;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "round_finisher_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "roundFinisher")
@JsonDeserialize(as = RoundFinisherBreak.class)
public abstract class RoundFinisher implements Serializable{
    @Id
    @GeneratedValue
    private Long id;

    public abstract void finishRound(Game game);

    public Long getId() {
        return id;
    }
}
