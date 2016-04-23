package ch.uzh.ifi.seal.soprafs16.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "targetType", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "target")
@JsonDeserialize(as = User.class)
public abstract class Target implements Serializable {

    @Id
    @GeneratedValue
    @JsonView(Views.Public.class)
    @Column(name = "ID")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
