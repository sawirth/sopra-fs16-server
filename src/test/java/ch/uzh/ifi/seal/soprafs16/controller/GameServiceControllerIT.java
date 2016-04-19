package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.Application;
import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.constant.RoundType;
import ch.uzh.ifi.seal.soprafs16.constant.TreasureType;
import ch.uzh.ifi.seal.soprafs16.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=0" })
public class GameServiceControllerIT {

    @Value("${local.server.port}")
    private int port;

    private URL base;
    private RestTemplate template;

    @Before
    public void setUp()
            throws Exception {
        this.base = new URL("http://localhost:" + port + "/");
        this.template = new TestRestTemplate();
    }

    @Test
    public void testAddGame() {
        User user = addUser();
        ResponseEntity<Game> gameEntity = template.exchange(base + "/games/new?token=" + user.getToken(), HttpMethod.POST, null, Game.class);

        Assert.assertThat(gameEntity.getStatusCode(), is(HttpStatus.OK));
        Assert.assertThat(gameEntity.getBody().getOwner(), is(user.getUsername()));
        Assert.assertNotNull(gameEntity.getBody().getId());
    }

    @Test
    public void testStartStopReEnterNewGame() {

        //Create new game
        User owner = addUser();
        ResponseEntity<Game> game = template.postForEntity(base + "/games/new?token=" + owner.getToken(), null, Game.class);
        Assert.assertThat(game.getBody().getOwner(), is(owner.getUsername()));
        Assert.assertThat(game.getBody().getStatus(), is(GameStatus.PENDING));

        //Add second player
        User player2 = addUser();
        game = template.postForEntity(base + "/games/" + game.getBody().getId() + "/player?token=" + player2.getToken(), null, Game.class);
        Assert.assertThat(game.getBody().getPlayers().size(), is(2));

        //Start and Stop game
        game = template.postForEntity(base + "/games/" + game.getBody().getId() + "/start?token=" + owner.getToken(), null, Game.class);
        Assert.assertThat(game.getBody().getStatus(), is(GameStatus.RUNNING));
        String url = base + "/games/{gameId}/stop?token={token}";
        Map<String, String> map = new HashMap<>();
        map.put("gameId", game.getBody().getId().toString());
        map.put("token", owner.getToken());
        template.put(url, null, map);
        url = base + "/games/{gameId}";
        game = template.getForEntity(url, Game.class, map);
        Assert.assertThat(game.getBody().getStatus(), is(GameStatus.FINISHED));

        //Create a new game
        game = template.postForEntity(base + "/games/new?token=" + owner.getToken(), null, Game.class);
        Assert.assertThat(game.getBody().getStatus(), is(GameStatus.PENDING));

        //Add second player
        game = template.postForEntity(base + "/games/" + game.getBody().getId() + "/player?token=" + player2.getToken(), null, Game.class);
        Assert.assertThat(game.getBody().getPlayers().size(), is(2));

        //Start game
        game = template.postForEntity(base + "/games/" + game.getBody().getId() + "/start?token=" + owner.getToken(), null, Game.class);
        Assert.assertThat(game.getBody().getStatus(), is(GameStatus.RUNNING));
    }

    @Test
    public void testStartGame() {
        User owner = addUser();
        HttpEntity<User> requestBody = new HttpEntity<>(owner);
        ResponseEntity<Game> gameResponse = template.exchange(base + "/games/new?token=" + owner.getToken(), HttpMethod.POST, requestBody, Game.class);

        Assert.assertEquals(GameStatus.PENDING, gameResponse.getBody().getStatus());

        //Trying to start the game with one player which should not be possible
        template.postForLocation(base + "/games/" + gameResponse.getBody().getId() + "/start?token=" + owner.getToken(), null);
        gameResponse = template.getForEntity(base + "/games/" + gameResponse.getBody().getId(), Game.class);
        Assert.assertNotEquals(GameStatus.RUNNING, gameResponse.getBody().getStatus());

        //Add a second player and check if adding was successful
        User secondPlayer = addUser();
        template.postForLocation(base +  "/games/" + gameResponse.getBody().getId() + "/player?token=" + secondPlayer.getToken(), null);
        gameResponse = template.getForEntity(base + "/games/" + gameResponse.getBody().getId(), Game.class);
        Assert.assertEquals(2, gameResponse.getBody().getPlayers().size());

        //Now the owner tries to start the game which must be possible
        template.postForLocation(base + "/games/" + gameResponse.getBody().getId() + "/start?token=" + owner.getToken(), gameResponse.getBody().getNextPlayer());
        gameResponse = template.getForEntity(base + "/games/" + gameResponse.getBody().getId(), Game.class);
        Assert.assertEquals(GameStatus.RUNNING, gameResponse.getBody().getStatus());


        /*
         * Test train setup
         */
        List<Wagon> train = gameResponse.getBody().getTrain();
        Assert.assertNotNull(train);

        //First wagon is locomotive so it must have the marshal
        Assert.assertThat(train.get(0).hasMarshal(), is(true));

        //All other wagons cannot have the marshal
        for (int i = 1; i < train.size(); i++) {
            Assert.assertThat(train.get(i).hasMarshal(), is(not(true)));
        }

        //First wagon must have Cashbox as the only treasure element
        Assert.assertThat(train.get(0).getLowerLevel().getTreasures().size(), is(1));
        Assert.assertThat(train.get(0).getLowerLevel().getTreasures().get(0).getType(), is(TreasureType.CASHBOX));

        //Test rounds
        List<Round> rounds =gameResponse.getBody().getRounds();
        Assert.assertNotNull(rounds);

        //5 rounds must be implemented
        Assert.assertThat(rounds.size(), is(5));

        //First four rounds cannot be an endRound
        for(int i=0;i<4;i++){
            Assert.assertTrue(rounds.get(i).getRoundType()!=RoundType.PICK_POCKETING &&
                    rounds.get(i).getRoundType()!=RoundType.REVENGE_MARSHAL && rounds.get(i).getRoundType()!=RoundType.HOSTAGE);
        }

        //Last round must be an endRound
        Assert.assertTrue(rounds.get(4).getRoundType()==RoundType.PICK_POCKETING ||
                rounds.get(4).getRoundType()==RoundType.REVENGE_MARSHAL || rounds.get(4).getRoundType()==RoundType.HOSTAGE);

        //test first player of round
        Assert.assertThat(rounds.get(0).getFirstPlayer(), is(0));
        Assert.assertThat(rounds.get(1).getFirstPlayer(), is(1));
        Assert.assertThat(rounds.get(2).getFirstPlayer(), is(0));
        Assert.assertThat(rounds.get(3).getFirstPlayer(), is(1));
        Assert.assertThat(rounds.get(4).getFirstPlayer(), is(0));

        //Moves must be empty for all rounds
        for(int i=0;i<rounds.size();i++){
            Assert.assertThat(rounds.get(i).getMoves().size(), is(0));
        }

        //MoveTypes list must not be empty
        for(int i=0;i<rounds.size();i++){
            Assert.assertThat(rounds.get(i).getMoveType().size(), not(0));
        }


        /* ========================
        Tests for moves (shift etc.)
         =========================*/

        //At the beginning the list of moves from the round must be empty
        Game game = gameResponse.getBody();
        Assert.assertThat(game.getRounds().get(0).getMoves().isEmpty(), is(true));

        //TODO Test: Second user should not be able to make a move
        //At the moment the logic for this is not yet implemented



        //First we have to get the handCards of the user
        ResponseEntity<User> userResponse = template.getForEntity(base + "/users/" + owner.getId(), User.class, new Object());
        Assert.assertThat(userResponse.getBody().getId(), is(owner.getId()));
        Move move = userResponse.getBody().getHandCards().get(0);

        //Number of shots must be 6 at the beginning
        assertThat(userResponse.getBody().getNumberOfShots(), is(6));

        //Then the user posts his move which means that the move is removed from his handCards and put into the list of moves from the round
        template.postForLocation(base + "/moves/" + move.getId() + "?token=" + owner.getToken(), null, new Object());
        ResponseEntity<Round> roundResponse = template.getForEntity(base + "/rounds/" + game.getRounds().get(0).getId(), Round.class, new Object());
        Assert.assertThat(roundResponse.getStatusCode(), is(HttpStatus.OK));
        Assert.assertThat(roundResponse.getBody().getMoves().size(), is(1));

        //Check that user now only has 5 cards remaining in his hand
        userResponse = template.getForEntity(base + "/users/" + owner.getId(), User.class, new Object());
        Assert.assertThat(userResponse.getBody().getHandCards().size(), is(5));

        //Check getMove in MoveController
        ResponseEntity<Move> moveResponse = template.getForEntity(base + "/moves/" + move.getId(), Move.class, new Object());
        Assert.assertThat(moveResponse.getStatusCode(), is(HttpStatus.OK));
        moveResponse = template.getForEntity(base + "/moves/1000", Move.class, new Object());
        Assert.assertThat(moveResponse.getStatusCode(), is(HttpStatus.NOT_FOUND));


        //TODO Second player is now able to make a move
    }


    private User addUser() {
        User request = new User();
        request.setName(String.valueOf(Math.random()));
        request.setUsername(String.valueOf(Math.random()));

        HttpEntity<User> httpEntity = new HttpEntity<>(request);
        ResponseEntity<User> response = template.exchange(base + "/users/", HttpMethod.POST, httpEntity, User.class);
        return response.getBody();
    }
}
