package ch.uzh.ifi.seal.soprafs16.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.swing.text.View;

import ch.uzh.ifi.seal.soprafs16.constant.CharacterType;
import ch.uzh.ifi.seal.soprafs16.constant.UserStatus;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class User implements Serializable {
	
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
	private String name;
	
	@Column(nullable = false, unique = true)
	@JsonView(Views.Public.class)
	private String username;
	
	@Column(nullable = false, unique = true)
	@JsonView(Views.Extended.class)
	private String token;
	
	@Column(nullable = false)
	@JsonView(Views.Public.class)
	private UserStatus status;

	@Column
	@JsonView(Views.Public.class)
	private CharacterType characterType;

    @ManyToMany(mappedBy = "players", cascade = CascadeType.ALL)
	@JsonView(Views.Internal.class)
    private List<Game> games;
	
    @OneToMany(mappedBy="user")
	@JsonView(Views.Internal.class)
    private List<Move> moves;

	@OneToMany(cascade = CascadeType.ALL)
	@JsonView(Views.Extended.class)
	private List<Treasure> treasures;

	@OneToMany(cascade = CascadeType.ALL)
	@JsonView(Views.Extended.class)
	private List<Move> deckCards;

	@OneToMany(cascade = CascadeType.ALL)
	@JsonView(Views.Extended.class)
	private List<Move> handCards;

	@Column
	@JsonView(Views.Public.class)
	private int numberOfShots;

	@Column
	@JsonView(Views.Internal.class)
	private int shotsTaken;


	public User(String name, String username) {
		this.name = name;
		this.username = username;

		//Initialize lists to avoid NullPointers
		games = new ArrayList<>();
		moves = new ArrayList<>();
		treasures = new ArrayList<>();
		deckCards = new ArrayList<>();
		handCards = new ArrayList<>();
	}

	public User() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Game> getGames() {
		return games;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}

	public List<Move> getMoves() {
		return moves;
	}

	public void setMoves(List<Move> moves) {
		this.moves = moves;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public List<Treasure> getTreasures() {
		return treasures;
	}

	public void setTreasures(List<Treasure> treasures) {
		this.treasures = treasures;
	}

	public CharacterType getCharacterType() {
		return characterType;
	}

	public void setCharacterType(CharacterType characterType) {
		this.characterType = characterType;
	}

	public List<Move> getDeckCards() {
		return deckCards;
	}

	public void setDeckCards(List<Move> deckCards) {
		this.deckCards = deckCards;
	}

	public int getShotsTaken() {
		return shotsTaken;
	}

	public void setShotsTaken(int shotsTaken) {
		this.shotsTaken = shotsTaken;
	}

	public int getNumberOfShots() {
		return numberOfShots;
	}

	public void setNumberOfShots(int numberOfShots) {
		this.numberOfShots = numberOfShots;
	}

	public List<Move> getHandCards() {
		return handCards;
	}

	public void setHandCards(List<Move> handCards) {
		this.handCards = handCards;
	}
}
