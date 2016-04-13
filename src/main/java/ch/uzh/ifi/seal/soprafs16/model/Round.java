package ch.uzh.ifi.seal.soprafs16.model;

import ch.uzh.ifi.seal.soprafs16.constant.MoveType;
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

    private final int NUMBER_OF_MOVES;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="GAME_ID")
    private Game game;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonView(Views.Public.class)
    private List<Move> moves;

    @Column
    @Enumerated
    @ElementCollection(targetClass = MoveType.class)
    @JsonView(Views.Public.class)
    private List<MoveType> moveTypes;

    //TODO implement roundfinisher

    public Round(int numberOfMoves, Game game, List<MoveType> moveTypes){
        NUMBER_OF_MOVES = numberOfMoves;
        this.game=game;
        this.moveTypes=moveTypes;
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

}
