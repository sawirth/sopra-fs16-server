package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.constant.CharacterType;
import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.constant.RoundType;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Game implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@JsonView(Views.Public.class)
	private Long id;

	@Column(nullable = false)
	@JsonView(Views.Public.class)
	private String owner;

	@Column
	@JsonView(Views.Public.class)
	private GameStatus status;

	@Column
	@JsonView(Views.Public.class)
	private Integer currentPlayer;

	@Column
	@JsonView(Views.Public.class)
	private Integer currentRound;

	@ManyToMany(cascade = CascadeType.ALL)
	@JsonView(Views.Public.class)
	private List<User> players;

	@OneToMany(cascade = CascadeType.ALL)
	@JsonView(Views.Public.class)
	private List<Round> rounds;

	@OneToMany(cascade = CascadeType.ALL)
	@JsonView(Views.Extended.class)
	private List<Wagon> train;

	@OneToOne(cascade = CascadeType.ALL)
	@JsonView(Views.Internal.class)
	private GameLog gameLog;

	public Game() {
		this.players = new ArrayList<>();
		this.rounds = new ArrayList<>();
		currentRound = 0;
		this.gameLog = new GameLog();
	}

	public void addLog(CharacterType characterType, String message) {
		this.gameLog.addLog(characterType, message);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public List<User> getPlayers() {
		return players;
	}

	public void setPlayers(List<User> players) {
		this.players = players;
	}

	public GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}

	public Integer getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Integer currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public int getNextPlayer() {
		return (getCurrentPlayer() + 1) % getPlayers().size();
	}

	public List<Wagon> getTrain() {
		return train;
	}

	public void setTrain(List<Wagon> train) {
		this.train = train;
	}

	public void setRounds(List<Round> rounds){
		this.rounds=rounds;
	}

	public List<Round> getRounds() {
		return rounds;
	}

	public Integer getCurrentRound() {
		return currentRound;
	}

	public void setCurrentRound(Integer currentRound) {
		this.currentRound = currentRound;
	}

	public GameLog getGameLog() {
		return gameLog;
	}

    @JsonView(Views.Extended.class)
    public RoundType getCurrentRoundType() {
		if (this.rounds == null || this.rounds.isEmpty()) {
			return null;
		} else {
			return this.rounds.get(currentRound).getRoundType();
		}
    }

    @JsonView(Views.Extended.class)
    public Move getLastPlayedMove() {

        if (this.rounds == null || this.rounds.isEmpty()) {
            return null;
        }

        Round currentRound = this.rounds.get(getCurrentRound());

        if (currentRound.getMoves().isEmpty()) {
            return null;
        }
        
        return currentRound.getMoves().get(currentRound.getMoves().size() - 1);
    }
}
