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

    protected abstract Round createNoFinisherRoundWithDoubleMove(Game game);
    protected abstract Round createNoFinisherRoundWithTunnel(Game game);
    protected abstract Round createCraneRound(Game game);
    protected abstract Round createTakeAllRound(Game game);
    protected abstract Round createAngryMarshalRound(Game game);
    protected abstract Round createResistanceRound(Game game);
    protected abstract Round createBrakeRound(Game game);

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

    protected Round createRound(RoundType roundType, RoundFinisher roundFinisher, Game game, MoveType... moveTypes) {
        List<MoveType> moveTypeList = Arrays.asList(moveTypes);
        int numberOfRounds = moveTypeList.size();
        return new Round(numberOfRounds, roundType, game, moveTypeList, roundFinisher);
    }
}
