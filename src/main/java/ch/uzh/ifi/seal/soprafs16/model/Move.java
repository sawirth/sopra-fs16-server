package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.constant.ActionMoveType;
import ch.uzh.ifi.seal.soprafs16.constant.CharacterType;
import ch.uzh.ifi.seal.soprafs16.model.moves.BlockerMove;
import ch.uzh.ifi.seal.soprafs16.model.moves.HorizontalMove;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "move")
@JsonDeserialize(as = HorizontalMove.class)
public abstract class Move implements Serializable {

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
	@JsonView(Views.Internal.class)
	private Round round;

	@Column
	@JsonView(Views.Public.class)
	private CharacterType characterType;

	@Column
	@JsonView(Views.Public.class)
	private ActionMoveType actionMoveType;

	@ManyToOne
	@JsonView(Views.Internal.class)
	private Game game;

	@OneToMany
	@JsonView(Views.Extended.class)
	private List<Target> possibleTargets;

	@OneToOne
	@JsonView(Views.Extended.class)
	private Target target;

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

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public List<Target> getPossibleTargets() {
		return possibleTargets;
	}

	public void setPossibleTargets(List<Target> possibleTargets) {
		this.possibleTargets = possibleTargets;
	}

	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	public abstract void executeAction();

	public abstract List<Target> calculateTargets();
}
