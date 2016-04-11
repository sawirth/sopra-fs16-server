package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.CharacterType;
import ch.uzh.ifi.seal.soprafs16.model.Move;
import ch.uzh.ifi.seal.soprafs16.model.User;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RoundServiceTest {

    private RoundService roundService;
    private User player;

    @Before
    public void setUp() throws Exception {
        roundService = new RoundService();

        this.player = new User("Sandro", "sw");
    }

    @Test
    public void testResetPlayer() throws Exception {

        //Reset player at the beginning of a round
        roundService.resetPlayer(this.player);

        //Number of shots must be 6 at the beginning
        assertThat(player.getNumberOfShots(), is(6));

        //In the first round the user should have 10 moves in his deck and no blocker moves
        assertThat(this.player.getDeckCards().size(), is(10));
        assertThat(this.player.getShotsTaken(), is(0));

        //Shoot 3 times on player and reset; the deck must now be of size 13
        this.player.setShotsTaken(3);
        roundService.resetPlayer(this.player);
        assertThat(this.player.getDeckCards().size(), is(13));

        //The deck must contain 3 blocker moves
        int countBlocker = 0;
        for (Move move : player.getDeckCards()) {
            if (move.getClass().getName().contains("BlockerMove")) {
                countBlocker++;
            }
        }
        assertThat(countBlocker, is(3));
    }

    @Test
    public void testDrawStartCards() throws Exception {

        //In this case the player has 6 cards at the beginning and the deck contains the other 4 cards
        this.player.setShotsTaken(0);
        roundService.resetPlayer(this.player);
        player.setCharacterType(CharacterType.BELLE);
        roundService.drawStartCards(this.player);
        assertThat(player.getHandCards().size(), is(6));
        assertThat(player.getDeckCards().size(), is(4));

        //If the user plays as Doc, he has 7 cards at the beginning and the deck contains the other 3 cards
        player.setCharacterType(CharacterType.DOC);
        roundService.drawStartCards(this.player);
        assertThat(player.getHandCards().size(), is(7));
        assertThat(player.getDeckCards().size(), is(3));
    }
}