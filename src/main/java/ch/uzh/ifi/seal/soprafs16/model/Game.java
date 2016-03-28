package ch.uzh.ifi.seal.soprafs16.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;

@Entity
public class Game implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false) 
	private String owner;
	
	@Column 
	private GameStatus status;
	
	@Column 
	private Integer currentPlayer;

    @OneToMany(mappedBy="game")
    private List<Move> moves;
    
    @ManyToMany(cascade = CascadeType.ALL)
    private List<User> players;

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
}
