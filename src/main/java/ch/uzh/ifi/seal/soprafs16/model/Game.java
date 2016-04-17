package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Game implements Serializable {
	
	/**
	 * 
	 */
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
    
    @ManyToMany(cascade = CascadeType.ALL)
	@JsonView(Views.Public.class)
    private List<User> players;

	@OneToMany(cascade = CascadeType.ALL)
	@JsonView(Views.Public.class)
	private List<Round> rounds;

	@OneToMany(cascade = CascadeType.ALL)
	@JsonView(Views.Extended.class)
	private List<Wagon> train;

	public Game() {
		this.players = new ArrayList<>();
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
   
	public User getNextPlayer() {
		return getPlayers().get((getCurrentPlayer() + 1) % getPlayers().size());
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
}
