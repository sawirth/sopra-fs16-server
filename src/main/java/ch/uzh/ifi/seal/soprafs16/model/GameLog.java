package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.constant.CharacterType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class GameLog implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<LogEntry> logEntryList;

    public GameLog() {
        this.logEntryList = new ArrayList<>();
    }

    public void addLog(CharacterType characterType, String message) {
        this.logEntryList.add(new LogEntry(characterType, message));
    }

    public List<LogEntry> getLogEntryList() {
        return logEntryList;
    }

    public Long getId() {
        return id;
    }
}
