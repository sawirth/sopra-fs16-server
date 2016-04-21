package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.constant.CharacterType;

import javax.persistence.*;

@Entity
public class LogEntry {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private CharacterType characterType;

    @Column
    private String message;

    @ManyToOne
    private GameLog gameLog;

    public LogEntry(CharacterType characterType, String message) {
        this.characterType = characterType;
        this.message = message;
    }

    public LogEntry() {
    }

    public Long getId() {
        return id;
    }

    public CharacterType getCharacterType() {
        return characterType;
    }

    public String getMessage() {
        return message;
    }
}
