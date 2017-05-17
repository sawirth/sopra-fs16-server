package ch.uzh.ifi.seal.soprafs16.roundFinisher;

import ch.uzh.ifi.seal.soprafs16.model.RoundFinisher;
import ch.uzh.ifi.seal.soprafs16.model.roundFinisher.RoundFinisherCrane;
import org.junit.Test;

public class CraneRoundFinisherTest extends BaseRoundFinisherTest {
    @Test
    public void finishRound_userIsInFirstWagonOnUpperLevel_userEndsUpOnLastWagonUpperLevel()
    {
        GivenUserPlacedInWagonAndLevel(firstUser, FIRST_WAGON, UPPER_LEVEL);
        WhenFinishingRound();
        ThenUserIsInWagonAndLevel(firstUser, LAST_WAGON, UPPER_LEVEL);
    }

    @Test
    public void finishRound_userIsInFirstWagonOnLowerLevel_userStays() {
        GivenUserPlacedInWagonAndLevel(firstUser, FIRST_WAGON, LOWER_LEVEL);
        WhenFinishingRound();
        ThenUserIsInWagonAndLevel(firstUser, FIRST_WAGON, LOWER_LEVEL);
    }

    @Test
    public void finishRound_userIsOnLastWagonOnUpperLevel_userStays() {
        GivenUserPlacedInWagonAndLevel(firstUser, LAST_WAGON, UPPER_LEVEL);
        WhenFinishingRound();
        ThenUserIsInWagonAndLevel(firstUser, LAST_WAGON, UPPER_LEVEL);
    }

    @Override
    protected void WhenFinishingRound() {
        RoundFinisher roundFinisher = new RoundFinisherCrane();
        super.WhenFinishingRound(roundFinisher);
    }
}
