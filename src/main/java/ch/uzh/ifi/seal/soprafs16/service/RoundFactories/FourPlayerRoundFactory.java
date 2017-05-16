package ch.uzh.ifi.seal.soprafs16.service.RoundFactories;

import ch.uzh.ifi.seal.soprafs16.constant.MoveType;
import ch.uzh.ifi.seal.soprafs16.constant.RoundType;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Round;

public class FourPlayerRoundFactory extends RoundFactory {
    @Override
    Round createNoFinisherRoundWithDoubleMove(Game game) {
        return createRound(RoundType.NO_EVENT1, game, MoveType.VISIBLE, MoveType.DOUBLE, MoveType.VISIBLE);
    }

    @Override
    Round createNoFinisherRoundWithTunnel(Game game) {
        return createRound(RoundType.NO_EVENT2, game, MoveType.VISIBLE, MoveType.HIDDEN, MoveType.VISIBLE, MoveType.HIDDEN, MoveType.VISIBLE);
    }

    @Override
    Round createCraneRound(Game game) {
        return createRound(RoundType.CRANE, game, MoveType.VISIBLE, MoveType.HIDDEN, MoveType.VISIBLE, MoveType.VISIBLE);
    }

    @Override
    Round createTakeAllRound(Game game) {
        return createRound(RoundType.TAKE_ALL, game, MoveType.VISIBLE, MoveType.HIDDEN, MoveType.DOUBLE, MoveType.VISIBLE);
    }

    @Override
    Round createAngryMarshalRound(Game game) {
        return createRound(RoundType.ANGRY_MARSHAL, game, MoveType.VISIBLE, MoveType.VISIBLE, MoveType.HIDDEN, MoveType.REVERSE);
    }

    @Override
    Round createResistanceRound(Game game) {
        return createRound(RoundType.RESISTANCE, game, MoveType.VISIBLE, MoveType.VISIBLE, MoveType.HIDDEN, MoveType.VISIBLE, MoveType.VISIBLE);
    }

    @Override
    Round createBrakeRound(Game game) {
        return createRound(RoundType.BREAK, game, MoveType.VISIBLE, MoveType.VISIBLE, MoveType.VISIBLE, MoveType.VISIBLE);
    }
}
