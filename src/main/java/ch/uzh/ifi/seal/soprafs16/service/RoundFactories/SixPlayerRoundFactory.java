package ch.uzh.ifi.seal.soprafs16.service.RoundFactories;

import ch.uzh.ifi.seal.soprafs16.constant.MoveType;
import ch.uzh.ifi.seal.soprafs16.constant.RoundType;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Round;

public class SixPlayerRoundFactory extends RoundFactory {
    @Override
    protected Round createNoFinisherRoundWithDoubleMove(Game game) {
        return createRound(RoundType.NO_EVENT1, game, MoveType.VISIBLE, MoveType.DOUBLE);
    }

    @Override
    protected Round createNoFinisherRoundWithTunnel(Game game) {
        return createRound(RoundType.NO_EVENT2, game, MoveType.VISIBLE, MoveType.HIDDEN, MoveType.VISIBLE, MoveType.HIDDEN);
    }

    @Override
    protected Round createCraneRound(Game game) {
        return createRound(RoundType.CRANE,
                game,
                MoveType.VISIBLE,
                MoveType.HIDDEN,
                MoveType.VISIBLE);
    }

    @Override
    protected Round createTakeAllRound(Game game) {
        return createRound(RoundType.TAKE_ALL, game, MoveType.VISIBLE, MoveType.DOUBLE, MoveType.REVERSE);
    }

    @Override
    protected Round createAngryMarshalRound(Game game) {
        return createRound(RoundType.ANGRY_MARSHAL, game, MoveType.VISIBLE, MoveType.VISIBLE, MoveType.REVERSE);
    }

    @Override
    protected Round createResistanceRound(Game game) {
        return createRound(RoundType.RESISTANCE, game, MoveType.VISIBLE, MoveType.HIDDEN, MoveType.VISIBLE, MoveType.REVERSE);
    }

    @Override
    protected Round createBrakeRound(Game game) {
        return createRound(RoundType.BREAK, game, MoveType.VISIBLE, MoveType.HIDDEN, MoveType.VISIBLE, MoveType.VISIBLE);
    }
}
