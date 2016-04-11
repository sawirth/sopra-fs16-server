package ch.uzh.ifi.seal.soprafs16.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.persistence.*;

import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import com.fasterxml.jackson.annotation.JsonView;

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

    @OneToMany(mappedBy="game")
	@JsonView(Views.Extended.class)
    private List<Move> moves;
    
    @ManyToMany(cascade = CascadeType.ALL)
	@JsonView(Views.Public.class)
    private List<User> players;

	//@Column
	//private List<Round> rounds;

	@OneToMany(cascade = CascadeType.ALL)
	@JsonView(Views.Extended.class)
	private List<Wagon> train;

	public Game() {
		this.moves = new ArrayList<>();
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

	public List<Move> getMoves() {
		return moves;
	}

	public void setMoves(List<Move> moves) {
		this.moves = moves;
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

	/*public List<Round> getRounds() {
		return rounds;
	}

	public List<Wagon> getTrain() {
		return train;
	}*/
}
