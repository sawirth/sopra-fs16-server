package ch.uzh.ifi.seal.soprafs16.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by David on 16.04.2016.
 */
@Entity
public abstract class RoundFinisher {
    @Id
    @GeneratedValue
    private Long id;

    public abstract void finishRound(Game game);
}
