package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.constant.MoveType;
import ch.uzh.ifi.seal.soprafs16.constant.RoundType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 *
 * Created by David on 05.04.2016.
 */
@Entity
public class Round implements Serializable{

    private static final long serialVersionUID = 1L;

    private final int NUMBER_OF_MOVES;

    @Id
    @GeneratedValue
    @JsonView(Views.Public.class)
    private Long id;

    @Column
    @JsonView(Views.Public.class)
    private RoundType roundType;

    @ManyToOne
    @JsonIgnore
    private Game game;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonView(Views.Extended.class)
    private List<Move> moves;

    @Column
    @Enumerated
    @ElementCollection(targetClass = MoveType.class)
    @JsonView(Views.Extended.class)
    private List<MoveType> moveTypes;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonView(Views.Public.class)
    private RoundFinisher roundFinisher;

    public Round(int numberOfMoves,RoundType roundType, Game game, List<MoveType> moveTypes, RoundFinisher roundFinisher){
        NUMBER_OF_MOVES = numberOfMoves;
        this.game=game;
        this.moveTypes=moveTypes;
        this.roundType=roundType;
        this.roundFinisher=roundFinisher;
    }

    public Round(){
        NUMBER_OF_MOVES=0;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getNUMBER_OF_MOVES() {
        return NUMBER_OF_MOVES;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    public List<MoveType> getMoveType() {
        return moveTypes;
    }

    public void setMoveType(List<MoveType> moveType) {
        this.moveTypes = moveType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoundType getRoundType() {
        return roundType;
    }

    public void setRoundType(RoundType roundType) {
        this.roundType = roundType;
    }

    public RoundFinisher getRoundFinisher() {
        return roundFinisher;
    }

    public void setRoundFinisher(RoundFinisher roundFinisher) {
        this.roundFinisher = roundFinisher;
    }
}
