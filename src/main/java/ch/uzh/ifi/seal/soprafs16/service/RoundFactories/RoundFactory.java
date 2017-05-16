package ch.uzh.ifi.seal.soprafs16.service.RoundFactories;

import ch.uzh.ifi.seal.soprafs16.constant.MoveType;
import ch.uzh.ifi.seal.soprafs16.constant.RoundType;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Round;
import ch.uzh.ifi.seal.soprafs16.model.RoundFinisher;
import ch.uzh.ifi.seal.soprafs16.model.roundFinisher.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class RoundFactory {
    public List<Round> createRounds(Game game, int numberOfRounds) {
        List<Round> normalRounds = new ArrayList<>(numberOfRounds);

        //Normale Spielrunden erstellen
        normalRounds.add(createNoFinisherRoundWithDoubleMove(game));
        normalRounds.add(createNoFinisherRoundWithTunnel(game));
        normalRounds.add(createCraneRound(game));
        normalRounds.add((createTakeAllRound(game)));
        normalRounds.add(createAngryMarshalRound(game));
        normalRounds.add(createResistanceRound(game));
        normalRounds.add(createBrakeRound(game));
        Collections.shuffle(normalRounds);

        //Finisher Runde erstellen
        List<Round> endRounds = new ArrayList<>(3);
        endRounds.add(createRevengeMarshalFinisherRound(game));
        endRounds.add(createHostageFinisherRound(game));
        endRounds.add(createPickPocketingFinisherRound(game));
        Collections.shuffle(endRounds);

        List<Round> roundsForGame = normalRounds
                .stream()
                .limit(numberOfRounds - 1)
                .collect(Collectors.toList());

        roundsForGame.add(endRounds.get(0));
        return roundsForGame;
    }

    abstract Round createNoFinisherRoundWithDoubleMove(Game game);
    abstract Round createNoFinisherRoundWithTunnel(Game game);
    abstract Round createCraneRound(Game game);
    abstract Round createTakeAllRound(Game game);
    abstract Round createAngryMarshalRound(Game game);
    abstract Round createResistanceRound(Game game);
    abstract Round createBrakeRound(Game game);

    private Round createPickPocketingFinisherRound(Game game)
    {
        List<MoveType> moveTypes = getFinisherRoundMoveTypes();
        RoundFinisher roundFinisher = new RoundFinisherPickPocketing();
        int numberOfRounds = moveTypes.size();
        return new Round(numberOfRounds, RoundType.PICK_POCKETING, game, moveTypes, roundFinisher);
    }

    private Round createRevengeMarshalFinisherRound(Game game)
    {
        List<MoveType> moveTypes = getFinisherRoundMoveTypes();
        RoundFinisher roundFinisher = new RoundFinisherPickPocketing();
        int numberOfRounds = moveTypes.size();
        return new Round(numberOfRounds, RoundType.REVENGE_MARSHAL, game, moveTypes, roundFinisher);
    }

    private Round createHostageFinisherRound(Game game)
    {
        List<MoveType> moveTypes = getFinisherRoundMoveTypes();
        RoundFinisher roundFinisher = new RoundFinisherHostage();
        int numberOfRounds = moveTypes.size();
        return new Round(numberOfRounds, RoundType.HOSTAGE, game, moveTypes, roundFinisher);
    }

    //All finisher round share the same move types in the same order
    private List<MoveType> getFinisherRoundMoveTypes()
    {
        List<MoveType> moveTypes = new ArrayList<>();
        moveTypes.add(MoveType.VISIBLE);
        moveTypes.add(MoveType.VISIBLE);
        moveTypes.add(MoveType.HIDDEN);
        moveTypes.add(MoveType.VISIBLE);
        return moveTypes;
    }

    protected Round createRound(RoundType roundType, Game game, MoveType... moveTypes) {
        List<MoveType> moveTypeList = Arrays.asList(moveTypes);
        int numberOfRounds = moveTypeList.size();
        RoundFinisher roundFinisher = null;

        switch (roundType) {
            case ANGRY_MARSHAL:
                roundFinisher = new RoundFinisherAngryMarshal();
                break;

            case BREAK:
                roundFinisher = new RoundFinisherBreak();
                break;

            case CRANE:
                roundFinisher = new RoundFinisherCrane();
                break;

            case HOSTAGE:
                roundFinisher = new RoundFinisherHostage();
                break;

            case PICK_POCKETING:
                roundFinisher = new RoundFinisherPickPocketing();
                break;

            case RESISTANCE:
                roundFinisher = new RoundFinisherResistance();
                break;

            case REVENGE_MARSHAL:
                roundFinisher = new RoundFinisherRevengeMarshal();
                break;

            case TAKE_ALL:
                roundFinisher =  new RoundFinisherTakeAll();
                break;

            default:
                break;
        }

        return new Round(numberOfRounds, roundType, game, moveTypeList, roundFinisher);
    }
}
