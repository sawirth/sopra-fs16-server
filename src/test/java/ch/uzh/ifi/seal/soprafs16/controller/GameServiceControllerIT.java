package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.Application;
import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.constant.RoundType;
import ch.uzh.ifi.seal.soprafs16.constant.TreasureType;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Round;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.Wagon;
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
        ResponseEntity<Game> response = template.exchange(base + "/games/new?token=" + owner.getToken(), HttpMethod.POST, requestBody, Game.class);

        Assert.assertEquals(GameStatus.PENDING, response.getBody().getStatus());

        //Trying to start the game with one player which should not be possible
        template.postForLocation(base + "/games/" + response.getBody().getId() + "/start?token=" + owner.getToken(), null);
        response = template.getForEntity(base + "/games/" + response.getBody().getId(), Game.class);
        Assert.assertNotEquals(GameStatus.RUNNING, response.getBody().getStatus());

        //Add a second player and check if adding was successful
        User secondPlayer = addUser();
        template.postForLocation(base +  "/games/" + response.getBody().getId() + "/player?token=" + secondPlayer.getToken(), null);
        response = template.getForEntity(base + "/games/" + response.getBody().getId(), Game.class);
        Assert.assertEquals(2, response.getBody().getPlayers().size());

        //Now the owner tries to start the game which must be possible
        template.postForLocation(base + "/games/" + response.getBody().getId() + "/start?token=" + owner.getToken(), response.getBody().getNextPlayer());
        response = template.getForEntity(base + "/games/" + response.getBody().getId(), Game.class);
        Assert.assertEquals(GameStatus.RUNNING, response.getBody().getStatus());


        /*
         * Test train setup
         */
        List<Wagon> train = response.getBody().getTrain();
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
        List<Round> rounds =response.getBody().getRounds();
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

        //Moves must be empty for all rounds
        for(int i=0;i<rounds.size();i++){
            Assert.assertThat(rounds.get(i).getMoves().size(), is(0));
        }

        //MoveTypes list must not be empty
        for(int i=0;i<rounds.size();i++){
            Assert.assertThat(rounds.get(i).getMoveType().size(), not(0));
        }

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
