package ch.uzh.ifi.seal.soprafs16.roundFinisher;

import ch.uzh.ifi.seal.soprafs16.model.RoundFinisher;
import ch.uzh.ifi.seal.soprafs16.model.roundFinisher.RoundFinisherBreak;
import org.junit.Test;

public class BrakeRoundFinisherTest extends BaseRoundFinisherTest {
    @Test
    public void finishRound_UserIsInFirstWagonUpperLevel_NoChange() {
        GivenUserPlacedInWagonAndLevel(firstUser, FIRST_WAGON, UPPER_LEVEL);
        WhenFinishingRound();
        ThenUserIsInWagonAndLevel(firstUser, FIRST_WAGON, UPPER_LEVEL);
    }

    @Test
    public void finishRound_UserIsInSecondWagonUpperLevel_UserEndsUpOnFirstWagon() {
        GivenUserPlacedInWagonAndLevel(firstUser, SECOND_WAGON, UPPER_LEVEL);
        WhenFinishingRound();
        ThenUserIsInWagonAndLevel(firstUser, FIRST_WAGON, UPPER_LEVEL);
    }

    @Test
    public void finishRound_UserIsInSecondWagonLowerLevel_NoChange() {
        GivenUserPlacedInWagonAndLevel(firstUser, SECOND_WAGON, LOWER_LEVEL);
        WhenFinishingRound();
        ThenUserIsInWagonAndLevel(firstUser, SECOND_WAGON, LOWER_LEVEL);
    }

    @Override
    protected void WhenFinishingRound() {
        RoundFinisher roundFinisher = new RoundFinisherBreak();
        super.WhenFinishingRound(roundFinisher);
    }
}
