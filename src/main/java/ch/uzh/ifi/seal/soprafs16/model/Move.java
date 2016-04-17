package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.constant.ActionMoveType;
import ch.uzh.ifi.seal.soprafs16.constant.CharacterType;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Move implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@JsonView(Views.Public.class)
	private Long id;
    
    @ManyToOne
    @JoinColumn
	@JsonView(Views.Internal.class)
    private User user;

	@ManyToOne
	@JsonView(Views.Public.class)
	private Round round;

	@Column
	@JsonView(Views.Public.class)
	private CharacterType characterType;

	@Column
	@JsonView(Views.Public.class)
	private ActionMoveType actionMoveType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Round getRound() {
		return round;
	}

	public void setRound(Round round) {
		this.round = round;
	}

	public ActionMoveType getActionMoveType() {
		return actionMoveType;
	}

	public void setActionMoveType(ActionMoveType actionMoveType) {
		this.actionMoveType = actionMoveType;
	}

	public CharacterType getCharacterType() {
		return characterType;
	}

	public void setCharacterType(CharacterType characterType) {
		this.characterType = characterType;
	}
}
